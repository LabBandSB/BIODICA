package util;

import java.awt.Cursor;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JFrame;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingWorker;

public class FileDownloadWorker  extends SwingWorker<Void, Void> {
    /*
     * Main task. Executed in background thread.
     */
	
	private String fileName;
	private InputStream jarPdf;
	private JFrame jParent;
	private ProgressMonitorInputStream pmis;
	
	public FileDownloadWorker(String fileName, JFrame jParent)
	{
		this.fileName = fileName;
		this.jParent = jParent;
		jarPdf= getClass().getClassLoader().getResourceAsStream(fileName);
	}
	
    @Override
    public Void doInBackground() throws IOException, InterruptedException {
          //Initialize progress property.
        setProgress(0);       
        if (Desktop.isDesktopSupported())   
        {   	          
        	File pdfTemp = new File(fileName);
        	//pdfTemp.deleteOnExit();
            try (OutputStream os = new FileOutputStream("BIODICA_manual.pdf")) {
                pmis = new ProgressMonitorInputStream(
                		jParent,
                        "Downloading file...",
                        jarPdf);

                pmis.getProgressMonitor().setMillisToPopup(0);
                byte[] buffer = new byte[2048];
                int nRead = 0;

                while ((nRead = pmis.read(buffer)) != -1) {
                    os.write(buffer, 0, nRead);
                    Thread.sleep(5);
                }
            }
            Desktop.getDesktop().open(pdfTemp);
        }
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
    	try {
            pmis.close();
            jParent.setCursor(Cursor.getDefaultCursor());
        } catch (Exception e) {
        }
    }
}
