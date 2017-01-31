package fr.curie.BIODICA;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class MATLABExcecutor {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
		
		//String scriptWithInputParameters = "/bioinfo/opt/build/Matlab-R2013a/bin/matlab -nodesktop -nosplash -r \"global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('bin/fastica++');doICA('/bioinfo/users/zinovyev/Datas/BIODICA/work/OVCA_ICA/','OVCA_ica_numerical.txt',3);quit;\"";
		String command[] = new String[]{"/bioinfo/opt/build/Matlab-R2013a/bin/matlab","-nodesktop","-nosplash","-r","global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('bin/fastica++');doICA('/bioinfo/users/zinovyev/Datas/BIODICA/work/OVCA_ICA/','OVCA_ica_numerical.txt',3);quit;"};
			//String scriptWithInputParameters = "matlab -nodesktop -nosplash -r \"global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('bin/fastica++');doICA('/bioinfo/users/zinovyev/Datas/BIODICA/work/OVCA_ICA/','OVCA_ica_numerical.txt',3);quit;\"";
		
		//Process process = Runtime.getRuntime().exec(command);
		
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.INHERIT);
		Process process = pb.start();
		
		int exitCode = process.waitFor();
		if (exitCode == 0) {
		    System.out.println("Executed successfully");
		}
		else {
		  System.out.println("Failed ...");
		}
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void executeMatlabFastICA(String matlabfolder, String matlabCodeFolder, String workingFolder, String dataFile, int numberOfComponents) throws Exception{
		System.setProperty("user.dir", matlabCodeFolder);
		//System.out.println(System.getProperty("user.dir"));
		//Process process = new ProcessBuilder("matlab","-nodesktop","-nosplash","-r","cd('"+matlabCodeFolder+"');pwd").start();
		File doneFile = new File(workingFolder+"/_done");
		doneFile.delete();
		Date timestart = new Date();
		String cmdline = matlabfolder+"matlab"+" -nodesktop"+" -nosplash"+" -r"+" \"global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICAtxt('"+workingFolder+"','"+dataFile+"',"+numberOfComponents+");quit;\"";
		System.out.println("Running "+cmdline);
		Process process = new ProcessBuilder(matlabfolder+"matlab","-nodesktop","-nosplash","-r","\"global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICAtxt('"+workingFolder+"','"+dataFile+"',"+numberOfComponents+");quit;\"").start();
		doneFile = new File(workingFolder+"/_done");
		while(!doneFile.exists()) Thread.sleep(2000);
		FileUtils.deleteQuietly(doneFile);
		System.out.println("MATLAB ICA Computations have been completed, time spent: "+((new Date().getTime()-timestart.getTime())/1000f)+" sec");
	}
	
	public static void executeMatlabFastICANumerical(String matlabfolder, String matlabCodeFolder, String workingFolder, String dataFile, int numberOfComponents[]) throws Exception{
		System.setProperty("user.dir", matlabCodeFolder);
		//System.out.println(System.getProperty("user.dir"));
		//Process process = new ProcessBuilder("matlab","-nodesktop","-nosplash","-r","cd('"+matlabCodeFolder+"');pwd").start();
		File doneFile = new File(workingFolder+"/_done");
		doneFile.delete();
		Date timestart = new Date();
		for(int i=0;i<numberOfComponents.length;i++){
			Date timestart1 = new Date();
			System.out.println("Computing ICA for "+numberOfComponents[i]+" components...");
			String cmdline = matlabfolder+"matlab"+" -nodesktop"+" -nosplash"+" -r"+" global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICA('"+workingFolder+"','"+dataFile+"',"+numberOfComponents[i]+");quit;";
			System.out.println("Running "+cmdline);
			ProcessBuilder pb = new ProcessBuilder(matlabfolder+"matlab","-nodesktop","-nosplash","-r","global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICA('"+workingFolder+"','"+dataFile+"',"+numberOfComponents[i]+");quit;");
			pb.redirectErrorStream(true);
			pb.redirectOutput(Redirect.INHERIT);
			Process process = pb.start();
			doneFile = new File(workingFolder+"/_done");
			while(!doneFile.exists()) Thread.sleep(2000);
			FileUtils.deleteQuietly(doneFile);
			
			int exitCode = process.waitFor();
			if (exitCode == 0) {
			    System.out.println("MATLAB code executed successfully... :)");
			}
			else {
			  System.out.println("MATLAB Computations failed... :((");
			}
			
			System.out.println("Time spent: "+((new Date().getTime()-timestart1.getTime())/1000f)+" sec");
		}
		System.out.println("MATLAB ICA Computations have been completed, total time spent: "+((new Date().getTime()-timestart.getTime())/1000f)+" sec");
	}
	
	
	

}
