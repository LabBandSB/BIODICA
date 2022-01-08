package logic;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import vdaoengine.ProcessTxtData;

public class PythonExcecutor {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
			
			ProcessTxtData.CompileAandSTables("C:\\Datas\\BIODICA_GUI\\work\\OVCA_ICA\\", "OVCA_ica");
			System.exit(0);
			
			String pythonCodeFolder = "C:/Datas/BIODICA_GUI/bin/python_sica/";
			String workingFolder = "C:/Datas/BIODICA_GUI/work/OVCA/"; 
			String dataFile = "OVCA_ica_numerical.txt";
			int numberOfComponents[] = {2,5,10};
			String ICAApproach = "fastica_def";
			String ICAMeasure = "cube";
			int ICAMaxNumIterations = 200;
			int ICANumberOfRuns = 100;
			String PythonVisualizationType = "umap";			
			
			executePythonICANumerical(pythonCodeFolder,workingFolder,dataFile,numberOfComponents,ICAApproach,ICAMeasure,ICAMaxNumIterations,ICANumberOfRuns,PythonVisualizationType);
			System.exit(0);
			
			ProcessBuilder pbt = new ProcessBuilder("python","C:/Datas/BIODICA_GUI/bin/python_sica/biodica_python.py","--input_folder","C:/MyPrograms/BIODICA_GUI/","--input_file","OVCA_ica_numerical.txt","--output_folder","C:/MyPrograms/BIODICA_GUI/_test/stability/","--components 2,3,4,5,10,15,20,30,50","--type_of_visualization","umap","--n_runs","100","--algorithm","fastica_par","--non_linearity","cube");
			pbt.redirectInput(Redirect.INHERIT);
			pbt.redirectErrorStream(true);
			pbt.redirectOutput(Redirect.INHERIT);
			Process processt = pbt.start();
			System.exit(0);
			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

	public static void executePythonICANumerical(String pythonCodeFolder, String workingFolder, String dataFile, int numberOfComponents[], String ICAApproach, String ICAMeasure, int ICAMaxNumIterations, int ICANumberOfRuns, String PythonVisualizationType) throws Exception{
		String currentDir = System.getProperty("user.dir");
		System.setProperty("user.dir", pythonCodeFolder);
		//File doneFile = new File(workingFolder+"_done");
		//doneFile.delete();
		Date timestart = new Date();
		
		String vis = "basic";

		String numberOfComponents_str = "";
		for(int i=0;i<numberOfComponents.length;i++)
			numberOfComponents_str+=numberOfComponents[i]+",";
		numberOfComponents_str = numberOfComponents_str.substring(0, numberOfComponents_str.length()-1);

			Date timestart1 = new Date();
			System.out.println("Computing ICA for "+numberOfComponents_str+" components...");

			ProcessBuilder pb = null;
			
			//String cmdline = "python"+" -nodesktop"+" -nodisplay"+" -nosplash"+" -r"+" global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICA('"+workingFolder+"','"+dataFile+"',"+numberOfComponents[i]+",'vis','"+vis+"','approach','"+ICAApproach+"','g','"+ICAMeasure+"','maxNumIterations',"+ICAMaxNumIterations+"');quit;";
			String cmdline = "python "+pythonCodeFolder+"/biodica_python.py"+" --input_folder "+workingFolder+" --input_file "+dataFile+" --output_folder "+workingFolder+" --components "+numberOfComponents_str+" --type_of_visualization "+PythonVisualizationType+" --n_runs "+ICANumberOfRuns+" --algorithm "+ICAApproach+" --non_linearity "+ICAMeasure;
			System.out.println("Running "+cmdline);
			//pb = new ProcessBuilder(matlabfolder+"matlab","-nodisplay","-nodesktop","-nosplash","-r","global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICA('"+workingFolder+"','"+dataFile+"',"+numberOfComponents[i]+",'vis','"+vis+"','approach','"+ICAApproach+"','g','"+ICAMeasure+"','maxNumIterations',"+ICAMaxNumIterations+"');quit;");
			pb = new ProcessBuilder("python",pythonCodeFolder+"/biodica_python.py","--input_folder",workingFolder,"--input_file",dataFile,"--output_folder",workingFolder,"--components",numberOfComponents_str,"--type_of_visualization",PythonVisualizationType,"--n_runs",""+ICANumberOfRuns,"--algorithm",ICAApproach,"--non_linearity",ICAMeasure);			
			
			pb.redirectErrorStream(true);
			pb.redirectOutput(Redirect.INHERIT);
			Process process = pb.start();
			//doneFile = new File(workingFolder+"_done");
			//while(!doneFile.exists()) Thread.sleep(2000);
			//FileUtils.deleteQuietly(doneFile);
			
			int exitCode = process.waitFor();
			if (exitCode == 0) {
			    System.out.println("Python code executed successfully... :)");
			}
			else {
			  System.out.println("Python Computations failed... :((");
			}
			
			
			System.out.println("Time spent: "+((new Date().getTime()-timestart1.getTime())/1000f)+" sec");
			

		System.setProperty("user.dir", currentDir);
	}
	
	public static void executePythonProduceStabilityPlots(String pythonCodeFolder, String workingFolder) throws Exception{
		//String currentDir = System.getProperty("user.dir");
		//System.setProperty("user.dir", pythonCodeFolder);
		//File doneFile = new File(workingFolder+"stability"+System.getProperty("file.separator")+"_done");
		//doneFile.delete();
		
		//System.out.println("workingfolder="+workingFolder);
		
		ProcessBuilder pb = null;
		
		String cmdline = "python "+pythonCodeFolder+"/biodica_mstd.py"+" --output_folder "+workingFolder;
		System.out.println("Running "+cmdline);
		pb = new ProcessBuilder("python",pythonCodeFolder+"/biodica_mstd.py","--output_folder",workingFolder);			


		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.INHERIT);
		
			Process process = pb.start();

			//doneFile = new File(workingFolder+"stability"+System.getProperty("file.separator")+"_done");
			//while(!doneFile.exists()) Thread.sleep(2000);
			//FileUtils.deleteQuietly(doneFile);
			
			int exitCode = process.waitFor();
			if (exitCode == 0) {
			    System.out.println("Python code executed successfully... :)");
			}
			else {
			  System.out.println("Python Computations failed... :((");
			}
			
	//System.setProperty("user.dir", currentDir);
	}
	


}
