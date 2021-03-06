
import java.io.IOException;
import java.util.ArrayList;

public class Stemmer {
    public static void main(String[] argv) throws NullPointerException, IOException{
        
        ArrayList<String> list_of_tokens = MyTokenizer.tokenizeFileContent("Corpus/doc1.txt"," _-.,;:!/'()[]");
        ArrayList<String> list_of_stopwords = MyTokenizer.tokenizeFileContent("Linguistics/stopwords.txt"," ");

        long start = System.currentTimeMillis();

        stemming(StopWordRemover.stopWordRemove(list_of_tokens, list_of_stopwords),6,1);

        long stemTime = System.currentTimeMillis();

        MyDictionary.getInstance().recapSubIndexes();

        long recapTime = System.currentTimeMillis();

        MyDictionary.getInstance().displayContent();

        long displayTime = System.currentTimeMillis();

        System.out.println("Stemming done in : "+((stemTime - start) / 1000F)/60);
        System.out.println("Recapitulation done in : "+((recapTime - start) / 1000F)/60);
        System.out.println("displaying done in : "+((displayTime - start) / 1000F)/60);


    }

    public static void stemming(ArrayList<String> list_of_tokens_without_stopwords,int truncSize,int Doc_i){
        //ArrayList<String> list_of_stems= new ArrayList<String>();
        

        for(String word : list_of_tokens_without_stopwords){
            
          //  list_of_stems.add(word.substring(0,Integer.min(word.length(), truncSize)));
            MyDictionary.getInstance().mapNewWord(word.substring(0,Integer.min(word.length(), truncSize)), Doc_i);

        }
        //return list_of_stems;

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
