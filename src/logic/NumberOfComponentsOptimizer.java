package logic;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


import edu.mit.broad.genome.utils.FileUtils;
import vdaoengine.TableUtils;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;

public class NumberOfComponentsOptimizer {

	//public static String prefixes[] = {"ACC","BLCA"};
	public static String prefixes[] = {"ACC","BLCA","BRCA","CESC","CHOL","COAD","DLBC","ESCA","GBM","HNSC","KICH","KIRC","KIRP","LGG","LIHC","LUAD","LUSC","MESO","OV","PAAD","PCPG","PRAD","READ","SARC","SKCM","STAD","TGCT","THCA","THYM","UCEC","UCS","UVM"};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			//makeConvergenceTable("C:/Datas/BIODICA_GUI/work/BRCATCGA_ICA/"); System.exit(0);
			
			/*Vector<Integer> nums = new Vector<Integer>();
			Vector<Float> stabilities = new Vector<Float>();
			readStabilityProfile("C:/Datas/BIODICA/work/pdx1000_ICA/stability/","pdx1000_ica",nums,stabilities);*/
			//String folder = "C:/Datas/MOSAIC/analysis/ica/full_rescue/BATCH_FIRST/stability/";
			//String prefix = "BATCH_FIRST";
			//String folder = "C:/Datas/BIODICA/work/pdx1000_ICA/stability/";
			//String prefix = "pdx1000_ica";
			//String folder = "C:/Datas/PanMethylome/methylome/BRCA/stability/";
			//String prefix = "BRCAn";
			//String folder = "C:/Datas/MOSAIC/analysis/ica/full_rescue/stability/";
			//String prefix = "fullc";
			//String folder = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/saves/test_stability/";
			//String prefix = "Rescue_ica";
			//String folder = "D:/ICA_complete_output/PanCan_ICA/OV/";
			//String prefix = "OV_expression_matrix";
			//String folder = "C:/Datas/BIODICA/work/NEUROBLASTOMAlf_ICA/stability/";
			//String prefix = "NEUROBLASTOMAlf_ica";
			 			
			//reccomendOptimalNumberOfComponentsFromStability(folder,prefix, 0.8f);
			
			/*String prefixes[] = {"ACC","BLCA","BRCA","CESC","CHOL","COAD","DLBC","ESCA","GBM","HNSC","KICH","KIRC","KIRP","LGG","LIHC","LUAD","LUSC","MESO","OV","PAAD","PCPG","PRAD","READ","SARC","SKCM","STAD","TGCT","THCA","THYM","UCEC","UCS","UVM"};
			//String prefixes[] = {"ACC","BLCA"};
			FileWriter fw = new FileWriter("C:/Datas/ICA_numberOfComponents/nums.txt");
			fw.write("CANCERTYPE\tnumberOfComponents6\tnumberOfComponents7\tnumberOfComponents8\n");
			for(int i=0;i<prefixes.length;i++){
				String folder = "D:/ICA_complete_output/PanCan_ICA/"+prefixes[i]+"/";
				String prefix = prefixes[i]+"_expression_matrix";
				int numberOfComponents6 = reccomendOptimalNumberOfComponentsFromStability(folder,prefix, 0.6f);
				int numberOfComponents7 = reccomendOptimalNumberOfComponentsFromStability(folder,prefix, 0.7f);
				int numberOfComponents8 = reccomendOptimalNumberOfComponentsFromStability(folder,prefix, 0.8f);
				fw.write(prefixes[i]+"\t"+numberOfComponents6+"\t"+numberOfComponents7+"\t"+numberOfComponents8+"\n");
				
			}
			fw.close();*/
			
			//compileDataSetForTesting();
			//analyzeMetagene("C:/Datas/ICA_numberOfComponents/UnderShoot/","C:/Datas/ICA_numberOfComponents/metagenes/M7_CELLCYCLE.rnk");
			//analyzeMetagene("C:/Datas/ICA_numberOfComponents/numberOfComponents6/","C:/Datas/ICA_numberOfComponents/metagenes/M7_CELLCYCLE.rnk");
			//analyzeMetagene("C:/Datas/ICA_numberOfComponents/OverShoot1/","C:/Datas/ICA_numberOfComponents/metagenes/M7_CELLCYCLE.rnk");
			//analyzeMetagene("C:/Datas/ICA_numberOfComponents/BRCA/","C:/Datas/ICA_numberOfComponents/metagenes/M12_MYOFIBROBLASTS.rnk");
			//analyzeMetagene("C:/Datas/ICA_numberOfComponents/BRCA/","C:/Datas/ICA_numberOfComponents/metagenes/BCT7_BASAL.rnk");
			//analyzeMetagene("C:/Datas/ICA_numberOfComponents/BRCA/","C:/Datas/ICA_numberOfComponents/metagenes/M4_MITOCHONRIA_TRANSLATION.rnk");
			
			//analyzeMetagene("C:/Datas/ICA_numberOfComponents/BRCA/","C:/Datas/ICA_numberOfComponents/metagenes/GENES_S.xls","C:/Datas/PanCancerTCGA/ICA_complete_output/PanCan_ICA/");
			//analyzeMetagene("C:/Datas/ICA_numberOfComponents/BRCA/","C:/Datas/ICA_numberOfComponents/metagenes/M7_CELLCYCLE.rnk");
			//analyzeMetagene("C:/Datas/ICA_numberOfComponents/BRCA/","C:/Datas/ICA_numberOfComponents/metagenes/M8_IMMUNE.rnk");
			//analyzeGeneSet("C:/Datas/ICA_numberOfComponents/BRCA/","C:/Datas/ICA_numberOfComponents/signatures/breast_cancer/brca.gmt","SMID_BREAST_CANCER_BASAL_UP",3f);
			//reformatICADecompositionFilesInFolder("C:/Datas/BIODICA/work/aggregated_1964_log_ICA/stability/");
			//reformatICADecompositionFilesInFolder("C:/Datas/BIODICA/work/metabric_ICA/");
			
			//analyzeMetagene("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/METABRIC/","C:/Datas/ICA_numberOfComponents/metagenes/M7_CELLCYCLE.rnk","C:/Datas/PanCancerTCGA/ICA_complete_output/PanCan_ICA/");
			//analyzeMetagene("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/METABRIC/","C:/Datas/ICA_numberOfComponents/metagenes/GENES_S.xls","C:/Datas/PanCancerTCGA/ICA_complete_output/PanCan_ICA/");
			//analyzeMetagene("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/METABRIC/","C:/Datas/ICA_numberOfComponents/metagenes/M12_MYOFIBROBLASTS.rnk","C:/Datas/PanCancerTCGA/ICA_complete_output/PanCan_ICA/");
			
			//analyzeGeneSet("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/METABRIC/","C:/Datas/ICA_numberOfComponents/signatures/breast_cancer/brca.gmt","SMID_BREAST_CANCER_BASAL_UP",3f);
			//analyzeGeneSet("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/METABRIC/","C:/Datas/ICA_numberOfComponents/signatures/cell_cycle/FREEMAN_CLUSTERS.gmt","FREEMAN_CELLCYCLE",3f);
			
			//analyzeMetageneList("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/METABRIC/","C:/Datas/ICA_numberOfComponents/metagenes/","C:/Datas/PanCancerTCGA/ICA_complete_output/PanCan_ICA/");
			
