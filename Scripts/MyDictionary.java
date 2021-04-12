package Scripts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyDictionary { //My first singleton in action!!
    private static Map<Integer,Index_hashMap> PostingList =null;//in an earlier stage.. 
    private static ArrayList<String> Terms = new ArrayList<String>();//word Terms
    private static MyDictionary INSTANCE=null;
    private Map<Integer,Map<Integer,Integer>> subPostingList =new  HashMap<Integer,Map<Integer,Integer>>();
    //lighter.. but I think it will be efficient.. 03/04/2021 11:49 AM
    //the idea is to have a sub-Dictionary for each file..

    private Map<Integer,Map<String,Integer>>  Documents = new HashMap<Integer,Map<String,Integer>>();



    private MyDictionary() {}

    public static MyDictionary getInstance(){
        if(INSTANCE==null)
            INSTANCE= new MyDictionary();
        return INSTANCE;
    }

    public Integer fileCount(){
        return Documents.size();
    }


    private Integer identifyWord(String word){ //this one is only for the dictionary
        if(Terms.contains(word))
            return Terms.indexOf(word);
        
        Terms.add(word);

        //PostingList.put(Terms.size()-1, new Index_hashMap());

        return Terms.size()-1;   //logically if it has been is just added..

    }

    public Integer getWordId(String word){ //this one is only for the dictionary
        if(Terms.contains(word))
            return Terms.indexOf(word);
        else
            return -1; 
    }


    private void freeSubIndexes(){
        subPostingList.clear();
        subPostingList=new HashMap<Integer,Map<Integer,Integer>>();
    }

    public boolean contains(String word){
        for (int i = 0 ;i<Terms.size();i++) {
            if (Terms.get(i).equals(word)) {
                return true;                
            }
        }
        return false;
        
    }

    public void mapNewWord(String word,Integer Doc_i){
        Integer ID=identifyWord(word);
        
        if(subPostingList.containsKey(Doc_i)){
            
            if(subPostingList.get(Doc_i).containsKey(ID))
                subPostingList.get(Doc_i).put(ID,subPostingList.get(Doc_i).get(ID)+1);
            else
                subPostingList.get(Doc_i).put(ID,1);
            
            return;
        }

        //new file case..
        Map<Integer,Integer> temp = new HashMap<Integer,Integer>();
        temp.put(ID,1);        
        subPostingList.put(Doc_i,temp);

        
        //PostingList.get(ID).addWord(Doc_i);
    }
    
    public Index_hashMap getIndex(String word){
        return (PostingList!=null)?PostingList.get(Terms.indexOf(word)):null;
    }

    public void recapSubIndexes(){
        long start = System.currentTimeMillis();
        if(PostingList==null)
            PostingList=new HashMap<Integer, Index_hashMap> ();

        for(    Integer doc : subPostingList.keySet()    ){
            for(Integer file : subPostingList.get(doc).keySet()){
                if(PostingList.containsKey(file))
                    PostingList.get(file).addWord(doc);
                else{
                    Index_hashMap tmp = new Index_hashMap();
                    tmp.addWord(doc);
                    PostingList.put(file,tmp);
                }

            }
        }
        

        System.out.println("Recapitulation done in : "+((System.currentTimeMillis() - start) / 1000F)/60);


        freeSubIndexes();
    }
    

    public void displayContent(){
        long start = System.currentTimeMillis();
        //System.out.println("obj");


        for (String word : Terms) {
            System.out.print(word);

            PostingList.get(identifyWord(word)).displayContent();
        }
        System.out.println("displaying done in : "+((System.currentTimeMillis() - start) / 1000F)/60);


    }


    public Integer getSize(Integer doc){
        
            for (String url : Documents.get(doc).keySet()) {
                return Documents.get(doc).get(url);
            }
            return null;

    }


    public Float avgdl(){
        Integer sum=0;
        for (Integer doc : Documents.keySet()) {
            for (String url : Documents.get(doc).keySet()) {//I know, it was meaningless
                sum+=Documents.get(doc).get(url);
            }
        }
        return (float) (sum/Documents.size());
    }

    public void addFileToIndex(Integer ID_Doc,Map<String,Integer> props){
        Documents.put(ID_Doc,props);
    }

    public void save_index_to_disk(String lexicon,String Postlist,String docProps) throws IOException{//Todo : 
        DataOutputStream lexy = new DataOutputStream (new FileOutputStream(lexicon))
        ,posty= new DataOutputStream (new FileOutputStream(Postlist));

        for (int currentID=0;currentID< Terms.size();currentID++) {
            lexy.writeInt(currentID);
            lexy.writeUTF(Terms.get(currentID));
            lexy.writeInt(PostingList.get(currentID).getDocumentCount());
            
            posty.writeInt(currentID);
            posty.writeInt(PostingList.get(currentID).getDocumentCount());
            for (Integer doc_i : PostingList.get(currentID).getKeySet()) {
                posty.writeInt(doc_i);
                posty.writeInt(PostingList.get(currentID).getFrequency(doc_i));
            }

        }

        lexy.close();
        posty.close();

        DataOutputStream proprietyDoc = new DataOutputStream (new FileOutputStream(docProps));

        for (Integer Id_Doc : Documents.keySet()) {
            proprietyDoc.writeInt(Id_Doc);
            for (String prop : Documents.get(Id_Doc).keySet()) {
                proprietyDoc.writeUTF(prop);
                proprietyDoc.writeInt(Documents.get(Id_Doc).get(prop));
            }
        }
        proprietyDoc.close();

    }








    public void read_index_from_disk(String lexicon,String Postlist,String docFile) throws IOException{//Todo :
        Init_Term_Index_on_Memory(lexicon);
        Init_Posting_List_on_Memory(Postlist);
        Init_Doc_Index_on_Memory(docFile);
    }
    
    private void Init_Term_Index_on_Memory(String lexicon) throws IOException{
        PostingList=new HashMap<>(); 
        DataInputStream lexy = new DataInputStream(new FileInputStream(lexicon));
        while(lexy.available()>0) {
            lexy.readInt() ;//termID  	 
            String termString = lexy.readUTF();
            lexy.readInt() ; //nbr_of_docs = 
            Terms.add(termString);
        }
        lexy.close();
        
    }


    
    private void Init_Posting_List_on_Memory(String Postlist) throws IOException{
        DataInputStream posty = new DataInputStream(new FileInputStream(Postlist));
        while (posty.available()>0) {
            int termID = 	 posty.readInt() ;
            int nbr_of_docs = posty.readInt() ;
            
            Index_hashMap index=new Index_hashMap();
            for (int i = 0; i < nbr_of_docs; i++) {
                int doc=posty.readInt();
                index.addWord(doc);
                index.setFrequency(doc,posty.readInt());
            }
            PostingList.put(termID,index);
        }
        posty.close();
        
    }
    
    private void Init_Doc_Index_on_Memory(String docFile) throws IOException{
        DataInputStream documentFile = new DataInputStream(new FileInputStream(docFile));

        while (documentFile.available()>0) {
            Integer id=documentFile.readInt();
            String url=documentFile.readUTF();
            Integer size=documentFile.readInt();
            Map<String,Integer> struct= new HashMap<String,Integer>();
            struct.put(url,size);

            Documents.put(id,struct);
        }



        documentFile.close();

    }
    
    
    
}
