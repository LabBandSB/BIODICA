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

import logic.NaviCellLoader;
import logic.OFTENAnalysis;
import model.ConstantCodes;
import model.NaviCellDTO;
import util.HTMLGenerator;
import vdaoengine.TableUtils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;


public class RunNaviCellWorker extends SwingWorker<Boolean, String>  {

	private JTextArea tAConsole;
	private JButton btnRunMethod;
	private JProgressBar pbProgress;
	private NaviCellDTO navicellDTO;
	private NaviCellVisualization navicellMethod;
	private File df;
	private File wfNaviCell;
	private String analysisprefix;
	private String path2PPINetwork;
	private int OFTENnstart=100, OFTENnstep=50, OFTENnend=600, OFTENnperm=100;
	
	public String action = ConstantCodes.ERROR;
	
	
	public RunNaviCellWorker(JTextArea tAConsole, JButton btnRunMethod,JProgressBar pbProgress, NaviCellDTO navicellDTO, NaviCellVisualization navicell){
		this.tAConsole = tAConsole;
		this.btnRunMethod = btnRunMethod;
		this.pbProgress = pbProgress;
		this.navicellDTO = navicellDTO;
		this.navicellMethod = navicell;
	}
	
	
	@Override
	protected Boolean doInBackground() throws Exception {
		pbProgress.setIndeterminate(true);
		Border progressBorder = BorderFactory.createTitledBorder("Running...");
		pbProgress.setBorder(progressBorder);
		
		if(init()){
			doNaviCell();
		}
		
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
		navicellMethod.setEnabled(true);
		switch(action){
			case ConstantCodes.FINISHED:
				JOptionPane.showMessageDialog (null, "Process has been successfully finished.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				
				break;
			default:
				JOptionPane.showMessageDialog (null, "An error occurred. ", "ERROR", JOptionPane.ERROR_MESSAGE);
				break;
		}
		super.done();
	}
	
	
	private boolean init()
	{		

		File wf = new File(navicellDTO.getDefaultWorkFolderPath());
		if(!wf.exists()){
			if(!wf.mkdir()){
				publish("FATAL ERROR: Can not create working folder \""+navicellDTO.getDefaultWorkFolderPath()+"\"");
				return false;
			}
		}
		
		File sf = new File(navicellDTO.getSTablePath());
		if(sf.exists()){
			analysisprefix = sf.getName().substring(0, sf.getName().length()-4);
			if(analysisprefix.endsWith("_ica_S"))
				analysisprefix = analysisprefix.substring(0, analysisprefix.length()-6);
		}else{
		df = new File(navicellDTO.getDataTablePath());
		if(!df.exists()){
			publish("FATAL ERROR: Can not find the data file \""+navicellDTO.getDataTablePath()+"\"");
			return false;
		}else{
			analysisprefix = df.getName().substring(0, df.getName().length()-4);
		}}
		
		return true;
	}
	
	

	private void doNaviCell() throws Exception{

		publish("===============================================");
		publish("======  Performing NaviCell visualization =====");
		publish("===============================================");
		
		File wfica = new File(navicellDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_ICA");
		wfNaviCell = new File(navicellDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_NAVICELL");
		wfNaviCell.mkdir();
		
		File sfile = null;
		if(!navicellDTO.getSTablePath().equals(""))
			sfile = new File(navicellDTO.getSTablePath());
		else
			sfile = new File(wfica.getAbsolutePath()+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls");
		navicellDTO.setSTablePath(sfile.getAbsolutePath());
		
		FileUtils.cleanDirectory(wfNaviCell);
		String smatrixfile = sfile.getAbsolutePath();
		publish("Copying S-file "+smatrixfile+"... ");
		String tablePath = navicellDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_NAVICELL"+File.separator+analysisprefix+"_ica_S.xls"; 
		if(new File(tablePath).exists()){
			FileUtils.deleteQuietly(new File(tablePath));
		}
		FileUtils.copyFileToDirectory(new File(smatrixfile), wfNaviCell, true);

		String url = navicellDTO.getNaviCellMapURL();
		if(url.endsWith(".html")) url = url.substring(0, url.length()-5)+".php";
		navicellDTO.setNaviCellMapURL(url);
		
		publish("Map to use: "+navicellDTO.getNaviCellMapURL());
		publish("Table to load: "+tablePath);
		publish("Threshold for top-contributing: "+navicellDTO.getThresholdTopContributing());
		
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(tablePath, true, "\t");
		TableUtils.findAllNumericalColumns(vt);
		VDataTable vt1 = NaviCellLoader.takeOnlyTopContributingGenes(vt,navicellDTO.getThresholdTopContributing());
		VDatReadWrite.useQuotesEverywhere = false;
		
		String filteredSfile = tablePath.substring(0, tablePath.length()-4)+"_onlytop.txt";
		
		VDatReadWrite.saveToSimpleDatFile(vt1, filteredSfile);

		String columnId = vt1.fieldNames[1];
		
		NaviCellLoader.LoadNaviCellWithData(filteredSfile,navicellDTO.getNaviCellMapURL(),columnId,analysisprefix);
		
		
		
		action = ConstantCodes.FINISHED;
	}

}
