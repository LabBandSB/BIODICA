package fr.curie.BIODICA;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import vdaoengine.ProcessTxtData;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;

public class BIODICAPipeLine {

	public String MATLABICAFolder = "";
	public String workFolder = "";
	public String DefaultWorkFolder = "";
	public String ConfigFileName = "";
	public String GSEAExecutable = "";
	public String GeneSetFolder = "";
	public String dataTablePath = "";
	public String sampleAnnotationFilePath = "";
	public String geneAnnotationFilePath = "";
	public String reportFolder = "";
	
	public String HTMLSourceFolder = "";
	
	public String analysisprefix = "";
	
	public String path2PPINetwork = "";

	
	public int numberOfComponents[] = {10};
	public int numberOfGSEAPermutations = 10;
	
	public float AssociationAnalysisThreshold = 3f;
	public float MinimalTolerableStability = 0.8f;
	public int MinNumberOfDistinctValuesInNumericals = 10;
	public int MinNumberOfSamplesInCategory = 3;
	public int MaxNumberOfCategories = 10;
	
	
	public boolean ComputeRobustStatistics = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			

			//FileUtils.deleteQuietly(new File("C:/Datas/BIODICA/work/pdx1000_ICA/_done"));
			//ProcessTxtData.CompileAandSTables("C:/Datas/BIODICA/work/pdx1000_ICA/stability/","pdx1000_ica");
			//System.exit(0);
			
			Locale.setDefault(new Locale("en", "US"));
			
			boolean doICAMATLAB = false;
			boolean doGSEA = false;
			boolean doMetaSampleAnalysis = false;			
			boolean doMetaGeneAnalysis = false;
			boolean doReporting = false;
			boolean doAnalysisForOptimalComponentNumber = false;
			boolean doCreateAandSFromPrecomputed = false;
			boolean doBBHGraph = false;
			boolean doOFTEN = false;
			boolean doToppGene = false;
			
			boolean splitPositiveAndNegativeTailsForMetaanalysis = false;

			String folderWithPrecomputedICAResults = "";
			int numberOfComponentsToSelect = 10;
			
			int OFTENnstart=100, OFTENnstep=50, OFTENnend=600, OFTENnperm=100;


			
			if(args.length==0){
				System.out.println("Usage of BIODICA PipeLine in command line mode:");
				System.out.println("java -jar BIODICA.jar [options] [actions]");
				System.out.println("Required options:");
				System.out.println("-config <filename> - configuration file path");
				System.out.println("-datatable <filename> - path to the datatable to work with");
				System.out.println("-outputfolder <foldername> - work folder (if it does not exist then it will be created)");
				System.out.println("============== ACTIONS ============= ");
				System.out.println("-doicamatlab <numberOfComponents> - calculate specified number of ICA components using fastica and icasso implemented in Matlab with default parameters. The method can run ICA trying many different specified number of components; in this case <number of components> should list several numbers separated by comma. All intermediate files with computed components will be written into a 'stability' subfolder. Adding '-donumbercomponents stability' option will perform several analyses for choosing the best number of components.");
				System.out.println("-donumbercomponents <folderWithICAresults> - makes analysis on a pre-computed results of ICA application with several values for the chosen number of components tried. The <folderWithICAresults> is a relative to the working subfolder path; normally, it should be 'stability'");
				System.out.println("-docompileprecomputed <folderWithICAresults#number_of_components> - look into the folder folderWithICAresults with precomputed ICA results and compile a new pair of A and S files for <number_of_components>. folderWithICAresults should be specified as relative to the work folder. Example of use: '-docompileprecomputed stability#10'. If the precomputed <number_of_components> file won't be found then the file with the maximal number of components will be used. Instead of a number, the <number_of_components> can be specified as 'optimal'; in this case the optimal number of components will be chosen based on stability criterion.");
				System.out.println("-dogsea <numberOfPermutations> - make GSEA analysis for all computed metagenes, using numberOfPermutations for assessing the p-values for the enrichment.");
				System.out.println("-dometasampleanalysis <sampleAnnotationFile> - make automated analysis for associations between computed ICs and the sample annotations.");
				System.out.println("-dometageneanalysis <geneAnnotationFile> - make automated analysis for associations between computed ICs and the sample annotations.");
				System.out.println("-dooften <ppinetworkfile>[#nstart,nstep,nend,nperm] - make automated analysis of ICA metagenes to associate them to a subnetwork in a global PPI network. If 'nstart,nstep,nend,nperm' values are not specified (parameters of OFTEN score scan) then the following defaults are used nstart=100, nstep=50, nend=600, nperm=100.");
				System.out.println("-doreport <folderToCreateReport> - produce HTML report for all analyses made in the working folder in the specified folder <folderToCreateReport>.");
				System.out.println("-dobbhgraph <folderWithSfiles> - compile BBH graph from a set of files ending with “_S.xls”, containing the metagenes corresponding to ICs.");
				System.out.println("-dotoppgene - perform gene set enrichment analysis using toppgene suite");
			}
			
			
			BIODICAPipeLine biodica = new BIODICAPipeLine();
			
