

import java.io.File;
import org.apache.tika.Tika;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.google.common.net.MediaType;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.odf.OpenDocumentParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;







public class TikaParser implements ReaderAPI{

    public static void main(String[] argv){

    }

    String content;

    @Override
    public void readFile(String filename) throws IOException, TikaException {
        //detecting the file type
      BodyContentHandler handler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      FileInputStream inputstream = new FileInputStream(new File("example.odp"));
      ParseContext pcontext = new ParseContext();
                
      //Open Document Parser
      OpenDocumentParser openofficeparser = new OpenDocumentParser (); 
        openofficeparser.parse(inputstream, handler, metadata,pcontext);
        // TODO Auto-generated catch block
      
      
      
        System.out.println("Contents of the document:" + handler.toString());      


        
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
    
    public static String detectDocTypeUsingDetector(InputStream stream) 
    throws IOException {
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

        org.apache.tika.mime.MediaType mediaType = detector.detect(stream, metadata);
        return mediaType.toString();
    }


}
