







import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class JsoupImplementation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        String url = "http://www.mdn.dz/";

        
        //String url = "https://jsoup.org/cookbook/extracting-data/example-list-links" ;
        
        // print("Fetching %s...", url);
        
        Document doc = Jsoup.connect(url).get();
        
        // //System.out.println(doc.body()) ;
        
        //System.exit(0) ;
        
        Elements a_links = doc.select("a");
        for (Element a_link :  a_links ) {
            // //System.out.println(a_link.attr("href")) ;
            // System.out.println(a_link.attr("abs:href")) ;
        }
        
        
        
        System.out.println(doc.select("body").text()); //---- Display all text in the body element and its sub-elements
        
        
        
        //---- Display the content of the meta tags
        
        Elements meta_tags = doc.select("meta");
        /*        
        for (Element _meta :  meta_tags ) {
            // System.out.println("meta attribute : " + _meta.attr("name")) ;
            // System.out.println("meta value     : " + _meta.attr("content")) ;
            // System.out.println("***********************************************") ;
        }
        */        
        
        //---- Display the content of the meta tag "keywords"
        meta_tags = doc.select("meta");
        for (Element _meta :  meta_tags ) {
            
            if (_meta.attr("name").equals("keywords")) {
                System.out.println("keywords are   : " + _meta.attr("content")) ;
            }
            
        }
        
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        
        
        // print("\nMedia: (%d)", media.size());
        for (Element src : media) {
        //     if (src.tagName().equals("img"))
        //     print(" * %s: <%s> %sx%s (%s)",
        //     src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
        //    trim(src.attr("alt"), 20));
        //     else;
        //     print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }
        
        // print("\nImports: (%d)", imports.size());
        
        for (Element link : imports) {
            // print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }
        
        // print("\nLinks: (%d)", links.size());
        
        // for (Element link : links) {
        //     print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        // }
    }
    
    static Integer docCount=1;
        
    
    
    public static ArrayList<String> indexPage(String url,String folderName) throws IOException{
        ArrayList<String> links = new ArrayList<String>();
        print("Fetching %s...", url);
    
        Document doc=null;
        try {
            doc = Jsoup.connect(url).get();
            
            Elements a_links = doc.select("a");
            for (Element a_link :  a_links ) {
                links.add(a_link.attr("abs:href"));
                // //System.out.println(a_link.attr("href")) ;
                //System.out.println("Adding "+a_link.attr("abs:href")) ;
            }
    
            String text = doc.select("body").text();
    

            FileOutputStream outputFile = new FileOutputStream(folderName+"/"+url.replace("/",",").replace(":",";"));//UNIX doesn't accept \
    
            outputFile.write(text.getBytes());
            
            docCount++;

        } catch (Exception e){
            System.out.println(e.getMessage());

            System.out.println("HTTP or null Exception.");
            //TODO: handle exception
            return null;
        }



        

        return links;
    }


    
    private static void print(String msg, Object... args) {
         System.out.println(String.format(msg, args));
    }
    
    private static String trim(String s, int width) {
        if (s.length() > width)
        return s.substring(0, width-1) + ".";
        else
        return s;
    }
    
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
