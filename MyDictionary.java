package Scripts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyDictionary { //obviously a singleton
    private static Map<Integer,MyIndex> INDEXER =null;//in an earlier stage.. 
    private static ArrayList<String> IDENTIFIER = new ArrayList<String>();//word identifier
    private static MyDictionary INSTANCE=null;
    private Map<Integer,Map<Integer,Integer>> subIndexer =new  HashMap<Integer,Map<Integer,Integer>>();
//lighter.. but I think it will be efficient.. 03/04/2021 11:49 AM
//the idea is to have a sub-Dictionary for each file..


    private MyDictionary() {}

    public static MyDictionary getInstance(){
        if(INSTANCE==null)
            INSTANCE= new MyDictionary();
        return INSTANCE;
    }

    private Integer identifyWord(String word){ //this one is only for the dictionary
        if(IDENTIFIER.contains(word))
            return IDENTIFIER.indexOf(word);
        
        IDENTIFIER.add(word);

        //INDEXER.put(IDENTIFIER.size()-1, new MyIndex());

        return IDENTIFIER.size()-1;   //logically if it has been is just added..

    }

    private void freeSubIndexes(){
        subIndexer.clear();
        subIndexer=new HashMap<Integer,Map<Integer,Integer>>();
    }

    public boolean contains(String word){
        return IDENTIFIER.contains(word);
    }

    public void mapNewWord(String word,Integer Doc_i){
        Integer ID=identifyWord(word);
        
        if(subIndexer.containsKey(Doc_i)){
            
            if(subIndexer.get(Doc_i).containsKey(ID))
                subIndexer.get(Doc_i).put(ID,subIndexer.get(Doc_i).get(ID)+1);
            else
                subIndexer.get(Doc_i).put(ID,1);
            
            return;
        }

        //new file case..
        Map<Integer,Integer> temp = new HashMap<Integer,Integer>();
        temp.put(ID,1);        
        subIndexer.put(Doc_i,temp);

        
        //INDEXER.get(ID).addWord(Doc_i);
    }
    
    public MyIndex getIndex(String word){
        return (INDEXER!=null)?INDEXER.get(IDENTIFIER.indexOf(word)):null;
    }

    public void recapSubIndexes(){
        long start = System.currentTimeMillis();
        if(INDEXER==null)
            INDEXER=new HashMap<Integer, MyIndex> ();

        for(    Integer doc : subIndexer.keySet()    ){
            for(Integer file : subIndexer.get(doc).keySet()){
                if(INDEXER.containsKey(file))
                    INDEXER.get(file).addWord(doc);
                else{
                    MyIndex tmp = new MyIndex();
                    tmp.addWord(doc);
                    INDEXER.put(file,tmp);
                }

            }
        }
        

        System.out.println("Recapitulation done in : "+((System.currentTimeMillis() - start) / 1000F)/60);


        freeSubIndexes();
    }
    

    public void displayContent(){
        long start = System.currentTimeMillis();
        //System.out.println("obj");


        for (String word : IDENTIFIER) {
            System.out.print(word);

            INDEXER.get(identifyWord(word)).displayContent();
        }
        System.out.println("displaying done in : "+((System.currentTimeMillis() - start) / 1000F)/60);


    }




}
