package Scripts;


import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import Scripts.termWeighting;   //I imported it to verify if the ID's already exist..


public class IndexMakerV1 implements IndexMaker {
    @Override
    public HashMap<String,Integer> list_of_ID(HashMap<String,Integer> list_of_frequencies){
        HashMap<String,Integer> ID_Array =new HashMap<String,Integer>() ;
        
        for(String s: list_of_frequencies.keySet())
                ID_Array.put(s, (termWeighting.dictionary.containsKey(s))?termWeighting.dictionary.get(s):termWeighting.ID_Token++);

        return ID_Array;

    }

    @Override
    public HashMap<Integer, Integer> CastMapKey(HashMap<String, Integer> list_of_frequencies) {
        HashMap<Integer,Integer> castedMap = new HashMap<Integer,Integer>();
        



        return castedMap;
    } 

}
