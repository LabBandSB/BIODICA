package logic;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class MATLABExcecutor {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{

			ProcessBuilder pbt = new ProcessBuilder("docker","run","-i","auranic/biodica");
			pbt.redirectInput(Redirect.INHERIT);
			pbt.redirectErrorStream(true);
			pbt.redirectOutput(Redirect.INHERIT);
			Process processt = pbt.start();
			System.exit(0);
			
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
	
	// this function is not used only executeMatlabFastICANumerical
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
	
	public static void executeMatlabFastICANumerical(String matlabfolder, String matlabCodeFolder, String workingFolder, String dataFile, int numberOfComponents[], boolean UseDocker, boolean visOption, String ICAApproach, String ICAMeasure, int ICAMaxNumIterations) throws Exception{
		String currentDir = System.getProperty("user.dir");
		System.setProperty("user.dir", matlabCodeFolder);
		//System.out.println(System.getProperty("user.dir"));
		//Process process = new ProcessBuilder("matlab","-nodesktop","-nosplash","-r","cd('"+matlabCodeFolder+"');pwd").start();
		File doneFile = new File(workingFolder+"_done");
		doneFile.delete();
		Date timestart = new Date();
		
		String vis = "basic";
		if(!visOption) vis = "off";
		
		for(int i=0;i<numberOfComponents.length;i++){
			Date timestart1 = new Date();
			System.out.println("Computing ICA for "+numberOfComponents[i]+" components...");

			ProcessBuilder pb = null;
			
			if(new File(matlabfolder).exists()){

			String cmdline = matlabfolder+"matlab"+" -nodesktop"+" -nodisplay"+" -nosplash"+" -r"+" global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICA('"+workingFolder+"','"+dataFile+"',"+numberOfComponents[i]+",'vis','"+vis+"','approach','"+ICAApproach+"','g','"+ICAMeasure+"','maxNumIterations',"+ICAMaxNumIterations+"');quit;";
			System.out.println("Running "+cmdline);
			pb = new ProcessBuilder(matlabfolder+"matlab","-nodisplay","-nodesktop","-nosplash","-r","global ICAFolder;global ICAFileName;global ICANumberOfComponents;cd('"+matlabCodeFolder+"');doICA('"+workingFolder+"','"+dataFile+"',"+numberOfComponents[i]+",'vis','"+vis+"','approach','"+ICAApproach+"','g','"+ICAMeasure+"','maxNumIterations',"+ICAMaxNumIterations+"');quit;");

			}else{
			// Using RunTime
				String systemname = System.getProperty("os.name");
				if(systemname.toLowerCase().contains("windows")){
					if(!UseDocker){
						pb = new ProcessBuilder(matlabCodeFolder+System.getProperty("file.separator")+"doICA"+System.getProperty("file.separator")+"doICA.exe",workingFolder,dataFile,""+numberOfComponents[i],"vis",vis,"approach",ICAApproach,"g",ICAMeasure,"maxNumIterations",""+ICAMaxNumIterations);
					}else{
						//pb = new ProcessBuilder(matlabCodeFolder+System.getProperty("file.separator")+"doICA"+System.getProperty("file.separator")+"doICA.exe",workingFolder,dataFile,""+numberOfComponents[i]);
						//docker run -it -v C:\Datas\BIODICA_work\docker_test\data\:/tmp/ auranic/biodica /BIODICA/run_doICA_linux.sh /opt/mcr/v81/ /tmp/ OVCA_ica_numerical.txt 20
						String sss = "docker"+" "+"run"+" "+"-i"+" "+"-v"+" "+workingFolder+":/tmp/"+" "+"auranic/biodica"+" "+"/BIODICA/run_doICA_linux.sh"+" "+"/opt/mcr/v81/"+" "+"/tmp/"+" "+dataFile+" "+""+numberOfComponents[i]+" vis "+vis+" approach "+ICAApproach+" g "+ICAMeasure+" maxNumIterations "+ICAMaxNumIterations;
						System.out.println(sss);
						pb = new ProcessBuilder("docker","run","-i","-v",workingFolder+":/tmp/","auranic/biodica","/BIODICA/run_doICA_linux.sh","/opt/mcr/v81/","/tmp/",dataFile,""+numberOfComponents[i],"vis",vis,"approach",ICAApproach,"g",ICAMeasure,"maxNumIterations",""+ICAMaxNumIterations);
						//pb = new ProcessBuilder("docker","run","-it","auranic/biodica");
						//pb = new ProcessBuilder(sss);
					}
				}else{
					
					if(!UseDocker){
						//System.out.println("WARNING: Using BIODICA MATLAB code under Linux is possible only through docker.");
						//System.out.println("If not done, install docker and run:");
						//System.out.println("");
						//System.out.println("docker pull auranic/biodica");
						//System.out.println("");
						//System.out.println("before using BIODICA.");
						pb = new ProcessBuilder(matlabCodeFolder+System.getProperty("file.separator")+"doICA"+System.getProperty("file.separator")+"doICA_linux",workingFolder,dataFile,""+numberOfComponents[i],"vis",vis,"approach",ICAApproach,"g",ICAMeasure,"maxNumIterations",""+ICAMaxNumIterations);						
					}else{
						String sss = "docker"+" "+"run"+" "+"-i"+" "+"-v"+" "+workingFolder+":/tmp/"+" "+"auranic/biodica"+" "+"/BIODICA/run_doICA_linux.sh"+" "+"/opt/mcr/v81/"+" "+"/tmp/"+" "+dataFile+" "+""+numberOfComponents[i]+" vis "+vis+" approach "+ICAApproach+" g "+ICAMeasure+" maxNumIterations "+ICAMaxNumIterations;
						System.out.println(sss);
						pb = new ProcessBuilder("docker","run","-i","-v",workingFolder+":/tmp/","auranic/biodica","/BIODICA/run_doICA_linux.sh","/opt/mcr/v81/","/tmp/",dataFile,""+numberOfComponents[i],"vis",vis,"approach",ICAApproach,"g",ICAMeasure,"maxNumIterations",""+ICAMaxNumIterations);
					}
				}
			}

			
			pb.redirectErrorStream(true);
			pb.redirectOutput(Redirect.INHERIT);
			Process process = pb.start();
			doneFile = new File(workingFolder+"_done");
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
		System.setProperty("user.dir", currentDir);
	}
	
	public static void executeMatlabProduceStabilityPlots(String matlabfolder, String matlabCodeFolder, String workingFolder, boolean UseDocker) throws Exception{
		String currentDir = System.getProperty("user.dir");
		System.setProperty("user.dir", matlabCodeFolder);
		File doneFile = new File(workingFolder+"stability"+System.getProperty("file.separator")+"_done");
		doneFile.delete();
		
		ProcessBuilder pb = null;
		
		if(new File(matlabfolder).exists()){
		
		String cmdline = matlabfolder+"matlab"+" -nodesktop"+" -nosplash"+" -r "+"cd('"+matlabCodeFolder+"');plotAverageStability('"+workingFolder+"stability"+System.getProperty("file.separator")+"');quit;";
		System.out.println("Running "+cmdline);
		pb = new ProcessBuilder(matlabfolder+"matlab","-nodesktop","-nosplash","-r","cd('"+matlabCodeFolder+"');plotAverageStability('"+workingFolder+"stability"+System.getProperty("file.separator")+"');quit;");
		
		}else{
		// Using RunTime
			String systemname = System.getProperty("os.name");
			if(systemname.toLowerCase().contains("windows")){
				if(!UseDocker){
					pb = new ProcessBuilder(matlabCodeFolder+System.getProperty("file.separator")+"doICA"+System.getProperty("file.separator")+"plotAverageStability.exe",workingFolder+"stability"+System.getProperty("file.separator"));
				}else{
					pb = new ProcessBuilder("docker","run","-i","-v",workingFolder+":/tmp/","auranic/biodica","/BIODICA/run_plotAverageStability_linux.sh","/opt/mcr/v81/","/tmp/");
				}
			}else{
				//System.out.println("WARNING: Using BIODICA MATLAB code under Linux is possible only through docker.");
				//System.out.println("If not done, install docker and run:");
				//System.out.println("");
				//System.out.println("docker pull auranic/biodica");
				//System.out.println("");
				//System.out.println("before using BIODICA.");
				if(!UseDocker){
					pb = new ProcessBuilder(matlabCodeFolder+System.getProperty("file.separator")+"doICA"+System.getProperty("file.separator")+"plotAverageStability_linux",workingFolder+"stability"+System.getProperty("file.separator"));
				}else{
					pb = new ProcessBuilder("docker","run","-i","-v",workingFolder+":/tmp/","auranic/biodica","/BIODICA/run_plotAverageStability_linux.sh","/opt/mcr/v81/","/tmp/");
				}
			}
		}

		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.INHERIT);
		
			Process process = pb.start();

			doneFile = new File(workingFolder+"stability"+System.getProperty("file.separator")+"_done");
			while(!doneFile.exists()) Thread.sleep(2000);
			FileUtils.deleteQuietly(doneFile);
			
			int exitCode = process.waitFor();
			if (exitCode == 0) {
			    System.out.println("MATLAB code executed successfully... :)");
			}
			else {
			  System.out.println("MATLAB Computations failed... :((");
			}
			
	System.setProperty("user.dir", currentDir);
	}
	
	

}
