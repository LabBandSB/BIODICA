

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
import logic.GSEAThread;
import logic.MetaGeneAnnotation;
import model.ConstantCodes;
import model.GseaDTO;


public class RunGSEAWorker extends SwingWorker<Boolean, String>  {
	
	private JTextArea tAConsole;
	private JButton btnRunMethod;
	private JProgressBar pbProgress;
	private GSEAMethod GSEAMethodDialog;
	private GseaDTO gseaDTO;
	private File df;
	private String analysisprefix;
	private GSEAMethod gSEAMethod;
	public String action = ConstantCodes.ERROR;
	private File wfgsear;
	public Vector<Thread> GSEAThreads;
	
	public RunGSEAWorker(GSEAMethod GSEAMethodDialog, GseaDTO gseaDTO,GSEAMethod gSEAMethod)
	{
		this.GSEAMethodDialog = GSEAMethodDialog;
		this.tAConsole = GSEAMethodDialog.tAConsole;
		this.btnRunMethod = GSEAMethodDialog.btnRunMethod;
		this.pbProgress = GSEAMethodDialog.pbProgress;
		this.gseaDTO = gseaDTO;
		this.gSEAMethod = gSEAMethod;
	}
	
	
	@Override
	protected Boolean doInBackground() throws Exception {
		pbProgress.setIndeterminate(true);
		Border progressBorder = BorderFactory.createTitledBorder("Running...");
		pbProgress.setBorder(progressBorder);
		if(init()){
			
			GSEAMethodDialog.btnRunMethod.setEnabled(false);
			GSEAMethodDialog.removeWindowListener(GSEAMethodDialog.windowHandler);
			
			doGSEA();
		}
		GSEAMethodDialog.btnRunMethod.setEnabled(true);		
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
		GSEAMethodDialog.btnStopMethod.setEnabled(true);
		GSEAMethodDialog.addWindowListener(GSEAMethodDialog.windowHandler);
		
		pbProgress.setIndeterminate(false);
		Border progressBorder = BorderFactory.createTitledBorder("");
		pbProgress.setBorder(progressBorder);
		gSEAMethod.setEnabled(true);
		switch(action){
		    case ConstantCodes.CANCELED:
				publish("Cancelling process...");
				JOptionPane.showMessageDialog (null, "Process has been cancelled.", "CANCEL", JOptionPane.INFORMATION_MESSAGE);
				break;
			case ConstantCodes.FINISHED:
				JOptionPane.showMessageDialog (null, "Process has been successfully finished.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
			try {
				Desktop.getDesktop().browse(new File(wfgsear.getAbsolutePath() + File.separator + "results_GSEA_filtered.html").toURI());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
			default:
				JOptionPane.showMessageDialog (null, "An error occurred.", "ERROR", JOptionPane.ERROR_MESSAGE);
				break;
		}
		super.done();
	}
	
	private boolean init()
	{		
		File wf = new File(gseaDTO.getDefaultWorkFolderPath());
		if(!wf.exists()){
			if(!wf.mkdir()){
				publish("FATAL ERROR: Can not create working folder \""+gseaDTO.getDefaultWorkFolderPath()+"\"");
				return false;
			}
		}
		File sf = new File(gseaDTO.getSTablePath());
		if(sf.exists()){
			analysisprefix = sf.getName().substring(0, sf.getName().length()-4);
			if(analysisprefix.endsWith("_ica_S"))
				analysisprefix = analysisprefix.substring(0, analysisprefix.length()-6);
		}else{
		df = new File(gseaDTO.getDataTablePath());
		if(!df.exists()){
			publish("FATAL ERROR: Can not find the data file \""+gseaDTO.getDataTablePath()+"\"");
			return false;
		}else{
			analysisprefix = df.getName().substring(0, df.getName().length()-4);
		}}
		
		return true;
	}
	
	
	private void doGSEA() throws Exception{
		
		publish("======================================");
		publish("======  Performing GSEA analysis =====");
		publish("======================================");
		
		File wfica = new File(gseaDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_ICA");
		// Merge all gmt files in one
		File wfgsea = new File(gseaDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_GSEA");
		wfgsea.mkdir();
		MetaGeneAnnotation.MergeGMTFilesInFolder(gseaDTO.getGeneSetFolderPath());
		
		File sfile = null;
		if(!gseaDTO.getSTablePath().equals(""))
			sfile = new File(gseaDTO.getSTablePath());
		else
			sfile = new File(wfica.getAbsolutePath()+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls");
		
		// Make RNK files
		MetaGeneAnnotation.produceRankFilesFromTable(sfile.getAbsolutePath(), wfgsea.getAbsolutePath(), analysisprefix);
		wfgsear = new File(gseaDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_GSEA"+System.getProperty("file.separator")+"results");

		GSEAMethodDialog.btnStopMethod.setEnabled(true);
		
		Date timestart = new Date();
		
		// Launch GSEA in multithread
		if(gseaDTO.isDoAnalysis()){
		// not to do GSEA
		if(wfgsear.exists()){
			System.out.println("Deleting "+wfgsear.getAbsolutePath()+"...");
			emptyFolder(wfgsear, false);
		}
		wfgsear.mkdir();
		System.setSecurityManager(new logic.BIODICASecurityManager());
		File list[] = wfgsea.listFiles();
		
		Vector<File> rnks = new Vector<File>();
		for(File f: list)if(f.getName().endsWith(".rnk")){
			rnks.add(f);
		}
		
		//If the number of threads is too high then do it in batches
		int maxNumThreads = 5;
		Vector<Vector<File>> rnkBatches = new Vector<Vector<File>>(); 
		int numberOfBatches = (int)(rnks.size()*1f/(float)maxNumThreads-0.001f)+1;

		int totalThreads=0;
		for(int batch=0;batch<numberOfBatches;batch++){
		GSEAThreads = new Vector<Thread>();
		// run maxNumThreads threads at a time
		for(int run=0;run<maxNumThreads;run++)if(totalThreads<rnks.size()){
			File f = rnks.get(totalThreads);
			totalThreads++;
			logic.GSEAThread gsea = new logic.GSEAThread();
			gsea.rnkFile = f.getAbsolutePath();
			gsea.gmtFile = gseaDTO.getGeneSetFolderPath()+System.getProperty("file.separator")+"total.gmt";
			gsea.numberOfPermutations = gseaDTO.getNumberOfGSEAPermutations();
			Thread gth = new Thread(gsea);
			GSEAThreads.add(gth);
			gth.start();
			Thread.sleep(10000);
		}
		// Wait when everything will be finished
		int progress = 0;
		int oldprogress = 0;
		while(progress<100){
			Thread.sleep(10000);
			int numberOfActiveThreads = 0;
			for(int i=0;i<GSEAThreads.size();i++){
				if(GSEAThreads.get(i).isAlive())
					numberOfActiveThreads++;
			}
			
			System.out.println("\n\n\n");
			for(int k=0;k<numberOfActiveThreads;k++)
				System.out.print("*");
			for(int k=0;k<GSEAThreads.size()-numberOfActiveThreads;k++)
				System.out.print("x");
			System.out.println("         Number Of Active Threads = "+numberOfActiveThreads);
			publish("         Number Of Active Threads = "+numberOfActiveThreads);
			System.out.println("\n\n\n");
			progress = (int)((1-(float)numberOfActiveThreads/(float)GSEAThreads.size())*100f+0.5);
			if(progress>oldprogress){
				//progress = MetaGeneAnnotation.checkGSEACompletion(folder, totalNumberOfGSEAAnalysis);
				System.out.println("\n\n\n*************************");
				System.out.println("BIODICA GSEA progress "+progress+"%");
				System.out.println("BIODICA GSEA total progress "+(int)(((float)totalThreads)/(float)rnks.size()*100f)+"%");
				publish("BIODICA GSEA total progress "+(int)(((float)totalThreads)/(float)rnks.size()*100f)+"%");
				System.out.println("\n\n\n*************************");
				oldprogress = progress;
			}
		}
		}// end batch
		}

		publish("Finished all GSEA computations. Total time spent "+((new Date().getTime()-timestart.getTime())/1000)+" secs");
		//} // not to do GSEA
		MetaGeneAnnotation.FilterGSEAResults(wfgsear.getAbsolutePath()+System.getProperty("file.separator"),analysisprefix,gseaDTO.getTopMinNumberValue(),gseaDTO.getTopThresholdValue(),gseaDTO.getFDRThresholdValue(),gseaDTO.getPValueThresholdValue());
		
		FileUtils.copyFile(new File(gseaDTO.getHTMLSourceFolder() +System.getProperty("file.separator")+"filteredgsea.css"), new File(wfgsear.getAbsolutePath()+System.getProperty("file.separator")+"filteredgsea.css"));
		
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
