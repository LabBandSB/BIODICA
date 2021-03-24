package util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileHelper {

	public void downloadFile(String fileName)
	{
		if (Desktop.isDesktopSupported())   
        {   
            InputStream jarPdf = getClass().getClassLoader().getResourceAsStream(fileName);
            try {
                File pdfTemp = new File(fileName);
                pdfTemp.deleteOnExit();
                
                FileOutputStream fos = new FileOutputStream(pdfTemp);
            
                while (jarPdf.available() > 0) {
                      fos.write(jarPdf.read());
                }  
                fos.close();
                Desktop.getDesktop().open(pdfTemp);
            }   
            catch (IOException ec) {
            }   
        }
	}
	
	
	   
    public static boolean openFile(File file)
    {
        try
        {
            if (OSDetector.isWindows())
            {
                Runtime.getRuntime().exec(new String[]
                {"rundll32", "url.dll,FileProtocolHandler",
                 file.getAbsolutePath()});
                return true;
            } else if (OSDetector.isLinux() || OSDetector.isMac())
            {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
                                                       file.getAbsolutePath()});
                return true;
            } else
            {
                // Unknown OS, try with desktop
                if (Desktop.isDesktopSupported())
                {
                    Desktop.getDesktop().open(file);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace(System.err);
            return false;
        }
    }
	
	
	
}
