package Scripts;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;



public class MyFileReader {
    private BufferedReader dataFile ;
    public static void main(String argv[]) throws IOException{
        //  let the main do the test..

        BufferedReader dataFile = new BufferedReader(new FileReader("Linguistics/stopwords.txt"));//argv.toString()

            
        String curLine=dataFile.readLine();
        while(curLine!=null){    
            System.out.println(curLine);
            curLine=dataFile.readLine();
        }
        dataFile.close();
    }

    public  void readFile(String filename) {
        
        //and let this function for later use..
        try {
            dataFile = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }

    public  String getLine() {
        try {
            return dataFile.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public void closeFile() {
        try {
            dataFile.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
