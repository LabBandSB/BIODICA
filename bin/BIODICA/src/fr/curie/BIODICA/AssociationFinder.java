package fr.curie.BIODICA;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import vdaoengine.data.VDataTable;
import vdaoengine.data.VStatistics;
import vdaoengine.utils.VSimpleFunctions;

public class AssociationFinder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    // strNames - selected fields to test for associations 
    public static void printAssosiationTable(VDataTable vt, String fout, Vector<String> strNames, BIODICAPipeLine biodica, boolean transposed){
      Vector<String> fieldNames = new Vector<String>();
      Vector<String[]> fieldClasses = new Vector<String[]>();
      Vector<String> valNames = new Vector<String>();
      Vector<float[]> vals = new Vector<float[]>();
      
      //int numOfDistinctValues[] = countNumberOfDistinctValuesInColumns(vt);
      Vector<HashMap<String, Integer>> categories = countNumberOfDistinctValuesInColumns(vt);
      boolean takeForTesting[] = new boolean[vt.colCount];
      boolean categorical[] = new boolean[vt.colCount];
      for(int i=1;i<vt.colCount;i++){
    	  takeForTesting[i] = true;
    	  int numOfValidCategories = 0;
    	  categorical[i] = vt.fieldTypes[i]==vt.STRING;
    	  if((categories.get(i).size()<biodica.MinNumberOfDistinctValuesInNumericals))
    		  categorical[i] = true;
    	  if(categorical[i])
    		  System.out.print(vt.fieldNames[i]+"("+(categorical[i]?"CAT":"NUM")+")"+":\tNumOfDistinctValues:"+categories.get(i).size()+"\t");
    	  Set<String> keys = categories.get(i).keySet();
    	  Vector<String> keysV = new Vector<String>();
    	  for(String s: keys) keysV.add(s);
    	  if(categorical[i]){
    	  for(String s: keysV){
    		  if(categories.get(i).get(s)<biodica.MinNumberOfSamplesInCategory){
    			  System.out.print(s+":only "+categories.get(i).get(s)+" samples"+"\t");
    		  }else{
    			  numOfValidCategories++;
    		  }
    	  }
    	  if(numOfValidCategories<=1) { System.out.print("NOTAKE: only one or zero category to test"); takeForTesting[i] = false; }
    	  if(numOfValidCategories>biodica.MaxNumberOfCategories) { System.out.print("NOTAKE: too many categories to test ("+numOfValidCategories+")"); takeForTesting[i] = false; }
    	  }else{
    		  int numberOfNotNAs = 0;
    		  for(String s: keysV) numberOfNotNAs+=categories.get(i).get(s);
    		  
    		  if(numberOfNotNAs<biodica.MinNumberOfDistinctValuesInNumericals){ System.out.print("NOTAKE: two few points to compute correlation ("+numberOfNotNAs+")"); takeForTesting[i] = false; }
    		  else{
    			  System.out.print(vt.fieldNames[i]+"("+"NUM"+")"+":\tNumOfPoints:"+numberOfNotNAs+"\t");
    		  }
    	  }
    	  System.out.println();
      }
      
      Vector<HashMap<String, Integer>> categories1 = new Vector<HashMap<String, Integer>>();      
      // Make list of all found string columns or those numericals which contains small number of labels (categorical)
      for(int i=0;i<vt.colCount;i++){
    	  if(categorical[i])if(takeForTesting[i]){
          fieldNames.add((String)vt.fieldNames[i]);
          String cl[] = new String[vt.rowCount];
          for(int j=0;j<vt.rowCount;j++)
            cl[j] = vt.stringTable[j][i];
          fieldClasses.add(cl);
          categories1.add(categories.get(i));
        }
      }

      // Make list of all found numerical values
      for(int i=0;i<vt.colCount;i++){
        if(!categorical[i])if(takeForTesting[i]){
          valNames.add(vt.fieldNames[i]);
          float f[] = new float[vt.rowCount];
          for(int j=0;j<vt.rowCount;j++){
        	if(vt.stringTable[j][i].equals("_")||vt.stringTable[j][i].equals("@")||vt.stringTable[j][i].equals("")||vt.stringTable[j][i].equals("NA")||vt.stringTable[j][i].equals("\"\""))
        		f[j] = Float.NaN;
        	else
        		f[j] = Float.parseFloat(vt.stringTable[j][i]);
          }
          vals.add(f);
        }
      }

      Vector<String[]> infoV = new Vector<String[]>();
      float f[][] = findAssosiations(strNames, fieldNames, fieldClasses, valNames, vals, infoV, categories1, biodica);
      String info[][] = new String[infoV.size()][infoV.get(0).length];
      for(int i=0;i<infoV.size();i++)
    	  info[i] = infoV.get(i);
      if(!transposed)
        printAssociations(f,strNames, fieldNames,valNames,fout,biodica.AssociationAnalysisThreshold);
      else
        printAssociationsT(f,info,strNames, fieldNames,valNames,fout,biodica.AssociationAnalysisThreshold);

    }
    
    public static void printAssociations(float vals[][], Vector<String> fieldsToTest, Vector fieldNames, Vector valNames, String fout, float thresh){
        try{
          FileWriter fw = new FileWriter(fout);
          fw.write("FIELD");
          for(int i=0;i<fieldNames.size();i++){
            fw.write((String)fieldNames.elementAt(i)+"\t");
          }
          for(int i=0;i<valNames.size();i++)
              fw.write((String)valNames.elementAt(i)+"\t");
          fw.write("\n");
          
          for(int i=0;i<fieldsToTest.size();i++){
          	fw.write(fieldsToTest.get(i)+"\t");
              for(int j=0;j<fieldNames.size();j++){
             	 float f = vals[i][j];
          	 DecimalFormat df = new DecimalFormat("#.##");
          	 String sf = df.format(f); 
             	 if(f>=thresh)
             		 fw.write(sf+"\t");
             	 else
             		 fw.write("_\t");
              }
               for(int j=0;j<valNames.size();j++){
              	 float f = vals[i][j+fieldNames.size()];
              	 DecimalFormat df = new DecimalFormat("#.##");
              	 String sf = df.format(f); 
              	 fw.write(sf+"\t");
               }
          fw.write("\n");
          }
          fw.close();
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    

    public static float[][] findAssosiations(Vector<String> fieldsToTest, Vector<String> fieldNames, Vector<String[]> fieldClasses, Vector<String> valNames, Vector<float[]> vals, Vector<String[]> infos, Vector<HashMap<String,Integer>> categories, BIODICAPipeLine biodica){
    	
        float res[][] = new float[fieldsToTest.size()][fieldNames.size()+valNames.size()];
        
        for(int i=0;i<fieldsToTest.size();i++){
        	String s[] = new String[fieldNames.size()+valNames.size()];
        	infos.add(s);
        }
        
        // For now we assume that all fieldsToTest are numerical
        for(int kk=0;kk<fieldsToTest.size();kk++){

        //System.out.println("Field "+fieldsToTest.get(kk));
        float val[] = (float[])vals.get(valNames.indexOf(fieldsToTest.get(kk)));
        
        // Associations with categorical values
        for(int i=0;i<fieldClasses.size();i++){
          String cl[] = (String[])fieldClasses.elementAt(i);
          Vector<String> lbls = new Vector<String>();
          for(int j=0;j<cl.length;j++){
            String lb = cl[j];
            if((!lb.equals("_"))&&(!lb.equals("NA"))&&(!lb.equals(""))&&(!lb.equals("\"\""))&&(!lb.equals("@"))){
              if(lbls.indexOf(lb)<0){
            	  if(categories.get(i).get(lb)==null){
            		  System.out.println("WARNING: "+lb+" label is not in the list of categories");
            	  }else{
            	  if(categories.get(i).get(lb)>=biodica.MinNumberOfSamplesInCategory) 
            		  lbls.add(lb);
            	  }
              }
            }
          }
          //for(String s:lbls) System.out.print(s+","); System.out.println();
          
          int countt = (int)(0.5f*(lbls.size()-1)*lbls.size());
          //System.out.println(fieldNames.get(i)+"\t"+lbls.size());
          if(lbls.size()<biodica.MaxNumberOfCategories){
          float tvalues[] = new float[countt];
          int k = 0;
          double maxVal = 0f;
          double maxValTTest = 0f;
          String maxLabel1 = "";
          String maxLabel2 = "";
          String compMax1 = "";
          String compMax2 = "";
          for(int k1=0;k1<lbls.size();k1++)
            for(int k2=k1+1;k2<lbls.size();k2++){
               String lb1 = (String)lbls.elementAt(k1);
               String lb2 = (String)lbls.elementAt(k2);
                 Vector set1 = new Vector();
                 Vector set2 = new Vector();
                 for(int jj=0;jj<cl.length;jj++){
                   if(cl[jj].equals(lb1)) if(!Float.isNaN(val[jj])) set1.add(new Float(val[jj]));
                   if(cl[jj].equals(lb2)) if(!Float.isNaN(val[jj])) set2.add(new Float(val[jj]));
                 }
               // Let us apply Wilconxon!!
               double tvalue = calcTTest(set1,set2);
               double ttest = tvalue;
               int tvalue_df = (int)calcTTestDegreesOfFreedom(set1,set2);
               double pvalue = VSimpleFunctions.ttest(tvalue, tvalue_df); 
               tvalue = -Math.log10(pvalue);
               if(tvalue>maxVal){
            	   maxVal = tvalue;
            	   maxLabel1 = lb1;
            	   maxLabel2 = lb2;
            	   maxValTTest = ttest;
               }
               tvalues[k++] = (float)Math.abs(tvalue);
            }
          float tval = (float)max(tvalues);
          res[kk][i] = tval;
          DecimalFormat df = new DecimalFormat("#.#"); 
          infos.get(kk)[i] = maxLabel1+"/"+maxLabel2+"("+df.format(maxValTTest)+")";
          
          // too many comparisons, does not make sense
          }else{
        	  res[kk][i] = Float.NaN; 
          }
        	  
          //descriptions.add(compMax1+"_vs_"+compMax2);
        }
        
        //Associations with numerical values (by Spearmann correlation)
        for(int i=0;i<valNames.size();i++){
        	if(valNames.get(i).equals("GC-CONTENT")&&fieldsToTest.get(kk).equals("IC14"))
        		System.out.println("");
        	float val1[] = (float[])vals.get(i);
        	float corr = VSimpleFunctions.calcSpearmanCorrelationCoeffMissingValues(val, val1);
        	//float corr = VSimpleFunctions.calcCorrelationCoeff(val, val1);
        	int npoints = 0;
        	for(int k=0;k<val.length;k++){
        		if(!Float.isNaN(val[k]))if(!Float.isNaN(val1[k])) npoints++;
        	}
        	DecimalFormat df = new DecimalFormat("#.##"); 
        	//infos.get(kk)[fieldClasses.size()+i] = valNames.get(i)+"/"+fieldsToTest.get(kk)+"("+df.format(corr)+")";        	
        	infos.get(kk)[fieldClasses.size()+i] = df.format(corr);
        	double pvalcorr = VSimpleFunctions.calcCorrelationPValue(corr, npoints);
        	corr = -(float)Math.log10(pvalcorr);
        	if(corr>100) corr=100;
        	res[kk][fieldClasses.size()+i] = corr;
        }
        }
        
        return res;
    }
    
    public static void printAssociationsT(float vals[][], String info[][], Vector<String> fieldsToTest, Vector fieldNames, Vector valNames, String fout, float thresh){
        try{
        	
          String fout_info = fout.substring(0, fout.length()-4)+"_info.xls";
          FileWriter fw = new FileWriter(fout);
          FileWriter fw_info = new FileWriter(fout_info);
          
          fw.write("VAL\t");
          fw_info.write("VAL\t");
          for(int i=0;i<fieldsToTest.size();i++){
            fw.write((String)fieldsToTest.elementAt(i)+"\t");
            fw_info.write((String)fieldsToTest.elementAt(i)+"\t");
          }
          fw.write("\n");
          fw_info.write("\n");
          
          boolean foundAssociation[] = new boolean[fieldNames.size()];
          for(int i=0;i<fieldNames.size();i++){
        	  foundAssociation[i] = false;
            	for(int j=0;j<fieldsToTest.size();j++){
              		float f = vals[j][i];
              		if(f>=thresh) foundAssociation[i] = true;
            	}
          }
          
          for(int i=0;i<fieldNames.size();i++)if(foundAssociation[i]){
          	fw.write(fieldNames.get(i)+"\t");
          	fw_info.write(fieldNames.get(i)+"\t");
          	for(int j=0;j<fieldsToTest.size();j++){
          		float f = vals[j][i];
             	 	DecimalFormat df = new DecimalFormat("#.##");
             	 	String sf = df.format(f); 
                  if(f>=thresh){
                      fw.write(sf+"\t");
                      fw_info.write(info[j][i]+"\t");
                  }
                    else{
                      fw.write("_\t");
                      fw_info.write("_\t");
                  }
          	}
              fw.write("\n");
              fw_info.write("\n");
          }
          
          //fw.write("\n");
          foundAssociation = new boolean[valNames.size()];
          for(int i=0;i<valNames.size();i++){
        	  for(int j=0;j<fieldsToTest.size();j++){
            		float f = vals[j][i+fieldNames.size()];
            		if(f>=thresh)if(!Float.isNaN(f))if(!Float.isInfinite(f)) foundAssociation[i] = true;
        	  }
          }
          
          for(int i=0;i<valNames.size();i++)if(foundAssociation[i]){
          	fw.write((String)valNames.elementAt(i)+"\t");
          	fw_info.write((String)valNames.elementAt(i)+"\t");
          	for(int j=0;j<fieldsToTest.size();j++){
          		float f = vals[j][i+fieldNames.size()];
         	 		DecimalFormat df = new DecimalFormat("#.##");
         	 		String sf = df.format(f);
         	 		boolean significant = false;
                    if(f>=thresh)if(!Float.isNaN(f))if(!Float.isInfinite(f)) significant = true;
                    if(significant){
                        fw.write(sf+"\t");
                        fw_info.write(info[j][i+fieldNames.size()]+"\t");
                    }else{
                        fw.write("_\t");
                        fw_info.write("_\t");
                    }
          	}
          	fw.write("\n");
          	fw_info.write("\n");
          }
               
          fw.close();
          fw_info.close();
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    
	
	
	public static void findAllNumericalColumns(VDataTable vt){
		for(int i=0;i<vt.colCount;i++){
			boolean isNumerical = true;
			for(int j=0;j<vt.rowCount;j++){
				String s = vt.stringTable[j][i].trim();
				if(s.equals("\"\"")||s.equals("NA")||s.equals("")||s.equals("_")){
					s = "@";
					vt.stringTable[j][i] = s;
				}else{
				try{
					Float f = Float.parseFloat(s);
				}catch(Exception e){
					//e.printStackTrace();
					isNumerical = false;
				}}
			}
			if(isNumerical)
				vt.fieldTypes[i] = vt.NUMERICAL;
		}
	}
	
	/*public static int[] countNumberOfDistinctValuesInColumns(VDataTable vt){
		int num[] = new int[vt.colCount];
		for(int i=0;i<vt.colCount;i++){
			HashSet<String> set = new HashSet<String>();
			for(int j=0;j<vt.rowCount;j++){
				String s = vt.stringTable[j][i].trim();
				if((!s.equals("\"\""))&&(!s.equals("NA"))&&(!s.equals(""))&&(!s.equals("@")&&(!s.equals("_")))){
					if(!set.contains(s))
						set.add(s);
				}
			}
			num[i] = set.size();
		}
		return num;
	}*/

	public static Vector<HashMap<String,Integer>> countNumberOfDistinctValuesInColumns(VDataTable vt){
	Vector<HashMap<String,Integer>> map = new Vector<HashMap<String,Integer>>();
	for(int i=0;i<vt.colCount;i++){
		HashMap<String, Integer> set = new HashMap<String, Integer>();
		map.add(set);
		for(int j=0;j<vt.rowCount;j++){
			String s = vt.stringTable[j][i].trim();
			if((!s.equals("\"\""))&&(!s.equals("NA"))&&(!s.equals(""))&&(!s.equals("@")&&(!s.equals("_")))){
				if(!set.containsKey(s)){
					set.put(s, 1);
				}else{
					set.put(s, set.get(s)+1);
				}
			}
		}
	}
	return map;
	}
	
    public static double calcTTest(Vector set1, Vector set2){
        double r = 0;
        VStatistics stat1 = new VStatistics(1);
        VStatistics stat2 = new VStatistics(1);
        float d[] = new float[1];
        for(int i=0;i<set1.size();i++){
          d[0] = ((Float)set1.elementAt(i)).floatValue();
          stat1.addNewPoint(d);
        }
        for(int i=0;i<set2.size();i++){
          d[0] = ((Float)set2.elementAt(i)).floatValue();
          stat2.addNewPoint(d);
        }
        stat1.calculate();
        stat2.calculate();
        r = (stat1.getMean(0)-stat2.getMean(0))/Math.sqrt(stat1.getStdDev(0)*stat1.getStdDev(0)/set1.size()+stat2.getStdDev(0)*stat2.getStdDev(0)/set2.size());
        return r;
      }
    public static double calcTTestDegreesOfFreedom(Vector set1, Vector set2){
        double r = 0;
        VStatistics stat1 = new VStatistics(1);
        VStatistics stat2 = new VStatistics(1);
        float d[] = new float[1];
        for(int i=0;i<set1.size();i++){
          d[0] = ((Float)set1.elementAt(i)).floatValue();
          stat1.addNewPoint(d);
        }
        for(int i=0;i<set2.size();i++){
          d[0] = ((Float)set2.elementAt(i)).floatValue();
          stat2.addNewPoint(d);
        }
        stat1.calculate();
        stat2.calculate();
        double s1 = stat1.getStdDev(0);
        double s2 = stat2.getStdDev(0);
        double n1 = stat1.pointsNumber;
        double n2 = stat2.pointsNumber;
        r = (s1*s1/n1+s2*s2/n2)*(s1*s1/n1+s2*s2/n2)/((s1*s1/n1)*(s1*s1/n1)/(n1-1)+(s2*s2/n2)*(s2*s2/n2)/(n2-1));
        return r;
      }
    

    public static float max(float[] t) {
        float maximum = 0;
          if(t.length!=0)
          {
          maximum = t[0];   // start with the first value
          for (int i=1; i<t.length; i++) {
              if (t[i] > maximum) {
                  maximum = t[i];   // new maximum
              }
          }
          }
          return maximum;
      }

    
    public static void findCorrelationsWithMetagenesRNK(String folderName, VDataTable vtS, String fout, BIODICAPipeLine biodica){
    	
    	Vector<Metagene> rnks = Metagene.loadFromAllRNKInFolder(folderName);
    	Vector<Metagene> ics = Metagene.decomposeTableIntoMetagenes(vtS);
    	
    	float vals[][] = new float[ics.size()][rnks.size()];
    	String info[][] = new String[ics.size()][rnks.size()];
    	Vector<String> fieldsToTest = new Vector<String>();
    	Vector<String> fieldNames = new Vector<String>(); 
    	Vector<String> valNames = new Vector<String>();
    	
    	for(int i=0;i<ics.size();i++) fieldsToTest.add(ics.get(i).name);
    	//for(int i=0;i<ics.size();i++) fieldNames.add(ics.get(i).name);
    	for(int i=0;i<rnks.size();i++) valNames.add(rnks.get(i).name);
    	
    	for(int i=0;i<rnks.size();i++)for(int j=0;j<ics.size();j++){
    		float corr = Metagene.CalcCorrelation(rnks.get(i),ics.get(j));
    		int npoints = Metagene.metageneIntersection(rnks.get(i),ics.get(j)).size();
    		double pvalue = VSimpleFunctions.calcCorrelationPValue(corr, npoints);
        	pvalue = -(float)Math.log10(pvalue);
        	if(pvalue>100) pvalue=100;
    		vals[j][i] = (float)pvalue;
    		DecimalFormat df = new DecimalFormat("#.##");
    		info[j][i] = df.format(corr);
    	}
    	
    	printAssociationsT(vals, info, fieldsToTest, fieldNames, valNames, fout, biodica.AssociationAnalysisThreshold);
    }
    
	

}
