package fr.curie.BIODICA;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.sun.org.apache.bcel.internal.generic.FMUL;

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
			//analyzeGeneSet("C:/Datas/ICA_numberOfComponents/BRCA/","C:/Datas/ICA_numberOfComponents/signatures/cell_cycle/FREEMAN_CLUSTERS.gmt","FREEMAN_CELLCYCLE",3f);
			reformatICADecompositionFilesInFolder("C:/Datas/PanCancerTCGA/ICA_complete_output/PanCan_ICA/ACC/");
			
			
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
	
	public static void analyzeMetagene(String folder, String metageneFile, String folderWithPrecomputedResults) throws Exception{
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
				
				String stabFn = folderWithPrecomputedResults+prefix+File.separator+prefix+"_expression_matrix_numerical.txt_"+ncomp+"_stability.txt"; 
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
	}
	
	public static void analyzeGeneSet(String folder, String geneSetFile, String geneSetName, float threshold) throws Exception{
		File files[] = (new File(folder)).listFiles();
		File f = new File(geneSetFile);
		
		Vector<String> geneList = new Vector<String>();
		Vector<Metagene> mgs = fr.curie.BIODICA.Utils.readGMTFile(geneSetFile,1,10000);
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
		String folderLists = "C:/Datas/ICA_numberOfComponents/lists_genesamples/";
		for(File f:fs){
			String fn = f.getName();
			if(fn.startsWith("S_"))if(fn.endsWith(".num")){
				String substring = "_expression_matrix_numerical.txt_";
				int k = fn.indexOf(substring);
				if(k<0){
					substring = "_expression_matrix_log_ica_numerical.txt_";
					k = fn.indexOf(substring);
				}
				String name = fn.substring(2, k);
				int compnum = Integer.parseInt(fn.substring(k+substring.length(), fn.length()-4));
				//System.out.println(fn+"\t"+name+"\t"+compnum);
				//FileUtils.copy(f, new File(folder+name+"_"+compnum+"_S.xls"));
				VDataTable vtS = VDatReadWrite.LoadFromSimpleDatFile(f.getAbsolutePath(), false, "\t");
				String compnums = ""+compnum;
				if(compnums.length()==1) compnums = "0"+compnum;
				FileWriter fS = new FileWriter(folder+File.separator+name+"_"+compnums+"_S.xls");
				Vector<String> genes = Utils.loadStringListFromFile(folderLists+name+"_expression_matrix_genes.txt");

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
	

}
