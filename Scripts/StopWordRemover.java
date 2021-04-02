package Scripts;
import java.io.IOException;
import java.util.ArrayList;
//import Scripts.MyFileReader;
import Scripts.MyTokenizer;


public class StopWordRemover {
    public static void main(String[] argv) throws NullPointerException, IOException{
        
        
        ArrayList<String> list_of_tokens = MyTokenizer.tokenizeFileContent("Corpus/doc1.txt"," _-.,;:!/'()[]");
        ArrayList<String> list_of_stopwords = MyTokenizer.tokenizeFileContent("Linguistics/stopwords.txt"," ");



        for(String token: stopWordRemove(list_of_tokens,list_of_stopwords)){
            System.out.println(token);
        }

    }  

    public static ArrayList<String> stopWordLoader(String filename) throws IOException{

        return MyTokenizer.tokenizeFileContent(filename," ");
    }

    public static ArrayList<String> stopWordRemove(ArrayList<String> list_of_tokens,ArrayList<String> stopwords){

        ArrayList<String> list_of_tokens_without_stopwords = new ArrayList<String>();
        for(String str : list_of_tokens){
            boolean found=false;
            for(String stw : stopwords){
                if(str.equals(stw)){
                    found=true;
                    break;
                }
            }
            if(!found)
                list_of_tokens_without_stopwords.add(str.toLowerCase());//lower case to improve decision quality..
        }
        return list_of_tokens_without_stopwords;


    }
    



}
