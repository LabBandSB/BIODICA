package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class PropertiesEx extends Properties {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void load(FileReader fr) throws IOException 
    {
        Scanner in = new Scanner(fr);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
    	try
        {
            while(in.hasNext()) 
            {	
                out.write(in.nextLine().replace("\\","\\\\").getBytes());
                out.write("\n".getBytes());
            }
        }
        finally
        {
        	in.close();
        }
        InputStream is = new ByteArrayInputStream(out.toByteArray());
        super.load(is);
    }
}