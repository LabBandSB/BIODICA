import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import org.apache.commons.io.FileUtils;

import logic.MATLABExcecutor;
import logic.PythonExcecutor;
import logic.NumberOfComponentsOptimizer;
import model.ConfigDTO;
import model.ConstantCodes;
import model.IcaDTO;
import util.ConfigHelper;
import util.HTMLGenerator;
import vdaoengine.ProcessTxtData;



public class RunICAWorker extends SwingWorker<Boolean, String>  {
	
	private JTextArea tAConsole;
	private JButton btnRunMethod;
	private JProgressBar pbProgress;
	private IcaDTO icaDTO;
	private String method;
	
	private String analysisprefix;
	private int numberOfComponents[] = {10};
	private int numberOfComponentsToSelect = 10;
	private File df;
	private String folderWithPrecomputedICAResults = "stability";
	public float minimalTolerableStability = 0.6f;
	public String action = ConstantCodes.ERROR;
	private IcaMethod iCAMethod;

	
	
	//New fields
	
	private String workingFolder;
	private String stabilityFolder;
	
	public RunICAWorker(JTextArea tAConsole, JButton btnRunMethod,JProgressBar pbProgress, IcaDTO icaDTO, String method, IcaMethod iCAMethod)
	{
		this.tAConsole = tAConsole;
		this.btnRunMethod = btnRunMethod;
		this.pbProgress = pbProgress;
		this.icaDTO = icaDTO;
		this.method = method;
		this.iCAMethod = iCAMethod;
	}
	
	
	@Override
	protected Boolean doInBackground() throws Exception {
		pbProgress.setIndeterminate(true);
		Border progressBorder = BorderFactory.createTitledBorder("Running...");
		pbProgress.setBorder(progressBorder);
		
		if(init())
		{
			if(method == ConstantCodes.ICA_METHOD)
			{
				doICA();
			}
			else if(method == ConstantCodes.OPTIMAL_COMPONENT_NO)
			{
				doAnalysisForOptimalComponentNumber();
			}
			else if(method == ConstantCodes.PRECOMPUTED){
				doPrecomputedComponentNumber();
			}
			else if(method == ConstantCodes.OPEN_ICA_RESULTS)
			{
				doOpenResultsPage();
			}
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
		iCAMethod.setEnabled(true);
		pbProgress.setIndeterminate(false);
		Border progressBorder = BorderFactory.createTitledBorder("");
		pbProgress.setBorder(progressBorder);
		iCAMethod.BindData();
		System.out.println("Action="+action);
		switch(action){
			case ConstantCodes.FINISHED:
				workingFolder = icaDTO.getDefaultWorkFolderPath()+File.separator + analysisprefix+"_ICA";
				stabilityFolder = workingFolder+File.separator+folderWithPrecomputedICAResults;
				File [] pngFiles = null;
				
				if((method == ConstantCodes.ICA_METHOD)||(method == ConstantCodes.OPEN_ICA_RESULTS))
				{
					JOptionPane.showMessageDialog (null, "Process has been successfully finished.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					
					pngFiles = new File(stabilityFolder).listFiles(new FileFilter() {
					     public boolean accept(File file) {
					         return file.isFile() && file.getName().toLowerCase().endsWith(".png");
					     }
					 });
				}
				else if(method == ConstantCodes.OPTIMAL_COMPONENT_NO)
				{
					
					JOptionPane.showMessageDialog (null, "Process has been successfully finished.\n "+icaDTO.getSNoOfComponents()+" components is selected.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					
					pngFiles = new File(stabilityFolder).listFiles(new FileFilter() {
					     public boolean accept(File file) {
					         return file.isFile() && file.getName().toLowerCase().endsWith(".png") && file.getName().toLowerCase().startsWith("_");
					     }
					 });
				}else if(method == ConstantCodes.PRECOMPUTED)
				{
					JOptionPane.showMessageDialog (null, "Process has been successfully finished.\n "+icaDTO.getSNoOfComponents()+" components is selected.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				}
//				for (File file : pngFiles) {
//					JOptionPane.showMessageDialog(null, file.getAbsolutePath());
//				}
				try {
					
					if(!method.equals(ConstantCodes.PRECOMPUTED)) {
						//String url1 = workingFolder+File.separator+analysisprefix+"_ica_A.xls";
						//String url2 = workingFolder+File.separator+analysisprefix+"_ica_S.xls";
						String url1 = "../"+analysisprefix+"_ICA/"+analysisprefix+"_ica_A.xls";
						String url2 = "../"+analysisprefix+"_ICA/"+analysisprefix+"_ica_S.xls";
						HTMLGenerator.generateICAHtml(url1,url2,pngFiles,analysisprefix);
					}
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
		
		File wf = new File(icaDTO.getDefaultWorkFolderPath());
		if(!wf.exists()){
			if(!wf.mkdir()){
				publish("FATAL ERROR: Can not create working folder \""+icaDTO.getDefaultWorkFolderPath()+"\"");
				return false;
			}
		}
		df = new File(icaDTO.getDataTablePath());
		if(!df.exists()){
			publish("FATAL ERROR: Can not find the data file \""+icaDTO.getDataTablePath()+"\"");
			return false;
		}else{
			analysisprefix = df.getName().substring(0, df.getName().length()-4);
		}
		return true;
	}
	
	private void doICA() throws Exception
	{		
		publish("=========================================");
		publish("======  Performing ICA computations =====");
		publish("=========================================");		
		File wfica = new File(icaDTO.getDefaultWorkFolderPath() + File.separator+analysisprefix+"_ICA");
		wfica.mkdir();
		File wficaStability = new File(icaDTO.getDefaultWorkFolderPath() + File.separator+analysisprefix+"_ICA"+File.separator+"stability");
		wficaStability.mkdir();
		
		String nums[] = icaDTO.getSNoOfComponents().split(",");
		numberOfComponents = new int[nums.length];
		for(int kk=0;kk<nums.length;kk++)
			numberOfComponents[kk] = Integer.parseInt(nums[kk]);

		
		FileUtils.copyFile(df, new File(wfica.getAbsolutePath()+File.separator + df.getName()));
			
		String arguments[] = new String[3];
		arguments[0] = wfica.getAbsolutePath()+File.separator+df.getName(); 
		//arguments[1] = "-center"; 
		//arguments[2] = "-prepare4ICA";
		arguments[1] = "-prepare4ICAFast";
		
		ProcessTxtData.main(arguments);
		
		String wfolder = wfica.getAbsolutePath();
		String wfolderstability = wficaStability.getAbsolutePath();
		String fn_numerical = df.getName().substring(0, df.getName().length()-4)+"_ica_numerical.txt"; 		
		
		if(icaDTO.getICAImplementation().equals("matlab")){	
			publish("Starting MATLAB ICA Computations...");
			String mfolder = icaDTO.getMATLABFolderPath();
			if(!mfolder.trim().equals(""))
				mfolder+=File.separator;
			MATLABExcecutor.executeMatlabFastICANumerical(mfolder,icaDTO.getMatlabicaFolderPath(), wfica.getAbsolutePath()+System.getProperty("file.separator"), fn_numerical, numberOfComponents, icaDTO.isUseDocker(),icaDTO.isVisOption(),icaDTO.getMATLABICAApproach(),icaDTO.getMATLABICAMeasure(),icaDTO.getMATLABICAMaxNumIterations());
		}
		
    	if(icaDTO.getICAImplementation().equals("python")){
    		publish("Starting Python ICA Computations...");
			String pythonCodeFolder = icaDTO.getPythonicaFolderPath();
			String workingFolder = wfica.getAbsolutePath()+System.getProperty("file.separator"); 
			String dataFile = fn_numerical;
			String ICAApproach = icaDTO.getPythonICAApproach();
			String ICAMeasure = icaDTO.getPythonICAMeasure();
			int ICAMaxNumIterations = icaDTO.getPythonICAMaxNumIterations();
			int ICANumberOfRuns = 100;
			String PythonVisualizationType = "umap";						
			PythonExcecutor.executePythonICANumerical(pythonCodeFolder,workingFolder,dataFile,numberOfComponents,ICAApproach,ICAMeasure,ICAMaxNumIterations,ICANumberOfRuns,PythonVisualizationType);
		}

		publish("Formatting results of ICA computations...");
		
		wfica = new File(wfolder);
		wficaStability = new File(wfolderstability);

		ProcessTxtData.CompileAandSTables(wfica.getAbsolutePath()+System.getProperty("file.separator"), analysisprefix+"_ica");
		
			
		publish("Cleaning the work folder...");
		File files[] = wfica.listFiles();
		for(int i=0;i<files.length;i++){
			String fn = files[i].getName();
			boolean move = false;
			if(fn.startsWith(analysisprefix+"_ica_numerical")) move = true;
			if(fn.startsWith("A_"+analysisprefix+"_ica_numerical")) move = true;	
			if(fn.startsWith("S_"+analysisprefix+"_ica_numerical")) move = true;
			if(fn.startsWith(analysisprefix+"_ica_ids")) move = true;
			if(fn.startsWith(analysisprefix+"_ica_samples")) move = true;
			try{
				if(move){
					if(new File(wficaStability.getAbsolutePath()+File.separator+fn).exists()){
						FileUtils.deleteQuietly(new File(wficaStability.getAbsolutePath()+File.separator+fn));
					}
					FileUtils.moveFileToDirectory(files[i], wficaStability, true);
				}
			if(fn.equals("_done")) FileUtils.forceDelete(files[i]);
			if(fn.equals(df.getName()+".dat")) FileUtils.forceDelete(files[i]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		action= ConstantCodes.FINISHED;
	}
	
	private void doPrecomputedComponentNumber()throws Exception{
		publish("=============================================================");
		publish("======  Compiling A and S files from a precomputed file =====");
		publish("=============================================================");

		File workingFolder = new File(icaDTO.getDefaultWorkFolderPath()+File.separator + analysisprefix+"_ICA"+File.separator);
		File stabilityFolder = new File(workingFolder.getAbsolutePath()+File.separator+folderWithPrecomputedICAResults+File.separator);

		String num = icaDTO.getSNoOfComponents();
		int numberOfComponentsToSelect = Integer.parseInt(num);
		
		ProcessTxtData.CompileAandSTables(stabilityFolder.getAbsolutePath()+File.separator, analysisprefix+"_ica", numberOfComponentsToSelect);
		
		
		System.out.println("Replacing A and S files in the working folder...");
		FileUtils.deleteQuietly(new File(workingFolder.getAbsolutePath()+File.separator+analysisprefix+"_ica_A.xls"));
		FileUtils.deleteQuietly(new File(workingFolder.getAbsolutePath()+File.separator+analysisprefix+"_ica_S.xls"));
		FileUtils.moveFileToDirectory(new File(stabilityFolder.getAbsolutePath()+File.separator+analysisprefix+"_ica_A.xls"), workingFolder, true);
		FileUtils.moveFileToDirectory(new File(stabilityFolder.getAbsolutePath()+File.separator+analysisprefix+"_ica_S.xls"), workingFolder, true);
		
		
		action= ConstantCodes.FINISHED;		
	}
	

	private void doAnalysisForOptimalComponentNumber()throws Exception
	{
		publish("=========================================================");
		publish("======  Estimating the optimal number of components =====");
		publish("=========================================================");
		
		File workingFolder = new File(icaDTO.getDefaultWorkFolderPath()+File.separator + analysisprefix+"_ICA"+File.separator);
		File stabilityFolder = new File(workingFolder.getAbsolutePath()+File.separator+folderWithPrecomputedICAResults+File.separator);
		StringBuffer report = new StringBuffer(); 
		
		ConfigHelper cfHelper = new ConfigHelper();
		ConfigDTO cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
		
		/*minimalTolerableStability = (float)cfDTO.getMinimalTolerableStability();
		numberOfComponentsToSelect = NumberOfComponentsOptimizer.reccomendOptimalNumberOfComponentsFromStability(stabilityFolder.getAbsolutePath()+File.separator,analysisprefix+"_ica",minimalTolerableStability,report);
		icaDTO.setSNoOfComponents(""+numberOfComponentsToSelect);
		publish(report.toString());

		publish("=============================================================");
		publish("======  Compiling A and S files from a precomputed file =====");
		publish("=============================================================");
		

		ProcessTxtData.CompileAandSTables(stabilityFolder.getAbsolutePath()+File.separator, analysisprefix+"_ica", numberOfComponentsToSelect);
		
		
		System.out.println("Replacing A and S files in the working folder...");
		FileUtils.deleteQuietly(new File(workingFolder.getAbsolutePath()+File.separator+analysisprefix+"_ica_A.xls"));
		FileUtils.deleteQuietly(new File(workingFolder.getAbsolutePath()+File.separator+analysisprefix+"_ica_S.xls"));
		FileUtils.moveFileToDirectory(new File(stabilityFolder.getAbsolutePath()+File.separator+analysisprefix+"_ica_A.xls"), workingFolder, true);
		FileUtils.moveFileToDirectory(new File(stabilityFolder.getAbsolutePath()+File.separator+analysisprefix+"_ica_S.xls"), workingFolder, true);

		*/

		File wfica = new File(icaDTO.getDefaultWorkFolderPath() + File.separator+analysisprefix+"_ICA");
		File wficaStability = new File(icaDTO.getDefaultWorkFolderPath() + File.separator+analysisprefix+"_ICA"+File.separator+"stability");
		String wfolder = wfica.getAbsolutePath();
		String wfolderstability = wficaStability.getAbsolutePath();
		
		if(icaDTO.getICAImplementation().equals("matlab")){	
			publish("Starting MATLAB script...");
			String mfolder = icaDTO.getMATLABFolderPath();
			if(!mfolder.trim().equals(""))
				mfolder+=File.separator;
			MATLABExcecutor.executeMatlabProduceStabilityPlots(mfolder,icaDTO.getMatlabicaFolderPath(), wfica.getAbsolutePath()+System.getProperty("file.separator"),icaDTO.isUseDocker());
		}
		
    	if(icaDTO.getICAImplementation().equals("python")){
    		publish("Starting Python script...");
    		PythonExcecutor.executePythonProduceStabilityPlots(icaDTO.getPythonicaFolderPath(), wficaStability.getAbsolutePath()+System.getProperty("file.separator"));
    	}

		
		action= ConstantCodes.FINISHED;
	}
	
	private void doOpenResultsPage(){
		action= ConstantCodes.FINISHED;
	}
	
}
