package Scripts;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.Map;

import Scripts.MyIndex;




public class termWeighting {


    //private static IndexMaker INDEX_MAKER = new IndexMakerV1();

    //full Index structure


    public static void main(String[] argv) throws NullPointerException, IOException{
        //index_collection("Corpus");


        // finding the time before the operation is executed
        long start = System.currentTimeMillis();


        mapEveryFileMap("myTestCorpus","Linguistics",6);

        MyDictionary.getInstance().displayContent();
    

        // finding the time after the operation is executed
        long end = System.currentTimeMillis();
        //finding the time difference and converting it into seconds
        Float sec =  ((end - start) / 1000F); 
        //System.out.println(sec + " seconds");

        System.out.println("Weighting finished successfully after : "+(sec/60)+" minutes.");
        //tested on 13/03/2021 at 17:24 , it gave 366.602 seconds. (for more than 200000 docs)
    }

    public static void mapEveryFileMap(String folderName,String linguisticsFolder,Integer stemParameter) throws NullPointerException, IOException{
        //Map<Integer,Map<String,Integer>> myFolderMap = new HashMap<Integer,Map<String,Integer>>();
        File testDirectory = new File(folderName);
        ArrayList<String> list_of_stopwords = MyTokenizer.
             tokenizeFileContent(linguisticsFolder+"/stopwords.txt"," ");
        String[] filenames = testDirectory.list();

        

        //let's index every file..
        for(int i_Doc=0;i_Doc<filenames.length;i_Doc++ ){
            String currentFile=filenames[i_Doc];
            //System.out.println(currentFile);
            ArrayList<String> list_of_tokens = MyTokenizer.
                tokenizeFileContent(folderName+"/"+currentFile
                 ," \"–0123456789_-.,;:!/'()[]");



            Stemmer.stemming(StopWordRemover.stopWordRemove(list_of_tokens, list_of_stopwords),6,i_Doc);

            
        }


        //myFolderMap
        //return ;
        
    }

    @Deprecated //I think..
    public static HashMap<String,Double> index_collection(String folderName,String linguisticsFolder,Integer stemParameter) throws NullPointerException, IOException{
        
        Map<Integer,Map<String,Integer>> foldermap = mapEveryFileMap(folderName,linguisticsFolder,stemParameter);
        
        HashMap<String,Double> indexCollection= new HashMap<String,Double>();
        
        
        


        Integer filesCount=foldermap.size();
        
        for(Map<String,Integer> filemap : foldermap.values()){
            for(String word : filemap.keySet()){
                if(indexCollection.containsKey(word)){//already calculated..
                    continue;
                }

                Integer count=0;//I avoided 1, comparing two maps looks way heavier than finding a key..
                for(Map<String,Integer> filemap2 : foldermap.values()){
                    if(filemap2.containsKey(word))
                        count++;
                }

                //System.out.println("Putting "+word+" N= "+fileNumber.toString()+" t= "+count.toString());
                indexCollection.put(word,Math.log(filesCount/count));



            }
        }

        return indexCollection;
    }



    

}
