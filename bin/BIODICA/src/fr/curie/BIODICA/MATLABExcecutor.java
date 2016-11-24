package fr.curie.BIODICA;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class MATLABExcecutor {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static void executeMatlabFastICA(String matlabCodeFolder, String workingFolder, String dataFile, int numberOfComponents) throws Exception{
		System.setProperty("user.dir", matlabCodeFolder);
		//System.out.println(System.getProperty("user.dir"));
		//Process process = new ProcessBuilder("matlab","-nodesktop","-nosplash","-r","cd('"+matlabCodeFolder+"');pwd").start();
		File doneFile = new File(workingFolder+"/_done");
		doneFile.delete();
		Date timestart = new Date();
		Process process = new ProcessBuilder("matlab","-nodesktop","-nosplash","-r","global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICAtxt('"+workingFolder+"','"+dataFile+"',"+numberOfComponents+");quit;").start();
		doneFile = new File(workingFolder+"/_done");
		while(!doneFile.exists()) Thread.sleep(2000);
		FileUtils.deleteQuietly(doneFile);
		System.out.println("MATLAB ICA Computations have been completed, time spent: "+((new Date().getTime()-timestart.getTime())/1000f)+" sec");
	}
	
	public static void executeMatlabFastICANumerical(String matlabCodeFolder, String workingFolder, String dataFile, int numberOfComponents[]) throws Exception{
		System.setProperty("user.dir", matlabCodeFolder);
		//System.out.println(System.getProperty("user.dir"));
		//Process process = new ProcessBuilder("matlab","-nodesktop","-nosplash","-r","cd('"+matlabCodeFolder+"');pwd").start();
		File doneFile = new File(workingFolder+"/_done");
		doneFile.delete();
		Date timestart = new Date();
		for(int i=0;i<numberOfComponents.length;i++){
			Date timestart1 = new Date();
			System.out.println("Computing ICA for "+numberOfComponents[i]+" components...");
			Process process = new ProcessBuilder("matlab","-nodesktop","-nosplash","-r","global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICA('"+workingFolder+"','"+dataFile+"',"+numberOfComponents[i]+");quit;").start();
			doneFile = new File(workingFolder+"/_done");
			while(!doneFile.exists()) Thread.sleep(2000);
			FileUtils.deleteQuietly(doneFile);
			System.out.println("Time spent: "+((new Date().getTime()-timestart1.getTime())/1000f)+" sec");
		}
		System.out.println("MATLAB ICA Computations have been completed, total time spent: "+((new Date().getTime()-timestart.getTime())/1000f)+" sec");
	}
	

}
