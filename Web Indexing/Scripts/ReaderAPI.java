
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tika.exception.TikaException;

public interface ReaderAPI {

    
    public void readFile(String filename) throws FileNotFoundException, IOException, TikaException;

    
    public String getLine();

    
    public void closeFile();
    
    
}
