import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;

import util.FileDownloadWorker;


public class FileDownloadProgressMonitor implements PropertyChangeListener{
	private ProgressMonitor progressMonitor;
	private JFrame jParent;
	private FileDownloadWorker fileDownloadWorker;
	
	
	public FileDownloadProgressMonitor(JFrame jParent)
	{
		this.jParent = jParent;
	}
	
	public void downloadFile()
	{
		progressMonitor = new ProgressMonitor(jParent,
                                  "Running a Long Task",
                                  "", 0, 100);
        progressMonitor.setProgress(0);
        //fileDownloadWorker = new FileDownloadWorker("BIODICA_manual.pdf");
        fileDownloadWorker.addPropertyChangeListener(this);
        fileDownloadWorker.execute();
        //startButton.setEnabled(false);
	}
	
	 public void propertyChange(PropertyChangeEvent evt) {
    if ("progress" == evt.getPropertyName() ) {
        int progress = (Integer) evt.getNewValue();
        progressMonitor.setProgress(progress);
        String message =
            String.format("Completed %d%%.\n", progress);
        progressMonitor.setNote(message);
        //taskOutput.append(message);
        if (progressMonitor.isCanceled() || fileDownloadWorker.isDone()) {
            if (progressMonitor.isCanceled()) {
            	fileDownloadWorker.cancel(true);
                //taskOutput.append("Task canceled.\n");
            } else {
                //taskOutput.append("Task completed.\n");
            }
            //startButton.setEnabled(true);
        }
    }

}
	
}
