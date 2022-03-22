

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import org.apache.commons.io.FileUtils;

import logic.BIODICASecurityManager;
import logic.MetaGeneAnnotation;
import logic.OFTENAnalysis;
import model.ConstantCodes;
import model.TOPPGeneDTO;


public class RunTOPPGeneWorker extends SwingWorker<Boolean, String>  {
	
	private JTextArea tAConsole;
	private JButton btnRunMethod;
	private JProgressBar pbProgress;
	private TOPPGeneDTO toppgeneDTO;
	private File df;
	private String analysisprefix;
	private TOPPGeneAnalysis TOPPGeneMethod;
	public String action = ConstantCodes.ERROR;
	private File wfgsear;
	public Thread executionThread;
	public boolean already_canceled = false;
	
	private TOPPGeneAnalysis TOPPGeneAnalysisDialog;
	
	public RunTOPPGeneWorker(TOPPGeneAnalysis TOPPGeneAnalysisDialog, TOPPGeneDTO toppgeneDTO,TOPPGeneAnalysis toppgeneMethod)
	{
	
		this.TOPPGeneAnalysisDialog = TOPPGeneAnalysisDialog;
		this.tAConsole = TOPPGeneAnalysisDialog.tAConsole;
		this.btnRunMethod = TOPPGeneAnalysisDialog.btnRunMethod;
		this.pbProgress = TOPPGeneAnalysisDialog.pbProgress;
		this.toppgeneDTO = toppgeneDTO;
		this.TOPPGeneMethod = toppgeneMethod;
	}
	
	
	@Override
	protected Boolean doInBackground() throws Exception {
		pbProgress.setIndeterminate(true);
		Border progressBorder = BorderFactory.createTitledBorder("Running...");
		pbProgress.setBorder(progressBorder);
		
		if(init()){
			
			logic.UseTOPPgeneSuiteForEnrichment.stop_execution = false;
			TOPPGeneAnalysisDialog.btnRunMethod.setEnabled(false);
			TOPPGeneAnalysisDialog.removeWindowListener(TOPPGeneAnalysisDialog.windowHandler);
			executionThread = new Thread(new Runnable() {
			    //private Process process;
			    @Override
			    public void run() {
			    	try { 			
			    		doTOPPGene();
			    	}catch(Exception e) {
			    		done();
			    	}
			    }
			});
		
			executionThread.start();
			while((!executionThread.isInterrupted())&(executionThread.isAlive()));
			
		}
			
		TOPPGeneAnalysisDialog.btnRunMethod.setEnabled(true);		
		return true;
	}

	@Override
	protected void process(List<String> outputs) 
	{
		for (final String output : outputs) 
		{
			tAConsole.append(output + "\n");
	    }
	}

	@Override
	protected void done() {
		btnRunMethod.setEnabled(true);
		pbProgress.setIndeterminate(false);
		Border progressBorder = BorderFactory.createTitledBorder("");
		pbProgress.setBorder(progressBorder);
		
		TOPPGeneAnalysisDialog.btnRunMethod.setEnabled(true);
				
		switch(action){
			case ConstantCodes.CANCELED: 
				if(!already_canceled) {
					already_canceled = true;
					logic.UseTOPPgeneSuiteForEnrichment.stop_execution = true;
					TOPPGeneAnalysisDialog.addWindowListener(TOPPGeneAnalysisDialog.windowHandler);
					publish("Cancelling process...");
					JOptionPane.showMessageDialog (null, "Process has been cancelled.", "CANCEL", JOptionPane.INFORMATION_MESSAGE);
				}
			break;
			case ConstantCodes.FINISHED:
				TOPPGeneAnalysisDialog.addWindowListener(TOPPGeneAnalysisDialog.windowHandler);				
				JOptionPane.showMessageDialog (null, "Process has been successfully finished.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
			try {
				Desktop.getDesktop().browse(new File(wfgsear.getAbsolutePath() + File.separator + "toppgeneReport.html").toURI());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
			default:
				JOptionPane.showMessageDialog (null, "An error occurred. ", "ERROR", JOptionPane.ERROR_MESSAGE);
				break;
		}
		super.done();
	}
	
	private boolean init()
	{		
		File wf = new File(toppgeneDTO.getDefaultWorkFolderPath());
		if(!wf.exists()){
			if(!wf.mkdir()){
				publish("FATAL ERROR: Can not create working folder \""+toppgeneDTO.getDefaultWorkFolderPath()+"\"");
				return false;
			}
		}
		File sf = new File(toppgeneDTO.getSTablePath());
		if(sf.exists()){
			analysisprefix = sf.getName().substring(0, sf.getName().length()-4);
			if(analysisprefix.endsWith("_ica_S"))
				analysisprefix = analysisprefix.substring(0, analysisprefix.length()-6);
		}else{
		df = new File(toppgeneDTO.getDataTablePath());
		if(!df.exists()){
			publish("FATAL ERROR: Can not find the data file \""+toppgeneDTO.getDataTablePath()+"\"");
			return false;
		}else{
			analysisprefix = df.getName().substring(0, df.getName().length()-4);
		}}
		
		return true;
	}
	
	
	private void doTOPPGene() throws Exception{
		
		publish("==========================================");
		publish("======  Performing TOPPGENE analysis =====");
		publish("==========================================");
		
		File wfica = new File(toppgeneDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_ICA");
		File wftoppgene = new File(toppgeneDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_TOPPGENE");
		wftoppgene.mkdir();
		File sfile = null;
		if(!toppgeneDTO.getSTablePath().equals(""))
			sfile = new File(toppgeneDTO.getSTablePath());
		else
			sfile = new File(wfica.getAbsolutePath()+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls");
		
		Date timestart = new Date();
		
		wfgsear = new File(toppgeneDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_TOPPGENE"+System.getProperty("file.separator"));
		
		TOPPGeneAnalysisDialog.btnStopMethod.setEnabled(true);
		
		String name = analysisprefix;
		
		logic.UseTOPPgeneSuiteForEnrichment.completeAnalysisOfMetaGeneFile(sfile.getAbsolutePath(),name,wftoppgene.getAbsolutePath()+System.getProperty("file.separator"),toppgeneDTO.getThresholdForPvalue(),toppgeneDTO.getNumberOfGene(),toppgeneDTO.getDoActualCalculations());

		File toppgene = new File(toppgeneDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_TOPPGENE"+System.getProperty("file.separator")+"toppgene");
		toppgene.mkdir();
		FileUtils.copyFile(new File(toppgeneDTO.getHTMLSourceFolder()+System.getProperty("file.separator")+"toppgene"+System.getProperty("file.separator")+"output.js"), new File(wfgsear.getAbsolutePath()+System.getProperty("file.separator")+"toppgene"+System.getProperty("file.separator")+"output.js"));
		FileUtils.copyFile(new File(toppgeneDTO.getHTMLSourceFolder()+System.getProperty("file.separator")+"toppgene"+System.getProperty("file.separator")+"styles.css"), new File(wfgsear.getAbsolutePath()+System.getProperty("file.separator")+"toppgene"+System.getProperty("file.separator")+"styles.css"));
		
		System.out.println("Finished all ToppGene computations. Total time spent "+((new Date().getTime()-timestart.getTime())/1000)+" secs");

		
		action = ConstantCodes.FINISHED;
	}
	
	
	public static void emptyFolder(File folder, boolean deleteTheFolderItself) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                emptyFolder(f, true);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    if(deleteTheFolderItself)
	    	folder.delete();
	}
}
