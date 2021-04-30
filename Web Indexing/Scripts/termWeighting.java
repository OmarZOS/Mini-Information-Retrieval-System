


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.Map;



public class termWeighting {




    public static void main(String[] argv) throws NullPointerException, IOException{

        //  mapDirectory("Corpus", "Linguistics", 6," \"–0123456789_-.,;:!/'()[]%");
        
        // MyDictionary.getInstance().save_index_to_disk("lexicon", "Postlist","docProps");

        MyDictionary.getInstance().read_index_from_disk("lexicon", "postingList","docProps");

        MyDictionary.getInstance().displayContent();
        
    }

    private static void mapEveryFileMap(String folderName,String linguisticsFolder,Integer stemParameter,String delimiters) throws NullPointerException, IOException{
        //Map<Integer,Map<String,Integer>> myFolderMap = new HashMap<Integer,Map<String,Integer>>();
        File testDirectory = new File(folderName);
        ArrayList<String> list_of_stopwords = MyTokenizer.
             tokenizeFileContent(linguisticsFolder+"/stopwords.txt"," ");
        String[] filenames = testDirectory.list();

        

        //let's index every file..
        for(int i_Doc=0;i_Doc<filenames.length;i_Doc++ ){
            Map<String,Integer> docStruct = new HashMap<String,Integer> ();


            String currentFile=filenames[i_Doc];
            //System.out.println(currentFile);
            ArrayList<String> list_of_tokens = MyTokenizer.
                tokenizeFileContent(folderName+"/"+currentFile
                 ,delimiters);


            ArrayList<String> lister = StopWordRemover.stopWordRemove(list_of_tokens, list_of_stopwords);
            Stemmer.stemming(lister,6,i_Doc);

            docStruct.put(testDirectory+"/"+filenames[i_Doc],lister.size());
            MyDictionary.getInstance().addFileToIndex((Integer)i_Doc,docStruct);
            //System.out.println(docStruct);
        }

    }

    public static void mapDirectory(String directory,String LangDirectory,int trunc,String delimiters) throws NullPointerException, IOException{
        mapEveryFileMap(directory, LangDirectory,trunc,delimiters);
        MyDictionary.getInstance().recapSubIndexes();//You must be asking why? well, I thought about making a "bottom up" approach for indexing the directory..

    }


    @Deprecated //Don't use this for it will return segfaults..
    public static HashMap<String,Double> index_collection(String folderName,String linguisticsFolder,Integer stemParameter,String delimiters) throws NullPointerException, IOException{
        
        Map<Integer,Map<String,Integer>> foldermap = new  HashMap<Integer,Map<String,Integer>>();
        //foldermap used to be fed by mapEveryFileMap(folderName,linguisticsFolder,stemParameter,delimiters).. before it's return type has changed..
        
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

    public static Double DF_Computing(String token){
        if (MyDictionary.getInstance().contains(token)) {
            return Math.log(1+MyDictionary.getInstance().fileCount()/MyDictionary.getInstance().getIndex(token).getDocumentCount());
            
        }
        else 
            return 0.0;
    }   

    public static Double IDF_Computing(String token){
        if (MyDictionary.getInstance().contains(token)) {
            // System.out.println(MyDictionary.getInstance().fileCount());
            // System.out.println(MyDictionary.getInstance().getIndex(token).getDocumentCount());
            // System.out.println(Math.log((0.5-MyDictionary.getInstance().fileCount()
            // +MyDictionary.getInstance().getIndex(token).getDocumentCount())
            // /
            // (MyDictionary.getInstance().fileCount()+0.5)));
            // System.out.println();
            // System.out.println();
            // System.out.println();





            Double value=Math.log((0.5-MyDictionary.getInstance().fileCount()
            +MyDictionary.getInstance().getIndex(token).getDocumentCount())
            /
            (MyDictionary.getInstance().fileCount()+0.5));;
            //System.out.println(value);
            return (value.equals(Double.NaN)?1:value);
        }
        else
            return 0.0;
    }


    

}