			//MakeGraphOfReproducibilityScoreFromCorrelationGraph("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/test/normal.xgmml");
			//System.out.println();
			//MakeGraphOfReproducibilityScoreFromCorrelationGraph("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/test/optimal0.6.xgmml");
			//MakeGraphOfReproducibilityScoreFromCorrelationGraph("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/100/graph100_nocit.xgmml","C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/optimalstability.txt","MSTD06");
			//MakeGraphOfReproducibilityScoreFromCorrelationGraph("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/optimal0.6/optimal0.6_1.xgmml","C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/optimalstability.txt","MSTD06");
			//MakeGraphOfReproducibilityScoreFromCorrelationGraph("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/optimal0.6/reproducibility_highorder/metabric_50.xgmml","C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/optimalstability.txt","MSTD");
			//MakeGraphOfReproducibilityScoreFromCorrelationGraph("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/MSTD/MSTD.xgmml","C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/optimalstability_slightlymodified.txt","MSTD");
			MakeGraphOfReproducibilityScoreFromCorrelationGraph("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/100/without_stabilisation/correlation_graph_3865.xgmml","C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/optimalstability_zero.txt","ZERO");
			
			//outlierAnalysis("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/100/without_stabilisation/BRCATCGA100_S.xls",true);
			
			//outlierAnalysis("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/100/metabric_100_S.xls");
			//outlierAnalysis("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/METABRIC/metabric_50_S.xls");
			//outlierAnalysis("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/METABRIC/metabric_20_S.xls");
			//createOutlierLists("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/METABRIC/");
			
			//findComponentsInHigherOrderDecompositions("C:/Datas/ICA_numberOfComponents/numberOfComponents6/","C:/Datas/ICA_numberOfComponents/PanCancer100/");
			//findComponentsInHigherOrderDecompositions("C:/Datas/ICA_numberOfComponents/numberOfComponents6/","C:/Datas/ICA_numberOfComponents/numberOfComponents8/");
			//findComponentsInHigherOrderDecompositions("C:/Datas/ICA_numberOfComponents/test1/","C:/Datas/ICA_numberOfComponents/test2/");
			//findComponentsInHigherOrderDecompositions("C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/optimal0.6/reproducibility_highorder/","C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/_metaanalysis/optimal0.6/reproducibility_highorder/50/"); 
			//analyzeHighOrderReproducibilityResults("C:/Datas/ICA_numberOfComponents/numberOfComponents6/50/","C:/Datas/ICA_numberOfComponents/nums.txt","numberOfComponents6");
			//countTypesOfReproducibleHighOrderComponents("C:/Datas/ICA_numberOfComponents/numberOfComponents6/100/metabric_100_avrank.xgmml","metabric",100,0.2f,0.3f);
			//countTypesOfReproducibleHighOrderComponents("C:/Datas/ICA_numberOfComponents/numberOfComponents6/50/metabric_50_avrank.xgmml","metabric",50,0.2f,0.3f);
			//countTypesOfReproducibleHighOrderComponentsInFolder("C:/Datas/ICA_numberOfComponents/numberOfComponents6/100/",0.2f,0.3f);
			
			
			//MetaGeneAnnotation.FilterGSEAResults("C:/Datas/ICA_numberOfComponents/GSEA_Analysis/BLCA_GSEA/results/",10,3f,0f,0f);			
			//AnalyzeGSEABIODICAResults("C:/Datas/BIODICA/work/metabric50_GSEA/results/","metabric50_");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static void countTypesOfReproducibleHighOrderComponentsInFolder(String folder, float thresholdForDetectingSimilarCorrelations, float thresholdForSignificantCorrelations) throws Exception{
		File files[] = (new File(folder)).listFiles();
		int allCounts[] = new int[7];
		float averageCountsF[] = new float[7];
		int allLowDimComponents = 0;
		int allHighDimComponents = 0;
		int numberOfSets = 0;
		
		for(File f: files){if(f.getName().endsWith(".xgmml"))if(!f.getName().endsWith("_avrank.xgmml"))if(!f.getName().endsWith("_conservation.xgmml")){
			MakeGraphOfReproducibilityScoreFromCorrelationGraph(f.getAbsolutePath(),"C:/Datas/PanCancerTCGA/ICA_complete_output/breast_cancer/optimalstability.txt","MSTD06");
		}}
		files = (new File(folder)).listFiles();
		for(File f: files)if(f.getName().endsWith("_avrank.xgmml")){
			String fname = f.getName().substring(0, f.getName().length()-13);
			String parts[] = fname.split("_");
			String name = parts[0];
			int HighOrderDimension = Integer.parseInt(parts[1]);
			System.out.println();
			int counts[] = countTypesOfReproducibleHighOrderComponents(f.getAbsolutePath(),name,HighOrderDimension,thresholdForDetectingSimilarCorrelations,thresholdForSignificantCorrelations);
			int thisAllLowDim = 0;
			int thisAllHighDim = 0;
			for(int i=1;i<=5;i++) { allLowDimComponents+=counts[i]; thisAllLowDim+=counts[i]; }
			for(int i=1;i<=6;i++) allCounts[i]+=counts[i]; 
			allHighDimComponents+=HighOrderDimension;
			thisAllHighDim=HighOrderDimension;
			numberOfSets++;
			for(int i=1;i<=5;i++) averageCountsF[i]+=(float)counts[i]/(float)thisAllLowDim;
			averageCountsF[6]+=(float)counts[6]/(float)thisAllHighDim;
		}
		
