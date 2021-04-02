package Scripts;

import java.io.File;
import org.apache.tika.Tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
//import org.xml.sax.SAXException;
//import org.apache.tika.Tika;

//import org.xml.sax.SAXException;



public class TikaParser implements ReaderAPI{

    public static void main(String[] argv){

    }

    String content;

    @Override
    public void readFile(String filename) {
        InputStream stream;
        try {
            stream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
            


        parser.parse(stream, handler, metadata);
                
            
        
    }

    @Override
    public String getLine() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void closeFile() {
        // TODO Auto-generated method stub
        
    }
    
}
