package fr.curie.BIODICA;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Vector;

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
			analyzeMetagene("C:/Datas/ICA_numberOfComponents/numberOfComponents8/","C:/Datas/ICA_numberOfComponents/metagenes/M7_CELLCYCLE.rnk");
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static int reccomendOptimalNumberOfComponentsFromStability(String folderWithPrecomputedResults, String prefix, float minimumTolerableStability) throws Exception{
		int optimalNumberOfComponents = -1;
		Vector<Integer> nums = new Vector<Integer>();
		Vector<Float> stabilities = new Vector<Float>();
		readStabilityProfile(folderWithPrecomputedResults,prefix,nums,stabilities);
		System.out.println();
		
		if(nums.size()==0){
			System.out.println("ERROR: no pre-computed ICA decompositions have been found.");
			System.exit(0);
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
		DecimalFormat df = new DecimalFormat("#.###");
		for(int i=0;i<nums.size();i++){
			System.out.print(nums.get(i)+"\t"+df.format(stabilities.get(i))+"\t");
			int n = (int)((stabilities.get(i)-0.5f)/0.05f);
		for(int k=0;k<n;k++) System.out.print("*");
		if(localMaxima.contains(nums.get(i))) System.out.print("\t<--");
			System.out.println();
		}
		System.out.println();
		
		
		float stabilityLevels[] = {0.9f,0.8f,0.7f,0.6f,minimumTolerableStability};
		Vector<Vector<Integer>> choicesOptimal = new Vector<Vector<Integer>>();
		
		for(int k=0;k<stabilityLevels.length;k++){
			if(k==stabilityLevels.length-1)
				System.out.print("For the stability level: "+stabilityLevels[k]+"(minimum tolerable): optimal choices are  ");
			else
				System.out.print("For the stability level: "+stabilityLevels[k]+": optimal choices are  ");
			Vector<Integer> found = new Vector<Integer>();
			for(int i=0;i<localMaxima.size();i++){
				if(stabilities.get(nums.indexOf(localMaxima.get(i)))>stabilityLevels[k])
					found.add(localMaxima.get(i));
			}
			if(found.size()==0) System.out.print("none.");
			else{
				for(int j=0;j<found.size();j++)
					if(j<found.size()-1)
						System.out.print(found.get(j)+", ");
					else
						System.out.print(found.get(j)+"(advised)");
			}
			System.out.println();
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
		String field = "numberOfComponents8";
		//String field = "OverShoot1";
		String folderLists = "C:/Datas/ICA_numberOfComponents/lists_genesamples/";
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/ICA_numberOfComponents/nums.txt", true, "\t");
		vt.makePrimaryHash("CANCERTYPE");
		String folderICAResults = "D:/ICA_complete_output/PanCan_ICA/";
		File dir = new File("C:/Datas/ICA_numberOfComponents/"+field);
		dir.mkdir();
		for(int i=0;i<prefixes.length;i++){
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
	
	public static void analyzeMetagene(String folder, String metageneFile) throws Exception{
		File files[] = (new File(folder)).listFiles();
		File f = new File(metageneFile);
		String mfn = f.getName();
		mfn = mfn.substring(0,mfn.length()-4);
		FileWriter fwCorrelations = new FileWriter(folder+mfn+"_corrs.txt");
		for(int i=0;i<files.length;i++){
			String fn = files[i].getName();
			if(fn.endsWith("_S.xls")){
				VDataTable vtS = VDatReadWrite.LoadFromSimpleDatFile(folder+fn, true, "\t");
				VDataTable vtM = VDatReadWrite.LoadFromSimpleDatFile(metageneFile, false, "\t");
				VDataTable merged = VSimpleProcedures.MergeTables(vtS, "GENE", vtM, "N1", "@");
				TableUtils.findAllNumericalColumns(merged);
				VDataSet mds = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(merged, -1);
				float correlations[] = new float[mds.coordCount-1];
				float maxCorrel = -1f;
				int imax  = -1;
				for(int j=0;j<mds.coordCount-1;j++){
					float x[] = new float[mds.pointCount];
					float y[] = new float[mds.pointCount];
					for(int k=0;k<mds.pointCount;k++){
						x[k] = mds.massif[k][j];
						y[k] = mds.massif[k][mds.coordCount-1];
					}
					correlations[j] = Math.abs(VSimpleFunctions.calcCorrelationCoeffMissingValues(x, y));
					if(correlations[j]>maxCorrel){
						imax = j;
						maxCorrel = correlations[j];
					}
				}
				fwCorrelations.write(fn+"\t");
				//for(int k=0;k<correlations.length;k++) fwCorrelations.write(correlations[k]+"\t"); fwCorrelations.write("\n");
				fwCorrelations.write(""+maxCorrel); fwCorrelations.write("\n");
				fwCorrelations.flush();
			}			
		}
		fwCorrelations.close();
	}
	

}
