package Neo4j;


import java.io.IOException;
import java.util.ArrayList;

public class Stemmer {
    public static void main(String[] argv) throws NullPointerException, IOException{
        
    }


    public static ArrayList<String> stemming(ArrayList<String> list_of_tokens_without_stopwords,int truncSize){
        ArrayList<String> list_of_stems= new ArrayList<String>();


        for(String word : list_of_tokens_without_stopwords){
            
          //  list_of_stems.add(word.substring(0,Integer.min(word.length(), truncSize)));
          list_of_stems.add(word.substring(0,Integer.min(word.length(), truncSize)));

        }
        return list_of_stems;

    }



}
