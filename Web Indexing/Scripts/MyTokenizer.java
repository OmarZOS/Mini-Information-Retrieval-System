
import java.io.IOException;

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.lang.NullPointerException;

public class MyTokenizer {

    public static void main(String argv[]) throws IOException,NullPointerException{
        
        
        tokenizeFileContent("Corpus/doc1.txt"," _-.,;:!/'()[]");
        
        
        
    }
    
    public static ArrayList<String> tokenizeFileContent(String filename,String delimiters) throws IOException,NullPointerException{
        
        //I wanted to invert the dependency here but I had dependency problems with Tika API

        MyFileReader docReader = new MyFileReader();
        ArrayList<String> list_of_tokens=new ArrayList<String>();
        docReader.readFile(filename);
        String curLine=docReader.getLine();
        while(curLine!=null){
            //System.out.println(curLine+"\n\n\n\n");
            StringTokenizer st = new StringTokenizer(curLine,delimiters);
            while(st.hasMoreTokens()){
                
                list_of_tokens.add(st.nextToken().toString());
            //   System.out.println(st.toString());
                
            }
    
    
    
            
            curLine=docReader.getLine();
        }
        docReader.closeFile();
    

        return list_of_tokens;
    }


    
    public static ArrayList<String> tokenizeText(String sentence,String delimiters) throws IOException,NullPointerException{
        
        //I wanted to invert the dependency here but I had dependency problems with Tika API

        ArrayList<String> list_of_tokens=new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(sentence,delimiters);
            while(st.hasMoreTokens()){
                
                list_of_tokens.add(st.nextToken().toString());
            //   System.out.println(st.toString());
                
            }
    
    

        return list_of_tokens;
    }



    






}
