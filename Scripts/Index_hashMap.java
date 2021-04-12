package Scripts;

import java.util.HashMap;
import java.util.Set;



public class Index_hashMap {
    private Integer n_Docs=0;   //logically, it should be 1, but that needs conditions..
    private HashMap<Integer,Integer> docOccurenceList=new HashMap<Integer,Integer>();



    public void addWord(Integer doc){
        if(docOccurenceList.containsKey(doc))
            docOccurenceList.put(doc,docOccurenceList.get(doc)+1);
        else{
            n_Docs++;
            docOccurenceList.put(doc,1);
        }
    }

    public void displayContent(){
        System.out.print(" "+n_Docs);
        System.out.println(docOccurenceList);
    }


    public Integer getFrequency(Integer Doc_i){
        return docOccurenceList.get(Doc_i);
    }

    public void Integrate(Index_hashMap someNew){
        //Todo: link Posting lists here..
    }

    public int getDocumentCount(){
        return n_Docs;
    }

    public Set<Integer> getKeySet(){
        return docOccurenceList.keySet();
    }

    public void setFrequency(Integer doc_i,Integer freq){
        docOccurenceList.put(doc_i, freq);
    } 


}


