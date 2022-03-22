import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import org.apache.commons.io.FileUtils;
import logic.OFTENAnalysis;
import model.ConstantCodes;
import model.OftenDTO;
import util.HTMLGenerator;


public class RunOFTENWorker extends SwingWorker<Boolean, String>  {

	private JTextArea tAConsole;
	private JButton btnRunMethod;
	private JProgressBar pbProgress;
	private OftenDTO oftenDTO;
	private OFTENMethod oftenMethod;
	private File df;
	private String analysisprefix;
	private String path2PPINetwork;
	private int OFTENnstart=100, OFTENnstep=50, OFTENnend=600, OFTENnperm=100;
	private File wfOFTEN;
	private String oftenSummary;
	private String oftenReport;
	public Thread executionThread;
	public boolean already_canceled = false;	
	
	private OFTENMethod OFTENMethodDialog;
	
	public String action = ConstantCodes.ERROR;
	
	public RunOFTENWorker(OFTENMethod OFTENMethodDialog, OftenDTO oftenDTO, OFTENMethod oftenMethod){
		this.OFTENMethodDialog = OFTENMethodDialog;
		this.tAConsole = OFTENMethodDialog.tAConsole;
		this.btnRunMethod = OFTENMethodDialog.btnRunMethod;
		this.pbProgress = OFTENMethodDialog.pbProgress;
		this.oftenDTO = oftenDTO;
		this.oftenMethod = oftenMethod;
	}
	
	
	@Override
	protected Boolean doInBackground() throws Exception {
		pbProgress.setIndeterminate(true);
		Border progressBorder = BorderFactory.createTitledBorder("Running...");
		pbProgress.setBorder(progressBorder);
		
		if(init()){
			
			OFTENAnalysis.stop_execution = false;
			OFTENMethodDialog.btnRunMethod.setEnabled(false);
			OFTENMethodDialog.removeWindowListener(OFTENMethodDialog.windowHandler);
			executionThread = new Thread(new Runnable() {
			    //private Process process;
			    @Override
			    public void run() {
			    	try { 			
			    		doOFTEN();
			    	}catch(Exception e) {
			    		done();
			    	}
			    }
			});
		
			executionThread.start();
			while((!executionThread.isInterrupted())&(executionThread.isAlive()));
						
		}
        
		OFTENMethodDialog.btnRunMethod.setEnabled(true);	
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
		oftenMethod.setEnabled(true);
		
		OFTENMethodDialog.btnRunMethod.setEnabled(true);
		
		switch(action){
			case ConstantCodes.CANCELED: 
				if(!already_canceled) {
					already_canceled = true;
					OFTENAnalysis.stop_execution = true;
					OFTENMethodDialog.addWindowListener(OFTENMethodDialog.windowHandler);
					publish("Cancelling process...");	
					JOptionPane.showMessageDialog (null, "Process has been cancelled.", "CANCEL", JOptionPane.INFORMATION_MESSAGE);
				}
			break;
			case ConstantCodes.FINISHED:
				JOptionPane.showMessageDialog (null, "Process has been successfully finished.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				
				Vector<File> vf = new Vector<File>();
				File fls[] = wfOFTEN.listFiles();
				for(int k=0;k<fls.length;k++)if(fls[k].getName().endsWith(".xgmml"))
					vf.add(fls[k]);
				for(int k=0;k<fls.length;k++)if(fls[k].getName().endsWith("_st.txt"))
					vf.add(fls[k]);

				File[] files = new File[2+vf.size()];
				files[0] = new File(oftenSummary);
				files[1] = new File(oftenReport);
				for(int k=0;k<vf.size();k++)
					files[2+k] = vf.get(k);
					
				try {
					HTMLGenerator.generateOFTENHtml("OFTEN Analysis","OFTEN",files,analysisprefix,oftenDTO);
				} catch (Exception e) {
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
		String parts[] = oftenDTO.getDoOften().split("#");
		if(parts.length==2){
			String ns[] = parts[1].split(",");
			OFTENnstart = Integer.parseInt(ns[0]);
			OFTENnstep = Integer.parseInt(ns[1]);
			OFTENnend = Integer.parseInt(ns[2]);
			OFTENnperm = Integer.parseInt(ns[3]);
		}
		path2PPINetwork = parts[0];
		

		File wf = new File(oftenDTO.getDefaultWorkFolderPath());
		if(!wf.exists()){
			if(!wf.mkdir()){
				publish("FATAL ERROR: Can not create working folder \""+oftenDTO.getDefaultWorkFolderPath()+"\"");
				return false;
			}
		}
		
		File sf = new File(oftenDTO.getSTablePath());
		if(sf.exists()){
			analysisprefix = sf.getName().substring(0, sf.getName().length()-4);
			if(analysisprefix.endsWith("_ica_S"))
				analysisprefix = analysisprefix.substring(0, analysisprefix.length()-6);
		}else{
		df = new File(oftenDTO.getDataTablePath());
		if(!df.exists()){
			publish("FATAL ERROR: Can not find the data file \""+oftenDTO.getDataTablePath()+"\"");
			return false;
		}else{
			analysisprefix = df.getName().substring(0, df.getName().length()-4);
		}}
		
		return true;
	}
	
	

	private void doOFTEN() throws Exception{

		publish("=======================================");
		publish("======  Performing OFTEN analysis =====");
		publish("=======================================");
		
		OFTENAnalysis of = new OFTENAnalysis();
		publish("Loading PPI network from "+path2PPINetwork);
		of.loadPPINetwork(path2PPINetwork);
		publish("Network loaded.");
		File wfica = new File(oftenDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_ICA");
		wfOFTEN = new File(oftenDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_OFTEN");
		wfOFTEN.mkdir();
		
		File sfile = null;
		if(!oftenDTO.getSTablePath().equals(""))
			sfile = new File(oftenDTO.getSTablePath());
		else
			sfile = new File(wfica.getAbsolutePath()+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls");
		oftenDTO.setSTablePath(sfile.getAbsolutePath());
		
		OFTENMethodDialog.btnStopMethod.setEnabled(true);
		
		FileUtils.cleanDirectory(wfOFTEN);
		String smatrixfile = sfile.getAbsolutePath();
		publish("Copying S-file "+smatrixfile+"... ");
		String tablePath = oftenDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_OFTEN"+File.separator+analysisprefix+"_ica_S.xls"; 
		if(new File(tablePath).exists()){
			FileUtils.deleteQuietly(new File(tablePath));
		}
		FileUtils.copyFileToDirectory(new File(smatrixfile), wfOFTEN, true);
		of.numberOfPermutationsForSizeTest = OFTENnperm;
		
		HashMap<String,String> significanceTables = new HashMap<String,String>(); 
		String report = of.completeOFTENAnalysisOfTable(tablePath,analysisprefix,OFTENnstart,OFTENnend,OFTENnstep,significanceTables);
		
		Set<String> files = significanceTables.keySet();
		for(String fn:files){
			logic.Utils.writeStringToFile(significanceTables.get(fn), wfOFTEN.getAbsolutePath()+File.separator+analysisprefix+"_"+fn+"_st.txt");
		}

		String reportSummary = OFTENAnalysis.extractScoresFromCombinedOFTENReport(report);
		
		logic.Utils.writeStringToFile(report, wfOFTEN.getAbsolutePath()+File.separator+"_OFTENreport.txt");
	
		oftenSummary = wfOFTEN.getAbsolutePath()+File.separator+"_OFTENreportSummary.txt";
		oftenReport = wfOFTEN.getAbsolutePath()+File.separator+"_OFTENreport.txt";
		
		logic.Utils.writeStringToFile(reportSummary, oftenSummary);
		
		action = ConstantCodes.FINISHED;
	}

}