		System.out.println();
		System.out.println();
		System.out.println("robustly reproducible\t"+(float)(allCounts[1])/(float)allLowDimComponents+"\t"+averageCountsF[1]/(float)numberOfSets); 
		System.out.println("lost stability\t"+(float)(allCounts[2])/(float)allLowDimComponents+"\t"+averageCountsF[2]/(float)numberOfSets); 
		System.out.println("split\t"+(float)(allCounts[3])/(float)allLowDimComponents+"\t"+averageCountsF[3]/(float)numberOfSets); 
		System.out.println("lost reciprocity\t"+(float)(allCounts[4])/(float)allLowDimComponents+"\t"+averageCountsF[4]/(float)numberOfSets); 
		System.out.println("lost correlation\t"+(float)(allCounts[5])/(float)allLowDimComponents+"\t"+averageCountsF[5]/(float)numberOfSets); 
		System.out.println("emerged in high-order\t"+(float)(allCounts[6])/(float)allHighDimComponents+"\t"+averageCountsF[6]/(float)numberOfSets);

		
	}
	
	public static int[] countTypesOfReproducibleHighOrderComponents(String graphFileName, String name, int HighOrderDimension, float thresholdForDetectingSimilarCorrelations, float thresholdForSignificantCorrelations) throws Exception{
		
		int counts[] = new int[7];
		
		Graph graph = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(graphFileName));
		graph.calcNodesInOut();
		int MSTD = 0;
		for(Node n: graph.Nodes){
			String setName = n.getFirstAttributeValue("SET");
			if(setName.equals(name)){
				int rank = Integer.parseInt(n.getFirstAttributeValue("RANK"));
				if(MSTD<rank) MSTD = rank;
			}
		}
		System.out.println("MSTD = "+MSTD);
		for(int rank=1;rank<=MSTD;rank++){
		for(Node n: graph.Nodes){
			String setName = n.getFirstAttributeValue("SET");
				int rankNode = Integer.parseInt(n.getFirstAttributeValue("RANK"));
				if(rank==rankNode){
					int numSignificantCorrelations = 0;
					int numReciprocalCorrelations = 0;
					int reciprocalRank = 0;
					int thisNodeRank = 0;
					float maxCorrelation = 0f;
					float secondMaxCorrelation = 0f;
					int reproducibilityType = 0;
					Vector<Edge> edges = new Vector<Edge>();
					for(Edge e: n.incomingEdges) edges.add(e);
					for(Edge e: n.outcomingEdges) edges.add(e);
					for(Edge e: edges){
						float corr = Float.parseFloat(e.getFirstAttributeValue("ABSCORR"));
						boolean reciprocal = Boolean.parseBoolean(e.getFirstAttributeValue("RECIPROCAL"));
						if(corr>=thresholdForSignificantCorrelations){ 
							numSignificantCorrelations++;
							if(reciprocal){ 
								numReciprocalCorrelations++;
								if(e.Node1.Id.equals(n.Id)) { 
									reciprocalRank = Integer.parseInt(e.Node2.getFirstAttributeValue("RANK"));
									thisNodeRank = Integer.parseInt(e.Node1.getFirstAttributeValue("RANK"));
								}
								if(e.Node2.Id.equals(n.Id)) { 
									reciprocalRank = Integer.parseInt(e.Node1.getFirstAttributeValue("RANK"));
									thisNodeRank = Integer.parseInt(e.Node2.getFirstAttributeValue("RANK"));
								}						
							}
							if(corr>maxCorrelation){
								secondMaxCorrelation = maxCorrelation;								
								maxCorrelation=corr;
							}
						}
					}
					
					if(setName.equals(name)){
					// Robustly reproducible
					if(numReciprocalCorrelations==1)if(numSignificantCorrelations==1)if(reciprocalRank<=MSTD) reproducibilityType=1;
					// Reproducible but lost stability
					if(numReciprocalCorrelations==1)if(numSignificantCorrelations==1)if(reciprocalRank>MSTD) reproducibilityType=2;
					// Split
					if(numReciprocalCorrelations==1)if(numSignificantCorrelations>1){ 
						float relativeDiff = (maxCorrelation-secondMaxCorrelation)/maxCorrelation;
						if(relativeDiff<thresholdForDetectingSimilarCorrelations)
							reproducibilityType=3;
						else{
							if(reciprocalRank<=MSTD)
								reproducibilityType=1;
							else
								reproducibilityType=2;
						}
					}
					// Lost reciprocality
					if(numReciprocalCorrelations==0)if(numSignificantCorrelations>0) reproducibilityType=4;
					// Lost correlation
					if(numReciprocalCorrelations==0)if(numSignificantCorrelations==0) reproducibilityType=5;
					
					}else{ // Higher-order component
					
						if(numReciprocalCorrelations==0)if(numSignificantCorrelations==0)if(thisNodeRank<=MSTD){
							reproducibilityType=6;
						}
						
					}
					
					String description = "not identified";
					switch(reproducibilityType){
						case 1: description = "robustly reproducible"; break;
						case 2: description = "lost stability"; break;
						case 3: description = "split"; break;
						case 4: description = "lost reciprocity"; break;
						case 5: description = "lost correlation"; break;
						case 6: description = "emerged in high-order"; break;
					}
					if(setName.equals(name)||(reproducibilityType==6)){		
						System.out.println(n.Id+"\t"+description);
					}
					counts[reproducibilityType]++;
				}
			
		}
		}
		
		System.out.println();
		System.out.println("Robustly reproducle\t"+counts[1]);
		System.out.println("Lost stability\t"+counts[2]);
		System.out.println("Split\t"+counts[3]);
		System.out.println("Lost reciprocity\t"+counts[4]);
		System.out.println("Lost correlation\t"+counts[5]);
		System.out.println("Emerged in high-order dimension\t"+counts[6]);
		
		return counts;
	}

	private static void createOutlierLists(String folder){
		File files[] = new File(folder).listFiles();
		Vector<Vector<String>> outls = new Vector<Vector<String>>(); 
		for(int i=2;i<=100;i++){
			String s = ""+i;
			if(s.length()==1) s = "0"+i;
			for(File f: files)if(f.getName().endsWith("_"+s+"_S.xls")){
				Vector<String> outliers = outlierAnalysis(f.getAbsolutePath(),false);
				Collections.sort(outliers);
				outls.add(outliers);
			}
		}
		int k = 0;
		for(int i=2;i<=100;i++){
			String s = ""+i;
			if(s.length()==1) s = "0"+i;
			for(File f: files)if(f.getName().endsWith(""+s+"_S.xls")){
				System.out.print(f.getName()+"\t");
				for(int j=0;j<outls.get(k).size();j++)
					System.out.print(outls.get(k).get(j)+"\t");
				System.out.println();
				k++;
			}
		}
	}
	

	private static Vector<String> outlierAnalysis(String sFile, boolean verbose) {
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(sFile, true, "\t");
		System.out.println("Outlier analysis: "+sFile);
		Vector<String> outliersAll = new Vector<String>();
		
		for(int fieldNumber=1;fieldNumber<vt.colCount;fieldNumber++){
			float vals[] = new float[vt.rowCount];
			for(int k=0;k<vt.rowCount;k++){
				String val = vt.stringTable[k][fieldNumber].toLowerCase();
				if(val.equals("n/a")) 
					vals[k] = Float.NaN;
				else
					vals[k] = Float.parseFloat(val);
			}
			vals = Utils.makeLongestTailPositive(vals,3f);
			Vector<Integer> outliers = Utils.findDistributionOutliersByGapAnalysis(vals, 3f, 1.6f);
			if(verbose)
				System.out.print(vt.fieldNames[fieldNumber]+"\t");
			for(int i=0;i<outliers.size();i++){
				if(verbose)
					System.out.print(vt.stringTable[outliers.get(i)][0]+"("+vt.stringTable[outliers.get(i)][fieldNumber]+")\t");
				if(!outliersAll.contains(vt.stringTable[outliers.get(i)][0]))
					outliersAll.add(vt.stringTable[outliers.get(i)][0]);
			}
			if(verbose)
				System.out.println();
		}
		return outliersAll;
	}


	private static void analyzeMetageneList(String folder, String metageneFolder, String folderWithPrecomputedResults) throws Exception{
		// TODO Auto-generated method stub
		File files[] = new File(metageneFolder).listFiles();
		Vector<String> listOfResults = new Vector<String>();
		for(File f:files)if(f.getName().endsWith(".rnk")){
			System.out.println(f.getName());
			System.out.println("=======================");
			String fn = analyzeMetagene(folder,f.getAbsolutePath(),folderWithPrecomputedResults);
			listOfResults.add(fn);
		}
		assembleResultsInFolder(folder);
	}
	
	private static void assembleResultsInFolder(String folder){
		
	}

	public static int reccomendOptimalNumberOfComponentsFromStability(String folderWithPrecomputedResults, String prefix, double minimumTolerableStability, StringBuffer report) throws Exception{
		int optimalNumberOfComponents = -1;
		Vector<Integer> nums = new Vector<Integer>();
		Vector<Float> stabilities = new Vector<Float>();
		readStabilityProfile(folderWithPrecomputedResults,prefix,nums,stabilities);
		System.out.println();
		
		if(nums.size()==0){
			System.out.println("ERROR: no pre-computed ICA decompositions have been found.");
			report.append("ERROR: no pre-computed ICA decompositions have been found.");
		}
		
		Vector<Integer> localMaxima = new Vector<Integer>();
		if(nums.size()>1){
			for(int i=0;i<nums.size();i++){
				if(i==0)if(stabilities.get(i)>stabilities.get(1)) localMaxima.add(nums.get(i));
				if(i==nums.size()-1)if(stabilities.get(i-1)<stabilities.get(i)) localMaxima.add(nums.get(i));
				if(i>0)if(i<nums.size()-2){
					if(stabilities.get(i-1)<stabilities.get(i))
						if(stabilities.get(i+1)<stabilities.get(i))
							localMaxima.add(nums.get(i));
				}
			}
		}else{
			localMaxima.add(nums.get(0));
		}
		
		
		System.out.println("NUMBER_OF_COMPONENTS\tAVERAGE_STABILITY");
		report.append("NUMBER_OF_COMPONENTS\tAVERAGE_STABILITY\n");
		DecimalFormat df = new DecimalFormat("#.###");
		DecimalFormatSymbols custom=new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(custom);				  
		for(int i=0;i<nums.size();i++){
			System.out.print(nums.get(i)+"\t"+df.format(stabilities.get(i))+"\t");
			report.append(nums.get(i)+"\t"+df.format(stabilities.get(i))+"\t");
			int n = (int)((stabilities.get(i)-0.5f)/0.05f);
		for(int k=0;k<n;k++){ System.out.print("*"); report.append("*"); }
		if(localMaxima.contains(nums.get(i))) { System.out.print("\t<--"); report.append("\t<--"); }
			System.out.println();
			report.append("\n");
		}
		System.out.println();
		report.append("\n");
		
		
		float stabilityLevels[] = {0.9f,0.8f,0.7f,0.6f,(float)minimumTolerableStability};
		Vector<Vector<Integer>> choicesOptimal = new Vector<Vector<Integer>>();
		
		for(int k=0;k<stabilityLevels.length;k++){
			if(k==stabilityLevels.length-1){
				System.out.print("For the stability level: "+stabilityLevels[k]+"(minimum tolerable): optimal choices are  ");
				report.append("For the stability level: "+stabilityLevels[k]+"(minimum tolerable): optimal choices are  ");
			}else{
				System.out.print("For the stability level: "+stabilityLevels[k]+": optimal choices are  ");
				report.append("For the stability level: "+stabilityLevels[k]+": optimal choices are  ");
				
			}
			Vector<Integer> found = new Vector<Integer>();
			for(int i=0;i<localMaxima.size();i++){
				if(stabilities.get(nums.indexOf(localMaxima.get(i)))>stabilityLevels[k])
					found.add(localMaxima.get(i));
			}
			if(found.size()==0) { System.out.print("none."); report.append("none."); }
			else{
				for(int j=0;j<found.size();j++)
					if(j<found.size()-1){
						System.out.print(found.get(j)+", ");
						report.append(found.get(j)+", ");
					}else{
						System.out.print(found.get(j)+"(advised)");
						report.append(found.get(j)+"(advised)");
					}
			}
			System.out.println();
			report.append("\n");
			choicesOptimal.add(found);
			if(k==stabilityLevels.length-1){
				if(found.size()>0)
					optimalNumberOfComponents = found.get(found.size()-1);
				else{
					optimalNumberOfComponents = 0;
					float maxstab = 0;
					for(int i=0;i<nums.size();i++){
						if(stabilities.get(i)>=stabilityLevels[k])
							if(stabilities.get(i)>maxstab){
								maxstab = stabilities.get(i);
								optimalNumberOfComponents = nums.get(i);
							}
					}
				}
			}
		}
		
		System.out.println("Final choice: "+optimalNumberOfComponents+" components");
		report.append("Final choice: "+optimalNumberOfComponents+" components");
		
		return optimalNumberOfComponents;
	}
	
	public static void readStabilityProfile(String folderWithPrecomputedResults, String prefix, Vector<Integer> nums, Vector<Float> stabilities)  throws Exception{
		File files[] = (new File(folderWithPrecomputedResults)).listFiles();
		for(File f: files){
			String fn = f.getName();
			if(fn.startsWith(prefix+"_numerical"))if(fn.endsWith("_stability.txt")){
				int k = Integer.parseInt(fn.substring(prefix.length()+15, fn.indexOf("_stability.txt")));
				nums.add(k);
				Vector<String> stabs = Utils.loadStringListFromFile(f.getAbsolutePath());
				float avstab = 0f;
				int count = 0;
				for(String s: stabs)if(!s.trim().equals("")){
					avstab+=Float.parseFloat(s);
					count++;
				}
				avstab/=(float)count;
				stabilities.add(avstab);
			}
		}
		float numsa[] = new float[nums.size()]; 
		float staba[] = new float[stabilities.size()];
		for(int i=0;i<nums.size();i++){ numsa[i]=(float)nums.get(i); staba[i] = stabilities.get(i); }
		int inds[] = Algorithms.SortMass(numsa);
	    nums.clear(); stabilities.clear();
	    for(int i=0;i<inds.length;i++){ nums.add((int)(numsa[inds[i]])); stabilities.add(staba[inds[i]]); }
	}
	
	public static void compileDataSetForTesting() throws Exception{
		//String field = "UnderShoot";
		String field = "numberOfComponents6";
		//String field = "OverShoot1";
		String folderLists = "C:/Datas/ICA_numberOfComponents/lists_genesamples/";
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/ICA_numberOfComponents/nums.txt", true, "\t");
		vt.makePrimaryHash("CANCERTYPE");
		String folderICAResults = "C:/Datas/PanCancerTCGA/ICA_complete_output/PanCan_ICA/";
		File dir = new File("C:/Datas/ICA_numberOfComponents/"+field);
		dir.mkdir();
		for(int i=0;i<prefixes.length;i++){
			
			reformatICADecompositionFilesInFolder(folderICAResults+prefixes[i]);
			
			int k = vt.tableHashPrimary.get(prefixes[i]).get(0);
			int numComp = Integer.parseInt(vt.stringTable[k][vt.fieldNumByName(field)]);
			String Sfile = folderICAResults+prefixes[i]+"/S_"+prefixes[i]+"_expression_matrix_numerical.txt_"+numComp+".num";
			String Afile = folderICAResults+prefixes[i]+"/A_"+prefixes[i]+"_expression_matrix_numerical.txt_"+numComp+".num";
			VDataTable vtS = VDatReadWrite.LoadFromSimpleDatFile(Sfile, false, "\t");
			
			VDataTable vtA = VDatReadWrite.LoadFromSimpleDatFile(Afile, false, "\t");
			FileWriter fS = new FileWriter(dir.getAbsolutePath()+File.separator+prefixes[i]+"_S.xls");
			FileWriter fA = new FileWriter(dir.getAbsolutePath()+File.separator+prefixes[i]+"_A.xls");
			Vector<String> genes = Utils.loadStringListFromFile(folderLists+prefixes[i]+"_expression_matrix_genes.txt");
			Vector<String> samples = Utils.loadStringListFromFile(folderLists+prefixes[i]+"_expression_matrix_samples.txt");
			fS.write("GENE\t"); for(int j=0;j<vtS.colCount;j++) fS.write("IC"+(j+1)+"\t"); fS.write("\n");
			for(int j=0;j<vtS.rowCount;j++){
				String gene = genes.get(j);
				gene = Utils.replaceString(gene, "\"", "");
				fS.write(gene+"\t"); 
				for(int s=0;s<vtS.colCount;s++) 
				{ float f = Float.parseFloat(vtS.stringTable[j][s]); fS.write(f+"\t"); } 
				fS.write("\n");
			}
			fS.close();
			
			fA.write("SAMPLE\t"); for(int j=0;j<vtA.colCount;j++) fA.write("IC"+(j+1)+"\t"); fA.write("\n");
			for(int j=0;j<vtA.rowCount;j++){
				String sample = samples.get(j);
				sample = Utils.replaceString(sample, "\"", "");
				fA.write(sample+"\t"); 
				for(int s=0;s<vtA.colCount;s++) 
				{ float f = Float.parseFloat(vtA.stringTable[j][s]); fA.write(f+"\t"); } 
				fA.write("\n");
			}
			fA.close();
		}
	}
	
	public static String analyzeMetagene(String folder, String metageneFile, String folderWithPrecomputedResults) throws Exception{
		File files[] = (new File(folder)).listFiles();
		File f = new File(metageneFile);
		String mfn = f.getName();
		mfn = mfn.substring(0,mfn.length()-4);
		FileWriter fwCorrelations = new FileWriter(folder+mfn+"_corrs.txt");
		
		fwCorrelations.write("name\tN\tmaxCorrel\tk\tstability\tcontrast\tgap\n"); 
		
		for(int i=0;i<files.length;i++){
			String fn = files[i].getName();
			if(fn.endsWith("_S.xls")){
				VDataTable vtS = VDatReadWrite.LoadFromSimpleDatFile(folder+fn, true, "\t");
				VDataTable vtM = VDatReadWrite.LoadFromSimpleDatFile(metageneFile, false, "\t");
				
				System.out.print(fn+": ");
				Vector<Float> correlationProfile = new Vector<Float>();
				//int k = chooseComponentMaximallyCorrelatedWithMetagene(vtS,vtM,correlationProfile,folder+mfn+"_"+fn.substring(0, fn.length()-6),3f);
				
				int k = chooseComponentMaximallyCorrelatedWithMetagene(vtS,vtM,correlationProfile,null,3f);
				
				fwCorrelations.write(fn+"\t");
				float maxCorrel = correlationProfile.get(k);
				float contrast = 0;
				float gap = 0;
				
				float secondMax = 0;
				float averageBesidesMax = 0;
				
				for(int l=0;l<correlationProfile.size();l++)if(l!=k){
					float corr = correlationProfile.get(l);
					if(corr>secondMax) secondMax = corr;
					averageBesidesMax+=corr;
				}
				averageBesidesMax/=(float)(correlationProfile.size()-1);
				contrast = maxCorrel/averageBesidesMax;
				gap = maxCorrel/secondMax;
				
				float stability = 0f;
				int ncomp = vtS.colCount-1;
				String prefix = fn.substring(0, fn.length()-9);
				if(prefix.endsWith("_"))
					prefix = prefix.substring(0, prefix.length()-1);
				
				String stabFn = folderWithPrecomputedResults+prefix+File.separator+prefix+"_expression_matrix_numerical.txt_"+ncomp+"_stability.txt";
				//System.out.println(stabFn);
				if(!new File(stabFn).exists()){
					stabFn = folderWithPrecomputedResults+prefix+File.separator+prefix+"_"+ncomp+"_stability.txt";
				}
				if(!new File(stabFn).exists()){
					stabFn = folderWithPrecomputedResults+prefix+File.separator+prefix+"_ica_numerical.txt_"+ncomp+"_stability.txt";
				}
				Vector<String> stabilities = Utils.loadStringListFromFile(stabFn);
				stability = Float.parseFloat(stabilities.get(k));
				
				//for(int k=0;k<correlations.length;k++) fwCorrelations.write(correlations[k]+"\t"); fwCorrelations.write("\n");
				fwCorrelations.write(""+ncomp+"\t"+maxCorrel+"\t"+k+"\t"+stability+"\t"+contrast+"\t"+gap+"\t"); 
				
				for(int l=0;l<correlationProfile.size();l++)
					fwCorrelations.write(""+correlationProfile.get(l)+"\t");
					
				fwCorrelations.write("\n");
				fwCorrelations.flush();
			}			
		}
		fwCorrelations.close();
		return folder+mfn+"_corrs.txt";
	}
	
	public static void analyzeGeneSet(String folder, String geneSetFile, String geneSetName, float threshold) throws Exception{
		File files[] = (new File(folder)).listFiles();
		File f = new File(geneSetFile);
		
		Vector<String> geneList = new Vector<String>();
		Vector<Metagene> mgs = logic.Utils.readGMTFile(geneSetFile,1,10000);
		for(int i=0;i<mgs.size();i++) if(mgs.get(i).name.equals(geneSetName)){
			mgs.get(i).updateGeneNames();
			geneList = mgs.get(i).geneNames;
		}
		
		String mfn = f.getName();
		mfn = mfn.substring(0,mfn.length()-4);
		FileWriter fwIntersections = new FileWriter(folder+mfn+"_intersections.txt");
		for(int i=0;i<files.length;i++){
			String fn = files[i].getName();
			if(fn.endsWith("_S.xls")){
				VDataTable vtS = VDatReadWrite.LoadFromSimpleDatFile(folder+fn, true, "\t");
				TableUtils.findAllNumericalColumns(vtS);
				VDataSet dS = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vtS, -1);
				//VDataTable vtM = VDatReadWrite.LoadFromSimpleDatFile(metageneFile, false, "\t");
				Vector<Vector<String>> refSets = new Vector<Vector<String>>();
				
				for(int l=0;l<dS.coordCount;l++){
					
					float sumPositive = 0f;
					float sumNegative = 0f;
					int sign = +1;
					for(int m=0;m<dS.pointCount;m++){
						float val = dS.massif[m][l];
						if(val>threshold) sumPositive+=val;
						if(val<-threshold) sumNegative+=val;
					}
					if(Math.abs(sumNegative)>Math.abs(sumPositive)) sign=-1;
					Vector<String> topContr = new Vector<String>();
					for(int m=0;m<dS.pointCount;m++){
						float val = dS.massif[m][l];
						val = val*sign;
						if(val>threshold)
							topContr.add(vtS.stringTable[m][0]);
					}
					refSets.add(topContr);
				}
				
				System.out.println(fn+"");
				Vector<Float> intersectionProfile = new Vector<Float>();
				int k = chooseComponentMaximallyIntersectedWithGeneSet(refSets,geneList,intersectionProfile,folder+mfn+"_"+fn.substring(0, fn.length()-6));
				
				fwIntersections.write(fn+"\t");
				float maxIntersection = intersectionProfile.get(k);
				float contrast = 0;
				float gap = 0;
				
				float secondMax = 0;
				float averageBesidesMax = 0;
				
				for(int l=0;l<intersectionProfile.size();l++)if(l!=k){
					float inters = intersectionProfile.get(l);
					if(inters>secondMax) secondMax = inters;
					averageBesidesMax+=inters;
				}
				averageBesidesMax/=(float)(intersectionProfile.size()-1);
				contrast = maxIntersection/(averageBesidesMax+0.01f);
				gap = maxIntersection/(secondMax+0.01f);
				
				//for(int k=0;k<correlations.length;k++) fwCorrelations.write(correlations[k]+"\t"); fwCorrelations.write("\n");
				fwIntersections.write(maxIntersection+"\t"+k+"\t"+contrast+"\t"+gap+"\t"); 
				
				for(int l=0;l<intersectionProfile.size();l++)
					fwIntersections.write(""+intersectionProfile.get(l)+"\t");
					
				fwIntersections.write("\n");
				fwIntersections.flush();
			}			
		}
		fwIntersections.close();
	}
	
	
	public static int chooseComponentMaximallyCorrelatedWithMetagene(VDataTable STable, VDataTable metagene, Vector<Float> CorrelationProfile, String fileToWriteValues, float threshold) throws Exception{
		int mostCorrelated = -1;
		VDataTable merged = VSimpleProcedures.MergeTables(STable, STable.fieldNames[0], metagene, metagene.fieldNames[0], "@");
		TableUtils.findAllNumericalColumns(merged);
		
		CorrelationProfile.clear();

		VDataSet mds = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(merged, -1);
		float correlations[] = new float[mds.coordCount-1];
		float maxCorrel = -1f;
		int imax  = -1;
		for(int j=0;j<mds.coordCount-1;j++){
			float x[] = new float[mds.pointCount];
			float y[] = new float[mds.pointCount];
			float sumPositive = 0f;
			float sumNegative = 0f;
			for(int k=0;k<mds.pointCount;k++){
				x[k] = mds.massif[k][j];
				y[k] = mds.massif[k][mds.coordCount-1];
				if(x[k]>threshold) sumPositive+=Math.abs(x[k]);
				if(x[k]<-threshold) sumNegative+=Math.abs(x[k]);
			}
			int sign = +1;
			if(sumNegative>sumPositive) sign = -1;
			for(int k=0;k<mds.pointCount;k++)if(!Float.isNaN(y[k])){
				if(y[k]<0) x[k] = Float.NaN;
				else
				if(x[k]*sign<0) x[k] = Float.NaN;
			}
			
			correlations[j] = Math.abs(VSimpleFunctions.calcCorrelationCoeffMissingValues(x, y));
			if(correlations[j]>maxCorrel){
				imax = j;
				maxCorrel = correlations[j];
			}
		}
		
		for(int i=0;i<correlations.length;i++)
			CorrelationProfile.add(correlations[i]);
		
		mostCorrelated = imax;
		
		if(fileToWriteValues!=null){
			FileWriter fw = new FileWriter(fileToWriteValues);
			File f = new File(fileToWriteValues);
			String s = f.getName().substring(0, f.getName().length()-4);
			fw.write("GENE\tMETAGENE\t"+s+"\n");
			for(int i=0;i<merged.rowCount;i++){
				float x = mds.massif[i][mostCorrelated];
				float y = mds.massif[i][mds.coordCount-1];
				if(!Float.isNaN(y))
					fw.write(merged.stringTable[i][0]+"\t"+y+"\t"+x+"\n");
			}
			fw.close();
		}
		
		return mostCorrelated;
	}
	
	public static int chooseComponentMaximallyIntersectedWithGeneSet(Vector<Vector<String>> refLists, Vector<String> geneList, Vector<Float> intersectionProfile, String fileToWriteValues) throws Exception{
		int mostIntersected = -1;

		intersectionProfile.clear();

		float intersections[] = new float[refLists.size()];
		float maxInters = -1f;
		int imax  = -1;
		
		HashSet<String> geneSet = new HashSet<String>();
		for(String s: geneList) geneSet.add(s);
		
		for(int j=0;j<refLists.size();j++){
			Vector<String> refList = refLists.get(j);
			
			HashSet<String> refSet = new HashSet<String>();
			for(String s: refList) refSet.add(s);
 			
			Set<String> intersectionSet = Utils.IntersectionOfSets(refSet, geneSet);
			Set<String> unionSet = Utils.UnionOfSets(refSet, geneSet);
			
			intersections[j] = (float)intersectionSet.size()/(float)unionSet.size();
			
			if(intersections[j]>maxInters){
				imax = j;
				maxInters = intersections[j];
			}
		}
		
		for(int i=0;i<intersections.length;i++)
			intersectionProfile.add(intersections[i]);
		
		mostIntersected = imax;
		
		if(fileToWriteValues!=null){
			FileWriter fw = new FileWriter(fileToWriteValues);
			fw.close();
		}
		
		return mostIntersected;
	}	
	public static void reformatICADecompositionFilesInFolder(String folder) throws Exception{
		File dir = new File(folder);
		File fs[] = dir.listFiles();
		//String folderLists = "C:/Datas/ICA_numberOfComponents/lists_genesamples/";
		String folderLists = folder;
		for(File f:fs){
			String fn = f.getName();
			//System.out.println(fn);
			if(fn.contains("FRENCH"))
				System.out.println();
			if(fn.startsWith("S_"))if(fn.endsWith(".num")){
				String substring = "_expression_matrix_numerical.txt_";
				int k = fn.indexOf(substring);
				if(k<0){
					substring = "_expression_matrix_log_ica_numerical.txt_";
					k = fn.indexOf(substring);
				}
				if(k<0){
					substring = "_expression_matrix.txt_";
					k = fn.indexOf(substring);
				}
				if(k<0){
					substring = "_expression_matrix_log.txt_";
					k = fn.indexOf(substring);
				}
				if(k<0){
					substring = "_expression.tsv.txt_";
					k = fn.indexOf(substring);
				}
				if(k<0){
					substring = "_rma_expression.tsv.txt_";
					k = fn.indexOf(substring);
				}
				if(k<0){
					substring = "_ica_numerical.txt_";
					k = fn.indexOf(substring);
				}
				if(k<0){
					substring = "_";
					k = fn.lastIndexOf("_");
				}
				String name = fn.substring(2, k);
				if(name.equals("BRCATCGA")) name = "BRCA";
				int compnum = Integer.parseInt(fn.substring(k+substring.length(), fn.length()-4));
				//System.out.println(fn+"\t"+name+"\t"+compnum);
				//FileUtils.copy(f, new File(folder+name+"_"+compnum+"_S.xls"));
				VDataTable vtS = VDatReadWrite.LoadFromSimpleDatFile(f.getAbsolutePath(), false, "\t");
				String compnums = ""+compnum;
				if(compnums.length()==1) compnums = "0"+compnum;
				FileWriter fS = new FileWriter(folder+File.separator+name+"_"+compnums+"_S.xls");
				
				Vector<String> genes = null;
				
				System.out.println(name);
				
				if(new File(folderLists+name+"_expression_matrix_genes.txt").exists())
					genes = Utils.loadStringListFromFile(folderLists+name+"_expression_matrix_genes.txt");
				if(new File(folderLists+name+"_genes.txt").exists())
					genes = Utils.loadStringListFromFile(folderLists+name+"_genes.txt");

				
				fS.write("GENE\t"); for(int j=0;j<vtS.colCount;j++) fS.write("IC"+(j+1)+"\t"); fS.write("\n");
				for(int j=0;j<vtS.rowCount;j++){
					String gene = genes.get(j);
					gene = Utils.replaceString(gene, "\"", "");
					fS.write(gene+"\t"); 
					for(int s=0;s<vtS.colCount;s++) 
					{ float number = Float.parseFloat(vtS.stringTable[j][s]); fS.write(number+"\t"); } 
					fS.write("\n");
				}
				fS.close();

			}
		}
	}

	private static void MakeGraphOfReproducibilityScoreFromCorrelationGraph(String fn, String MSTDValuesTable, String MSTDField) throws Exception{
		Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(fn));
		VDataTable mstds = VDatReadWrite.LoadFromSimpleDatFile(MSTDValuesTable, true, "\t");
		mstds.makePrimaryHash(mstds.fieldNames[0]);

		gr.calcNodesInOut();
		Vector<String> listOfSets = new Vector<String>();
		for(Node n: gr.Nodes){
			String id = n.Id;
			String parts[] = id.split("_");
			String ds = parts[1];
			if(!listOfSets.contains(ds)) listOfSets.add(ds);
		}
		int NumberOfComponents[] = new int[listOfSets.size()];
		for(Node n: gr.Nodes){
			String id = n.Id;
			String parts[] = id.split("_");
			String ds = parts[1];
			//System.out.print(parts[0]+"\t");
			int number = Integer.parseInt(parts[0].substring(2, parts[0].length()));
			//System.out.println(number);
			int k = listOfSets.indexOf(ds);
			if(NumberOfComponents[k]<number) NumberOfComponents[k]=number;
		}
		int mstdsVals[] = new int[listOfSets.size()];
		int maxMSTD = -1;
		for(int i=0;i<listOfSets.size();i++){
			String set = listOfSets.get(i);
			int k = mstds.tableHashPrimary.get(set).get(0);
			mstdsVals[i] = Integer.parseInt(mstds.stringTable[k][mstds.fieldNumByName(MSTDField)]);
			if(maxMSTD<mstdsVals[i]) maxMSTD = mstdsVals[i]; 
		}
		
		Vector<Vector<Float>> scores = new Vector<Vector<Float>>();
		for(int i=0;i<listOfSets.size();i++){
			Vector<Float> profile = new Vector<Float>();
			for(int j=0;j<NumberOfComponents[i];j++) profile.add(0f);
			scores.add(profile);
		}
		for(Edge e: gr.Edges){
			float corr = Float.parseFloat(e.getFirstAttributeValue("ABSCORR"));
			String id1 = e.Node1.Id;
			String id2 = e.Node2.Id;
			String parts1[] = id1.split("_");
			String parts2[] = id2.split("_");
			String ds1 = parts1[1];
			int number1 = Integer.parseInt(parts1[0].substring(2, parts1[0].length()));
			String ds2 = parts2[1];
			int number2 = Integer.parseInt(parts2[0].substring(2, parts2[0].length()));
			int k1 = listOfSets.indexOf(ds1);
			int k2 = listOfSets.indexOf(ds2);
			//System.out.println(number1+"\t"+number2);
			float score1 = scores.get(k1).get(number1-1);
			float score2 = scores.get(k2).get(number2-1);
			scores.get(k1).set(number1-1,score1+corr);
			scores.get(k2).set(number2-1,score2+corr);
			//scores.get(k1).set(number1-1,score1+1);
			//scores.get(k2).set(number2-1,score2+1);
		}
		gr.calcNodesInOut();
		for(Node n: gr.Nodes){
			Vector<Node> neighbours = new Vector<Node>();
			neighbours.add(n);
			for(Edge e: n.incomingEdges) neighbours.add(e.Node1);
			for(Edge e: n.outcomingEdges) neighbours.add(e.Node2);
			float averageRank = 0f;
			float averageRelativeRank = 0f;
			for(Node nn: neighbours){
				String id = nn.Id;
				String parts[] = id.split("_");
				String ds = parts[1];
				int number = Integer.parseInt(parts[0].substring(2, parts[0].length()));
				int kk = listOfSets.indexOf(ds);
				averageRank += number;
				averageRelativeRank += number-mstdsVals[kk];
			}
			averageRank/=(float)neighbours.size();
			averageRelativeRank/=(float)neighbours.size();
			n.setAttributeValueUnique("AVERAGE_RANK", ""+averageRank, Attribute.ATTRIBUTE_TYPE_REAL);
			n.setAttributeValueUnique("AVERAGE_RELATIVERANK", ""+averageRelativeRank, Attribute.ATTRIBUTE_TYPE_REAL);

			String id = n.Id;
			String parts[] = id.split("_");
			String ds = parts[1];
			int kk = listOfSets.indexOf(ds);
			int number = Integer.parseInt(parts[0].substring(2, parts[0].length()));
			n.setAttributeValueUnique("RANK", ""+number, Attribute.ATTRIBUTE_TYPE_REAL);
			n.setAttributeValueUnique("RELATIVERANK", ""+(number-mstdsVals[kk]), Attribute.ATTRIBUTE_TYPE_REAL);
			
		}
		for(int i=0;i<listOfSets.size();i++){
			System.out.print(listOfSets.get(i)+"\t");
			for(int j=0;j<scores.get(i).size();j++) System.out.print(scores.get(i).get(j)+"\t");
			System.out.println();
		}
		
		int maxnum = 0; for(int i=0;i<NumberOfComponents.length;i++) if(NumberOfComponents[i]>maxnum) maxnum = NumberOfComponents[i]; 
		
		/*System.out.print("AVERAGE\t");
		for(int j=0;j<maxnum;j++){
			Vector<Float> scs = new Vector<Float>();
			for(int i=0;i<listOfSets.size();i++){
				if(scores.get(i).size()>=j-1)
					scs.add(scores.get(i).get(j));
			}
			float average = 0f; for(int k=0;k<scs.size();k++) average+=scs.get(k);
			average/=scs.size();
			System.out.print(average+"\t");
		}
		System.out.println();
		*/
		
		System.out.println();
		
		System.out.print("RELATIVE_COMPONENT_RANK\t");
		for(int j=-maxMSTD-1;j<=maxnum;j++) System.out.print(j+"\t");
		System.out.println();
		
		for(int i=0;i<listOfSets.size();i++){
			System.out.print(listOfSets.get(i)+"\t");
			for(int j=-maxMSTD-1;j<=maxnum;j++){
				if((j+mstdsVals[i]-1>=0)&&(j+mstdsVals[i]-1<scores.get(i).size())){
					System.out.print(scores.get(i).get(j+mstdsVals[i]-1)+"\t");
				}else{
					System.out.print("\t");
				}
			}
			System.out.println();
		}
		
		XGMML.saveToXGMML(gr, fn.substring(0, fn.length()-6)+"_avrank.xgmml");
		
		/*System.out.print("AVERAGE_CENTERED_MSTD\t");
		for(int j=-maxnum;j<=maxnum;j++){
			Vector<Float> scs = new Vector<Float>();
			
			float average = 0f; for(int k=0;k<scs.size();k++) average+=scs.get(k);
			average/=scs.size();
			System.out.print(average+"\t");
		}
		System.out.println();
		*/
		
	}
	
	  public static void findComponentsInHigherOrderDecompositions(String folderLow, String folderHigh) throws Exception{
		  File listLow[] = (new File(folderLow)).listFiles();
		  File listHigh[] = (new File(folderHigh)).listFiles();
		  for(File flow: listLow)if(flow.getName().endsWith("_S.xls")){
			  String fname = flow.getName();
			  String name = fname.substring(0, fname.length()-6);
			  for(File fhigh: listHigh)if(fhigh.getName().endsWith("_S.xls"))if(fhigh.getName().startsWith(name)){
				  String nameHigh = fhigh.getName().substring(0,fhigh.getName().length()-6);
				  System.out.println(name+"\tvs\t"+nameHigh);
				  VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(flow.getAbsolutePath(), true, "\t");
				  VDataTable vt2 = VDatReadWrite.LoadFromSimpleDatFile(fhigh.getAbsolutePath(), true, "\t");
				  TableUtils.findAllNumericalColumns(vt1);
				  TableUtils.findAllNumericalColumns(vt2);
				  nameHigh = name+"_"+(vt2.colCount-1);
				  Graph graph1 = MakeCorrelationGraph.makeTableCorrelationGraph(vt1, name, vt2, nameHigh, 0.3f, true, false, false, 3f);
				  Graph graph2 = MakeCorrelationGraph.makeTableCorrelationGraph(vt2, nameHigh, vt1, name, 0.3f, true, false, false, 3f);
				  Graph gr = new Graph();
				  gr.addNodes(graph1);
				  gr.addEdges(graph1);
				  gr.addNodes(graph2);
				  gr.addEdges(graph2);
				  for(Edge e: graph2.Edges){
					  Node n1 = gr.getNode(e.Node1.Id);
					  e.Node1 = n1;
					  Node n2 = gr.getNode(e.Node2.Id);
					  e.Node2 = n2;
				  }
				  Graph gr1 = MakeCorrelationGraph.RemoveReciprocalEdgesInCorrelationGraph(gr);
				  gr1.name = nameHigh;
				  XGMML.saveToXGMML(gr1, folderLow+nameHigh+".xgmml");
				  
			  }
		  }
	  }
	  
	  public static void analyzeHighOrderReproducibilityResults(String folder,String MSTDValuesTable, String MSTDField) throws Exception{
		  File files[] = (new File(folder)).listFiles();
		  VDataTable mstds = VDatReadWrite.LoadFromSimpleDatFile(MSTDValuesTable, true, "\t");

		  Vector<String> listOfSets = new Vector<String>();
		  for(File f: files)if(f.getName().endsWith("xgmml")){
			  Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(f.getAbsolutePath()));
			  String parts[] = gr.name.split("_");
			  String cancerType = parts[0];
			  listOfSets.add(cancerType);
		  }
		  
		  mstds.makePrimaryHash(mstds.fieldNames[0]);
			int mstdsVals[] = new int[listOfSets.size()];
			int maxMSTD = -1;
			for(int i=0;i<listOfSets.size();i++){
				String set = listOfSets.get(i);
				int k = mstds.tableHashPrimary.get(set).get(0);
				mstdsVals[i] = Integer.parseInt(mstds.stringTable[k][mstds.fieldNumByName(MSTDField)]);
				if(maxMSTD<mstdsVals[i]) maxMSTD = mstdsVals[i]; 
			}
			
		  System.out.print("ANALYSIS\t"); for(int i=-maxMSTD;i<=100;i++) System.out.print(i+"\t"); System.out.println();
		  for(File f: files)if(f.getName().endsWith("xgmml")){
			  Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(f.getAbsolutePath()));
			  gr.calcNodesInOut();
			  System.out.print(gr.name+"\t");
			  String parts[] = gr.name.split("_");
			  String cancerType = parts[0];
			  int k = listOfSets.indexOf(cancerType);
			  for(int i=-maxMSTD;i<=100;i++){
				  int relativeRank = i;
				  int absoluteRank = i+mstdsVals[k];
				  if(absoluteRank>0){
				  for(Node n: gr.Nodes)if(n.getFirstAttributeValue("SET").equals(cancerType))if(n.getFirstAttributeValue("FIELD").equals("IC"+absoluteRank)){
					  String val = "N/F";
					  for(Edge e: n.incomingEdges){
						  if(e.getFirstAttribute("RECIPROCAL")!=null)
						  if(e.getFirstAttributeValue("RECIPROCAL").equals("TRUE")){
							  val = e.getFirstAttributeValue("ABSCORR");
						  }
					  }
					  for(Edge e: n.outcomingEdges){
						  if(e.getFirstAttribute("RECIPROCAL")!=null)
						  if(e.getFirstAttributeValue("RECIPROCAL").equals("TRUE")){
							  val = e.getFirstAttributeValue("ABSCORR");
						  }
					  }
					  System.out.print(val+"\t");
				  }
				  }else{
					  System.out.print("\t");
				  }
			  }
			  System.out.println();
		  }
	  }

	  public static void AnalyzeGSEABIODICAResults(String folder, String prefix){
		  File files[] = (new File(folder)).listFiles();
		  HashMap<String,Vector<Float>> scores = new HashMap<String,Vector<Float>>();
		  // scores
		  // score1 - top NES
		  // score2 - average top 10 NES
		  // score3 - number of NES >5
		  // score4 - number of NES >3
		  
		  for(File f: files)if(f.isDirectory())if(!f.getName().endsWith("_filtered")){
			  String name = f.getName();
			  File indir = f.listFiles()[0];
			  File infiles[] = indir.listFiles();
			  Vector<Float> NES = new Vector<Float>();
			  String filteredFolder = f.getAbsolutePath()+"_filtered";
			  File filteredFiles[] = (new File(filteredFolder)).listFiles();
			  HashSet<String> filteredSet = new HashSet<String>();
			  for(File ff: filteredFiles) filteredSet.add(ff.getName());
			  for(File fin: infiles){if(fin.getName().endsWith("xls"))if(fin.getName().startsWith("gsea_report")){
				  VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fin.getAbsolutePath(), true, "\t");
				  for(int i=0;i<vt.rowCount;i++){
					  float ns = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("NES")]);
					  String signame = vt.stringTable[i][vt.fieldNumByName("NAME")];
					  if(filteredSet.contains(signame+".html")){
						  	NES.add(Math.abs(ns));
					  }
				  }
			  }
			  }
			  
			  float topNES = 0f;
			  float averageTop10NES = 0f;
			  float numNESgr5 = 0f; float numNESgr3 = 0f;

			  if(NES.size()>0){
			  float nes_mass[] = new float[NES.size()];
			  for(int i=0;i<NES.size();i++) nes_mass[i] = NES.get(i);
			  int inds[] = Algorithms.SortMass(nes_mass);
			  
			  topNES = nes_mass[inds[inds.length-1]];
			  for(int i=0;i<Math.min(5,NES.size());i++) averageTop10NES+=nes_mass[inds[inds.length-1-i]];
			  averageTop10NES/=Math.min(5,NES.size());
			  for(int i=0;i<nes_mass.length;i++){
				  if(nes_mass[i]>5) numNESgr5+=1f;
				  if(nes_mass[i]>3) numNESgr3+=1f;
			  }
			  }
			  Vector<Float> sc = new Vector<Float>();
			  sc.add(topNES);
			  sc.add(averageTop10NES);
			  sc.add(numNESgr5);
			  sc.add(numNESgr3);
			  scores.put(name, sc);
		  }
		  
		  System.out.println("COMPONENT\tTOPNES\tAVTOP10NES\tNES5\tNES3");
		  for(int i=1;i<=100;i++){
			  String key = prefix+"IC"+i;
			  if(scores.containsKey(key)){
				  Vector<Float> sc = scores.get(key);
				  System.out.println(i+"\t"+sc.get(0)+"\t"+sc.get(1)+"\t"+sc.get(2)+"\t"+sc.get(3));
			  }
		  }
		  
	  }
	  
	  public static void makeConvergenceTable(String folder) throws Exception{
		  File files[] = (new File(folder)).listFiles();
		  for(File f:files){
			  if(f.getName().startsWith("Convergence")){
				  String parts[] = f.getName().split("_");
				  int num = Integer.parseInt(parts[parts.length-1].substring(0, parts[parts.length-1].length()-4));
				  VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(f.getAbsolutePath(), false, "\t");
				  float averageConvergence = 0f;
				  int numberOfFails = 0;
				  for(int i=0;i<vt.rowCount;i++){
					  float conv = Float.parseFloat(vt.stringTable[i][0]);
					  if(conv>0) averageConvergence+=conv; else {
						  numberOfFails++;
						  averageConvergence+=100;
					  }
				  }
				  averageConvergence/=(float)(vt.rowCount);
				  System.out.println(num+"\t"+averageConvergence+"\t"+numberOfFails);

			  }
		  }
	  }
	  
}
