package Scripts;

import java.io.IOException;

import Scripts.MyFileReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.lang.NullPointerException;

public class MyTokenizer {

    public static void main(String argv[]) throws IOException,NullPointerException{
        
        
        tokenizeFileContent("Corpus/doc1.txt"," _-.,;:!/'()[]");
        
        
        
    }
    
    public static ArrayList<String> tokenizeFileContent(String filename,String delimiters) throws IOException,NullPointerException{
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


    






}
