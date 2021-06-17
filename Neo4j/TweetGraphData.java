package Neo4j;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
// import org.neo4j.ogm.transaction.Transaction.Status;
import java.util.Map;

import Neo4j.Extractor.Entity;
import Neo4j.Extractor.Entity.Type;
import Neo4j.Neo4J_Implementation.TwitterShipTypes;
import Neo4j.Neo4J_Implementation.*;
import twitter4j.Status;
import twitter4j.UserMentionEntity;
import Neo4j.MyTokenizer;
import org.neo4j.graphdb.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


class TweetGraphData implements TweetData {
	
	
	Extractor extractor = new Extractor();
	
	static Neo4J_Implementation neo = new Neo4J_Implementation("D:\\labwor/reality");;
	static String delimiters=" _-.,;:!/'()[]@#";
	static int stemParameter=6;
	static int TermCountInsideIndex=-1;//initially like this..


	@Override
	public void saveTweet(Status tweet) {
		
		//inserting USER if non existant
		Long userID = tweet.getUser().getId();
		String screenName = tweet.getUser().getScreenName();
		Integer nbr_of_followers = tweet.getUser().getFollowersCount();
		neo.setNode(TwitterTypes.USER.toString(),"user_id",
			userID.toString(),"screen_name",screenName,"nbr_of_followers",nbr_of_followers.toString());

		Integer favCount=tweet.getFavoriteCount(),retweetCount=tweet.getRetweetCount();

		
			
		//setting the tweet node
		Long tweetID = tweet.getId();
			neo.setNode(TwitterTypes.TWEET.toString(),"tweetID",tweetID.toString(),"tweet_text",tweet.getText());

		
		//setting the relationship user-tweet
		neo.createRelationShip(TwitterShipTypes.POST.toString(),
			TwitterTypes.USER.toString(),"user_id",userID.toString(),
			TwitterTypes.TWEET.toString(),"tweetID",tweetID.toString(),
			"nbr_of_favorites",favCount.toString(),
			"nbr_of_retweets",retweetCount.toString(),
			"date_of_creation",tweet.getCreatedAt().toString()
        );

		
		

		//handling mentions
		UserMentionEntity[] mentions =tweet.getUserMentionEntities();
		for (UserMentionEntity userMentionEntity : mentions) {
			Long mentionedUserID = tweet.getUser().getId();
			neo.createRelationShip(TwitterShipTypes.MENTION.toString(),
			TwitterTypes.USER.toString(),"user_id",mentionedUserID.toString(),
			TwitterTypes.TWEET.toString(),"tweetID",tweetID.toString());

		}



		
		List<Entity> entities = extractor.extractEntitiesWithIndices(tweet.getText());
		for (Entity entity : entities) {
			if(entity.type==Type.URL){
				ArrayList<String> titleTokens = getTitleTag(entity.getValue());

				for (String token: titleTokens) {
					neo.createRelationShip(
					TwitterShipTypes.EXISTS_IN.toString(),
					TwitterTypes.TERM.toString(),"term_ID",token,
					TwitterTypes.TWEET.toString(),"tweetID",tweetID.toString()
				);
				}


				
				System.exit(0);
			}
			
			if(entity.type==Type.MENTION){
				continue;//already treated
			}


			if(entity.type==Type.HASHTAG){

				Integer count = calculateOccurences(tweet.getText(),"#"+entity.getValue());

				neo.createRelationShip(
					TwitterShipTypes.EXISTS_IN.toString(),
					TwitterTypes.HASHTAG.toString(),"hashtag_ID",entity.getValue(),
					TwitterTypes.TWEET.toString(),"tweetID",tweetID.toString(),
					"tf_raw",count.toString()
				);
			}
		}

		try {

			
			//indexing tweet content 

			HashMap<String,Integer> map = TF_Computing.term_frequency_computing(Stemmer.stemming(MyTokenizer.tokenizeText(tweet.getText(), delimiters),stemParameter));
					
			for (String term : map.keySet()) {
				
				neo.createRelationShip(
					TwitterShipTypes.EXISTS_IN.toString(),
					TwitterTypes.TERM.toString(),"term_ID",term,
					TwitterTypes.TWEET.toString(),"tweetID",tweetID.toString(),
					"tf_raw",map.get(term).toString()
				);


			}
				
				




		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}





	}
	@Override
	public void close() {}