			for(int i=0;i<args.length;i++){
				if(args[i].equals("-config"))
					biodica.ConfigFileName = args[i+1];
				if(args[i].equals("-datatable"))
					biodica.dataTablePath = args[i+1];
				if(args[i].equals("-outputfolder"))
					biodica.workFolder = args[i+1];
				
				if(args[i].equals("-doicamatlab")){
					String nums[] = args[i+1].split(",");
					biodica.numberOfComponents = new int[nums.length];
					for(int kk=0;kk<nums.length;kk++)
						biodica.numberOfComponents[kk] = Integer.parseInt(nums[kk]);
					doICAMATLAB = true;
				}
				if(args[i].equals("-donumbercomponents")){
					folderWithPrecomputedICAResults = args[i+1];
					doAnalysisForOptimalComponentNumber = true;
				}
				if(args[i].equals("-docompileprecomputed")){
					String arg = args[i+1];
					try{
					String pair[] = arg.split("#");
					folderWithPrecomputedICAResults = pair[0];
					numberOfComponentsToSelect = Integer.parseInt(pair[1]);
					doCreateAandSFromPrecomputed = true;
					}catch(Exception e){
						System.out.println("ERROR: the argument for 'docompileprecomputed' action is not in the format string#number (read README).");
					}
				}
				if(args[i].equals("-dogsea")){
					biodica.numberOfGSEAPermutations = Integer.parseInt(args[i+1]);
					doGSEA = true;
				}
				if(args[i].equals("-dotoppgene")){
					doToppGene = true;
				}
				if(args[i].equals("-dometasampleanalysis")){
					biodica.sampleAnnotationFilePath = args[i+1];
					doMetaSampleAnalysis = true;
				}
				if(args[i].equals("-dometageneanalysis")){
					biodica.geneAnnotationFilePath = args[i+1];
					doMetaGeneAnalysis = true;
				}
				if(args[i].equals("-dooften")){
					String parts[] = args[i+1].split("#");
					if(parts.length==2){
						String ns[] = parts[1].split(",");
						OFTENnstart = Integer.parseInt(ns[0]);
						OFTENnstep = Integer.parseInt(ns[1]);
						OFTENnend = Integer.parseInt(ns[2]);
						OFTENnperm = Integer.parseInt(ns[3]);
					}
					biodica.path2PPINetwork = parts[0];
					doOFTEN = true;
				}
				if(args[i].equals("-doreport")){
					biodica.reportFolder = args[i+1];
					doReporting = true;
				}
				if(args[i].equals("-dobbhgraph")){
					doBBHGraph = true;
					folderWithPrecomputedICAResults = args[i+1];
					String parts[] = folderWithPrecomputedICAResults.split("#");
					if(parts.length==2){
						folderWithPrecomputedICAResults = parts[0];
						splitPositiveAndNegativeTailsForMetaanalysis = parts[1].trim().equals("split");
					}
				}
				
			}
			
			File cf = new File(biodica.ConfigFileName);
			if(!cf.exists()){
				System.out.println("FATAL ERROR: Can not find the configuration file \""+biodica.ConfigFileName+"\"");
				System.exit(-1);
			}

			/*  =======================================================================================
			 * 	==========================            READING BIODICA CONFIGURATION  ===================
			 *  =======================================================================================						
			 */ 
			
			biodica.readConfigFile();

			/*  =======================================================================================
			 * 	==========================            PRELIMINARY CHECKS  =============================
			 *  =======================================================================================						
			 */ 
			
