import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleProcedures;
import logic.AssociationFinder;
import model.ConstantCodes;
import model.MetaSampleDTO;
import util.HTMLGenerator;


public class RunMetaSampleWorker extends SwingWorker<Boolean, String>  {
	
	private JTextArea tAConsole;
	private JButton btnRunMethod;
	private JProgressBar pbProgress;
	private MetaSampleDTO metaSampleDTO;
	private MetaSampleMethod metaSampleMethod;
	private File df;
	private String analysisprefix;
	private String dataCategoricalFile;
	private String dataNumericalFile;
	private File wfmsample;

	
	public String action = ConstantCodes.ERROR;
	
	
	public RunMetaSampleWorker(JTextArea tAConsole, JButton btnRunMethod,JProgressBar pbProgress, MetaSampleDTO metaSampleDTO, MetaSampleMethod metaSampleMethod)
	{
		this.tAConsole = tAConsole;
		this.btnRunMethod = btnRunMethod;
		this.pbProgress = pbProgress;
		this.metaSampleDTO = metaSampleDTO;
		this.metaSampleMethod = metaSampleMethod;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		pbProgress.setIndeterminate(true);
		Border progressBorder = BorderFactory.createTitledBorder("Running...");
		pbProgress.setBorder(progressBorder);
		
		if(init()){
			doMetaSampleAnalysis();
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
		metaSampleMethod.setEnabled(true);
		switch(action){
			case ConstantCodes.FINISHED:
				JOptionPane.showMessageDialog (null, "Process has been successfully finished.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);

				File[] files = wfmsample.listFiles(new FileFilter() {
				     public boolean accept(File file) {
				         return file.isFile() && file.getName().toLowerCase().endsWith(".xls");
				     }
				 });
				
				try {
					
					HTMLGenerator.generateMetaSampleHtml("Meta Sample Annotation for "+analysisprefix,"MetaSample",files,analysisprefix,dataCategoricalFile,dataNumericalFile);
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
		File wf = new File(metaSampleDTO.getDefaultWorkFolderPath());
		if(!wf.exists()){
			if(!wf.mkdir()){
				publish("FATAL ERROR: Can not create working folder \""+metaSampleDTO.getDefaultWorkFolderPath()+"\"");
				return false;
			}
		}
		
		File af = new File(metaSampleDTO.getATablePath());
		if(af.exists()){
			analysisprefix = af.getName().substring(0, af.getName().length()-4);
			if(analysisprefix.endsWith("_ica_A"))
				analysisprefix = analysisprefix.substring(0, analysisprefix.length()-6);
		}else{
		df = new File(metaSampleDTO.getDataTablePath());
		if(!df.exists()){
			publish("FATAL ERROR: Can not find the data file \""+metaSampleDTO.getDataTablePath()+"\"");
			return false;
		}else{
			analysisprefix = df.getName().substring(0, df.getName().length()-4);
		}}		
		
		return true;
	}
	
	
	private void doMetaSampleAnalysis() throws Exception{
		
		publish("=============================================================");
		publish("======  Associate ICA metasamples to sample annotations =====");
		publish("=============================================================");
		
		// Association study
		File wfica = new File(metaSampleDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_ICA");
		wfmsample = new File(metaSampleDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_MSAMPLE");
		String amatrixfile = wfica.getAbsolutePath()+System.getProperty("file.separator")+analysisprefix+"_ica_A.xls";
		wfmsample.mkdir();
		publish("Loading A-matrix from "+amatrixfile+"... ");
		VDataTable vta = VDatReadWrite.LoadFromSimpleDatFile(amatrixfile, true, "\t");
		publish("Loading sample annotation file from "+metaSampleDTO.getSampleAnnotationFilePath()+" ... ");
		VDataTable vtann = VDatReadWrite.LoadFromSimpleDatFile(metaSampleDTO.getSampleAnnotationFilePath(), true, "\t");
		vtann.makePrimaryHash(vtann.fieldNames[0]);
		int numberOfMatchedSamples = 0;
		for(int k=0;k<vta.rowCount;k++) if(vtann.tableHashPrimary.get(vta.stringTable[k][0])!=null) numberOfMatchedSamples++;
		publish(numberOfMatchedSamples+" samples have been matched ("+(int)(((float)numberOfMatchedSamples/(float)vta.rowCount)*100f)+"% from data matrix, "+(int)(((float)numberOfMatchedSamples/(float)vtann.rowCount)*100f)+"% from annotation file)");

		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.writeNumberOfColumnsRows = false;
		VDataTable merged = VSimpleProcedures.MergeTables(vta, vta.fieldNames[0], vtann, vtann.fieldNames[0], "_");
		VDatReadWrite.saveToSimpleDatFile(merged, wfmsample+System.getProperty("file.separator")+analysisprefix+"_A_annot.xls");
		VSimpleProcedures.findAllNumericalColumns(merged);
		merged = Utils.PrepareTableForVidaExpert(merged);
		VDatReadWrite.saveToVDatFile(merged, wfmsample+System.getProperty("file.separator")+analysisprefix+"_A_annot.dat");
		
		Vector<String> strNames = new Vector<String>();
		for(int i=1;i<vta.colCount;i++) strNames.add(vta.fieldNames[i]);
		AssociationFinder.printAssosiationTable(merged, wfmsample+System.getProperty("file.separator")+analysisprefix+"_A_associations.xls", strNames, true, true);
		
		dataCategoricalFile = wfmsample+System.getProperty("file.separator")+analysisprefix+"_A_associations.data";
		dataNumericalFile = wfmsample+System.getProperty("file.separator")+analysisprefix+"_A_associations.data";
		
		action = ConstantCodes.FINISHED;
	}

}