	public Double TweetScore(Status tweet){
        
        Long id = tweet.getId();
		if(TermCountInsideIndex==-1)
			setTermCount();
			
		String query="MATCH (t : TERM)-[rel : EXISTS_IN]-> (n: TWEET) where n.id=\""+id.toString()+"\" RETURN t.term_ID,rel.tf_raw";
			
			// String queryTF="MATCH (t : TERM)-[rel : EXISTS_IN]-> (n: TWEET) where n.id=\""+id.toString()+"\" and t.term_ID= RETURN t.term_ID,rel.tf_raw";


        Result res = neo.interrogate(query);

		Double value=0.0;
		while(res.hasNext()) {



            System.out.println("I was here");
            Map<String, Object> _row = res.next();
            for(Map.Entry<String, Object> _col : _row.entrySet()) {
                
				String term_id= _col.getValue().toString();

				 
				value+= Math.log(TermCountInsideIndex
				/(countNumberOfTweetsContainingTerm(term_id)+1))
				*(1+Math.log(Double.parseDouble(_col.getValue().toString())));
				
				
				// System.out.println("Property_Name is :" + _col.getKey() + " - Property_Value is : " + _col.getValue()) ;
            }
        }
        res.close();



        return value*TweetReputation(tweet)*UserImportance(tweet.getUser().getId());
    }

	public Double TweetReputation(Status tweet){

		Long id = tweet.getId();
		String query = "MATCH (t : TWEET) where n.id=\""+id.toString()+"\" RETURN t.nbr_of_favorites,t.nbr_of_retweets";
		Result res = neo.interrogate(query);
		Double result = 0.0;

		while(res.hasNext()) {
			    // System.out.println("I was here");
			    Map<String, Object> _row = res.next();
			    for(Map.Entry<String, Object> _col : _row.entrySet()) {
			        // System.out.println("Property_Name is :" + _col.getKey() + " - Property_Value is : " + _col.getValue()) ;
					result+=Math.log(1+Double.parseDouble((String) _col.getValue()));
				} 	
			}
			return result;

	}

	public Double UserImportance(Long userID){
		String query = "MATCH (t : USER) where n.id=\""+userID.toString()+"\" RETURN t.nbr_of_favorites,rel.nbr_of_retweets";
		Result res = neo.interrogate(query);
		Double result = 0.0;

		while(res.hasNext()) {
			    // System.out.println("I was here");
			    Map<String, Object> _row = res.next();
			    for(Map.Entry<String, Object> _col : _row.entrySet()) {
			        // System.out.println("Property_Name is :" + _col.getKey() + " - Property_Value is : " + _col.getValue()) ;
					result+=Math.log(1+Double.parseDouble((String) _col.getValue()));
				} 	
			}
			return result;


	}


	public void setTermCount(){
		String query = "MATCH (t:TERMS) RETURN t.term_ID";
		TermCountInsideIndex = neo.countQuery(query);
	}

	public int countNumberOfTweetsContainingTerm(String term){
		String query = "MATCH (t:TERMS) WHERE t.id=\""+term+"\" RETURN t.term_ID";
		return neo.countQuery(query);
	}




	public ArrayList<String> getTitleTag(String url){


		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			return MyTokenizer.tokenizeText(doc.select("title").text(), delimiters) ;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		return null;

	}

	public int calculateOccurences(String big,String small){
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){

			lastIndex = big.indexOf(small,lastIndex);

			if(lastIndex != -1){
				count ++;
				lastIndex += small.length();
			}
		}
		return count;
	}

}