			// Check out existence of work folder and the data file
			if(biodica.workFolder.equals("")) biodica.workFolder = biodica.DefaultWorkFolder;
			File wf = new File(biodica.workFolder);
			if(!wf.exists()){
				if(!wf.mkdir()){
					System.out.println("FATAL ERROR: Can not create working folder \""+biodica.workFolder+"\"");
					System.exit(-1);
				}
			}
			File df = new File(biodica.dataTablePath);
			
			if(!biodica.dataTablePath.trim().equals("")){
			if(!df.exists()){
				System.out.println("FATAL ERROR: Can not find the data file \""+biodica.dataTablePath+"\"");
				System.exit(-1);
			}else{
				biodica.analysisprefix = df.getName().substring(0, df.getName().length()-4);
			}
			}
			
			/*  =======================================================================================
			 * 	==========================            ACTIONS             =============================
			 *  =======================================================================================						
			 */ 

			/*  =======================================================================================
			 * 	==========================            ICA COMPUTATION     =============================
			 *  =======================================================================================						
			 */ 
			if(doICAMATLAB){
				
				System.out.println("=========================================");
				System.out.println("======  Performing ICA computations =====");
				System.out.println("=========================================");
				
				//MATLABExcecutor.executeMatlabFastICA(biodica.MATLABICAFolder, biodica.workFolder, df.getName(), biodica.numberOfComponents);
				/*File wfica = new File(biodica.workFolder+"/"+biodica.analysisprefix+"_ICA");
				wfica.mkdir();
				FileUtils.copyFile(df, new File(wfica.getAbsolutePath()+"/"+df.getName()));
				System.out.println("Starting MATLAB ICA Computations...");
				MATLABExcecutor.executeMatlabFastICA(biodica.MATLABICAFolder, wfica.getAbsolutePath()+System.getProperty("file.separator"), df.getName(), 2);*/
				File wfica = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_ICA");
				wfica.mkdir();
				File wficaStability = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_ICA"+File.separator+"stability");
				wficaStability.mkdir();
				
				FileUtils.copyFile(df, new File(wfica.getAbsolutePath()+File.separator+df.getName()));
				System.out.println("Preparing data file for ICA computations...");
				String arguments[] = new String[3];
				arguments[0] = wfica.getAbsolutePath()+File.separator+df.getName(); 
				arguments[1] = "-center"; 
				arguments[2] = "-prepare4ICA";
				ProcessTxtData.main(arguments);;
				System.out.println("Starting MATLAB ICA Computations...");
				String fn_numerical = df.getName().substring(0, df.getName().length()-4)+"_ica_numerical.txt"; 
				MATLABExcecutor.executeMatlabFastICANumerical(biodica.MATLABICAFolder, wfica.getAbsolutePath()+System.getProperty("file.separator"), fn_numerical, biodica.numberOfComponents);
				System.out.println("Formatting results of ICA computations...");
				ProcessTxtData.CompileAandSTables(wfica.getAbsolutePath()+System.getProperty("file.separator"), biodica.analysisprefix+"_ica");
				
				System.out.println("Cleaning the work folder...");
				File files[] = wfica.listFiles();
				for(int i=0;i<files.length;i++){
					String fn = files[i].getName();
					boolean move = false;
					if(fn.startsWith(biodica.analysisprefix+"_ica_numerical")) move = true;
					if(fn.startsWith("A_"+biodica.analysisprefix+"_ica_numerical")) move = true;	
					if(fn.startsWith("S_"+biodica.analysisprefix+"_ica_numerical")) move = true;
					if(fn.startsWith(biodica.analysisprefix+"_ica_ids")) move = true;
					if(fn.startsWith(biodica.analysisprefix+"_ica_samples")) move = true;
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
			}

			/*
			 * Select the optimal number of components and make the A and S matrices out of it
			 */
			if(doAnalysisForOptimalComponentNumber){
				
				System.out.println("=======================================================");
				System.out.println("======  Choosing the optimal number of components =====");
				System.out.println("=======================================================");
				
				File workingFolder = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_ICA"+File.separator);
				File stabilityFolder = new File(workingFolder.getAbsolutePath()+File.separator+folderWithPrecomputedICAResults+File.separator);
				numberOfComponentsToSelect = NumberOfComponentsOptimizer.reccomendOptimalNumberOfComponentsFromStability(stabilityFolder.getAbsolutePath()+File.separator,biodica.analysisprefix+"_ica",biodica.MinimalTolerableStability);
				doCreateAandSFromPrecomputed = true;
			}
			/*
			 * Create A and S matrices from a precomputed file
			 */
			if(doCreateAandSFromPrecomputed){
				
				System.out.println("=============================================================");
				System.out.println("======  Compiling A and S files from a precomputed file =====");
				System.out.println("=============================================================");
				
				
				File workingFolder = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_ICA"+File.separator);
				File stabilityFolder = new File(workingFolder.getAbsolutePath()+File.separator+folderWithPrecomputedICAResults+File.separator);
				ProcessTxtData.CompileAandSTables(stabilityFolder.getAbsolutePath()+File.separator, biodica.analysisprefix+"_ica", numberOfComponentsToSelect);
				System.out.println("Replacing A and S files in the working folder...");
				FileUtils.deleteQuietly(new File(workingFolder.getAbsolutePath()+File.separator+biodica.analysisprefix+"_ica_A.xls"));
				FileUtils.deleteQuietly(new File(workingFolder.getAbsolutePath()+File.separator+biodica.analysisprefix+"_ica_S.xls"));
				FileUtils.moveFileToDirectory(new File(stabilityFolder.getAbsolutePath()+File.separator+biodica.analysisprefix+"_ica_A.xls"), workingFolder, true);
				FileUtils.moveFileToDirectory(new File(stabilityFolder.getAbsolutePath()+File.separator+biodica.analysisprefix+"_ica_S.xls"), workingFolder, true);
			}
			

			/*  =======================================================================================
			 * 	==========================         METAGENE ANNOTATIONS   =============================
			 *  =======================================================================================						
			 */ 
			if(doGSEA){
				
				System.out.println("======================================");
				System.out.println("======  Performing GSEA analysis =====");
				System.out.println("======================================");
				
				File wfica = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_ICA");
				// Merge all gmt files in one
				File wfgsea = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_GSEA");
				wfgsea.mkdir();
				MetaGeneAnnotation.MergeGMTFilesInFolder(biodica.GeneSetFolder);
				// Make RNK files
				MetaGeneAnnotation.produceRankFilesFromTable(wfica.getAbsolutePath()+System.getProperty("file.separator")+biodica.analysisprefix+"_ica_S.xls", wfgsea.getAbsolutePath(), biodica.analysisprefix);
				File wfgsear = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_GSEA"+System.getProperty("file.separator")+"results");
				
				// Launch GSEA in multithread
				//if(false){
				// not to do GSEA
				if(wfgsear.exists()){
					System.out.println("Deleting "+wfgsear.getAbsolutePath()+"...");
					emptyFolder(wfgsear, false);
				}
				wfgsear.mkdir();
				Date timestart = new Date();
				System.setSecurityManager(new BIODICASecurityManager());
				File list[] = wfgsea.listFiles();
				Vector<Thread> GSEAThreads = new Vector<Thread>();
				for(File f: list)if(f.getName().endsWith(".rnk")){
					GSEAThread gsea = new GSEAThread();
					gsea.rnkFile = f.getAbsolutePath();
					gsea.gmtFile = biodica.GeneSetFolder+System.getProperty("file.separator")+"total.gmt";
					gsea.numberOfPermutations = biodica.numberOfGSEAPermutations;
					Thread gth = new Thread(gsea);
					GSEAThreads.add(gth);
					gth.start();
					Thread.sleep(2000);
				}
				// Wait when everything will be finished
				int progress = 0;
				int oldprogress = 0;
				while(progress<100){
					Thread.sleep(10000);
					int numberOfActiveThreads = 0;
					for(int i=0;i<GSEAThreads.size();i++){
						if(GSEAThreads.get(i).isAlive())
							numberOfActiveThreads++;
					}
					for(int k=0;k<numberOfActiveThreads;k++)
						System.out.print("*");
					for(int k=0;k<GSEAThreads.size()-numberOfActiveThreads;k++)
						System.out.print("x");
					System.out.println("         Number Of Active Threads = "+numberOfActiveThreads);
					progress = (int)((1-(float)numberOfActiveThreads/(float)GSEAThreads.size())*100f+0.5);
					if(progress>oldprogress){
						//progress = MetaGeneAnnotation.checkGSEACompletion(folder, totalNumberOfGSEAAnalysis);
						System.out.println("GSEA progress "+progress+"%");
						oldprogress = progress;
					}
				}
				System.out.println("Finished all GSEA computations. Total time spent "+((new Date().getTime()-timestart.getTime())/1000)+" secs");
				//} // not to do GSEA
				MetaGeneAnnotation.FilterGSEAResults(wfgsear.getAbsolutePath()+System.getProperty("file.separator"),5,3f,0.01f,0.01f);
				FileUtils.copyFile(new File(biodica.HTMLSourceFolder+System.getProperty("file.separator")+"filteredgsea.css"), new File(wfgsear.getAbsolutePath()+System.getProperty("file.separator")+"filteredgsea.css"));
			}
			
			if(doToppGene){
				
				System.out.println("==========================================");
				System.out.println("======  Performing ToppGene analysis =====");
				System.out.println("==========================================");
				
				File wfica = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_ICA");
				// Merge all gmt files in one
				File wftoppgene = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_TOPPGENE");
				wftoppgene.mkdir();
				// Make RNK files
				File sfile = new File(wfica.getAbsolutePath()+System.getProperty("file.separator")+biodica.analysisprefix+"_ica_S.xls");
				Date timestart = new Date();

				System.out.println("Finished all ToppGene computations. Total time spent "+((new Date().getTime()-timestart.getTime())/1000)+" secs");
				//FileUtils.copyFile(new File(biodica.HTMLSourceFolder+System.getProperty("file.separator")+"filteredgsea.css"), new File(wfgsear.getAbsolutePath()+System.getProperty("file.separator")+"filteredgsea.css"));
			}
			
			
			if(doMetaGeneAnalysis){
				
				System.out.println("================================================================");
				System.out.println("======  Associating ICA metagenes to known gene properties =====");
				System.out.println("================================================================");
				
				//File wfgsea = new File(biodica.workFolder+"/"+biodica.analysisprefix+"_MGENE");
				//wfgsea.mkdir();
				File wfica = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_ICA");
				File wfmgene = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_MGENE");
				wfmgene.mkdir();
				
				File f = new File(biodica.geneAnnotationFilePath);
				if(f.isDirectory()){
					
					String smatrixfile = wfica.getAbsolutePath()+System.getProperty("file.separator")+biodica.analysisprefix+"_ica_S.xls";
					System.out.println("Loading S-matrix from "+smatrixfile+"... ");
					VDataTable vts = VDatReadWrite.LoadFromSimpleDatFile(smatrixfile, true, "\t");
					VSimpleProcedures.findAllNumericalColumns(vts);
					AssociationFinder.findCorrelationsWithMetagenesRNK(f.getAbsolutePath(), vts, wfmgene+System.getProperty("file.separator")+biodica.analysisprefix+"_S_associationsRNK.xls", biodica);					
					
				}else{

					String smatrixfile = wfica.getAbsolutePath()+System.getProperty("file.separator")+biodica.analysisprefix+"_ica_S.xls";
					System.out.println("Loading S-matrix from "+smatrixfile+"... ");
					VDataTable vts = VDatReadWrite.LoadFromSimpleDatFile(smatrixfile, true, "\t");
					System.out.println("Loading gene annotation file from "+biodica.geneAnnotationFilePath+" ... ");
					VDataTable vtann = VDatReadWrite.LoadFromSimpleDatFile(biodica.geneAnnotationFilePath, true, "\t");
					vtann.makePrimaryHash(vtann.fieldNames[0]);
					int numberOfMatchedGenes = 0;
					for(int k=0;k<vts.rowCount;k++) if(vtann.tableHashPrimary.get(vts.stringTable[k][0])!=null) numberOfMatchedGenes++;
					System.out.println(numberOfMatchedGenes+" genes have been matched ("+(int)(((float)numberOfMatchedGenes/(float)vts.rowCount)*100f)+"% from data matrix, "+(int)(((float)numberOfMatchedGenes/(float)vtann.rowCount)*100f)+"% from annotation file)");

					VDatReadWrite.useQuotesEverywhere = false;
					VDatReadWrite.writeNumberOfColumnsRows = false;
					VDataTable merged = VSimpleProcedures.MergeTables(vts, vts.fieldNames[0], vtann, vtann.fieldNames[0], "_");
					VDatReadWrite.saveToSimpleDatFile(merged, wfmgene+System.getProperty("file.separator")+biodica.analysisprefix+"_S_annot.xls");
					VSimpleProcedures.findAllNumericalColumns(merged);
					merged = Utils.PrepareTableForVidaExpert(merged);
					VDatReadWrite.saveToVDatFile(merged, wfmgene+System.getProperty("file.separator")+biodica.analysisprefix+"_S_annot.dat");
					
					Vector<String> strNames = new Vector<String>();
					for(int i=1;i<vts.colCount;i++) strNames.add(vts.fieldNames[i]);
					AssociationFinder.printAssosiationTable(merged, wfmgene+System.getProperty("file.separator")+biodica.analysisprefix+"_S_associations.xls", strNames, biodica, true);
					
				}
			}
			if(doOFTEN){
				
				System.out.println("=======================================");
				System.out.println("======  Performing OFTEN analysis =====");
				System.out.println("=======================================");
				
				
				OFTENAnalysis of = new OFTENAnalysis();
				System.out.println("Loading PPI network from "+biodica.path2PPINetwork);
				of.loadPPINetwork(biodica.path2PPINetwork);
				System.out.println("Network loaded.");
				File wfica = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_ICA");
				File wfOFTEN = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_OFTEN");
				wfOFTEN.mkdir();
				FileUtils.cleanDirectory(wfOFTEN);
				String smatrixfile = wfica.getAbsolutePath()+System.getProperty("file.separator")+biodica.analysisprefix+"_ica_S.xls";
				System.out.println("Copying S-file "+smatrixfile+"... ");
				String tablePath = biodica.workFolder+File.separator+biodica.analysisprefix+"_OFTEN"+File.separator+biodica.analysisprefix+"_ica_S.xls"; 
				if(new File(tablePath).exists()){
					FileUtils.deleteQuietly(new File(tablePath));
				}
				FileUtils.copyFileToDirectory(new File(smatrixfile), wfOFTEN, true);
				of.numberOfPermutationsForSizeTest = OFTENnperm;
				String report = of.completeOFTENAnalysisOfTable(tablePath,biodica.analysisprefix,OFTENnstart,OFTENnend,OFTENnstep);
				String reportSummary = of.extractScoresFromCombinedOFTENReport(report);
				
				fr.curie.BIODICA.Utils.writeStringToFile(report, wfOFTEN.getAbsolutePath()+File.separator+"_OFTENreport.txt");
				fr.curie.BIODICA.Utils.writeStringToFile(reportSummary, wfOFTEN.getAbsolutePath()+File.separator+"_OFTENreportSummary.txt");
			}

			/*  =======================================================================================
			 * 	==========================         METASAMPLE ANNOTATIONS   ===========================
			 *  =======================================================================================						
			 */ 
			if(doMetaSampleAnalysis){
				
				System.out.println("=============================================================");
				System.out.println("======  Associate ICA metasamples to sample annotations =====");
				System.out.println("=============================================================");
				
				// Association study
				File wfica = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_ICA");
				File wfmsample = new File(biodica.workFolder+File.separator+biodica.analysisprefix+"_MSAMPLE");
				String amatrixfile = wfica.getAbsolutePath()+System.getProperty("file.separator")+biodica.analysisprefix+"_ica_A.xls";
				wfmsample.mkdir();
				System.out.println("Loading A-matrix from "+amatrixfile+"... ");
				VDataTable vta = VDatReadWrite.LoadFromSimpleDatFile(amatrixfile, true, "\t");
				System.out.println("Loading sample annotation file from "+biodica.sampleAnnotationFilePath+" ... ");
				VDataTable vtann = VDatReadWrite.LoadFromSimpleDatFile(biodica.sampleAnnotationFilePath, true, "\t");
				vtann.makePrimaryHash(vtann.fieldNames[0]);
				int numberOfMatchedSamples = 0;
				for(int k=0;k<vta.rowCount;k++) if(vtann.tableHashPrimary.get(vta.stringTable[k][0])!=null) numberOfMatchedSamples++;
				System.out.println(numberOfMatchedSamples+" samples have been matched ("+(int)(((float)numberOfMatchedSamples/(float)vta.rowCount)*100f)+"% from data matrix, "+(int)(((float)numberOfMatchedSamples/(float)vtann.rowCount)*100f)+"% from annotation file)");

				VDatReadWrite.useQuotesEverywhere = false;
				VDatReadWrite.writeNumberOfColumnsRows = false;
				VDataTable merged = VSimpleProcedures.MergeTables(vta, vta.fieldNames[0], vtann, vtann.fieldNames[0], "_");
				VDatReadWrite.saveToSimpleDatFile(merged, wfmsample+System.getProperty("file.separator")+biodica.analysisprefix+"_A_annot.xls");
				VSimpleProcedures.findAllNumericalColumns(merged);
				merged = Utils.PrepareTableForVidaExpert(merged);
				VDatReadWrite.saveToVDatFile(merged, wfmsample+System.getProperty("file.separator")+biodica.analysisprefix+"_A_annot.dat");
				
				Vector<String> strNames = new Vector<String>();
				for(int i=1;i<vta.colCount;i++) strNames.add(vta.fieldNames[i]);
				AssociationFinder.printAssosiationTable(merged, wfmsample+System.getProperty("file.separator")+biodica.analysisprefix+"_A_associations.xls", strNames, biodica, true);
				
				// Bi-modality tests
			}
			
			if(doBBHGraph){
				
				System.out.println("==================================================");
				System.out.println("======  Compute Best-Bidirectional Hit graph =====");
				System.out.println("==================================================");
				
				if(splitPositiveAndNegativeTailsForMetaanalysis)
					MakeCorrelationGraph.SplitAllFilesIntoPositiveAndNegativeTails(folderWithPrecomputedICAResults);
				MakeCorrelationGraph.MakeCorrelationGraph(folderWithPrecomputedICAResults, false);
				MakeCorrelationGraph.assembleCorrelationGraph(folderWithPrecomputedICAResults);
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void readConfigFile() throws Exception{
		Vector<String> lines = vdaoengine.utils.Utils.loadStringListFromFile(ConfigFileName);
		System.out.println("Reading BIODICA configuration file.. "+ConfigFileName);
		for(String s: lines){
			StringTokenizer st = new StringTokenizer(s,"=");
			String left = st.nextToken().trim().toLowerCase();
			String right = st.nextToken().trim();
			if(left.equals("matlabicafolder")){
				MATLABICAFolder = right;
				System.out.println("MATLABICAFolder = "+right);
			}
			if(left.equals("defaultworkfolder")){
				DefaultWorkFolder  = right;
				System.out.println("DefaultWorkFolder = "+right);
			}
			/*if(left.equals("gseaexecutable")){
				GSEAExecutable  = right;
				System.out.println("GSEAExecutable = "+right);
			}*/
			if(left.equals("genesetfolder")){
				GeneSetFolder  = right;
				System.out.println("GeneSetFolder = "+right);
			}
			if(left.equals("htmlsourcefolder")){
				HTMLSourceFolder = right;
				System.out.println("HTMLSourceFolder = "+right);
			}
			if(left.equals("computerobuststatistics")){
				ComputeRobustStatistics = right.toLowerCase().equals("true");
				System.out.println("ComputeRobustStatistics = "+right);
			}
			if(left.equals("minnumberofdistinctvaluesinnumericals")){
				MinNumberOfDistinctValuesInNumericals = Integer.parseInt(right.toLowerCase());
				System.out.println("MinNumberOfDistinctValuesInNumericals = "+MinNumberOfDistinctValuesInNumericals);
			}
			if(left.equals("minnumberofsamplesincategory")){
				MinNumberOfSamplesInCategory = Integer.parseInt(right.toLowerCase());
				System.out.println("MinNumberOfSamplesInCategory = "+MinNumberOfSamplesInCategory);
			}
			if(left.equals("maxnumberofcategories")){
				MaxNumberOfCategories = Integer.parseInt(right.toLowerCase());
				System.out.println("MaxNumberOfCategories = "+MaxNumberOfCategories);
			}
			if(left.equals("associationanalysisthreshold")){
				AssociationAnalysisThreshold = Float.parseFloat(right.toLowerCase());
				System.out.println("AssociationAnalysisThreshold = "+AssociationAnalysisThreshold);
			}
			if(left.equals("minimaltolerablestability")){
				MinimalTolerableStability = Float.parseFloat(right.toLowerCase());
				System.out.println("MinimalTolerableStability= "+MinimalTolerableStability);
			}			
						
			//if(left.equals("")){
			//	= Integer.parseInt(right.toLowerCase());
			//	System.out.println(" = "+);
			//}
			
			
		}
	}
	
	public static void emptyFolder(File folder, boolean deleteTheFolderItself) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                emptyFolder(f, true);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    if(deleteTheFolderItself)
	    	folder.delete();
	}

}
