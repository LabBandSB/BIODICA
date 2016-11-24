package fr.curie.BIODICA;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Vector;

import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;

public class NumberOfComponentsOptimizer {

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
			String folder = "C:/Datas/BIODICA/work/NEUROBLASTOMAlf_ICA/stability/";
			String prefix = "NEUROBLASTOMAlf_ica";
			 			
			reccomendOptimalNumberOfComponentsFromStability(folder,prefix, 0.8f);
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

}
