package Scripts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;




public class TF_Computing {

    public static void main(String[] argv) throws NullPointerException, IOException{
        ArrayList<String> list_of_tokens = MyTokenizer.tokenizeFileContent("Corpus/doc1.txt"," _-.,;:!/'()[]");
        ArrayList<String> list_of_stopwords = MyTokenizer.tokenizeFileContent("Linguistics/stopwords.txt"," ");


        //this messy instruction does the processes of : StopWordRemoval,stemming,frequencyComputing..
        HashMap<String,Integer> list_of_stems_with_frequencies=term_frequency_computing(Stemmer.stemming(StopWordRemover.stopWordRemove(list_of_tokens,list_of_stopwords), 5));

        for(String key : list_of_stems_with_frequencies.keySet()){
            System.out.println(key+"  "+list_of_stems_with_frequencies.get(key).toString());
        }

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

    public static Double computing_TF(String term,Integer Doc_i){
        if(MyDictionary.getInstance().contains(term)){
            return 1+Math.log(MyDictionary.getInstance().getIndex(term).getFrequency(Doc_i));
        }
        return 0.0;
    }

    
}
