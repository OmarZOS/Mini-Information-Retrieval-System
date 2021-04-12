package Scripts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Evaluator {

    
    private ArrayList<Integer> idealResults =null; 
    private ArrayList<String> currentQuery = new  ArrayList<String>();


    public static void main(String[] args) throws IOException{
        //for the first time to index, use these two lines
        // termWeighting.mapDirectory("Quran", "Linguistics", 5," \"–0123456789_-.,;:!/'()[]%");
        // MyDictionary.getInstance().save_index_to_disk("lexicon", "Postlist","docProps");
        

        MyDictionary.getInstance().read_index_from_disk("lexicon","Postlist","docProps");

        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter your query");

        boolean keep=true;
        String query = scanner.nextLine();
        
        String delimiters= " \"–0123456789_-.,;:!/'()[]%";

        ArrayList<String> list_of_stopwords = MyTokenizer.tokenizeFileContent("Linguistics/stopwords.txt"," ");

        Evaluator evaluator = new Evaluator();

        while(keep){

            evaluator.Index_Query(query,delimiters,list_of_stopwords,5);
            
            System.out.println("Response Using Okapi RSV : ");


            Map<Integer,Double> x= evaluator.Evaluate_Query();//Okapi_Evaluate_Query(1.3,0.75)
            if(x.equals(null))
                System.out.println("No results found");
            else
                System.out.println(x);




            System.out.println("Enter your query");

            query = scanner.nextLine();
            if(query.equals(""))
                keep=false;

        }

        scanner.close();



    }


    public void loadIdealResults(ArrayList<Integer> userSet){
        idealResults=userSet;
    } 


    public Float calculateScore(){





        
        return null;
    }
    
    public void Index_Query(String queryText,String delimiters,ArrayList<String> list_of_stopwords ,int trunc) throws NullPointerException, IOException{
        
        currentQuery=Stemmer.stemming(StopWordRemover.stopWordRemove(MyTokenizer.tokenizeText(queryText, delimiters),list_of_stopwords), trunc);
       // System.out.println(currentQuery);
        
        for (int i=0;i<currentQuery.size(); i++) {
            String term=currentQuery.get(i);
            if(!MyDictionary.getInstance().contains(term)){
                //System.out.println(term);
                currentQuery.remove(term);
            }
        }

        //System.out.println(currentQuery);

    }

    public Map<String,Index_hashMap> Init_Posting_List_of_Query_on_Memory(){
        Map<String,Index_hashMap> response = new  HashMap<String,Index_hashMap>();
        if(currentQuery.size()<0)
            return null;

        for (int i=0;i<currentQuery.size(); i++) {
            String token=currentQuery.get(i);
            if(MyDictionary.getInstance().contains(token)){
                response.put(token,Posting_List_of_Query_on_Memory_step_1(token));
            }
        }

        return response;
    }
    
    public Index_hashMap Posting_List_of_Query_on_Memory_step_1(String token){
        //System.out.println(MyDictionary.getInstance().getIndex(token));
        return MyDictionary.getInstance().getIndex(token);

    }


    public Map<Integer,Double> Evaluate_Query(){

        Map<Integer,Double> Query_Doc_Scores = new HashMap<Integer,Double>();

        Map<String,Index_hashMap> response = Init_Posting_List_of_Query_on_Memory();

        if(response.equals(null))
            return null;

        for (String token : response.keySet()) {
            for ( Integer doc : response.get(token).getKeySet()) {
                
                if(Query_Doc_Scores.containsKey(doc)){
                    Query_Doc_Scores.put(doc,Query_Doc_Scores.get(doc)+TF_Computing.computing_TF(token,doc)*termWeighting.DF_Computing(token));
                }
                else{
                    // System.out.println(TF_Computing.computing_TF(token,doc));
                    // System.out.println(termWeighting.DF_Computing(token));
                    // System.out.println(TF_Computing.computing_TF(token,doc)*termWeighting.DF_Computing(token));
                    Query_Doc_Scores.put(doc, TF_Computing.computing_TF(token,doc)*termWeighting.DF_Computing(token));
                }

            }

        }

        return Query_Doc_Scores;

    }

    public Map<Integer,Double> Okapi_Evaluate_Query(Double k,Double b){

        Map<Integer,Double> Query_Doc_Scores = new HashMap<Integer,Double>();

        Map<String,Index_hashMap> response = Init_Posting_List_of_Query_on_Memory();

        if(response.equals(null))
            return null;

        Float avgdl=MyDictionary.getInstance().avgdl();

        //System.out.println(avgdl);


        for (String token : response.keySet()) {


            for ( Integer doc : response.get(token).getKeySet()) {
                
                try {
                    if(Query_Doc_Scores.containsKey(doc)){
                        Query_Doc_Scores.put(doc,Query_Doc_Scores.get(doc)+ OkapiFormulae(doc, token, avgdl, k, b));
                    }
                    else{
    
                        //System.out.println("is this a value? ");
    
                        Query_Doc_Scores.put(doc, OkapiFormulae(doc, token, avgdl, k, b));
                    }
                    
                } catch (Exception e) {
                    
                }


            }

        }

        return Query_Doc_Scores;

    }

    public Double OkapiFormulae(Integer doc, String token,Float avgdl,Double k,Double b){

        // System.out.println("IDF "+termWeighting.IDF_Computing(token));

        Double value = (double) (termWeighting.IDF_Computing(token)
        *
        MyDictionary.getInstance().getIndex(token).getFrequency(doc)
        *(k+1)/(
            MyDictionary.getInstance().getIndex(token).getFrequency(doc)
            +
            k
            *
            (
                1-b+b*MyDictionary.getInstance().getSize(doc)
                /
                avgdl

            )
        ));
        //System.out.println(value);


        return value;


    }


}
