package Neo4j;

import java.io.File;
import java.sql.Driver;
import java.util.Map;

// import org.neo4j.cypher.internal.runtime.expressionVariableAllocation.Result;
// import org.neo4j.driver.Session;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
// import org.neo4j.shell.Session;
//import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.driver.*;


public class Neo4J_Implementation{
    public static void main(String[] args){

        Neo4J_Implementation neo = new Neo4J_Implementation("D:\\labwor/reality");
        



        
        // neo.createNode(TwitterTypes.USER.toString(),"ID","Omar","age","23");
        // neo.createNode(TwitterTypes.USER.toString(),"ID","someone","age","22");
        // neo.createNode(TwitterTypes.USER.toString(),"ID","someoneElseKhlas","age","28");
        // neo.createRelationShip(TwitterShipTypes.POST.toString(),
        //     TwitterTypes.USER.toString(),"ID","Omar",
        //     TwitterTypes.USER.toString(),"ID","someoneElseKhlas",
        //     "score","26"
        //     );

        neo.createRelationShip(TwitterShipTypes.POST.toString(),
        TwitterTypes.USER.toString(),"ID","someoneElseKhlas",
        TwitterTypes.USER.toString(),"ID","someone",
        "score","25","talks","136","walks","78","common","8"
        );
        // neo.interrogate("MATCH (n)  RETURN n");


    }
    // Driver driver;
    GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
    GraphDatabaseService dataBaseInstance ;//= dbFactory.newEmbeddedDatabase(new File("dbName"));
    

    public Neo4J_Implementation(String dbName){
        
        //dataBaseInstance = (GraphDatabaseService) GraphDatabase.driver("reality1", AuthTokens.basic("neo4j", "omarstarwars"));//dbFactory.newEmbeddedDatabase(new File(dbName));
        dataBaseInstance =dbFactory.newEmbeddedDatabase(new File(dbName));
        //OpenGraphDatabase(dbName);   
    }

    public enum TwitterTypes implements Label {
        USER, HASHTAG,TWEET, 
        URL , CASHTAG,TERM
    }
    
    public enum TwitterShipTypes implements RelationshipType {
        USER_RETWEET, POST,TAG,REPLY,QUOTE,MENTION,LIKE,EXISTS_IN;
    }
    
        

    private Node createNode(String... properties){

        // System.out.println(properties.toString());
        try (Transaction tx = dataBaseInstance.beginTx()) {
            

            // ... DB Graph Operations (add node, relationships, etc.) ... //
            Node node = dataBaseInstance.createNode(Label.label(properties[0]));
            for(int i=1;i<properties.length;i+=2){
                node.setProperty(properties[i],properties[i+1]);
            }
            
            tx.success();
            return node;
        }
    }

    public void setNode(String... properties){
        assert(properties.length%2==1);

        // System.out.println(properties.toString());
        try (Transaction tx = dataBaseInstance.beginTx()) {
            

            // ... DB Graph Operations (add node, relationships, etc.) ... //
            Node node = dataBaseInstance.findNode(Label.label(properties[0]), properties[1], properties[2]);
            if(node==null)
                node = dataBaseInstance.createNode(Label.label(properties[0]));
            for(int i=1;i<properties.length;i+=2){
                node.setProperty(properties[i],properties[i+1]);
            }
            tx.success();
            
        }
    }

    Node getAnyNode(String... props){
        assert(props.length>2);
        Node node1 = dataBaseInstance.findNode(Label.label(props[0]), props[1], props[2]);
        if(node1==null)
                return createNode(props[0], props[1], props[2]);
        return node1;
    }

    
    public void createRelationShip(String... edge){
        assert(edge.length>=7&&edge.length%2==1);
        for (String string : edge) {
            System.out.println(string);
        }
        
        try (Transaction tx = dataBaseInstance.beginTx()) {
            // ... DB Graph Operations (add node, relationships, etc.) ... //
            Node node1 = getAnyNode(edge[1], edge[2], edge[3]);
            Node node2 = getAnyNode(edge[4], edge[5], edge[6]);
            
            Relationship rel = node1.createRelationshipTo(node2,RelationshipType.withName(edge[0]));
            // System.out.println("I was here");
            
            for(int i=7;i<edge.length;i+=2){
                rel.setProperty(edge[i],edge[i+1]);
                //node.setProperty(properties[i],properties[i+1]);
            }
            tx.success();
            
        }
    }
    
    public Result interrogate(String query){
        Result res = dataBaseInstance.execute(query);
        
        return res;
        // while(res.hasNext()) {
        //     // System.out.println("I was here");
        //     Map<String, Object> _row = res.next();
        //     for(Map.Entry<String, Object> _col : _row.entrySet()) {
        //         System.out.println("Property_Name is :" + _col.getKey() + " - Property_Value is : " + _col.getValue()) ;
        //     }
        // }
        // res.close();
    }
    
    
    public int countQuery(String query){
        Result res = dataBaseInstance.execute(query);
        int i=0;
        while(res.hasNext()) {
            i++;
            }
            res.close();
                return i;
    }



}

