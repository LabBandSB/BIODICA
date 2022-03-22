package logic;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;
import logic.Attribute;
import logic.Edge;
import logic.Graph;
import logic.Node;
import logic.XGMML;

public class MakeCorrelationGraph {
	
	public static boolean stop_execution = false; 

	public static void main(String[] args) {
		try{
			
			String folder = "./";
			boolean computeCorrelations = false;
			boolean assembleGraph = false;
			boolean splitTails = false;
			boolean computeCorrelationOnlyForLongestTails = false;
			boolean computeResidues = false;
			float ThresholdForTopContributingGenes = 3f;
			String file1 = "";
			String file2 = "";
			
			for(int i=0;i<args.length;i++){
				if(args[i].equals("-folder"))
					folder = args[i+1];
				if(args[i].equals("-compute"))
					computeCorrelations = true;
				if(args[i].equals("-assemble"))
					assembleGraph = true;
				if(args[i].equals("-splittails"))
					splitTails = true;
				if(args[i].equals("-residues")){
					computeResidues = true;
					file1 = args[i+1];
					file2 = args[i+2];
				}
				if(args[i].equals("-longtail")){
					computeCorrelationOnlyForLongestTails = true;
				}
				if(args[i].equals("-contributionthreshold")){
					ThresholdForTopContributingGenes = Float.parseFloat(args[i+1]);
				}
			}
			
			if(!(computeCorrelations||assembleGraph||splitTails||computeResidues)){
				System.out.println("Making a correlation graph from best bidirectional hits. "
						+ "\nThe folder must contains tab-delimited files ending with _S.xls, "
						+ "\nwith first column been object ids. Table can contain N/A values.");
				System.out.println("Use: java getools.MakeCorrelationGraph [commands] [options]");
				System.out.println("Commands");
				System.out.println("-compute : will compute all pairwise correlation subgraphs");
				System.out.println("-assemble : will compute all pairwise correlation subgraphs");
				System.out.println("-splittails : split positive and negative tails of the distributions");
				System.out.println("-residues [values] [reference]: transform [values] file into file containing resudes based on [reference]");
				System.out.println("Options");
				System.out.println("-folder : folder containing the files, default './' ");
				System.out.println("-longtails : compute correlation only between longest tails");
				System.out.println("-contributionthreshold : threshold for defining top contributing genes");
			}
			
			if(splitTails){
				SplitAllFilesIntoPositiveAndNegativeTails(folder);
			}
			if(computeCorrelations){
				MakeCorrelationGraph(folder, false, computeCorrelationOnlyForLongestTails, ThresholdForTopContributingGenes);
			}
			if(assembleGraph){
				assembleCorrelationGraph(folder);
			}
			if(computeResidues){
				ComputeResidueFile(folder,file1,file2);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void MakeCorrelationGraph(String folder, boolean splitTails, boolean computeCorrelationOnlyForLongestTails, float ThresholdForTopContributingGenes) throws Exception{
		Vector<VDataTable> tables = new Vector<VDataTable>();
		Vector<String> fileNames = new Vector<String>();
		File fs[] = new File(folder).listFiles();
		for(int i=0;i<fs.length;i++)if(fs[i].getName().endsWith("_S.xls")){
			System.out.println(fs[i].getName()+"\t"+Utils.getUsedMemoryMb());
			//VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(fs[i].getAbsolutePath(), true, "\t");
			//for(int k=1;k<vt1.colCount;k++) vt1.fieldTypes[k] = vt1.NUMERICAL;
			VDataTable vt1 = null;
			tables.add(vt1);
			fileNames.add(fs[i].getName());
		}
		
		Graph graph = new Graph();
		for(int i=0;i<tables.size();i++){
			String fn1 = new String(fileNames.get(i));
			VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(folder+fn1, true, "\t");
			fn1 = fn1.substring(0, fn1.length()-6);
			System.out.println("Table i :"+fn1);
			for(int k=1;k<vt1.colCount;k++) vt1.fieldTypes[k] = vt1.NUMERICAL;
			for(int j=0;j<tables.size();j++){
			if(stop_execution) break;
			if(i!=j){
				
				if(stop_execution) break;
				
				String fn2 = new String(fileNames.get(j));
				String fn2_short = fn2.substring(0, fn2.length()-6);
				
				File computedGraph = new File(folder+fn1+"_"+fn2_short+".xgmml");
				if(!computedGraph.exists()){
					
				FileWriter fw = new FileWriter(computedGraph.getAbsolutePath());
				fw.write("<empty>");
				fw.close();
				
				VDataTable vt2 = VDatReadWrite.LoadFromSimpleDatFile(folder+fn2, true, "\t");
				System.out.println("Table j :"+fn2_short);
				for(int k=1;k<vt2.colCount;k++) vt2.fieldTypes[k] = vt2.NUMERICAL;
				
				fn2 = fn2_short;
				System.out.println("=============================");
				System.out.println(fn1+"\tvs\t"+fn2);
				//VDataTable vt1 = tables.get(i);
				//VDataTable vt2 = tables.get(j);
				if(splitTails){
					Date dt = new Date();
					logic.Utils.separatePositiveAndNegativeValues(vt1);
					logic.Utils.separatePositiveAndNegativeValues(vt2);
					VDatReadWrite.saveToSimpleDatFile(vt1, folder+fn1+"_pn.tmp");
					System.out.println("Time: "+(new Date().getTime()-dt.getTime()));
					//VDatReadWrite.saveToSimpleDatFile(vt1, folder+fn2+"_pn.xls");
				}
				Graph gr = makeTableCorrelationGraph(vt1, fn1, vt2, fn2, 0.1f, true, false, computeCorrelationOnlyForLongestTails, ThresholdForTopContributingGenes);
				//graph.addNodes(gr);
				//graph.addEdges(gr);
				gr.name = "corr_graph"+fn1+"_"+fn2+"_"+(new Random()).nextInt(10000);
				XGMML.saveToXGMML(gr, computedGraph.getAbsolutePath());
				}
			}}
		}
	}

	/** 
	 * The tables should have first column as index.
	 * @param vt1
	 * @param prefix1 
	 * @param vt2
	 * @param prefix2
	 * @param folder is used for keeping the results
	 * @param correlationThreshold
	 * @throws Exception
	 */
	public static Graph makeTableCorrelationGraph(VDataTable vt1, String prefix1, VDataTable vt2, String prefix2, float correlationThreshold, boolean chooseOnlyMaximalCorrelation, boolean splitTails, boolean computeCorrelationOnlyForLongestTails, float ThresholdForTopContributingGenes) throws Exception{
		vt1.makePrimaryHash(vt1.fieldNames[0]);
		vt2.makePrimaryHash(vt2.fieldNames[0]);
		// make common list of objects
		HashSet<String> names_set = new HashSet<String>();
		Vector<String> names = new Vector<String>();
		for(int i=0;i<vt1.rowCount;i++){
			String name1 = vt1.stringTable[i][0];
			if(vt2.tableHashPrimary.get(name1)!=null){
				if(!names_set.contains(name1))
					names_set.add(name1);
			}
		}
		for(String s: names_set)
			names.add(s);
		Collections.sort(names);
		System.out.println(names.size()+" common objects are found.");
		
		int sign1[] = new int[vt1.colCount-1];
		int sign2[] = new int[vt2.colCount-1];
		
		if(computeCorrelationOnlyForLongestTails){
			VDataSet vd1 = VSimpleProcedures.SimplyPreparedDataset(vt1, -1);
			VDataSet vd2 = VSimpleProcedures.SimplyPreparedDataset(vt2, -1);
			sign1 = Utils.determineTheLongestTales(vd1,ThresholdForTopContributingGenes);
			sign2 = Utils.determineTheLongestTales(vd2,ThresholdForTopContributingGenes);
			System.out.print(prefix1+"\t"); for(int l=0;l<sign1.length;l++) System.out.print(sign1[l]+"\t"); System.out.println();
			System.out.print(prefix2+"\t"); for(int l=0;l<sign2.length;l++) System.out.print(sign2[l]+"\t"); System.out.println();
		}
		
		
		//FileWriter fw  = new FileWriter(folder+prefix1+"_"+prefix2+".txt");
		//fw.write("FIELD1\tFIELD2\tCORRELATION\tCORRELATION_ABS\n");
		
		Graph graph = new Graph();
		
		for(int i=0;i<vt1.colCount;i++)if(vt1.fieldTypes[i]==vt1.NUMERICAL){
			String fni = vt1.fieldNames[i]+"_"+prefix1;
			Node ni = graph.getCreateNode(fni);
			ni.setAttributeValueUnique("FIELD", vt1.fieldNames[i], Attribute.ATTRIBUTE_TYPE_STRING);
			String set1 = prefix1;
			if(prefix1.endsWith("_positive")) set1 = prefix1.substring(0, prefix1.length()-9);
			if(prefix1.endsWith("_negative")) set1 = prefix1.substring(0, prefix1.length()-9);
			//System.out.println(prefix1+"->"+set1);
			ni.setAttributeValueUnique("SET", set1, Attribute.ATTRIBUTE_TYPE_STRING);
			if(vt1.fieldNames[i].endsWith("_ps")||prefix1.contains("_positive"))
				ni.setAttributeValueUnique("TAIL", "positive", Attribute.ATTRIBUTE_TYPE_STRING);
			if(vt1.fieldNames[i].endsWith("_ng")||prefix1.contains("_negative"))
				ni.setAttributeValueUnique("TAIL", "negative", Attribute.ATTRIBUTE_TYPE_STRING);
			
			//assignOutlierAttribute(vt1,i,ni,ThresholdForTopContributingGenes);
			
			int maxi = -1;
			float maxabscorr = 0f;
			int maxnumberofpoints = 0;
			float maxcorr = 0f;
			for(int j=0;j<vt2.colCount;j++)if(vt2.fieldTypes[j]==vt2.NUMERICAL){
				String fnj = vt2.fieldNames[j]+"_"+prefix2;
				Node nj = graph.getCreateNode(fnj);
				nj.setAttributeValueUnique("FIELD", vt2.fieldNames[j], Attribute.ATTRIBUTE_TYPE_STRING);
				String set2 = prefix2;
				if(prefix2.endsWith("_positive")) set2 = prefix2.substring(0, prefix2.length()-9);
				if(prefix2.endsWith("_negative")) set2 = prefix2.substring(0, prefix2.length()-9);
				//System.out.println(prefix2+"->"+set2);
				nj.setAttributeValueUnique("SET", set2, Attribute.ATTRIBUTE_TYPE_STRING);
				if(vt2.fieldNames[j].endsWith("_ps")||prefix2.contains("_positive"))
					nj.setAttributeValueUnique("TAIL", "positive", Attribute.ATTRIBUTE_TYPE_STRING);
				if(vt2.fieldNames[j].endsWith("_ng")||prefix2.contains("_negative"))
					nj.setAttributeValueUnique("TAIL", "negative", Attribute.ATTRIBUTE_TYPE_STRING);
				
				//assignOutlierAttribute(vt2,j,nj,ThresholdForTopContributingGenes);
				
				float xi[] = new float[names.size()];
				float xj[] = new float[names.size()];
				for(int k=0;k<names.size();k++){
					String vi = vt1.stringTable[vt1.tableHashPrimary.get(names.get(k)).get(0)][i];
					String vj = vt2.stringTable[vt2.tableHashPrimary.get(names.get(k)).get(0)][j];
					if(vi.toLowerCase().equals("n/a"))
						xi[k] = Float.NaN;
					else
						xi[k] = Float.parseFloat(vi);
					if(vj.toLowerCase().equals("n/a"))
						xj[k] = Float.NaN;
					else
						xj[k] = Float.parseFloat(vj);
				}
				
				if(computeCorrelationOnlyForLongestTails){
					for(int l=0;l<xi.length;l++)if(!Float.isNaN(xi[l])){
						if(sign1[i-1]==1)if(xi[l]<0) xi[l] = Float.NaN;
						if(sign1[i-1]==-1)if(xi[l]>0) xi[l] = Float.NaN;
					}
					for(int l=0;l<xj.length;l++)if(!Float.isNaN(xj[l])){
						if(sign2[j-1]==1)if(xj[l]<0) xj[l] = Float.NaN;
						if(sign2[j-1]==-1)if(xj[l]>0) xj[l] = Float.NaN;
					}
				}
				
				float corr = VSimpleFunctions.calcCorrelationCoeffMissingValues(xi, xj);
				float abscorr = Math.abs(corr);
				int numberOfPoints = VSimpleFunctions.getNumberOfCompleteNumberPairs(xi, xj);
				double pval = VSimpleFunctions.calcCorrelationPValue(abscorr, numberOfPoints);
				if(numberOfPoints<10){ 
					abscorr = 0f;
					corr = 0f;
				}
				
				System.out.print(corr+"\t");
				if(!chooseOnlyMaximalCorrelation)
				if(abscorr>=correlationThreshold)if(numberOfPoints>10)if(pval<0.01){
				//	fw.write(fni+"_"+prefix1+"\t"+fnj+"_"+prefix2+"\t"+corr+"\t"+Math.abs(corr)+"\n");
					Edge e = graph.getCreateEdge(fni+"__"+fnj);
					e.Node1 = ni;
					e.Node2 = nj;
					e.setAttributeValueUnique("CORR", ""+corr, Attribute.ATTRIBUTE_TYPE_REAL);
					e.setAttributeValueUnique("ABSCORR", ""+abscorr, Attribute.ATTRIBUTE_TYPE_REAL);
					
					e.setAttributeValueUnique("NUMBER_OF_POINTS", ""+numberOfPoints, Attribute.ATTRIBUTE_TYPE_REAL);
					e.setAttributeValueUnique("LOG10PVAL", ""+(-Math.log10(pval)), Attribute.ATTRIBUTE_TYPE_REAL);
					
				}
				if(abscorr>maxabscorr){
					maxabscorr = abscorr;
					maxnumberofpoints = numberOfPoints;
					maxi = j;
				}
			}
			System.out.println();
			if(chooseOnlyMaximalCorrelation){
				if(maxabscorr>=correlationThreshold){
				//	fw.write(fni+"_"+prefix1+"\t"+fnj+"_"+prefix2+"\t"+corr+"\t"+Math.abs(corr)+"\n");
					String fnj = vt2.fieldNames[maxi]+"_"+prefix2;
					Node nj = graph.getCreateNode(fnj);
					Edge e = graph.getCreateEdge(fni+"__"+fnj);
					e.Node1 = ni;
					e.Node2 = nj;
					e.setAttributeValueUnique("CORR", ""+maxcorr, Attribute.ATTRIBUTE_TYPE_REAL);
					e.setAttributeValueUnique("ABSCORR", ""+maxabscorr, Attribute.ATTRIBUTE_TYPE_REAL);

					double pval1 = VSimpleFunctions.calcCorrelationPValue(maxabscorr, maxnumberofpoints);
					e.setAttributeValueUnique("NUMBER_OF_POINTS", ""+maxnumberofpoints, Attribute.ATTRIBUTE_TYPE_REAL);
					e.setAttributeValueUnique("LOG10PVAL", ""+(-Math.log10(pval1)), Attribute.ATTRIBUTE_TYPE_REAL);
					
				}
			}
		}
		return graph;
		//fw.close();
	}
	
	private static void assignOutlierAttribute(VDataTable vt1, int fieldNumber, Node ni, float threshold) {
		float vals[] = new float[vt1.rowCount];
		for(int k=0;k<vt1.rowCount;k++){
			String val = vt1.stringTable[k][fieldNumber].toLowerCase();
			if(val.equals("n/a")) 
				vals[k] = Float.NaN;
			else
				vals[k] = Float.parseFloat(val);
		}
		vals = Utils.makeLongestTailPositive(vals,threshold);
		Vector<Integer> outliers = Utils.findDistributionOutliersByGapAnalysis(vals, threshold, 1.5f);
		if(outliers.size()>0){
			ni.setAttributeValueUnique("OUTLIER_DRIVEN", "1", Attribute.ATTRIBUTE_TYPE_STRING);
			String outlierString = "";
			String outlierStringWithWeights = "";
			for(int k=0;k<outliers.size();k++){
				outlierString +=vt1.stringTable[outliers.get(k)][0]+";";
				outlierStringWithWeights +=vt1.stringTable[outliers.get(k)][0]+"("+vt1.stringTable[outliers.get(k)][fieldNumber]+");";
			}
			outlierString = outlierString.substring(0, outlierString.length()-1);
			outlierStringWithWeights = outlierStringWithWeights.substring(0, outlierStringWithWeights.length()-1);
			ni.setAttributeValueUnique("OUTLIERS", outlierString, Attribute.ATTRIBUTE_TYPE_STRING);
			ni.setAttributeValueUnique("OUTLIERSWEIGHTS", outlierStringWithWeights, Attribute.ATTRIBUTE_TYPE_STRING);
		}
	}

	
	public static void SplitAllFilesIntoPositiveAndNegativeTails(String folder) throws Exception{
		Vector<VDataTable> tables = new Vector<VDataTable>();
		Vector<String> fileNames = new Vector<String>();
		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.numberOfDigitsToKeep = 3;
		File fs[] = new File(folder).listFiles();
		for(int i=0;i<fs.length;i++)if(fs[i].getName().endsWith("_S.xls"))if(!fs[i].getName().contains("_positive_"))if(!fs[i].getName().contains("_negative_")){
			System.out.println(fs[i].getName()+"\t"+Utils.getUsedMemoryMb());
			VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(fs[i].getAbsolutePath(), true, "\t");
			VSimpleProcedures.findAllNumericalColumns(vt1);
			VDataTable vtp = logic.Utils.separatePositiveOrNegativeValues(vt1, true);
			VDataTable vtn = logic.Utils.separatePositiveOrNegativeValues(vt1, false);
			String fn = fs[i].getAbsolutePath();
			fn = fn.substring(0,fn.length()-6);
			VDatReadWrite.saveToSimpleDatFile(vtp, fn+"_positive_S.xls");
			VDatReadWrite.saveToSimpleDatFile(vtn, fn+"_negative_S.xls");
		}
	}
	
	  public static Graph RemoveReciprocalEdgesInCorrelationGraph(Graph graph){
		  Graph gr = graph;
		  float connectivity[] = new float[graph.Nodes.size()];
		  gr.calcNodesInOut();
		  graph.calcNodesInOut();
		  for(int i=0;i<graph.Nodes.size();i++){
			  //System.out.println(graph.Nodes.get(i).Id+"\t"+graph.Nodes.get(i).incomingEdges.size()+"\t"+graph.Nodes.get(i).outcomingEdges.size());
			  connectivity[i] = 0f+(float)graph.Nodes.get(i).incomingEdges.size()+(float)graph.Nodes.get(i).outcomingEdges.size();
		  }
		  int ind[] = Algorithms.SortMass(connectivity);
		  for(int i=0;i<ind.length;i++){
			  Node n = graph.Nodes.get(ind[i]);
			  gr.calcNodesInOut();
			  //graph.calcNodesInOut();
			  for(int j=0;j<n.incomingEdges.size();j++)for(int k=0;k<n.outcomingEdges.size();k++){
				  Edge ej = n.incomingEdges.get(j);
				  Edge ek = n.outcomingEdges.get(k);
				  float corrj = Float.parseFloat(ej.getFirstAttributeValue("ABSCORR"));
				  float corrk = Float.parseFloat(ek.getFirstAttributeValue("ABSCORR"));
				  if(ej.Node1==ek.Node2)if(ej.Node2==ek.Node1){
					     if(corrk>corrj){
					    	 ej.setAttributeValueUnique("ABSCORR", ""+corrk, Attribute.ATTRIBUTE_TYPE_REAL);
					    	 ej.setAttributeValueUnique("CORR", ""+Float.parseFloat(ek.getFirstAttributeValue("CORR")), Attribute.ATTRIBUTE_TYPE_REAL);
					     }
						 gr.removeEdge(ek.Id);
						 ej.setAttributeValueUnique("RECIPROCAL", "TRUE", Attribute.ATTRIBUTE_TYPE_STRING);
				  }
			  }
			  gr.calcNodesInOut();
			  for(int j=0;j<n.incomingEdges.size();j++)for(int k=j+1;k<n.incomingEdges.size();k++){
				  Edge ej = n.incomingEdges.get(j);
				  Edge ek = n.incomingEdges.get(k);
				  float corrj = Float.parseFloat(ej.getFirstAttributeValue("ABSCORR"));
				  float corrk = Float.parseFloat(ek.getFirstAttributeValue("ABSCORR"));
				  if(ej.Node1==ek.Node1)if(ej.Node2==ek.Node2){
					     if(corrk>corrj){
					    	 ej.setAttributeValueUnique("ABSCORR", ""+corrk, Attribute.ATTRIBUTE_TYPE_REAL);
					    	 ej.setAttributeValueUnique("CORR", ""+Float.parseFloat(ek.getFirstAttributeValue("CORR")), Attribute.ATTRIBUTE_TYPE_REAL);
					     }
						 gr.removeEdge(ek.Id);
						 ej.setAttributeValueUnique("RECIPROCAL", "TRUE", Attribute.ATTRIBUTE_TYPE_STRING);
				  }
			  }
		  }
		  return gr;
	  }

	  public static void assembleCorrelationGraph(String folder) throws Exception{
			Graph gr = new Graph();
			File fles[] = (new File(folder)).listFiles();
			for(File f:fles){
				if(f.getName().endsWith(".xgmml"))if(!f.getName().startsWith("correlation")){
					System.out.println("Loaded "+f.getName());
					Graph gri = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(f.getAbsolutePath()));
					gr.addNodes(gri);
					gr.addEdges(gri);
				}
			}
			XGMML.saveToXGMML(gr, folder+"correlation_graph.xgmml");
			gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(folder+"correlation_graph.xgmml"));
			System.out.println(gr.Nodes.size()+" nodes, "+gr.Edges.size()+" edges.");
			Graph gr1 = RemoveReciprocalEdgesInCorrelationGraph(gr);
			gr1.name = "correlation_graph_"+(new Random()).nextInt(10000);
			System.out.println(gr1.Nodes.size()+" nodes, "+gr1.Edges.size()+" edges.");
			XGMML.saveToXGMML(gr1, folder+"correlation_graph_norecipedges.xgmml");
	  }
	  
	  public static void ComputeResidueFile(String folder, String file, String reference) throws Exception{
		  VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+file, true, "\t");
		  VDataTable ref = VDatReadWrite.LoadFromSimpleDatFile(folder+reference, true, "\t");
		  logic.Utils.findAllNumericalColumns(vt); vt.makePrimaryHash(vt.fieldNames[0]);
		  logic.Utils.findAllNumericalColumns(ref); ref.makePrimaryHash(ref.fieldNames[0]);
		  Vector<String> names = new Vector<String>();
		  VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		  VDataSet refd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(ref, -1);
		  Vector<float[]> values = new Vector<float[]>();
		  Vector<Float> refvals = new Vector<Float>();
		  for(int i=0;i<ref.rowCount;i++){
			  String name = ref.stringTable[i][0];
			  if(vt.tableHashPrimary.containsKey(name))if(!Float.isNaN(refd.massif[i][0])){
				  names.add(name);
				  int k = vt.tableHashPrimary.get(name).get(0);
				  values.add(vd.massif[k]);
				  refvals.add(refd.massif[i][0]);
			  }
		  }
		  float vals[][] = new float[values.size()][vd.coordCount];
		  float factor[] = new float[values.size()];
		  for(int i=0;i<values.size();i++){
			  for(int j=0;j<values.get(i).length;j++){
				  vals[i][j] = values.get(i)[j];
				  //System.out.print(vals[i][j]+"\t");
			  }
			  //System.out.println();
		  }
		  //System.out.println();
		  for(int i=0;i<refvals.size();i++){
			  factor[i] = refvals.get(i);
			  //System.out.println(factor[i]);
		  }
		  
		  float valsr[][] = VSimpleFunctions.calcResiduesFromRegressionOnCommonFactor(vals, factor);
		  /*System.out.println();
		  for(int i=0;i<values.size();i++){
			  for(int j=0;j<values.get(i).length;j++){
				  System.out.print(valsr[i][j]+"\t");
			  }
			  System.out.println();
		  }*/
		  String fn = file.substring(0,file.length()-4);
		  if(fn.endsWith("_S")) { fn = fn.substring(0,fn.length()-2)+"_r_S.xls"; }
		  else
			  fn = fn+"_r_S.xls";
		  FileWriter fw = new FileWriter(folder+fn);
		  for(int i=0;i<vt.fieldNames.length;i++) fw.write(vt.fieldNames[i]+"\t"); fw.write("\n");
		  for(int i=0;i<names.size();i++){
			  fw.write(names.get(i)+"\t");
			  for(int j=0;j<vd.coordCount;j++)
				  if(!Float.isNaN(valsr[i][j]))
					  fw.write(valsr[i][j]+"\t");
				  else
					  fw.write("N/A\t");
			  fw.write("\n");
		  }
 		  fw.close();		  
	  }


}
