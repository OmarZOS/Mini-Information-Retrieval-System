package Neo4j;



//import org.neo4j.ogm.transaction.Transaction.Status;
import org.neo4j.graphdb.Transaction;

import twitter4j.Status;


public interface TweetData{
	void saveTweet(Status tweet);
	void close();
}