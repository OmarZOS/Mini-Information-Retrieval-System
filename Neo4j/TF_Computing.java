package Neo4j;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;




public class TF_Computing {

    public static void main(String[] argv) throws NullPointerException, IOException{

    }

    @Deprecated
    public static HashMap<String,Integer> term_frequency_computing(ArrayList<String> list_of_stems){
        Map<String,Integer> list_of_stems_with_frequencies = new HashMap<String,Integer>();

        for(String stem : list_of_stems){
            
            
            if(list_of_stems_with_frequencies.containsKey(stem)){
                
                list_of_stems_with_frequencies.put(stem,list_of_stems_with_frequencies.get(stem)+1);
            }
            else
                list_of_stems_with_frequencies.put(stem,1);

        }

        

        return (HashMap<String, Integer>) list_of_stems_with_frequencies;
    }

    
    
}
