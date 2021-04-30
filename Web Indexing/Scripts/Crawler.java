import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.google.common.net.HostSpecifier;

import org.apache.poi.hssf.record.BoolErrRecord;






public class Crawler {

    public static void main(String[] args) throws IOException{
        Crawler crawler = new Crawler("http://www.onec.dz/",null,4);
        crawler.Start_crawling("crawlData");




    }



    String RootURL="";
/*
    HashMap<String,ArrayList<webPage>> crawlingArea = new  HashMap<String,ArrayList<webPage>>();
*/

//static??
    static ArrayList<webPage> URLListtoDiscover=new ArrayList<webPage>();
    int Depth;

    Crawler(String rootUrl,ArrayList<String> toDiscover,int hostDepth){
        
        Depth=hostDepth;
        if(RootURL.equals(""))
            RootURL=rootUrl;
        
        URLListtoDiscover.add(new webPage(rootUrl,1,1));
        if(toDiscover!=null)
            for (String discoverable : toDiscover) {
                if(isHTMLPage(discoverable)){
                    if(!discoverable.equals(rootUrl))
                        URLListtoDiscover.add(new webPage(discoverable,1,1));
                }
            }

    }

    public void iterateCrawling(ArrayList<String> toDiscover,int hostDepth){
        if(URLListtoDiscover==null)
            URLListtoDiscover=new ArrayList<webPage>();
        if(toDiscover!=null)
            for (String discoverable : toDiscover) {
                if(isHTMLPage(discoverable)){
                    boolean exists=false;
                    for (webPage page : URLListtoDiscover) {

                            if(page.url.equals(discoverable)){
                                exists=true;
                                //TODO: this doesn't update the depth value.. work on it later..
                                URLListtoDiscover.set(URLListtoDiscover.indexOf(page), new webPage(page.url,
                                Math.min(page.depth,hostDepth+1),
                                page.references+1));
                                break;
                            }
                    }
                    if(!exists){
                        URLListtoDiscover.add(new webPage(discoverable,hostDepth+1,1));
                    }
                    
                }
            }
    }



    public static boolean isHTMLPage(String URL){
        return (URL.endsWith(".php")||URL.endsWith(".html")||URL.endsWith(".asp"));
    }


    public void Start_crawling(String folderName) throws IOException{
        
        webPage currentPage = choosePage();
        System.out.println("Chosen page : "+currentPage.url +" Depth = "+currentPage.depth);
        while(currentPage!=null){
            iterateCrawling(JsoupImplementation.indexPage(currentPage.url,folderName),currentPage.depth);
            currentPage = choosePage();
        }
    }

    private webPage choosePage(){
        //and filters them..
        
        //System.out.println("choosing from : "+URLListtoDiscover);

        while(URLListtoDiscover.size()>0){
        
            webPage page=URLListtoDiscover.get(0);
            System.out.println("Choosing : "+ page.url + " Depth : "+page.depth);
            URLListtoDiscover.remove(0);
            if(page.depth<Depth)
                return page;

        }
        // System.out.println("Returning NULL..");
        return null;
    }

    class webPage{
        webPage(String urls,int depths,int referen){
            url=urls;   depth=depths;   references=referen;
        }
        public String url;
        public int depth;
        public int references;
    }


}
