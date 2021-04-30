
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.Map;



public class Evaluator {

    public static void main(String[] args) throws IOException{

        Evaluator evaluator= new Evaluator();
        evaluator.loadAssessments("assessments.txt");


        
        File resultDirectory = new File("Retrieval_Results");
        String[] filenames = resultDirectory.list();

        for(int i_Doc=0;i_Doc<filenames.length;i_Doc++ ){//each system..
            System.out.println("\n**************************************************************************\n");
            System.out.println("Evaluating "+ filenames[i_Doc]);


            File systemDirectory = new File("Retrieval_Results/"+filenames[i_Doc]);
            String[] queryNames = systemDirectory.list();
            // for (String quern : queryNames) {
            //     System.out.println(quern);
            // }
            for(int i_Query=0;i_Query<queryNames.length;i_Query++ ){//each query file
                evaluator.listMeasures("Retrieval_Results/"+filenames[i_Doc]+"/"+queryNames[i_Query],queryNames[i_Query].replace(".results",""),5,5);
            }
            System.out.println("\n****** Map measure value "+evaluator.MAP("Retrieval_Results/"+filenames[i_Doc], "assessments.txt"));



        }

        




    }










    class Assessment{
        int n_Docs;
        public ArrayList<String> response=new ArrayList<String> ();
        Assessment(int docs){
            
            n_Docs=docs;
        }
    }
    HashMap<String,Assessment> response= new HashMap<String,Assessment>();
    public HashMap<String,Assessment> assessments = new HashMap<String,Assessment> ();
    

    public void loadAssessments(String assessments_file) throws IOException{
        BufferedReader dataFile = new BufferedReader(new FileReader(assessments_file));
        
        String curLine=dataFile.readLine();
        while(curLine!=null){    
            ArrayList<String> tokens = MyTokenizer.tokenizeText(curLine,"\t ");
            // System.out.println(tokens);
            // System.out.println(tokens.get(0));
            Assessment element= new Assessment(Integer.parseInt(tokens.get(1)));
            // System.out.println(tokens.get(0).toString());
            for(int i = 0;i<element.n_Docs;i++){
                element.response.add(dataFile.readLine().toString());    
            }
            //System.out.println("putting "+tokens.get(0).toString());
            assessments.put(tokens.get(0).toString(),element);

            curLine=dataFile.readLine();
        }
        dataFile.close();



    }



    Double recall (String result_file,String query){
        Double recallResult=0.0;
        //System.out.println("query : "+ query);
        
        MyFileReader docReader = new MyFileReader();
        ArrayList<String> list_of_tokens=new ArrayList<String>();
        docReader.readFile(result_file);
        String curLine=docReader.getLine();
        int count=0;
        // Response element = new Response();
        while(curLine!=null){
            
            if(assessments.containsKey(query))
                if(assessments.get(query).response.contains(curLine))
                    recallResult++;
            count++;
            // element.responseDocs.add(curLine);
            curLine=docReader.getLine();
        }


        recallResult=recallResult/count;
            
        docReader.closeFile();



        return recallResult;
    }


    Double precision (String result_file,String query){
        Double recallResult=0.0;

        MyFileReader docReader = new MyFileReader();
        ArrayList<String> list_of_tokens=new ArrayList<String>();
        docReader.readFile(result_file);
        String curLine=docReader.getLine();
        // Response element = new Response();
        while(curLine!=null){
            if(assessments.containsKey(query))
            if(assessments.get(query).response.contains(curLine))
                recallResult++;
            // element.responseDocs.add(curLine);
            curLine=docReader.getLine();
        }
        if(assessments.containsKey(query))
        recallResult=recallResult/assessments.get(query).n_Docs;
        docReader.closeFile();
        return recallResult;
    }
    
    Double F_measure(String result_file,String query){
        Double p=precision(result_file, query),r=recall(result_file, query);
        return 2*p*r/(r+p);
    }

    Double Precision_at_K(String result_file,String query,int k){
        Double precisionResult=0.0;
        MyFileReader docReader = new MyFileReader();
        ArrayList<String> list_of_tokens=new ArrayList<String>();
        docReader.readFile(result_file);
        String curLine=docReader.getLine();
        // Response element = new Response();
        int count=0;
        while(curLine!=null){
            // System.out.println("was here");
            count++;
            if(assessments.containsKey(query))
                if(assessments.get(query).response.contains(curLine)){
                    precisionResult++;
                }
            curLine=docReader.getLine();
        }
        docReader.closeFile();
		

        return precisionResult/count;

    }


    Double Precision_at_R(String result_file,String query,int R){
        Double precisionResult=0.0;
        MyFileReader docReader = new MyFileReader();
        ArrayList<String> list_of_tokens=new ArrayList<String>();
        docReader.readFile(result_file);
        String curLine=docReader.getLine();
        // Response element = new Response();
        int count=0;
        while(curLine!=null){
            if(count++>=R)
                break;
                if(assessments.containsKey(query))
            if(assessments.get(query).response.contains(curLine)){
                precisionResult++;
            }
            curLine=docReader.getLine();
        }
        docReader.closeFile();
        if(assessments.containsKey(query))
        precisionResult=precisionResult/assessments.get(query).n_Docs;

        return precisionResult;

    }

    Double Ave_P(String result_file,String query){
        Double precisionResult=0.0;
        MyFileReader docReader = new MyFileReader();
        ArrayList<String> list_of_tokens=new ArrayList<String>();
        docReader.readFile(result_file);
        String curLine=docReader.getLine();
        // Response element = new Response();
        int count=0;
        Double relevants=0.0;
        while(curLine!=null){
            count++;
            if(assessments.containsKey(query))
            if(assessments.get(query).response.contains(curLine)){
                precisionResult+=(++relevants)/count;
            }
            curLine=docReader.getLine();
        }
        docReader.closeFile();
        if(assessments.containsKey(query))
        precisionResult=precisionResult/assessments.get(query).n_Docs;

        return precisionResult;

    }

    Double MAP(String directory,String assessments_file) throws IOException{
        Double map=0.0;
        File testDirectory = new File(directory);
        loadAssessments(assessments_file);
        String[] filenames = testDirectory.list();
        for(int i_Doc=0;i_Doc<filenames.length;i_Doc++ ){
            map+=Ave_P(directory+"/"+filenames[i_Doc],filenames[i_Doc].replace(".results", ""));
        }

        map/=filenames.length;

        return map;
    }

    void listMeasures(String result_file,String query,int k, int rank){
        // System.out.println(result_file);
        System.out.println("\n"+query+"-------------------------------------");
        System.out.println("Recall value : "+ recall ( result_file, query));
        System.out.println("Precision value : "+ precision ( result_file, query));
        System.out.println("F_measure value : "+ F_measure ( result_file, query));
        System.out.println("Precision_at_K value : "+ Precision_at_K(result_file, query,k));
        System.out.println("Precision_at_R value : "+ Precision_at_R(result_file, query,rank));
    }


}
