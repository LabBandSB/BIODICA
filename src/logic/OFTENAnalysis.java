package logic;

import java.util.*;
import java.io.*;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;

public class OFTENAnalysis {

	/**
	 * @param args
	 */
	
	public Graph PPINetwork = null;
	public Vector<String> listOfRankedGenes = null;
	public Graph extractedNetwork = null;
	public Vector<String> rankedGeneList = null;
	public SubnetworkProperties snp = null;
	public int numberOfPermutationsForSizeTest = 100;
	public float thresholdSelectionConnectedComponents = 0.1f;
	public float thresholdIntersectionOfGraphs = 0.4f;
	public String fileName = "";
	
	int optimalNumberOfGenes = -1;
	float optimalScore = -1f;
	float pvalue = 1f;
	int optimalComponentSize = -1;
	int sizeOfComponentToSelect = -1;
	
	public static void main(String[] args) {
		try{
			
			//extractScoresFromOFTENReports("C:/Datas/ColonCancer/analysis/often_communities/_analysis/");
			//makeGMTfromSetOfXGMMLfiles("C:/Datas/ColonCancer/analysis/often_communities/pca/significant/");
			//System.exit(0);
			
			String pathToPPINetwork = null;
			String tablePath = "";
			String name = "";
			int nstart = 100; 
			int nend = 1000; 
			int nstep = 50;
			
			OFTENAnalysis of = new OFTENAnalysis();
			
			for(int i=0;i<args.length;i++){
				if(args[i].equals("-xgmml")) pathToPPINetwork = args[i+1];
				if(args[i].equals("-table")) tablePath = args[i+1];
				if(args[i].equals("-name")) name = args[i+1];
				if(args[i].equals("-start")) nstart = Integer.parseInt(args[i+1]);
				if(args[i].equals("-end")) nend = Integer.parseInt(args[i+1]);
				if(args[i].equals("-step")) nstep = Integer.parseInt(args[i+1]);
				if(args[i].equals("-nperm")) of.numberOfPermutationsForSizeTest = Integer.parseInt(args[i+1]);
			}
			of.loadPPINetwork(pathToPPINetwork);
			of.completeOFTENAnalysisOfTable(tablePath,name,nstart,nend,nstep,null);
			
			System.exit(0);
			
			//of.loadPPINetwork(pathToPPINetwork);
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/network_analysis/S_CIT.txt","CIT");
			//of.completeOFTENAnalysisOfTable("C:/Datas/CAFibroblasts/caf_lc_S.xls","caf");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ASSET/miRNA_Iljin_Kristina/analysis/scores.txt","ASP14");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ASSET/miRNA_Iljin_Kristina/UW228/scores.xls","UW228");
			//of.completeOFTENAnalysisOfTable("C:/Datas/SingleCellTranscriptomics/MonocleData/ica/GSE52529_uniquelog_ica_S.xls","SSM");
			//of.makeGSEABatchFile("C:/Datas/SingleCellTranscriptomics/MonocleData/ica/gsea/","coordinated_set_of_genes_150.gmt","results/");
			//of.makeGSEABatchFile("C:/Datas/ICA/Anne/metaranking/GSEA/","vantveer_signatures.gmt","results/");
			System.exit(0);
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/SADP_rda_files/S_CIT.txt","CIT");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/cellline/broad/S_CCLE05.txt","CCLE05");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/cellline/exon_curie/S_CUEX20.txt","CUEX20");
			//of.completeOFTENAnalysisOfTable("C:/Datas/OvarianCancer/TCGA/analysis/GSEA/S_OVAFFY.txt","OVAFFY");
			//of.completeOFTENAnalysisOfTable("C:/Datas/MOSAIC/analysis/gsea/ica/S_EWING96.txt","S_EWING96");
			//of.completeOFTENAnalysisOfTable("C:/Datas/MOSAIC/analysis/ica/rhabdoid_48/S_RHD96.txt","S_RHD96");
			//of.completeOFTENAnalysisOfTable("C:/Datas/Ivashkine/ZebraFish/repeat/differences.txt","DF");
			
			//of.completeOFTENAnalysisOfTable("C:/Datas/DeDaL/appealing_example/tissues/S_tissues_table.txt","TISS");
			//of.completeOFTENAnalysisOfTable("C:/Datas/DeDaL/appealing_example/tissues/networks/tissues_grouped_c.txt","TS");
			//of.completeOFTENAnalysisOfTable("C:/Datas/DeDaL/appealing_example/tissues/networks/smoothed/tissues_grouped_c.txt0.5.txt","TSNS");
			//System.exit(0);
			 
			//of.makeGSEABatchFile("C:/Datas/ICA/Anne/attractor_metagenes/","C:/Datas/ICA/Anne/attractor_metagenes/attractor_metagenes.gmt","C:/Datas/ICA/Anne/attractor_metagenes/results/");
			//of.makeGSEABatchFile("C:/Datas/Ivashkine/ZebraFish/repeat/gsea/","C:/Datas/Ivashkine/ZebraFish/repeat/gsea/go_symbol.gmt","C:/Datas/Ivashkine/ZebraFish/repeat/gsea/results/");
			//of.makeGSEABatchFile("C:/Datas/OvarianCancer/TCGA/analysis/GSEA/","C:/Datas/ICA/Anne/GSEA/msigdb.v4.0.symbols.gmt","C:/Datas/OvarianCancer/TCGA/analysis/GSEA/results/");
			//of.makeGSEABatchFile("C:/Datas/ICA/Anne/cellline/broad/GSEA/","C:/Datas/ICA/Anne/GSEA/msigdb.v4.0.symbols.gmt","C:/Datas/ICA/Anne/cellline/broad/GSEA/results/");
			//of.makeGSEABatchFile("C:/Datas/ICA/Anne/cellline/exon_curie/GSEA/","C:/Datas/ICA/Anne/GSEA/msigdb.v4.0.symbols.gmt","C:/Datas/ICA/Anne/cellline/exon_curie/GSEA/results/");
			//of.makeGSEABatchFile("C:/Datas/MOSAIC/analysis/gsea/ica/","C:/Datas/ICA/Anne/GSEA/msigdb.v4.0.symbols.gmt","C:/Datas/MOSAIC/analysis/gsea/ica/results/");
			//of.makeGSEABatchFile("C:/Datas/MOSAIC/analysis/gsea/ica_rhabdo/","msigdb.v4.0.symbols.gmt","results/");
			//of.makeGSEABatchFile("C:/Datas/ICA/Anne/CDK12/","CDK12.gmt","results/");
			
			//of.makeGSEABatchFile("C:/Datas/PanMethylome/methylome/BLCAC/gsea/","msigdb_acsn.gmt","results/");
			//of.makeGSEABatchFile("C:/Datas/PanMethylome/methylome/BLCA/gsea/","msigdb_acsn.gmt","results/");
			
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/network_analysis/S_TCGABLADDER.txt","TCGABLCA");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/network_analysis/S_TCGABREAST.txt","TCGABREAST");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/network_analysis/S_COAD.txt","COAD");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/network_analysis/S_KIRC.txt","KIRC");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/network_analysis/S_LUAD.txt","LUAD");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/network_analysis/S_LUSC.txt","LUSC");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/network_analysis/S_READ.txt","READ");
			//of.completeOFTENAnalysisOfTable("C:/Datas/ICA/Anne/network_analysis/S_TCGABRCAAGL.txt","TCGABRCAAGL");
			
			//String networks[] = {"CIT_networks.txt","TCGABLCA_networks.txt"};
			//String prefixes[] = {"CIT","TCGABLCA"};
			//String networks[] = {"CIT_networks.txt","TCGABLCA_networks.txt","TCGABREAST_networks.txt"};
			//String prefixes[] = {"CIT","TCGABLCA","TCGABREAST"};
			//MakeNetworkIntersectionGraph("C:/Datas/ICA/Anne/network_analysis/",networks,prefixes,0.1f,0.02f,"C:/Datas/ICA/Anne/network_analysis/network_intersection.txt");
			
			String networks[] = {"tissues.txt"};
			String prefixes[] = {"TS"};
			MakeNetworkIntersectionGraph("C:/Datas/DeDaL/appealing_example/tissues/networks/",networks,prefixes,0.05f,0.02f,"C:/Datas/DeDaL/appealing_example/tissues/networks/network_intersection.txt");
			 
			
			System.exit(0);
				
			//String prefix = "C:/Datas/EWING/ICA/";
			String prefix = "C:/Datas/Kairov/DifferentialExpression4BC/lists/"; 
			nstart = 530;
			nend = nstart;
			int step = 10;	
			
			int valuesToTest[] = new int[(int)((nend-nstart)/step)+1];
			int k = 0;
			for(int i=nstart;i<=nend;i+=step)
				valuesToTest[k++] = i;
			OFTENAnalysis metaoften = new OFTENAnalysis();
			metaoften.loadPPINetwork(pathToPPINetwork);
			
			Vector<String> listOfGroups = Utils.loadStringListFromFile(prefix+"list");
			
			for(int i=0;i<listOfGroups.size();i++){
				Vector<String> listOfFileNames = new Vector<String>();
				StringTokenizer st = new StringTokenizer(listOfGroups.get(i),"_");
				while(st.hasMoreTokens())
					listOfFileNames.add(prefix+st.nextToken());
				OFTENAnalysis often = new OFTENAnalysis();
				often.PPINetwork = metaoften.PPINetwork;
				often.makeMetaOFTENFromRandkedGeneListFiles(listOfFileNames, valuesToTest);
				often.extractedNetwork.name = listOfGroups.get(i);
				XGMML.saveToXGMML(often.extractedNetwork, prefix+listOfGroups.get(i)+".xgmml");
			}
			
			System.exit(0);
			
			
			
			String fileRankedGenes = "1ic4";
			OFTENAnalysis often = new OFTENAnalysis();
			often.loadPPINetwork(pathToPPINetwork);
			often.loadRandkedGeneList(prefix+fileRankedGenes);
			often.fileName = fileRankedGenes;
			often.makeOFTENAnalysis(valuesToTest);
			often.extractedNetwork.name = fileRankedGenes;
			XGMML.saveToXGMML(often.extractedNetwork, prefix+fileRankedGenes+".xgmml");
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public String completeOFTENAnalysisOfTable(String fn, String name, int nstart, int nend, int nstep, HashMap<String,String> significanceTables) throws Exception{
		// produce ranked lists from a table
		String folder = (new File(fn)).getParentFile().getAbsolutePath();
		
		VDataTable tab = new VDataTable();
		if(fn.endsWith(".rnk")){
			tab = VDatReadWrite.LoadFromSimpleDatFile(fn, false, "\t");
		}else{
			tab = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		}
		
		for(int i=1;i<tab.colCount;i++){
			String field = tab.fieldNames[i];
			FileWriter fw_minus_rnk = new FileWriter(folder+"/"+name+"_"+field+".rnk");
			FileWriter fw_plus = new FileWriter(folder+"/"+name+"_"+field+"_plus");
			FileWriter fw_minus = new FileWriter(folder+"/"+name+"_"+field+"_minus");
			FileWriter fw_abs = new FileWriter(folder+"/"+name+"_"+field+"_abs");
			float vals[] = new float[tab.rowCount];
			float val_abs[] = new float[tab.rowCount];
			for(int j=0;j<tab.rowCount;j++){ 
				float f = Float.parseFloat(tab.stringTable[j][i]); 
				vals[j] = f;
				val_abs[j] = Math.abs(f);
			}
			int inds[] = Algorithms.SortMass(vals);
			int ind_abs[] = Algorithms.SortMass(val_abs);
			for(int j=0;j<inds.length;j++){
				/*fw_plus.write(tab.stringTable[inds[inds.length-1-j]][0]+"\t"+vals[inds[inds.length-1-j]]+"\n");
				fw_minus.write(tab.stringTable[inds[j]][0]+"\t"+vals[inds[j]]+"\n");
				fw_abs.write(tab.stringTable[ind_abs[inds.length-1-j]][0]+"\t"+val_abs[ind_abs[inds.length-1-j]]+"\n");*/
				fw_plus.write(tab.stringTable[inds[inds.length-1-j]][0]+"\n");
				fw_minus.write(tab.stringTable[inds[j]][0]+"\n");
				fw_abs.write(tab.stringTable[ind_abs[inds.length-1-j]][0]+"\n");				
			}
			
			HashMap<String, Float> values = new HashMap<String, Float>(); 
			for(int j=0;j<tab.rowCount;j++){
				String nm = tab.stringTable[j][0];
				float f = Float.parseFloat(tab.stringTable[j][i]);
				Float fv =values.get(nm);
				if(fv==null)
					values.put(nm, f);
				else{
					if(Math.abs(f)>Math.abs(fv)){
						values.put(nm, f);
					}
				}
			}
			float vls[] = new float[values.keySet().size()];
			String nms[] = new String[values.keySet().size()];
			int k=0;
			for(String s: values.keySet()){
				nms[k] = s;
				vls[k] = values.get(s);
				k++;
			}
			inds = Algorithms.SortMass(vls);
			for(int j=0;j<inds.length;j++){
				fw_minus_rnk.write(nms[inds[j]]+"\t"+vls[inds[j]]+"\n");
			}
			
			fw_plus.close();fw_minus.close();fw_abs.close(); fw_minus_rnk.close();
		}
		// make often for all lists
		//int nstart = 300;
		//int nend = 1000;
		int step = nstep;	
		int valuesToTest[] = new int[(int)((nend-nstart)/step)+1];
		int k = 0;
		for(int i=nstart;i<=nend;i+=step)
			valuesToTest[k++] = i;
		Vector<String> names = new Vector<String>();
		Vector<Float> scores_plus = new Vector<Float>();
		Vector<Float> scores_minus = new Vector<Float>();
		Vector<Float> scores_abs = new Vector<Float>();
		Vector<Float> pval_plus = new Vector<Float>();
		Vector<Float> pval_minus = new Vector<Float>();
		Vector<Float> pval_abs = new Vector<Float>();
		Vector<Integer> sizes_plus = new Vector<Integer>();
		Vector<Integer> sizes_minus = new Vector<Integer>();
		Vector<Integer> sizes_abs = new Vector<Integer>();
		Vector<Integer> genes_plus = new Vector<Integer>();
		Vector<Integer> genes_minus = new Vector<Integer>();
		Vector<Integer> genes_abs = new Vector<Integer>();
		for(int i=1;i<tab.colCount;i++){ 
			String field = tab.fieldNames[i];
			System.out.println("============================");
			System.out.println("=====  FIELD "+field+"   ==========");
			System.out.println("============================");
			names.add(field); 
			for(k=0;k<3;k++){
			String suff = "_abs";
			if(k==1) suff = "_plus";
			if(k==2) suff = "_minus";
			String filename = folder+"/"+name+"_"+field+suff;
			loadRandkedGeneList(filename);
			String significanceTable = makeOFTENAnalysis(valuesToTest, false);
			if(significanceTables!=null)
				significanceTables.put(field+suff, significanceTable);
			if(k==0) { scores_abs.add(optimalScore); sizes_abs.add(optimalComponentSize); genes_abs.add(optimalNumberOfGenes); pval_abs.add(pvalue); }
			if(k==1) { scores_plus.add(optimalScore); sizes_plus.add(optimalComponentSize); genes_plus.add(optimalNumberOfGenes); pval_plus.add(pvalue); }
			if(k==2) { scores_minus.add(optimalScore); sizes_minus.add(optimalComponentSize); genes_minus.add(optimalNumberOfGenes); pval_minus.add(pvalue); }
			extractedNetwork.name = field+suff;
			XGMML.saveToXGMML(extractedNetwork, filename+".xgmml");
		}
		}

		String report = "";
		System.out.println("=====================================");
		String s = "NAME\tPLUS_GENES\tPLUS_N\tPLUS_SC\tPLUS_PVAL\tMINUS_GENES\tMINUS_N\tMINUS_SC\tMINUS_PVAL\tABS_GENES\tABS_N\tABS_SC\tABS_PVAL";
		System.out.println(s);
		report+=s+"\n";
		for(int i=0;i<names.size();i++){
			s = names.get(i)+"\t"+genes_plus.get(i)+"\t"+sizes_plus.get(i)+"\t"+scores_plus.get(i)+"\t"+pval_plus.get(i)+"\t"+genes_minus.get(i)+"\t"+sizes_minus.get(i)+"\t"+scores_minus.get(i)+"\t"+pval_minus.get(i)+"\t"+genes_abs.get(i)+"\t"+sizes_abs.get(i)+"\t"+scores_abs.get(i)+"\t"+pval_abs.get(i);
			System.out.println(s);
			report+=s+"\n";
		}
		
		return report;
		
	}
	
	public String makeOFTENAnalysis(int valuesToTest[]) throws Exception{
		return makeOFTENAnalysis(valuesToTest, true);
	}
	
	public String makeOFTENAnalysis(int valuesToTest[], boolean verbose) throws Exception{
		snp = new SubnetworkProperties();
		snp.modeOfSubNetworkConstruction = snp.SIMPLY_CONNECT;
		
		//fr.curie.BiNoM.pathways.utils.Utils.saveStringListToFile(this.rankedGeneList, fileName+"_");
		
		snp.network = PPINetwork;
    	String reportSizeSignificance = snp.calcSignificanceVsNumberOfGenes(PPINetwork, rankedGeneList, numberOfPermutationsForSizeTest, valuesToTest, false);
	
    	VDataTable tb = new VDataTable();
    	tb = VDatReadWrite.LoadFromSimpleDatFileString(reportSizeSignificance, true, "\t");
    	optimalNumberOfGenes = -1;
    	optimalScore = -1f;
    	pvalue = 1f;
    	for(int i=0; i<tb.rowCount;i++){
    		int ngenes = Integer.parseInt(tb.stringTable[i][tb.fieldNumByName("NGENES")]);
    		float score = Float.parseFloat(tb.stringTable[i][tb.fieldNumByName("SCORE")]);
    		int largestconncomp = Integer.parseInt(tb.stringTable[i][tb.fieldNumByName("LARGESTCOMPONENT")]);
    		float pval = Float.parseFloat(tb.stringTable[i][tb.fieldNumByName("SIGNIFICANCE")]);
    		if(score>optimalScore){
    			optimalScore = score;
    			optimalNumberOfGenes = ngenes;
    			optimalComponentSize = largestconncomp;
    			pvalue = pval;
    		}
    	}
    	if(verbose)
    		System.out.println("Optimal number of genes for OFTEN ("+fileName+") = "+optimalNumberOfGenes);
    	extractSubnetwork(optimalNumberOfGenes);
    	
		int components[][] = snp.calcDistributionOfConnectedComponentSizes(extractedNetwork);
		if(verbose){
		System.out.print("Distribution of connected components: ");
		for(int i=0;i<components.length;i++){
			System.out.print(components[i][0]+":"+components[i][1]+"\t");
		}
		}
		sizeOfComponentToSelect = (int)(thresholdSelectionConnectedComponents*components[components.length-1][0]);
		if(verbose)
			System.out.println("sizeOfComponentToSelect = "+sizeOfComponentToSelect);
		Vector<Graph> comps = GraphAlgorithms.ConnectedComponents(extractedNetwork);
		extractedNetwork = new Graph();
		for(int i=0;i<comps.size();i++){
			if(comps.get(i).Nodes.size()>=sizeOfComponentToSelect)
				extractedNetwork.addNodes(comps.get(i));
		}
		extractedNetwork.addConnections(PPINetwork);
		
		return reportSizeSignificance;
	}
	
	public void loadPPINetwork(String pathToPPINetwork) throws Exception{
		GraphXGMMLParser parser = new GraphXGMMLParser();
		parser.parse(pathToPPINetwork);
		PPINetwork = parser.graph;
	}
	
	public void loadRandkedGeneList(String fileName){
		rankedGeneList = new Vector<String>();
		Vector<String> list = Utils.loadStringListFromFile(fileName);
		for(int i=0;i<list.size();i++){
			String geneName = list.get(i);
			StringTokenizer st = new StringTokenizer(geneName,"\t /");
			while(st.hasMoreTokens()){
				String s = st.nextToken();
				if(!rankedGeneList.contains(s))
					rankedGeneList.add(s);
			}
		}
	}
	
	public void extractSubnetwork(int ngenes){
		PPINetwork.selectedIds.clear();
		for(int i=0;i<ngenes;i++){
			PPINetwork.selectedIds.add(rankedGeneList.get(i));
		}
		extractedNetwork = PPINetwork.getSelectedNodes();
	}
	
	public void makeMetaOFTENFromRandkedGeneListFiles(Vector<String> listOfFileNames, int valuesToTest[]) throws Exception{
		Vector<Graph> graphs = new Vector<Graph>();
		for(int i=0;i<listOfFileNames.size();i++){
			File f = new File(listOfFileNames.get(i)+".xgmml");
			Graph graph = null;
			if(!f.exists()){
				OFTENAnalysis often = new OFTENAnalysis();
				often.PPINetwork = PPINetwork;
				often.loadRandkedGeneList(listOfFileNames.get(i));
				often.fileName = listOfFileNames.get(i);
				often.makeOFTENAnalysis(valuesToTest);
				graph = often.extractedNetwork;
				StringTokenizer st = new StringTokenizer(often.fileName,"/");
				while(st.hasMoreTokens())
					graph.name = st.nextToken();
				XGMML.saveToXGMML(often.extractedNetwork, listOfFileNames.get(i)+".xgmml");
			}else{
				GraphXGMMLParser parser = new GraphXGMMLParser();
				parser.parse(listOfFileNames.get(i)+".xgmml");
				graph = parser.graph;;
			}
			graphs.add(graph);
		}
		extractedNetwork = intersectGraphs(graphs);
	}
	
	public Graph intersectGraphs(Vector<Graph> graphs){
	    Graph mergedGraph = logic.Utils.MergeNetworkAndFilter(graphs, thresholdIntersectionOfGraphs);
		return mergedGraph;
	}
	
	public static void MakeNetworkIntersectionGraph(String folder, String network_files[], String prefixes[], float score_threshold, float inters_threshold, String outfilename) throws Exception{
		Vector<String> fileNames = new Vector<String>();
		Vector<String> node_names = new Vector<String>();
		FileWriter fwn = new FileWriter(outfilename.substring(0,outfilename.length()-4)+"_nodes.txt");
		fwn.write("NODE\tCONN_COMP_SCORE\n");
		for(int k=0;k<network_files.length;k++){
			VDataTable tab = new VDataTable();
			Graph mergedGraph = new Graph();
			tab = VDatReadWrite.LoadFromSimpleDatFile(folder+network_files[k], true, "\t");
			for(int i=0;i<tab.rowCount;i++){
				float scplus = Float.parseFloat(tab.stringTable[i][tab.fieldNumByName("PLUS_SC")]);
				float scminus = Float.parseFloat(tab.stringTable[i][tab.fieldNumByName("MINUS_SC")]);
				float scabs = Float.parseFloat(tab.stringTable[i][tab.fieldNumByName("ABS_SC")]);
				String suffix = "";
				float f = Math.max(Math.max(scabs, scminus), scplus);
				if((scplus>scminus)&&(scplus>scabs)) suffix = "plus";
				if((scminus>scplus)&&(scminus>scabs)) suffix = "minus";
				if((scabs>scplus)&&(scabs>scminus)) suffix = "abs";
				String name = tab.stringTable[i][tab.fieldNumByName("NAME")];
				fwn.write(prefixes[k]+"_"+name+"\t"+f+"\n");
				if(f>score_threshold){
					String fname = prefixes[k]+"_"+name+"_"+suffix+".xgmml";
					fileNames.add(fname);
					node_names.add(prefixes[k]+"_"+name);
					
					Graph g = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(folder+fname));
					mergedGraph.addNodes(g);
					mergedGraph.addEdges(g);
				}
			}
			String s = network_files[k];
			if(s.endsWith(".txt")) s = s.substring(0, s.length()-4); 
			XGMML.saveToXGMML(mergedGraph, folder+s+"_merged.xgmml");
		}
		fwn.close();
		FileWriter fw = new FileWriter(outfilename);
		for(int i=0;i<fileNames.size();i++)for(int j=i+1;j<fileNames.size();j++){
			String fni = folder+fileNames.get(i);
			String fnj = folder+fileNames.get(j);
			System.out.println(fni+"\t"+fnj);			
			Graph gi = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(fni));
			Graph gj = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(fnj));
			Vector ni = new Vector();
			Vector nj = new Vector();
			for(Node n: gi.Nodes) ni.add(n.Id);
			for(Node n: gj.Nodes) nj.add(n.Id);
			int inters = logic.Utils.compareTwoSets(ni, nj);
			int union = (logic.Utils.UnionOfVectors(ni,nj)).size();
			float conn = (float)inters/(float)union;
			if(conn>inters_threshold)
				fw.write(node_names.get(i)+"\t"+node_names.get(j)+"\t"+conn+"\n");
		}
		fw.close();
	}
	
	public static void makeGSEABatchFile(String folder, String gmtFile, String outfolder) throws Exception{
		File dir = new File(folder);
		File files[] = dir.listFiles();
		FileWriter fw = new FileWriter(folder+"GSEA.bat"); 
		FileWriter fwsh = new FileWriter(folder+"GSEA.sh");
		for(File f: files){
			String fileName = f.getAbsolutePath();
			if(fileName.endsWith(".rnk")){
				String prefix = f.getName();
				String fname = f.getName();
				prefix = prefix.substring(0, prefix.length()-4);
				String command = "java -cp .;gsea2-2.0.14.jar -Xmx3000m xtools.gsea.GseaPreranked -gmx "+gmtFile+" -collapse false -mode Max_probe -norm meandiv -nperm 1000 -rnk "+fileName+" -scoring_scheme classic -rpt_label "+prefix+" -include_only_symbols true -make_sets true -plot_top_x 200 -rnd_seed timestamp -set_max 200 -set_min 8 -zip_report false -out "+outfolder+prefix+" -gui false";
				String commandsh = "java -cp .:gsea2-2.0.14.jar -Xmx3000m xtools.gsea.GseaPreranked -gmx "+gmtFile+" -collapse false -mode Max_probe -norm meandiv -nperm 1000 -rnk "+fname+" -scoring_scheme classic -rpt_label "+prefix+" -include_only_symbols true -make_sets true -plot_top_x 200 -rnd_seed timestamp -set_max 200 -set_min 8 -zip_report false -out "+outfolder+prefix+" -gui false";
				fw.write(command+"\n");
				fwsh.write(command+"\n");
			}
		}
		fw.close();
		fwsh.close();
	}
	
	public static void extractScoresFromOFTENReports(String folder) throws Exception{
		File files[] = (new File(folder)).listFiles();
		System.out.println("========================================");
		System.out.println("LABEL\tSCORE\tPVAL\tNGENES\tN\tTYPE");
		for(int i=0;i<files.length;i++){
			String fn = files[i].getName();
			if(fn.startsWith("report_"))
				if(fn.endsWith(".txt")){
					String label = fn.substring(7, fn.length()-4);
					LineNumberReader lr = new LineNumberReader(new FileReader(files[i]));
					String s = null;
					String table = null;
					while((s=lr.readLine())!=null){
						if(s.startsWith("NAME\tPLUS_GENES")){
							table = s+"\n";
							table+=lr.readLine()+"\n";
						}
					}
					VDataTable tab = new VDataTable();
					tab = VDatReadWrite.LoadFromSimpleDatFileString(table,true,"\t");
					float plus_sc = Float.parseFloat(tab.stringTable[0][tab.fieldNumByName("PLUS_SC")]);
					float minus_sc = Float.parseFloat(tab.stringTable[0][tab.fieldNumByName("MINUS_SC")]);
					float abs_sc = Float.parseFloat(tab.stringTable[0][tab.fieldNumByName("ABS_SC")]);
					float score = 0f;
					float pval = 1f;
					int numgenes = 0;
					int networksize = 0;
					String tp = null;
					if(plus_sc>Math.max(minus_sc, abs_sc)){
						score = plus_sc;
						pval = Float.parseFloat(tab.stringTable[0][tab.fieldNumByName("PLUS_PVAL")]);
						numgenes = Integer.parseInt(tab.stringTable[0][tab.fieldNumByName("PLUS_GENES")]);
						networksize = Integer.parseInt(tab.stringTable[0][tab.fieldNumByName("PLUS_N")]);
						tp = "PLUS";
					}
					if(minus_sc>Math.max(plus_sc, abs_sc)){
						score = minus_sc;
						pval = Float.parseFloat(tab.stringTable[0][tab.fieldNumByName("MINUS_PVAL")]);
						numgenes = Integer.parseInt(tab.stringTable[0][tab.fieldNumByName("MINUS_GENES")]);
						networksize = Integer.parseInt(tab.stringTable[0][tab.fieldNumByName("MINUS_N")]);
						tp = "MINUS";
					}
					if(abs_sc>Math.max(plus_sc, minus_sc)){
						score = abs_sc;
						pval = Float.parseFloat(tab.stringTable[0][tab.fieldNumByName("ABS_PVAL")]);
						numgenes = Integer.parseInt(tab.stringTable[0][tab.fieldNumByName("ABS_GENES")]);
						networksize = Integer.parseInt(tab.stringTable[0][tab.fieldNumByName("ABS_N")]);
						tp = "ABS";
					}
					System.out.println(label+"\t"+score+"\t"+pval+"\t"+numgenes+"\t"+networksize+"\t"+tp);
				}
		}
	}
	
	public static String extractScoresFromCombinedOFTENReport(String reportString) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFileString(reportString, true, "\t");
		
		String reportSummary = "";
		
		System.out.println("========================================");
		String s = "LABEL\tSCORE\tPVAL\tNGENES\tN\tTYPE";
		System.out.println(s); reportSummary+=s+"\n"; 
		for(int i=0;i<vt.rowCount;i++){
					String label = vt.stringTable[i][0];
					float plus_sc = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("PLUS_SC")]);
					float minus_sc = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("MINUS_SC")]);
					float abs_sc = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("ABS_SC")]);
					float score = 0f;
					float pval = 1f;
					int numgenes = 0;
					int networksize = 0;
					String tp = null;
					if(plus_sc>Math.max(minus_sc, abs_sc)){
						score = plus_sc;
						pval = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("PLUS_PVAL")]);
						numgenes = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("PLUS_GENES")]);
						networksize = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("PLUS_N")]);
						tp = "PLUS";
					}
					if(minus_sc>Math.max(plus_sc, abs_sc)){
						score = minus_sc;
						pval = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("MINUS_PVAL")]);
						numgenes = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("MINUS_GENES")]);
						networksize = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("MINUS_N")]);
						tp = "MINUS";
					}
					if(abs_sc>Math.max(plus_sc, minus_sc)){
						score = abs_sc;
						pval = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("ABS_PVAL")]);
						numgenes = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("ABS_GENES")]);
						networksize = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("ABS_N")]);
						tp = "ABS";
					}
					s = label+"\t"+score+"\t"+pval+"\t"+numgenes+"\t"+networksize+"\t"+tp;
					System.out.println(s); reportSummary+=s+"\n"; 
		}
		return reportSummary;
	}	
	
	public static void makeGMTfromSetOfXGMMLfiles(String folder) throws Exception{
		File f[] = new File(folder).listFiles();
		FileWriter fw = new FileWriter(folder+"result.gmt");
		for(int i=0;i<f.length;i++)if(!f[i].isDirectory())if(f[i].getName().endsWith("xgmml")){
			Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(f[i].getAbsolutePath()));
			String fn = f[i].getName();
			fn = fn.substring(0, fn.length()-6);
			fw.write(fn+"\tna\t");
			for(int j=0;j<gr.Nodes.size();j++)
				fw.write(gr.Nodes.get(j).Id+"\t");
			fw.write("\n");
		}
		fw.close();
	}

}
