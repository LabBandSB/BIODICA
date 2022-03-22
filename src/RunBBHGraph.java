import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import logic.MakeCorrelationGraph;
import logic.OFTENAnalysis;
import model.BBHGraphDTO;
import model.ConstantCodes;
import model.OftenDTO;
import util.HTMLGenerator;


public class RunBBHGraph extends SwingWorker<Boolean, String> {

	private JTextArea tAConsole;
	private JButton btnRunMethod;
	private JProgressBar pbProgress;
	private BBHGraphDTO bBHGraphDTO;
	private BBHGraph bBHGraph;
	private String analysisprefix;
	private String  folderWithPrecomputedICAResults;
	private boolean splitPositiveAndNegativeTailsForMetaanalysis = false;
	
	public Thread executionThread;
	public boolean already_canceled = false;	
	private BBHGraph BBHGraphDialog;
	
	public String action = ConstantCodes.ERROR;
	
	public RunBBHGraph(BBHGraph BBHGraphDialog, BBHGraphDTO bBHGraphDTO,BBHGraph bBHGraph){
		this.BBHGraphDialog = BBHGraphDialog;
		this.tAConsole = BBHGraphDialog.tAConsole;
		this.btnRunMethod = BBHGraphDialog.btnRunMethod;
		this.pbProgress = BBHGraphDialog.pbProgress;
		this.bBHGraphDTO = bBHGraphDTO;
		this.bBHGraph = bBHGraph;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		pbProgress.setIndeterminate(true);
		Border progressBorder = BorderFactory.createTitledBorder("Running...");
		pbProgress.setBorder(progressBorder);
		
		if(init()){
			
			MakeCorrelationGraph.stop_execution = false;
			BBHGraphDialog.btnRunMethod.setEnabled(false);
			BBHGraphDialog.removeWindowListener(BBHGraphDialog.windowHandler);
			executionThread = new Thread(new Runnable() {
			    //private Process process;
			    @Override
			    public void run() {
			    	try { 			
			    		doBBHGraph();
			    	}catch(Exception e) {
			    		done();
			    	}
			    }
			});
		
			executionThread.start();
			while((!executionThread.isInterrupted())&(executionThread.isAlive()));
					
		}
        
		BBHGraphDialog.btnRunMethod.setEnabled(true);	
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
		bBHGraph.setEnabled(true);
		switch(action){
			case ConstantCodes.CANCELED: 
				if(!already_canceled) {
					BBHGraphDialog.addWindowListener(BBHGraphDialog.windowHandler);
					publish("Cancelling process...");	
					JOptionPane.showMessageDialog (null, "Process has been cancelled.", "CANCEL", JOptionPane.INFORMATION_MESSAGE);
					already_canceled = true;
					MakeCorrelationGraph.stop_execution = true;
				}
			break;
			case ConstantCodes.FINISHED:
				JOptionPane.showMessageDialog (null, "Process has been successfully finished.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				String file = folderWithPrecomputedICAResults+System.getProperty("file.separator")+"correlation_graph_norecipedges.xgmml";
				try{
				HTMLGenerator.generateBBHGraphHtml("RBH Graph Analysis","RBH Graph",file,bBHGraphDTO);
				}catch(Exception e){
					e.printStackTrace();
				}
				break;
			default:
				JOptionPane.showMessageDialog (null, "An error occurred.", "ERROR", JOptionPane.ERROR_MESSAGE);
				break;
		}
		super.done();
	}
	
	private boolean init(){
		folderWithPrecomputedICAResults = bBHGraphDTO.getTfFinalGraph();
		String parts[] = folderWithPrecomputedICAResults.split("#");
		if(parts.length==2){
			folderWithPrecomputedICAResults = parts[0];
			splitPositiveAndNegativeTailsForMetaanalysis = parts[1].trim().equals("split");
		}
		
		return true;
	}
	
	
	
	private void doBBHGraph() throws Exception{
		
		publish("==================================================");
		publish("======  Compute Reciprocally Best Hit graph  =====");
		publish("==================================================");
		
		BBHGraphDialog.btnStopMethod.setEnabled(true);
		
		if(splitPositiveAndNegativeTailsForMetaanalysis)
			MakeCorrelationGraph.SplitAllFilesIntoPositiveAndNegativeTails(folderWithPrecomputedICAResults+System.getProperty("file.separator"));
		MakeCorrelationGraph.MakeCorrelationGraph(folderWithPrecomputedICAResults+System.getProperty("file.separator"), false, false, 3f);
		MakeCorrelationGraph.assembleCorrelationGraph(folderWithPrecomputedICAResults+System.getProperty("file.separator"));
		
		
		action = ConstantCodes.FINISHED;
	}
	
	
}
