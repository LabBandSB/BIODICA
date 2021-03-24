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

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleProcedures;
import logic.AssociationFinder;
import model.ConstantCodes;
import model.MetaGeneDTO;
import util.HTMLGenerator;


public class RunMetaGeneWorker extends SwingWorker<Boolean, String>  {

	private JTextArea tAConsole;
	private JButton btnRunMethod;
	private JProgressBar pbProgress;
	private MetaGeneDTO metaGeneDTO;
	private MetaGeneMethod metaGeneMethod;
	private String analysisprefix;
	private File df;
	private File wfmgene;
	private String dataCategoricalFile;
	private String dataNumericalFile;

	
	public String action = ConstantCodes.ERROR;
	
	public RunMetaGeneWorker(JTextArea tAConsole, JButton btnRunMethod,JProgressBar pbProgress, MetaGeneDTO metaGeneDTO,MetaGeneMethod metaGeneMethod){
		this.tAConsole = tAConsole;
		this.btnRunMethod = btnRunMethod;
		this.pbProgress = pbProgress;
		this.metaGeneDTO = metaGeneDTO;
		this.metaGeneMethod = metaGeneMethod;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		pbProgress.setIndeterminate(true);
		Border progressBorder = BorderFactory.createTitledBorder("Running...");
		pbProgress.setBorder(progressBorder);
		
		if(init()){
			doMetaGeneAnalysis();
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
		metaGeneMethod.setEnabled(true);
		switch(action){
			case ConstantCodes.FINISHED:
				JOptionPane.showMessageDialog (null, "Process has been successfully finished.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				
				File[] files = wfmgene.listFiles(new FileFilter() {
				     public boolean accept(File file) {
				         return file.isFile() && file.getName().toLowerCase().endsWith(".xls");
				     }
				 });
						
				try {
					HTMLGenerator.generateMetaGeneHtml("Meta Gene Annotation","MetaGene",files, analysisprefix, dataCategoricalFile, dataNumericalFile);
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
		File wf = new File(metaGeneDTO.getDefaultWorkFolderPath());
		if(!wf.exists()){
			if(!wf.mkdir()){
				publish("FATAL ERROR: Can not create working folder \""+metaGeneDTO.getDefaultWorkFolderPath()+"\"");
				return false;
			}
		}
		
		File sf = new File(metaGeneDTO.getSTablePath());
		if(sf.exists()){
			analysisprefix = sf.getName().substring(0, sf.getName().length()-4);
			if(analysisprefix.endsWith("_ica_S"))
				analysisprefix = analysisprefix.substring(0, analysisprefix.length()-6);
		}else{
		df = new File(metaGeneDTO.getDataTablePath());
		if(!df.exists()){
			publish("FATAL ERROR: Can not find the data file \""+metaGeneDTO.getDataTablePath()+"\"");
			return false;
		}else{
			analysisprefix = df.getName().substring(0, df.getName().length()-4);
		}}		
		
		return true;
	}
	
	
	private void doMetaGeneAnalysis() throws Exception{
		
		publish("================================================================");
		publish("======  Associating ICA metagenes to known gene properties =====");
		publish("================================================================");
		
		//File wfgsea = new File(biodica.workFolder+"/"+biodica.analysisprefix+"_MGENE");
		//wfgsea.mkdir();
		File wfica = new File(metaGeneDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_ICA");
		wfmgene = new File(metaGeneDTO.getDefaultWorkFolderPath()+File.separator+analysisprefix+"_MGENE");
		wfmgene.mkdir();
		
		File sfile = null;
		if(!metaGeneDTO.getSTablePath().equals(""))
			sfile = new File(metaGeneDTO.getSTablePath());
		else
			sfile = new File(wfica.getAbsolutePath()+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls");
		

		File f = new File(metaGeneDTO.getGeneAnnotationFilePath());
		if(f.isDirectory()){
			
			String smatrixfile = sfile.getAbsolutePath();
			publish("Loading S-matrix from "+smatrixfile+"... ");
			VDataTable vts = VDatReadWrite.LoadFromSimpleDatFile(smatrixfile, true, "\t");
			VSimpleProcedures.findAllNumericalColumns(vts);
			AssociationFinder.findCorrelationsWithMetagenesRNK(f.getAbsolutePath(), vts, wfmgene+System.getProperty("file.separator")+analysisprefix+"_S_associationsRNK.xls");					

			dataCategoricalFile = wfmgene+System.getProperty("file.separator")+analysisprefix+"_S_associationsRNK.corr";
			dataNumericalFile = wfmgene+System.getProperty("file.separator")+analysisprefix+"_S_associationsRNK.corr";
			
		}else{

			String smatrixfile = sfile.getAbsolutePath();
			publish("Loading S-matrix from "+smatrixfile+"... ");
			VDataTable vts = VDatReadWrite.LoadFromSimpleDatFile(smatrixfile, true, "\t");
			publish("Loading gene annotation file from "+metaGeneDTO.getGeneAnnotationFilePath() + " ... ");
			VDataTable vtann = VDatReadWrite.LoadFromSimpleDatFile(metaGeneDTO.getGeneAnnotationFilePath(), true, "\t");
			vtann.makePrimaryHash(vtann.fieldNames[0]);
			int numberOfMatchedGenes = 0;
			for(int k=0;k<vts.rowCount;k++) if(vtann.tableHashPrimary.get(vts.stringTable[k][0])!=null) numberOfMatchedGenes++;
			publish(numberOfMatchedGenes+" genes have been matched ("+(int)(((float)numberOfMatchedGenes/(float)vts.rowCount)*100f)+"% from data matrix, "+(int)(((float)numberOfMatchedGenes/(float)vtann.rowCount)*100f)+"% from annotation file)");

			VDatReadWrite.useQuotesEverywhere = false;
			VDatReadWrite.writeNumberOfColumnsRows = false;
			VDataTable merged = VSimpleProcedures.MergeTables(vts, vts.fieldNames[0], vtann, vtann.fieldNames[0], "_");
			VDatReadWrite.saveToSimpleDatFile(merged, wfmgene+System.getProperty("file.separator")+analysisprefix+"_S_annot.xls");
			VSimpleProcedures.findAllNumericalColumns(merged);
			merged = Utils.PrepareTableForVidaExpert(merged);
			VDatReadWrite.saveToVDatFile(merged, wfmgene+System.getProperty("file.separator")+analysisprefix+"_S_annot.dat");
			
			Vector<String> strNames = new Vector<String>();
			for(int i=1;i<vts.colCount;i++) strNames.add(vts.fieldNames[i]);
			AssociationFinder.printAssosiationTable(merged, wfmgene+System.getProperty("file.separator")+analysisprefix+"_S_associations.xls", strNames, true, false);

			dataCategoricalFile = wfmgene+System.getProperty("file.separator")+analysisprefix+"_S_associations.data";
			dataNumericalFile = wfmgene+System.getProperty("file.separator")+analysisprefix+"_S_associations.data";
			
		}
		
		action = ConstantCodes.FINISHED;
	}
	
	
}
