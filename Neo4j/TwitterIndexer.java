package Neo4j;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// package twitter_example_for_students;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author omar
 */
public class TwitterIndexer {



    //--- These credentials are are specific to my twitter account (Just for test) 
    static final String CONSUMER_KEY = "YA0ZNUHTCrdYd4cekg1aAdIJa";
    static final String CONSUMER_SECRET = "qqS96sRUSStE0EHMxE26CsAti0g7qu2NYccqSfLw6KtOXA744x";
    static final String ACCESS_TOKEN = "2231814860-iMYju4GGcxDeF6mor7muPWEuxS6sgPo2GQb7zJD";
    static final String ACCESS_TOKEN_SECRET = "CEyNDfKUa6wWY6wKpf0Oetx26ntbTLsmX5Ir5vE2v1bXv";       
    Twitter twitter_connect = null ;
    User user = null ;
    static ArrayList<Status> extracted_Tweets = null ;

    Neo4J_Implementation dataBaseService ;


    public TwitterIndexer(String dbName){
        dataBaseService = new Neo4J_Implementation(dbName);

    }


    public TwitterIndexer(Neo4J_Implementation db){
        dataBaseService = db;

    }

    
    public void connect() throws TwitterException {

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
              .setOAuthConsumerKey(CONSUMER_KEY)
              .setOAuthConsumerSecret(CONSUMER_SECRET)
              .setOAuthAccessToken(ACCESS_TOKEN)
              .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

            // gets Twitter instance with default credentials
            twitter_connect = new TwitterFactory(cb.build()).getInstance();
            user = twitter_connect.verifyCredentials();            
    }

    public static void main(String[] args) throws TwitterException {
       
        TweetGraphData tweetGraphData = new TweetGraphData();
        
        TwitterIndexer Example = new TwitterIndexer(tweetGraphData.neo) ;
        
        Example.connect(); //--- Connect using Twitter4J
        
        System.out.println("was I here?");
        
        ArrayList<Status> tweets = Example.retrieve_tweets_from_account("bbcworld" , "2019-01-30" , 30 );

        
        for (Status tweet : tweets) {
            tweetGraphData.saveTweet(tweet);
        }
        

        
        
        
        
        
        System.exit(0);
    }    


    

    public ArrayList<String> statusesToString(ArrayList<Status> tweets){
        ArrayList<String> tweetsText =new ArrayList<String>();

        for (Status stat : tweets) {
            tweetsText.add("@" + stat.getUser().getScreenName() + " - " + stat.getId() + " - " + stat.getText() + " - " + stat.getCreatedAt() );
        }
        return tweetsText;
    } 
    
    public void display_statuses (List<Status> statuses) {
        for (Status status : statuses) {
            System.out.println("@" + status.getUser().getScreenName() + " - " + status.getId() + " - " + status.getText() + " - " + status.getCreatedAt() );
        }
    }
    
    public void extract_current_user_tweets() throws TwitterException {
        List<Status> statuses = twitter_connect.getHomeTimeline();
        System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
        display_statuses(statuses) ;
    }
    
    public Status retrieve_tweet_by_ID(String tweet_ID) {
        //----- Extract of a Tweet using ID
        try {
                Status status = twitter_connect.showStatus(Long.parseLong(tweet_ID));
                if (status == null) { // 
                    // don't know if needed - T4J docs are very bad
                    System.out.println("This Tweet_ID is not correct !!!");
                    return null ;
                    
                } else {
                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                    return status ;
                }
        } catch (TwitterException e) {
                System.err.print("Failed to find tweet: " + e.getMessage());
                return null ;
        }        
    
    }

    //-- Example of Use : retrieve_tweets_from_account("TebbouneAmadjid" , "2021-04-30" , 100 );
    public ArrayList<Status> retrieve_tweets_from_account(String Account_ID, String _Since, int Number_of_tweets) throws TwitterException {

        ArrayList<Status> tweets = new ArrayList<>();

        Query query = new Query("from:"+ Account_ID).since(_Since);
        query.setCount(Number_of_tweets);
        QueryResult result = twitter_connect.search(query);

        tweets.addAll(result.getTweets());
        return tweets;
    }    
}