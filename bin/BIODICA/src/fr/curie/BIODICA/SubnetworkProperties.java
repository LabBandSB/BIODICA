package fr.curie.BIODICA;

import vdaoengine.data.io.*;
import vdaoengine.data.*;
import java.util.*;
import java.io.*;

public class SubnetworkProperties {
	
	public Graph network = null;
	
	public HashMap<String,Vector<String>> complexMap = new HashMap<String,Vector<String>>(); 
	public HashMap<String,Vector<String>> proteinComplexMap = new HashMap<String,Vector<String>>();

	public HashMap<String,Integer> SubnetworkComplexProfile = new HashMap<String,Integer>(); 
	
	public HashMap<Integer,Vector<Node>> degreeDistribution = new HashMap<Integer,Vector<Node>>();
	public Vector<Integer> degrees = new Vector<Integer>();
	public Vector<Node> listOfNodesInitial = new Vector<Node>(); 
	public Graph subnetwork = null;
	public Vector<Graph> sampling = new Vector<Graph>();
	
	public double subnetworkDistanceMatrix[][] = null;
	public int networkDistanceMatrix[][] = null;
	
	public boolean addOnlyConnectingNeighbours = true;
	public String path = null;
	
	public int[][] distributionOfConnectedComponentSizes = null;
	public float[] significanceOfConnectedComponents = null;
	public float averageSizeOfRandomBiggestComponent = 0f;
	
	public static int SIMPLY_CONNECT = 0;
	public static int SIMPLY_CONNECT_WITH_COMPLEX_NODES = 1;
	public static int SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS = 2;
	public static int ADD_FIRST_NEIGHBOURS = 3;
	public static int CONNECT_BY_SHORTEST_PATHS = 4;
	
	public static int SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS_DIRECTED = 5;
	public static int SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS_DIRECTED_UPSTREAM = 6;
	public static int SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS_DIRECTED_DOWNSTREAM = 7;
	
	public int modeOfSubNetworkConstruction = ADD_FIRST_NEIGHBOURS;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			int numberOfSamples = 100;
			
			/*SubnetworkProperties snp = new SubnetworkProperties();
			snp.path = "c:/datas/binomtest/";
			snp.loadNetwork(snp.path+"M-Phase2.xgmml");
			Vector<String> lst = new Vector<String>();
			lst.add("Cdc2");
			lst.add("Cdc13");
			lst.add("Cdc13_Cdc2");
			snp.selectNodesFromList(lst);
			snp.network.assignEdgeIds();
			
			snp.addFirstNeighbours(snp.subnetwork, snp.network, true, SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS_DIRECTED_DOWNSTREAM);
			XGMML.saveToXGMML(snp.subnetwork, snp.path+"M-phase2_sel.xgmml");
			System.exit(0);*/
			
			
			
			//String path = "c:/datas/hprd9/";
			/*VDataTable vt2 = VDatReadWrite.LoadFromSimpleDatFile(path+"BINARY_PROTEIN_PROTEIN_INTERACTIONS.txt", true, "\t");
			for(int i=0;i<vt2.rowCount;i++){
				if(vt2.stringTable[i][vt2.fieldNumByName("SOURCE_NAME")].equals("-"))
					vt2.stringTable[i][vt2.fieldNumByName("SOURCE_NAME")] = vt2.stringTable[i][vt2.fieldNumByName("SOURCE_NP")];
				if(vt2.stringTable[i][vt2.fieldNumByName("SOURCE_NAME")].equals("-"))				
					vt2.stringTable[i][vt2.fieldNumByName("SOURCE_NAME")] = "HPRD_"+vt2.stringTable[i][vt2.fieldNumByName("SOURCE_ID")];					
				if(vt2.stringTable[i][vt2.fieldNumByName("TARGET_NAME")].equals("-"))
					vt2.stringTable[i][vt2.fieldNumByName("TARGET_NAME")] = vt2.stringTable[i][vt2.fieldNumByName("TARGET_NP")];
				if(vt2.stringTable[i][vt2.fieldNumByName("TARGET_NAME")].equals("-"))
					vt2.stringTable[i][vt2.fieldNumByName("TARGET_NAME")] = "HPRD_"+vt2.stringTable[i][vt2.fieldNumByName("TARGET_ID")];
			}
			VDatReadWrite.saveToSimpleDatFile(vt2, path+"HPRD_PPI.txt");*/
			/*VDataTable vt2 = VDatReadWrite.LoadFromSimpleDatFile(path+"PROTEIN_COMPLEXES.txt", true, "\t");
			for(int i=0;i<vt2.rowCount;i++){
				if(vt2.stringTable[i][vt2.fieldNumByName("TARGET_NAME")].equals("-"))
					vt2.stringTable[i][vt2.fieldNumByName("TARGET_NAME")] = vt2.stringTable[i][vt2.fieldNumByName("TARGET_NP")];
			}
			VDatReadWrite.saveToSimpleDatFile(vt2, path+"HPRD_PC.txt");
			System.exit(0);*/
			
			
			SubnetworkProperties SP = new SubnetworkProperties();
			//SP.path = "c:/datas/HPRD9/";
			SP.path = "c:/datas/biogrid/";
			//SP.path = "C:/Datas/SyntheticInteractions/Constanzo2010/";
			//SP.path = "C:/Datas/SERVIER/PPIanalysis/";
			//SP.loadNetwork(SP.path+"hprd_pc_ppi.xgmml");
			//XGMML.saveToXGMML(SP.network, SP.path+"hprd_pc_ppi1.xgmml");
			//SP.loadNetwork(SP.path+"hprd9.xgmml");
			//SP.loadNetwork(SP.path+"hprd8_.xgmml");
			//SP.loadNetwork(SP.path+"hprd9.xgmml");
			//SP.loadNetwork(SP.path+"sgadata_costanzo2009_stringentCutoff_101120.txt.xgmml");
			SP.loadNetwork(SP.path+"biogrid.xgmml");
			System.out.println("Loaded");
			//SP.loadNetwork(SP.path+"prolif_hits.xgmml");
			//SP.loadNetwork(SP.path+"hprd.xgmml");
			//SP.loadNetwork(SP.path+"test2.xgmml");
			//Utils.CorrectCytoscapeEdgeIds(SP.network);
			removeSelfInteractions(SP.network);
			System.out.println("Loaded network: "+SP.network.Nodes.size()+" nodes, "+SP.network.Edges.size()+" edges");
			XGMML.saveToXGMML(SP.network, SP.path+"biogrid_noselfinteractions.xgmml");
			calcDegreeDistribution(SP.network, SP.degreeDistribution, SP.degrees, true);			
			System.exit(0);
			
			/*VDataTable vtnom = VDatReadWrite.LoadFromSimpleDatFile(SP.path+"PROTEIN_NOMENCLATURE.txt",true,"\t");
			for(int i=0;i<vtnom.rowCount;i++){
				String name = vtnom.stringTable[i][vtnom.fieldNumByName("NAME")];
				if(name.equals("-"))
					name = vtnom.stringTable[i][vtnom.fieldNumByName("REFSEQ")];
				if(SP.network.getNode(name)==null){
					System.out.println(name);
					SP.network.getCreateNode(name);
				}
			}
			XGMML.saveToXGMML(SP.network, SP.path+"hprd8_.xgmml");*/
			
			//SP.loadNetwork(path+"sysewing.xgmml");
			//SP.loadNetwork(path+"prolif_hits.xgmml");
			//SP.loadNetwork(path+"comb_screen_subnetwork.xgmml");
			//SP.loadNetwork(path+"test1.xgmml");
			//SP.loadNetwork(path+"ewing.xgmml");
			//SP.loadNetwork(path+"sysewing.xgmml");
			//SP.loadNetwork(path+"ewing_network.xgmml");
			/*System.setErr(System.out);*/
			
			/*double centr[] = calcNodeBetweenness(SP.network);
			System.out.println("NODE\tCONNECTIVITY\tBETWEENNESS");
			SP.network.calcNodesInOut();
			for(int i=0;i<centr.length;i++)
				System.out.println(SP.network.Nodes.get(i).Id+"\t"+(SP.network.Nodes.get(i).incomingEdges.size()+SP.network.Nodes.get(i).outcomingEdges.size())+"\t"+centr[i]);
			/*for(int i=0;i<centr.length;i++){
				Node n = SP.network.Nodes.get(i);
				n.Attributes.add(new Attribute("BETWEENNESS",""+centr[i]));
			}
			XGMML.saveToXGMML(SP.network, path+"out_betweenness.xgmml");*/
			//System.exit(0);
			
			
			/*VDataTable vtnf = VDatReadWrite.LoadFromSimpleDatFile(SP.path+"prolif_screen_notfound.txt",true,"\t");
			VDataTable vtnom = VDatReadWrite.LoadFromSimpleDatFile(SP.path+"PROTEIN_NOMENCLATURE.txt",true,"\t");
			for(int i=0;i<vtnom.rowCount;i++){
				String s = vtnom.stringTable[i][vtnom.fieldNumByName("REFSEQ")];
				if(s==null)
					System.out.println("Row "+i+", REFSEQ is null");
				if(s.indexOf(".")>=0)
					s = s.substring(0, s.indexOf("."));
				vtnom.stringTable[i][vtnom.fieldNumByName("REFSEQ")] = s;
			}
			vtnom.makePrimaryHash("REFSEQ");
			VDataTable vtnom_hugo = VDatReadWrite.LoadFromSimpleDatFile(SP.path+"PROTEIN_NOMENCLATURE.txt",true,"\t");
			vtnom_hugo.makePrimaryHash("NAME");
			vtnf.addNewColumn("NP_FOUND_IN_NOMENCLATURE", "", "", vtnf.STRING, "_");
			vtnf.addNewColumn("NP_FOUND_IN_NETWORK", "", "", vtnf.STRING, "_");
			vtnf.addNewColumn("HUGO_FOUND_IN_NOMENCLATURE", "", "", vtnf.STRING, "_");
			vtnf.addNewColumn("HUGO_FOUND_IN_NETWORK", "", "", vtnf.STRING, "_");
			for(int i=0;i<vtnf.rowCount;i++){
				String hugo = vtnf.stringTable[i][vtnf.fieldNumByName("GENE")];
				String np = vtnf.stringTable[i][vtnf.fieldNumByName("NP")];
				if(vtnom.tableHashPrimary.containsKey(np))
					vtnf.stringTable[i][vtnf.fieldNumByName("NP_FOUND_IN_NOMENCLATURE")] = "found";
				if(vtnom_hugo.tableHashPrimary.containsKey(hugo))
					vtnf.stringTable[i][vtnf.fieldNumByName("HUGO_FOUND_IN_NOMENCLATURE")] = "found";
				for(int j=0;j<SP.network.Nodes.size();j++){
					String s = SP.network.Nodes.get(j).Id;
					if(s.indexOf(".")>=0)
						s = s.substring(0, s.indexOf("."));
					if(s.equals(np)) vtnf.stringTable[i][vtnf.fieldNumByName("NP_FOUND_IN_NETWORK")] = "found";
					if(s.equals(hugo)) vtnf.stringTable[i][vtnf.fieldNumByName("HUGO_FOUND_IN_NETWORK")] = "found"; 
				}
				Vector ids = vtnom.tableHashPrimary.get(np);
				if(ids!=null)
					for(int j=0;j<ids.size();j++){
						String hugo_found = vtnom.stringTable[(Integer)ids.get(j)][vtnom.fieldNumByName("NAME")];
						System.out.println("For NP="+np+" HUGO="+hugo_found);
						if(SP.network.getNode(hugo)!=null)
							vtnf.stringTable[i][vtnf.fieldNumByName("HUGO_FOUND_IN_NETWORK")] = "found";
					}
			}
			VDatReadWrite.saveToSimpleDatFile(vtnf, SP.path+"prolif_screen_notfound.xls");
			System.exit(0);*/
			
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(SP.path+"PROTEIN_NOMENCLATURE.txt", true, "\t");
			for(int i=0;i<vt.rowCount;i++){
				String name = vt.stringTable[i][vt.fieldNumByName("NAME")];
				if(vt.stringTable[i][vt.fieldNumByName("NAME")]!=null){
				if(vt.stringTable[i][vt.fieldNumByName("NAME")].equals("-"))
					vt.stringTable[i][vt.fieldNumByName("NAME")] = vt.stringTable[i][vt.fieldNumByName("REFSEQ")];
				if(vt.stringTable[i][vt.fieldNumByName("NAME")].equals("-"))				
					vt.stringTable[i][vt.fieldNumByName("NAME")] = "HPRD_"+vt.stringTable[i][vt.fieldNumByName("ID")];
				if(!name.equals(vt.stringTable[i][vt.fieldNumByName("NAME")]))
					System.out.println(vt.stringTable[i][vt.fieldNumByName("ID")]+" name changed for "+vt.stringTable[i][vt.fieldNumByName("NAME")]);
				}else{
					System.out.println("There is a problem in line "+(i+1));
				}
			}
			VDatReadWrite.saveToSimpleDatFile(vt, SP.path+"PROTEIN_NOMENCLATURE_mod.txt");
			System.exit(0);*/			
			
			// Try weighted paths
			/*SP.assignEdgeWeightsByConnectivity(SP.network,false);		
			Node n1 = SP.network.getNode("NOX1");
			//Vector<String> list1 = Utils.loadStringListFromFile(path+"pathways/apoptosis_execution");
			Vector<Node> vt = new Vector<Node>();
			//for(int i=0;i<list1.size();i++) if(SP.network.getNode(list1.get(i))!=null)
			//	vt.add(SP.network.getNode(list1.get(i)));
			Node n2 = SP.network.getNode("RB1");
			Node n3 = SP.network.getNode("BIRC2");
			vt.add(n2); vt.add(n3);
			Vector<Graph> v = new Vector<Graph>();
			v = GraphAlgorithms.DijkstraAlgorithm(SP.network, n1, vt, false, Float.MAX_VALUE);
			if(v.size()>0){
				for(int i=0;i<v.size();i++)
					System.out.println("From "+v.get(i).Nodes.get(0).Id+" to "+v.get(i).Nodes.get(v.get(i).Nodes.size()-1).Id+", of "+(v.get(i).Nodes.size()-1)+" length");
			}
			XGMML.saveToXGMML(MergeGraphs(v), SP.path+"test.xgmml");
			
			System.exit(0);*/			
		

			//SP.network.calcNodesInOut();
			//System.out.println("CBX3 "+SP.network.getNode("CBX3").incomingEdges.size()+"\t"+SP.network.getNode("CBX3").outcomingEdges.size());
			
			//XGMML.saveToXGMML(SP.network, path+"hprd2.xgmml");
			
			Vector<String> pathways = new Vector<String>();

			/*pathways.add("rbpathway");
			pathways.add("ros");
			pathways.add("apoptosis_execution");
			pathways.add("apoptosis_inhibition");
			pathways.add("e2f1_targets");
			pathways.add("nfkb_targets");
			pathways.add("nfkb");
			pathways.add("pi3k");
			pathways.add("rtk");
			pathways.add("tor");*/
			//spathways.add("p_screen");
			//pathways.add("rb_test");
			//pathways.add("apoptosis_execution");
			//pathways.add("rbpathway")
			//pathways.add("comb_screen");
			//pathways.add("module_upgma_1");
			//pathways.add("module_upgma_2");
			//pathways.add("module_upgma_3");
			//pathways.add("module_upgma_4");
			//pathways.add("prolif_hits");
			//pathways.add("prolif_hits1");
			//pathways.add("sample1");
			//pathways.add("ewing");
			//pathways.add("sysewing");
			//pathways.add("comb_screen");
			//pathways.add("test1");
			//pathways.add("basnormal_717");
			//pathways.add("basnormal_717_mcc");
			//pathways.add("basluminala_448");
			//pathways.add("basluminala_448_mcc");
			//pathways.add("basnormal_717_mcc_module2");
			//pathways.add("basal_normal_full");
			//pathways.add("bn_400");
			//pathways.add("bl_400");
			//pathways.add("basal_luminal_full");
			//pathways.add("basal_her2_full");
			//pathways.add("bh2_400");
			//pathways.add("her2_normal.txt");
			//pathways.add("her2_luminal_full");
			//pathways.add("targets");
			pathways.add("as134");
			
			
			//SP.modeOfSubNetworkConstruction = SP.SIMPLY_CONNECT;
			//SP.modeOfSubNetworkConstruction = SP.SIMPLY_CONNECT_WITH_COMPLEX_NODES;
			SP.modeOfSubNetworkConstruction = SP.SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS;
			//SP.modeOfSubNetworkConstruction = SP.ADD_FIRST_NEIGHBOURS;
			//SP.modeOfSubNetworkConstruction = SP.CONNECT_BY_SHORTEST_PATHS;

			SP.readComplexes(SP.path+"HPRD_PC.txt",40);
			//SP.complexMap.remove("COM_2971");
			SP.addComplexesToNetworkAsClicks(); System.out.println("After adding complexes: "+SP.network.Nodes.size()+" nodes, "+SP.network.Edges.size()+" edges");			
			//SP.addComplexesToNetworksAsNodes(); System.out.println("After adding complexes: "+SP.network.Nodes.size()+" nodes, "+SP.network.Edges.size()+" edges");
			SP.removeDoubleEdges();				System.out.println("After removing double edges: "+SP.network.Nodes.size()+" nodes, "+SP.network.Edges.size()+" edges");
			calcDegreeDistribution(SP.network, SP.degreeDistribution, SP.degrees, true);			
			XGMML.saveToXGMML(SP.network, SP.path+"hprd9_pc_clicks.xgmml");
			
			//calcPercolationThreshold(SP.network);
			
			System.exit(0);
			
			/*Vector<String> fulllist = Utils.loadStringListFromFile(SP.path+"pathways/"+pathways.get(0));
			int nga[] = {100,150,155,160,165,170,175,180,185,190,195,200,250,260,270,280,290,300,310,320,330,340,350,360,370,380,390,400,420,440,460,480,500,520,540,560,580,600,620,640,660,680,700,1000};
			//int nga[] = {100,150,155,156,157,158,159,160,165,170,175,180,185,190,195,200,250};
			calcSignificanceVsNumberOfGenes(SP.network,fulllist,100,nga); 			
			System.exit(0);*/
			
			/*SP.subnetwork = SP.network;
			SP.connectByShortestPaths();
			SP.saveDistanceMatrix(SP.subnetworkDistanceMatrix,SP.path+"temp_spdm.txt",true);
			System.exit(0);*/
			
			for(int i=0;i<pathways.size();i++){
				String pth = pathways.get(i);
				System.out.println("Pathway : "+pth);				
				Vector<String> list = Utils.loadStringListFromFile(SP.path+"pathways/"+pth);
				
				SP.selectNodesFromList(list);
				
				
				// Make Compactness test
				/*Vector<Integer> indices = new Vector<Integer>();
				for(int k=0;k<list.size();k++){
					Node n = SP.network.getNode(list.get(k));
					System.out.print(list.get(k)+"\t");
					if(SP.network.Nodes.indexOf(n)==-1)
						System.out.println("not found");
					else{
						System.out.println(SP.network.Nodes.indexOf(n));
						indices.add(SP.network.Nodes.indexOf(n));
					}
				}*/
				Vector<String> fixedNodes = new Vector<String>(); 
				/*fixedNodes = Utils.loadStringListFromFile("c:/datas/aposys_gws/fixednodes");
				int p=0;
				while(p<fixedNodes.size()){
					if(SP.network.getNode(fixedNodes.get(p))==null){
						fixedNodes.remove(p);
						System.out.println("Fixed node "+fixedNodes.get(p)+" NOT FOUND in the network");
					}else
						p++;
				}*/
				
				//makeCompactnessTest(SP.path+"hprd8_spdm.full",indices,100,fixedNodes);				
				//SP.makeCompactnessTest(SP.path+"hprd8_spdm_complexes_nodes.full",indices,1000,true,SP.path+"prolif_hits_complexes",1);
				//SP.makeCompactnessTest(SP.path+"hprd8_spdm_complexes_nodes.full",indices,1000,true);
				//SP.makeCompactnessTest(SP.path+"hprd8_spdm_complexes_clicks.full",indices,10000,true,fixedNodes);
				//SP.makeCompactnessTest(SP.path+"hprd8_spdm.full",indices,10000,true,fixedNodes);
				//SP.makeCompactnessTest(SP.path+"hprd8_spdm_complexes_nodes.full",indices,10000,true,fixedNodes);
				//SP.makeCompactnessTest(SP.path+"hprd8_spdm.full",indices,10000,true,SP.path+"prolif_hits_complexes",5);
				//SP.makeCompactnessTest(SP.path+"hprd8_spdm.full",indices,1000,true,fixedNodes);
				//SP.makeCompactnessTest(SP.path+"hprd8_spdm_complexes_nodes.full",indices,1000,true);
				//System.exit(0);
				
				
				System.out.print("FOUND IN HRPD NOMENCLATURE BUT NOT IN THE INTERACTIONS:\t");
				VDataTable vnom = VDatReadWrite.LoadFromSimpleDatFile(SP.path+"PROTEIN_NOMENCLATURE.txt", true, "\t");
				vnom.makePrimaryHash("NAME");
				for(int kk=0;kk<list.size();kk++){
					String id = list.get(kk);
					if(vnom.tableHashPrimary.get(id)!=null)
						if(SP.subnetwork.getNode(id)==null){
							System.out.print(id+"\t");
							//SP.subnetwork.getCreateNode(id);
						}
				}
				System.out.println();
				System.out.print("NOT FOUND IN HRPD NOMENCLATURE:\t");
				for(int kk=0;kk<list.size();kk++){
					String id = list.get(kk);
					if(vnom.tableHashPrimary.get(id)==null)
							System.out.print(id+"\t");
				}
				System.out.println();
				
				//SP.subnetwork = SP.network;
				
				
				FileWriter fwt = new FileWriter(SP.path+pth+".names");
				for(int k=0;k<SP.subnetwork.Nodes.size();k++)
					fwt.write(SP.subnetwork.Nodes.get(k).Id+"\n");
				fwt.close();
				
				HashMap<Integer,Vector<Node>> dd = new HashMap<Integer,Vector<Node>>();
				Vector<Integer> ds = new Vector<Integer>();
				calcDegreeDistribution(SP.subnetwork, dd, ds, false);
				
				int numberOfNodesBefore = SP.subnetwork.Nodes.size();
				
				if(SP.modeOfSubNetworkConstruction == SP.SIMPLY_CONNECT){
					SP.subnetwork.addConnections(SP.network);
				}

				if(SP.modeOfSubNetworkConstruction == SP.SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS){
					SP.subnetwork.addConnections(SP.network);
					//SP.produceConnectionsSecondOrder(SP.subnetwork,SP.path+"hprd8_spdm_complexes_clicks.full");
					SP.produceConnectionsSecondOrder(SP.subnetwork,SP.path+"hprd8_spdm_complexes_clicks.full");
				}
				
				if(SP.modeOfSubNetworkConstruction == SP.SIMPLY_CONNECT_WITH_COMPLEX_NODES){
					System.out.println("In subnetwork: "+SP.subnetwork.Nodes.size()+" nodes, "+SP.subnetwork.Edges.size()+" edges");
					SP.subnetwork.addConnections(SP.network);
					SP.addComplexNodes();
					SP.makeComplexProfile(SP.path+pth+"_complexes");
				}
				
				if(SP.modeOfSubNetworkConstruction == SP.ADD_FIRST_NEIGHBOURS){
					SP.subnetwork.addConnections(SP.network);
					SP.addFirstNeighbours();
				}
				if(SP.modeOfSubNetworkConstruction == SP.CONNECT_BY_SHORTEST_PATHS){
					//SP.connectByShortestPaths();
					//SP.saveDistanceMatrix(SP.subnetworkDistanceMatrix,SP.path+pth+"_spdm.txt",true);
				}
				
				XGMML.saveToXGMML(SP.subnetwork, SP.path+pth+".xgmml");
				
				// Make connected component distribution test
				SP.calcSubnetworkComplexProfile();
				SP.makeTestOfConnectivity(1000, true, null, -1, fixedNodes);
				//SP.makeTestOfConnectivity(1000, true, null, -1);
				//SP.makeTestOfConnectivity(1000, true, SP.path+"prolif_hits_complexes.mem",3);
				System.exit(0);

				System.out.println("... added "+(SP.subnetwork.Nodes.size()-numberOfNodesBefore)+" nodes and "+SP.subnetwork.Edges.size()+" edges.");
				
				
				
				System.out.println("Table of degrees:");
				System.out.println("NODE\tSUBNETWORK\tGLOBAL\tRATIO\tINITIAL");
				for(int k=0;k<SP.subnetwork.Nodes.size();k++){
					Node n = SP.subnetwork.Nodes.get(k);
					SP.subnetwork.calcNodesInOut();
					int local = (n.incomingEdges.size()+n.outcomingEdges.size());
					SP.network.calcNodesInOut();
					int global = (n.incomingEdges.size()+n.outcomingEdges.size());
					if(global==0) global=1;
					String initial = "FALSE";
					if(list.indexOf(n.Id)>=0)
						initial = "TRUE";
					System.out.println(n.Id+"\t"+local+"\t"+global+"\t"+((float)(local)/global)+"\t"+initial);
				}
				
				SP.generateSampling(numberOfSamples);
				SP.saveSampling();
				float avCon = SP.getAverageConnectivity(SP.subnetwork);				
				float avCons[] = SP.getAverageConnectivities();
				float avConPValue = SP.getPValue(avCon, avCons, false);
				System.out.println("\tAverage connectivity ("+avCon+") P-value = "+avConPValue);
				
				SP.generateSamplingConserveDegreeDistribution(numberOfSamples);
				SP.saveSampling();
				avCon = SP.getAverageConnectivity(SP.subnetwork);				
				avCons = SP.getAverageConnectivities();
				avConPValue = SP.getPValue(avCon, avCons, false);
				System.out.println("\tAverage connectivity ("+avCon+"), dd preserved, P-value = "+avConPValue);
				
				if(SP.modeOfSubNetworkConstruction != SP.SIMPLY_CONNECT){
					float size = SP.subnetwork.Nodes.size();				
					float sizes[] = SP.getSamplingSizes();
					float sizePValue = SP.getPValue(size, sizes, true);
					System.out.println("\tSize ("+SP.subnetwork.Nodes.size()+") P-value = "+sizePValue);
					printMassif(sizes);
				}
				
				//printMassif(avCons);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadNetwork(String fn) throws Exception{
		System.out.print("Loading network from "+fn+"...");
		//GraphXGMMLParser pars = new GraphXGMMLParser();
		//pars.parse(fn);
		//network = pars.graph;
		network = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(fn));		
		System.out.println(network.Nodes.size()+" nodes and "+network.Edges.size()+" edges in network");		
		fr.curie.BIODICA.Utils.CorrectCytoscapeNodeIds(network);
		//for(int i=0;i<network.Nodes.size();i++)
		//	System.out.println(network.Nodes.get(i).Id);
		System.out.println("loaded.");
		network.calcNodesInOut();
		//System.out.println("Before: "+network.Edges.size());
		removeSelfInteractions(network);
		//System.out.println("After: "+network.Edges.size());
		calcDegreeDistribution(network, degreeDistribution, degrees, true);
		
		for(int i=0;i<network.Edges.size();i++){
			Edge e = network.Edges.get(i);
			if(e.Node1==null) System.out.println("Node1 not found for "+e.Id);
			if(e.Node2==null) System.out.println("Node2 not found for "+e.Id);
			//e.getFirstAttribute("SOURCE_ID").value = e.Node1.Id;
			//e.getFirstAttribute("TARGET_ID").value = e.Node2.Id;
			e.Id = e.EdgeLabel;
		}
		for(int i=0;i<network.Nodes.size();i++){
			Node n = network.Nodes.get(i);
			if(n.Id.startsWith("COM_")){
				n.Attributes.add(new Attribute("NODE_TYPE","COMPLEX"));
			}
		}
		
	}
	
	public static void removeSelfInteractions(Graph gr){
		int i = 0;
		while(i<gr.Edges.size()){
			Edge e = gr.Edges.get(i);
			if(e.Node1.Id.equals(e.Node2.Id)){
				//if(e.Node1.Id.equals("RB1"))
				//	System.out.println("Removing "+e.Node1.Id);
				gr.removeEdge(e.Id);
			}else i++;
		}
	}
	
	public static void calcDegreeDistribution(Graph network, HashMap<Integer,Vector<Node>> degreeDistribution, Vector<Integer> degrees, boolean printDistribution){
		degreeDistribution.clear();
		degrees.clear();
		network.calcNodesInOut();
		for(int i=0;i<network.Nodes.size();i++){
			Node n = network.Nodes.get(i);
			Vector<Node> v = degreeDistribution.get(n.incomingEdges.size()+n.outcomingEdges.size());
			if(v == null){ 
				v = new Vector<Node>(); 
				degreeDistribution.put(n.incomingEdges.size()+n.outcomingEdges.size(),v); 
			}
			v.add(n);
			//if(n.Id.equals("CBX3"))
			//	System.out.println("CBX3\t"+n.incomingEdges.size()+"\t"+n.outcomingEdges.size());
				
		}
		Iterator<Integer> it = degreeDistribution.keySet().iterator();
		while(it.hasNext()){
			int degree = it.next();
			degrees.add(degree);
		}
		Collections.sort(degrees);
		if(printDistribution)
			for(int i=0;i<degrees.size();i++){
				System.out.print(degrees.get(i)+"\t"+degreeDistribution.get(degrees.get(i)).size());
				//if(degreeDistribution.get(degrees.get(i)).size()<3)
					for(int k=0;k<degreeDistribution.get(degrees.get(i)).size();k++)
						System.out.print("\t"+degreeDistribution.get(degrees.get(i)).get(k).Id);
				System.out.println();
			}
	}
	
	public void selectNodesFromList(Vector<String> list){
		subnetwork = selectNodesFromList(network, list);
		System.out.println(subnetwork.Nodes.size()+" nodes from "+list.size()+" in the list are identified and selected.");
	}
	
	public static Graph selectNodesFromList(Graph graph, Vector<String> list){
		Graph sub = new Graph();
		Vector<String> notfound = new Vector<String>();
		for(int i=0;i<list.size();i++){
			String id = list.get(i);
			//System.out.print("Searching "+id+"...");
			Node n = graph.getNode(id);
			if(n!=null){
				sub.addNode(n);
				//System.out.println("found");
				Attribute att = new Attribute("NODE_TYPE","INITIAL");
				n.Attributes.add(att);
			}else{
				notfound.add(id);
			}
			//else
			//	System.out.println("NOT found");
		}
		if(notfound.size()>0){
			System.out.print("NOT FOUND\t");
		for(int i=0;i<notfound.size();i++)
			System.out.print(notfound.get(i)+"\t");
		System.out.println();
		}
		return sub;
	}
	
	public void generateSampling(int numberOfSamples){
		int size = subnetwork.Nodes.size();
		sampling.clear();
		Random r = new Random();
		System.out.print("Sampling:\t");
		for(int k=0;k<numberOfSamples;k++){
			if(k==(int)(0.1f*k)*10) 
				System.out.print(k+"\t");
			Graph gr = new Graph();
			for(int i=0;i<size;i++){
				int j = r.nextInt(network.Nodes.size());
				gr.addNode(network.Nodes.get(j));
				//gr.getCreateNode(network.Nodes.get(j).Id);
				//Node n = gr.getCreateNode(network.Nodes.get(j).Id);
				//n.Attributes.add(new Attribute("NODE_TYPE","SAMPLED"));
			}
			if(modeOfSubNetworkConstruction == ADD_FIRST_NEIGHBOURS){
				addFirstNeighbours(gr,network,addOnlyConnectingNeighbours);
			}
			gr.addConnections(network);
			sampling.add(gr);
		}
		System.out.println();
	}
	
	public void saveSampling() throws Exception{
		for(int i=0;i<sampling.size();i++){
			XGMML.saveToXGMML(sampling.get(i), path+"samples/sample"+(i+1)+".xgmml");
		}
	}
	
	public void generateSamplingConserveDegreeDistribution(int numberOfSamples){
		int size = subnetwork.Nodes.size();
		network.calcNodesInOut();
		sampling.clear();
		Random r = new Random();
		System.out.print("Sampling:\t");
		for(int k=0;k<numberOfSamples;k++){
			if(k==(int)(0.1f*k)*10) 
				System.out.print(k+"\t");
			Graph gr = new Graph();
			for(int i=0;i<size;i++){
				Node n = network.getNode(subnetwork.Nodes.get(i).Id);
				int degree = n.incomingEdges.size()+n.outcomingEdges.size();
				Vector<Node> v = degreeDistribution.get(degree);
				if(v==null){
					int mindev = Integer.MAX_VALUE;
					int im = -1;
					for(int l=0;l<degrees.size();l++){
						if(Math.abs(degrees.get(l)-degree)<mindev){
							mindev = Math.abs(degrees.get(l)-degree);
							im = l;
						}
					}
					v = degreeDistribution.get(degrees.get(im));
					//System.out.println("Did not found a node ("+n.Id+") with connectivity "+degree+" use "+degrees.get(im)+" instead");
				}
				int j = -1;
				while(j==-1){
					j = r.nextInt(v.size());
					if(gr.getNode(v.get(j).Id)!=null)
						j = -1;
				}
				gr.addNode(v.get(j));
			}
			if(modeOfSubNetworkConstruction == ADD_FIRST_NEIGHBOURS){
				addFirstNeighbours(gr,network,addOnlyConnectingNeighbours);
			}
			gr.addConnections(network);
			sampling.add(gr);
			// We check the degree distributions in the global network
			//System.out.println("Sample "+(k+1)+":");
			/*System.out.println(gr.Nodes.size());
			for(int i=0;i<gr.Nodes.size();i++){
				String s = gr.Nodes.get(i).Id;
				Node n = network.getNode(s);
				System.out.print((n.incomingEdges.size()+n.outcomingEdges.size())+"\t");
			}
			System.out.println();*/
		}
		System.out.println();
	}	
	
	public static float getPValue(float val, float samplings[], boolean toBeSmaller){
		float p = 0;
		for(int i=0;i<samplings.length;i++){
			if(toBeSmaller){
			   if(val>=samplings[i]) p+=1.0f;
			}
			else{
			   if(val<=samplings[i]) p+=1.0f;
			   //System.out.println(val+"\t"+samplings[i]+"\t"+p);
			}
		}
		return p/(float)samplings.length;
	}
	
	public static float getAverageConnectivity(Graph gr){
		/*gr.calcNodesInOut();
		float av = 0;
		for(int i=0;i<gr.Nodes.size();i++)
			av += (float)gr.Nodes.get(i).incomingEdges.size()+(float)gr.Nodes.get(i).outcomingEdges.size();
		av/=(float)gr.Nodes.size();
		System.out.println(av+"\t"+(float)gr.Edges.size()/(float)gr.Nodes.size()*(float)2);
		return av;*/
		return (float)gr.Edges.size()/(float)gr.Nodes.size()*(float)2;
	}
	
	public float[] getAverageConnectivities(){
		float avcons[] = new float[sampling.size()];
		for(int i=0;i<sampling.size();i++)
			avcons[i] = getAverageConnectivity(sampling.get(i));
		return avcons;
	}

	public float[] getSamplingSizes(){
		float vals[] = new float[sampling.size()];
		for(int i=0;i<sampling.size();i++)
			vals[i] = (float)sampling.get(i).Nodes.size();
		return vals;
	}	
	
	public static void printMassif(float f[]){
		for(int i=0;i<f.length;i++)
			System.out.print(f[i]+"\t");
		System.out.println();
	}
	
	public static void addFirstNeighbours(Graph subgraph, Graph network, boolean onlyConnecting){
		addFirstNeighbours(subgraph, network, onlyConnecting, SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS);
	}
	
	public static void addFirstNeighbours(Graph subgraph, Graph network, boolean onlyConnecting, int connectionOption){
		network.calcNodesInOut();
		Vector<Node> vn = new Vector<Node>(); 
		for(int i=0;i<subgraph.Nodes.size();i++){
			vn.add(subgraph.Nodes.get(i));
		}
		for(int i=0;i<vn.size();i++){
			//System.out.println(""+(i+1)+":\t"+vn.get(i).Id);
			Node n = network.getNode(vn.get(i).Id);
			Vector<Node> vnneigh = getNeighbours(network,n);
			for(int j=0;j<n.incomingEdges.size();j++){
				Edge e = (Edge)n.incomingEdges.get(j);
				Vector<Node> neigh = getNeighbours(network,e.Node1);
				Node found = null;
				for(int k=0;k<neigh.size();k++)
					if((vn.indexOf(neigh.get(k))>=0)&&(!neigh.get(k).Id.equals(e.Node2.Id))&&(vnneigh.indexOf(neigh.get(k))<0)){
						found = neigh.get(k);
							if(connectionOption==SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS)
								subgraph.addNode(e.Node1);
							if(connectionOption==SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS_DIRECTED){
								boolean check = false;
								for(Edge e1in: e.Node1.incomingEdges)
									if(found.Id.equals(e1in.Node1.Id))
										check = true;
								if(check)
									subgraph.addNode(e.Node1);
							}
							if(connectionOption==SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS_DIRECTED_UPSTREAM){
								boolean check = false;
								for(Edge e1in: e.Node1.outcomingEdges)
									if(found.Id.equals(e1in.Node2.Id))
										check = true;
								if(check)
									subgraph.addNode(e.Node1);
							}
						if(!onlyConnecting){
							subgraph.addNode(e.Node1);
						}
					}
			}
			for(int j=0;j<n.outcomingEdges.size();j++){
				Edge e = (Edge)n.outcomingEdges.get(j);
				Vector<Node> neigh = getNeighbours(network,e.Node2);
				Node found = null;
				for(int k=0;k<neigh.size();k++)
					if((vn.indexOf(neigh.get(k))>=0)&&(!neigh.get(k).Id.equals(e.Node1.Id))&&(vnneigh.indexOf(neigh.get(k))<0)){
						found = neigh.get(k);
						if(connectionOption==SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS)
							subgraph.addNode(e.Node2);
						if(connectionOption==SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS_DIRECTED){
							boolean check = false;
							for(Edge e1in: e.Node2.outcomingEdges)
								if(found.Id.equals(e1in.Node2.Id))
									check = true;
							if(check)
								subgraph.addNode(e.Node2);
						}
						if(connectionOption==SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS_DIRECTED_DOWNSTREAM){
							boolean check = false;
							for(Edge e1in: e.Node2.incomingEdges)
								if(found.Id.equals(e1in.Node1.Id))
									check = true;
							if(check)
								subgraph.addNode(e.Node2);
						}
						if(!onlyConnecting)
							subgraph.addNode(e.Node2);
					}
			}
		}
		subgraph.addConnections(network);
	}
	
	public static void addComplexNodes(Graph subgraph, Graph network){
		network.calcNodesInOut();
		Vector<Node> vn = new Vector<Node>(); 
		for(int i=0;i<subgraph.Nodes.size();i++){
			vn.add(subgraph.Nodes.get(i));
		}
		for(int i=0;i<vn.size();i++){
			Node n = network.getNode(vn.get(i).Id);
			//if(n==null)
			//	System.out.println(vn.get(i).Id+" not found in the network, function addComplexNodes");
			for(int j=0;j<n.incomingEdges.size();j++){
				Edge e = (Edge)n.incomingEdges.get(j);
				Vector<Node> neigh = getNeighbours(network,e.Node1);
				boolean found = false;
				for(int k=0;k<neigh.size();k++)
					if((vn.indexOf(neigh.get(k))>=0)&&(!neigh.get(k).Id.equals(e.Node2.Id)))
							found = true;
				if(found){
					String et = e.Node1.getFirstAttributeValue("NODE_TYPE");
					if(et!=null)
						if(et.equals("COMPLEX"))					
							subgraph.addNode(e.Node1);
				}
			}
			for(int j=0;j<n.outcomingEdges.size();j++){
				Edge e = (Edge)n.outcomingEdges.get(j);
				Vector<Node> neigh = getNeighbours(network,e.Node2);
				boolean found = false;
				for(int k=0;k<neigh.size();k++)
					if((vn.indexOf(neigh.get(k))>=0)&&(!neigh.get(k).Id.equals(e.Node1.Id)))
							found = true;
				if(found){
					String et = e.Node1.getFirstAttributeValue("NODE_TYPE");					
					if(et!=null)					
						if(et.equals("COMPLEX"))					
							subgraph.addNode(e.Node2);
				}
			}
		}
		subgraph.addConnections(network);
	}
	
	
	public static Vector<Node> getNeighbours(Graph network, Node n){
		Vector<Node> neigh = new Vector<Node>();
		for(int i=0;i<n.incomingEdges.size();i++){
			Edge e = (Edge)n.incomingEdges.get(i);
			neigh.add(e.Node1);
		}
		for(int i=0;i<n.outcomingEdges.size();i++){
			Edge e = (Edge)n.outcomingEdges.get(i);
			neigh.add(e.Node2);
		}
		return neigh;
	}
	
	public void addFirstNeighbours(){
		addFirstNeighbours(subnetwork, network, addOnlyConnectingNeighbours);
	}
	public void addComplexNodes(){
		addComplexNodes(subnetwork, network);
	}
	
	


	
	
	public static Graph MergeGraphs(Vector<Graph> graphs){
		Graph gr = new Graph();
		for(int i=0;i<graphs.size();i++){
			gr.addNodes(graphs.get(i));
			gr.addConnections(graphs.get(i));
		}
		return gr;
	}
	
	public static void saveDistanceMatrix(double distMat[][], String fn, boolean roundDistance) throws Exception{
		FileWriter fw = new FileWriter(fn);
		for(int i=0;i<distMat.length;i++){
			for(int j=0;j<distMat.length;j++){
				if(distMat[i][j]<1000){
				if(roundDistance)
					fw.write((int)(distMat[i][j]+0.5)+"\t");
				else
					fw.write(distMat[i][j]+"\t");
				}else
					fw.write("-\t");
			}
		fw.write("\n");
		}
		fw.close();
	}
	
	public static void assignEdgeWeightsByConnectivity(Graph graph, boolean directedVersion){
		graph.calcNodesInOut();
		for(int i=0;i<graph.Nodes.size();i++){
			if(directedVersion){
				graph.Nodes.get(i).Attributes.add(new Attribute("INGOING_CONNECTIVITY",""+graph.Nodes.get(i).incomingEdges.size()));
				float weight = graph.Nodes.get(i).incomingEdges.size();
				for(int j=0;j<graph.Nodes.get(i).incomingEdges.size();j++){
					Edge e = (Edge)graph.Nodes.get(i).incomingEdges.get(j);
					e.Attributes.add(new Attribute("WEIGHT",""+weight));
					e.weight = weight;
				}
				graph.Nodes.get(i).Attributes.add(new Attribute("OUTGOING_CONNECTIVITY",""+graph.Nodes.get(i).outcomingEdges.size()));
			}else{
				graph.Nodes.get(i).Attributes.add(new Attribute("CONNECTIVITY",""+(graph.Nodes.get(i).incomingEdges.size()+graph.Nodes.get(i).outcomingEdges.size())));
				/*float weight = graph.Nodes.get(i).incomingEdges.size()+graph.Nodes.get(i).outcomingEdges.size();
				for(int j=0;j<graph.Nodes.get(i).incomingEdges.size();j++){
					Edge e = (Edge)graph.Nodes.get(i).incomingEdges.get(j);
					e.Attributes.add(new Attribute("WEIGHT",""+weight));
					e.weight = weight;
				}
				for(int j=0;j<graph.Nodes.get(i).outcomingEdges.size();j++){
					Edge e = (Edge)graph.Nodes.get(i).outcomingEdges.get(j);
					e.Attributes.add(new Attribute("WEIGHT",""+weight));
					e.weight = weight;
				}*/
			}
		}
		if(!directedVersion){
			for(int i=0;i<graph.Edges.size();i++){
				Edge e = (Edge)graph.Edges.get(i);
				float weight = e.Node1.incomingEdges.size()+e.Node1.incomingEdges.size()+e.Node2.outcomingEdges.size()+e.Node2.outcomingEdges.size();
				e.Attributes.add(new Attribute("WEIGHT",""+weight));
				//e.weight = weight;
			}
		}
	}
	
	public void makeCompactnessTest(String distMatrixFile, Vector<Integer> listOfIndices, int numOfSamples, boolean conserveDegrees, Vector<String> fixedNodes) throws Exception{
		makeCompactnessTest(distMatrixFile, listOfIndices, numOfSamples, conserveDegrees, null, -1, fixedNodes);
	}
	
	public void makeCompactnessTest(String distMatrixFile, Vector<Integer> listOfIndices, int numOfSamples, boolean conserveDegrees, String fileNameForComplexProfile, int thresholdForComplexProfile, Vector<String> fixedNodes) throws Exception{
		network.calcNodesInOut();
		System.out.print("Loading distance matrix... ");
		//VDataTable fullMatText = VDatReadWrite.LoadFromSimpleDatFile(distMatrixFile, false, "\t");
		int fullMat[][] = VDatReadWrite.LoadIntegerMassifTabDelimited(distMatrixFile);
		System.out.println(" loaded.");
		
		// check the size
		if(fullMat.length!=network.Nodes.size()){
			System.out.println("ERROR: the size of the distance matrix ("+fullMat.length+") does not correspond to");
			System.out.println("       the size of the network ("+network.Nodes.size()+").");
		}
		
			FileWriter fw = new FileWriter(path+"alldistances");
			for(int i=0;i<fullMat.length;i++)
				for(int j=i+1;j<fullMat[i].length;j++)if(fullMat[i][j]<100)
					fw.write(fullMat[i][j]+"\n");
			fw.close();
		double distMat[][] = extractDistanceSubMatrix(fullMat,listOfIndices);
		double avDist = calcAveragePairwiseDistance(distMat);
		System.out.println("Average pairwise distance = "+avDist+" ");
		int num = 0;

		Vector<String> complexes = readComplexProfile(fileNameForComplexProfile,thresholdForComplexProfile); 
		
		for(int i=0;i<numOfSamples;i++){
			if(i==(int)((float)i*0.005)*200) 
				System.out.println("Sample "+i);
			Vector<Integer> sampleIndices = new Vector<Integer>();
			
			Vector<Integer> fixedIndices = new Vector<Integer>();
			if(fixedNodes!=null){
				for(int l=0;l<fixedNodes.size();l++){
					fixedIndices.add(network.getNodeIndex(fixedNodes.get(l)));
				}
			}
			sampleIndices = generateSample(listOfIndices, conserveDegrees, complexes, fixedIndices);

			// save sample network
			if(i<10){
			Graph grsample = new Graph();
			FileWriter sw = new FileWriter(path+"samples/sample"+(i+1));
			for(int l=0;l<sampleIndices.size();l++){
				grsample.addNode(network.Nodes.get(sampleIndices.get(l)));
				sw.write(network.Nodes.get(sampleIndices.get(l)).Id+"\n");
			}
			sw.close();			

			grsample.addConnections(network);
			XGMML.saveToXGMML(grsample, path+"samples/sample"+(i+1)+".xgmml");
			}
			double distMatSample[][] = extractDistanceSubMatrix(fullMat,sampleIndices);
			double avDistSample = calcAveragePairwiseDistance(distMatSample);
			//System.out.println(avDistSample);
			if(avDistSample<avDist)
				num++;
		}
		double pvalue = (double)num/(double)numOfSamples;
		System.out.println("p-value="+pvalue);
	}
	
	public String makeTestOfConnectivity(int numOfSamples, boolean conserveDegrees, String fileNameForComplexProfile, int thresholdForComplexProfile, Vector<String> fixedNodes) throws Exception{
		return makeTestOfConnectivity(numOfSamples, conserveDegrees, fileNameForComplexProfile, thresholdForComplexProfile, fixedNodes, true);
	}

	public String makeTestOfConnectivity(int numOfSamples, boolean conserveDegrees, String fileNameForComplexProfile, int thresholdForComplexProfile, Vector<String> fixedNodes, boolean verbose) throws Exception{
		
		StringBuffer report = new StringBuffer(); 
		
		network.calcNodesInOut();
		
		int components[][] = calcDistributionOfConnectedComponentSizes(subnetwork);
		distributionOfConnectedComponentSizes = components;
		if(verbose)
			System.out.print("Distribution of connected components: ");
		for(int i=0;i<components.length;i++){
			if(verbose)
				System.out.print(components[i][0]+":"+components[i][1]+"\t");
			report.append(components[i][0]+":"+components[i][1]+"\t");
		}
		if(verbose) System.out.println(); report.append("\n");

		Vector<String> complexes = readComplexProfile(fileNameForComplexProfile,thresholdForComplexProfile);
		
		Vector<Integer> listOfIndices = new Vector<Integer>();
		for(int i=0;i<subnetwork.Nodes.size();i++){
			Node n = subnetwork.Nodes.get(i);
			listOfIndices.add(network.getNodeIndex(n.Id));
		}
		
		float totalComps[] = new float[subnetwork.Nodes.size()+1];
		float pvalues[] = new float[components.length];
		int maxSize = -1;
		
		averageSizeOfRandomBiggestComponent = 0;
		
		for(int i=0;i<numOfSamples;i++){
			//System.out.println("Sample "+i);
			if(i==(int)((float)i*0.005)*200){ 
				//System.out.println("Sample "+i);
				report.append("Sample "+i+"\n");
			}
			Vector<Integer> sampleIndices = new Vector<Integer>();
			Vector<Integer> fixedIndices = new Vector<Integer>();
			if(fixedNodes!=null){
				for(int l=0;l<fixedNodes.size();l++){
					fixedIndices.add(network.getNodeIndex(fixedNodes.get(l)));
				}
			}
			sampleIndices = generateSample(listOfIndices, conserveDegrees, complexes, fixedIndices);
			Graph grsample = new Graph();
			for(int l=0;l<sampleIndices.size();l++)
				grsample.addNode(network.Nodes.get(sampleIndices.get(l)));

			if(modeOfSubNetworkConstruction == SIMPLY_CONNECT){
				grsample.addConnections(network);
			}

			if(modeOfSubNetworkConstruction == SIMPLY_CONNECT_WITH_SECOND_ORDER_CONNECTIONS){
				grsample.addConnections(network);
				//produceConnectionsSecondOrder(grsample,path+"hprd8_spdm_complexes_clicks.full");
				//addFirstNeighbours(subnetwork, network, true);
				//Date d = new Date();
				produceConnectionsSecondOrderFromDistMatrix(grsample);
				//System.out.println("Time spend for connecting: "+((new Date()).getTime()-d.getTime()));
			}

			if(modeOfSubNetworkConstruction == SIMPLY_CONNECT_WITH_COMPLEX_NODES){
				grsample.addConnections(network);
				addComplexNodes();
			}
				
			if(modeOfSubNetworkConstruction == ADD_FIRST_NEIGHBOURS){
				grsample.addConnections(network);
				addFirstNeighbours(subnetwork, network, false);
			}
			if(modeOfSubNetworkConstruction == CONNECT_BY_SHORTEST_PATHS){
				//connectByShortestPaths();
			}
			
			if(false)if(i<10){
				FileWriter sw = new FileWriter(path+"samples/sample"+(i+1));
				for(int l=0;l<sampleIndices.size();l++)
					sw.write(network.Nodes.get(sampleIndices.get(l)).Id+"\t"+(network.Nodes.get(sampleIndices.get(l)).outcomingEdges.size()+network.Nodes.get(sampleIndices.get(l)).incomingEdges.size())+"\n");
				sw.close();
				XGMML.saveToXGMML(grsample, path+"samples/sample"+(i+1)+".xgmml");
			}
			
			if(i<10){
				if(verbose)
					System.out.println("Sample "+i+", size="+subnetwork.Nodes.size()+")");
				report.append("Sample "+i+")\n");
			}
			int componentSample[][] = calcDistributionOfConnectedComponentSizes(grsample);
			
			if(componentSample.length>0)
				averageSizeOfRandomBiggestComponent+=componentSample[componentSample.length-1][0];
			
			for(int k=0;k<componentSample.length;k++){
				if(i<10){
					if(verbose)
						System.out.print(componentSample[k][0]+":"+componentSample[k][1]+"\t");
					report.append(componentSample[k][0]+":"+componentSample[k][1]+"\t");
				}
				totalComps[componentSample[k][0]] += componentSample[k][1];
				if(componentSample[k][0]>maxSize)
					maxSize = componentSample[k][0];
			}
			if(i<10){
				if(verbose)
					System.out.println();
				report.append("\n");
			}
			for(int l=0;l<components.length;l++){
					int sizeToEvaluate = components[l][0];
					int numToEvaluate = 0;
					for(int kk=l;kk<components.length;kk++) 
						numToEvaluate+=components[kk][1];
					int num = 0;
					for(int kk=0;kk<componentSample.length;kk++)
						if(componentSample[kk][0]>=sizeToEvaluate)
							num+=componentSample[kk][1];
					if(i<10){
						if(verbose)
							System.out.println("\tcomponentSize > "+sizeToEvaluate+":"+numToEvaluate+", met "+num+" times:");
						report.append("\tcomponentSize > "+sizeToEvaluate+":"+numToEvaluate+", met "+num+" times:\n");
					}
					if(num>=numToEvaluate)
						pvalues[l]+=1f;
			}
		}
		//System.out.println("Connected components frequencies:");
		report.append("Connected components frequencies:\n");
		for(int i=2;i<=maxSize;i++){
			//System.out.println(i+"\t"+totalComps[i]/(float)numOfSamples);
			report.append(i+"\t"+totalComps[i]/(float)numOfSamples+"\n");
		}
		if(verbose)
			System.out.println("Connected components p-values:");
		report.append("Connected components p-values:\n");
		
		averageSizeOfRandomBiggestComponent/=(float)numOfSamples;
		significanceOfConnectedComponents = new float[components.length]; 
		
		for(int l=0;l<components.length;l++){
			int sizeToEvaluate = components[l][0];
			int numToEvaluate = 0;
			for(int kk=l;kk<components.length;kk++) 
				numToEvaluate+=components[kk][1];
			if(verbose)
				System.out.println(sizeToEvaluate+":"+numToEvaluate+", p-value="+pvalues[l]/(float)numOfSamples);
			report.append(sizeToEvaluate+":"+numToEvaluate+", p-value="+pvalues[l]/(float)numOfSamples+"\n");
			significanceOfConnectedComponents[l] = pvalues[l]/(float)numOfSamples;
		}
		
		return report.toString();
	}

	public Vector<String> readComplexProfile(String fileNameForComplexProfile, int thresholdForComplexProfile) throws Exception{
		Vector<String> complexes =  new Vector<String>();
		if(thresholdForComplexProfile!=-1){
		if(fileNameForComplexProfile!=null){
			LineNumberReader lr = new LineNumberReader(new FileReader(fileNameForComplexProfile));
			String s = null;
			while((s=lr.readLine())!=null){
				StringTokenizer st = new StringTokenizer(s,"\t");
				String complex = st.nextToken();
				int numComplex = Integer.parseInt(st.nextToken());
				if(numComplex>=thresholdForComplexProfile){
					complexes.add(complex);
				}
			}
			lr.close();
		}else{
			Iterator<String> it = SubnetworkComplexProfile.keySet().iterator();
			while(it.hasNext()){
				String compid = it.next();
				if(SubnetworkComplexProfile.get(compid)>=thresholdForComplexProfile)
					complexes.add(compid);
			}
			
		}}
		return complexes;
	}


	public Vector<Integer> generateSample(Vector<Integer> listOfIndices, boolean conserveDegrees, Vector<String> complexes, Vector<Integer> fixedIndices){
			Vector<Integer> sampleIndices = new Vector<Integer>();
			Random r = new Random();

			Vector<Integer> newListOfIndices = new Vector<Integer>();
			for(int i=0;i<fixedIndices.size();i++){
				sampleIndices.add(fixedIndices.get(i));
				newListOfIndices.add(fixedIndices.get(i));
			}
			
			for(int i=0;i<listOfIndices.size();i++)
				if(!fixedIndices.contains(listOfIndices.get(i)))
					newListOfIndices.add(listOfIndices.get(i));
			
			while(sampleIndices.size()<newListOfIndices.size()){
				int k = r.nextInt(network.Nodes.size());
				int degree = -1;
				Vector<Node> v = null;
				// Detect if the protein belongs to a complex
				boolean alreadyChosen = false;
				if(complexes.size()>0){
					Node n = network.Nodes.get(newListOfIndices.get(sampleIndices.size()));
					Vector<String> clist = proteinComplexMap.get(n.Id);
					String found = null;
					if(clist!=null){
					int maxsize = 0;
					for(int l=0;l<clist.size();l++)
						if(complexes.indexOf(clist.get(l))>=0){
							// among several alternatives, we select the biggest complex
							Vector<String> pl = complexMap.get(clist.get(l));
							if(pl.size()>maxsize){
								found = clist.get(l);
								maxsize = pl.size();
							}
						}
					if(found!=null){
						Vector<String> plist = complexMap.get(found);
						String pname = plist.get(r.nextInt(plist.size()));
						k = network.getNodeIndex(pname);
						alreadyChosen = true;
					}
					}
				}
				if(conserveDegrees&&(!alreadyChosen)){
					Node n = network.Nodes.get(newListOfIndices.get(sampleIndices.size()));
					int index = newListOfIndices.get(sampleIndices.size());
					if(fixedIndices.contains(index)){
						k = index;
						degree = n.incomingEdges.size()+n.outcomingEdges.size();
					}else{
					degree = n.incomingEdges.size()+n.outcomingEdges.size();
					//System.out.println(sampleIndices.size()+") Node "+n.Id+" degree="+degree);					
					v = degreeDistribution.get(degree);
					if(v==null){
						int mindev = Integer.MAX_VALUE;
						int im = -1;
						for(int l=0;l<degrees.size();l++){
							if(Math.abs(degrees.get(l)-degree)<mindev){
								mindev = Math.abs(degrees.get(l)-degree);
								im = l;
							}
						}
						v = degreeDistribution.get(degrees.get(im));
						System.out.println("Did not found a node ("+n.Id+") with connectivity "+degree+" use "+degrees.get(im)+" instead");
					}
					int j = -1;
					//while(j==-1){
					j = r.nextInt(v.size());
						//if(gr.getNode(v.get(j).Id)!=null)
						//	j = -1;
					//}
					k = network.Nodes.indexOf(v.get(j));
					//System.out.println("Try "+k+"("+network.Nodes.get(k).Id+") ");
					}
				}
				if(sampleIndices.indexOf(k)<0){
					String ntype = "";
					//if(k==-1)
					//	System.out.println("k==-1 in sample "+i)
					if(k!=-1){
					if(network.Nodes.get(k).getFirstAttributeValue("NODE_TYPE")!=null)
						 ntype = network.Nodes.get(k).getFirstAttributeValue("NODE_TYPE");
					if(!ntype.equals("COMPLEX"))
							sampleIndices.add(k);
					}
				    //System.out.println("Inserted "+k+"("+network.Nodes.get(k).Id+") degree "+degree);
				}else{
					//System.out.println("Did not managed to insert "+k+"("+network.Nodes.get(k).Id+") degree "+degree);
				}
			}
			return sampleIndices;
	}
	
	public static double[][] extractDistanceSubMatrix(int fullMat[][], Vector<Integer> listOfIndices){
		double distMat[][] = new double[listOfIndices.size()][listOfIndices.size()];
		for(int i=0;i<listOfIndices.size();i++)
			for(int j=0;j<listOfIndices.size();j++){
				double d = Double.POSITIVE_INFINITY;
				//if(!fullMatText.stringTable[listOfIndices.get(i)][listOfIndices.get(j)].equals("-")){
				//	d = Double.parseDouble(fullMatText.stringTable[listOfIndices.get(i)][listOfIndices.get(j)]);
				//	distMat[i][j] = d;
				if(fullMat[listOfIndices.get(i)][listOfIndices.get(j)]<Integer.MAX_VALUE){
					d = (double)fullMat[listOfIndices.get(i)][listOfIndices.get(j)];
					distMat[i][j] = d;
				}
			}
		return distMat;
	}
	
	public static double calcAveragePairwiseDistance(double distMat[][]){
		double avDist = 0;
		int n = 0;
		for(int i=0;i<distMat.length;i++)
			for(int j=i+1;j<distMat.length;j++)if(distMat[i][j]>0.1)if(distMat[i][j]<1000){
				avDist+=distMat[i][j];
				n+=1;
			}
		return avDist/(double)n;
	}

	public static int[][] calcDistributionOfConnectedComponentSizes(Graph graph){
		Vector<Graph> comps = GraphAlgorithms.ConnectedComponents(graph);
		HashMap<Integer,Integer> compSizes = new HashMap<Integer,Integer>();
		//Vector<Integer> sizes = new Vector<Integer>();
		for(int i=0;i<comps.size();i++){
			Graph gr = comps.get(i);
			if(gr.Nodes.size()>1){
				//sizes.add(gr.Nodes.size());
				Integer n = compSizes.get(gr.Nodes.size());
				if(n==null) n = new Integer(0);
				n++;
				compSizes.put(gr.Nodes.size(),n);
			}
		}
		Vector<Integer> sizes = new Vector<Integer>();
		Iterator<Integer> it = compSizes.keySet().iterator();
		while(it.hasNext())
			sizes.add(it.next());
		Collections.sort(sizes);
		int components[][] = new int[sizes.size()][2];
		for(int i=0;i<sizes.size();i++){
			components[i][0] = sizes.get(i);
			components[i][1] = compSizes.get(sizes.get(i));
		}
		return components;
	}
	
	public void readComplexes(String fn, int complexMaximumSize){
		VDataTable vc = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		for(int i=0;i<vc.rowCount;i++){
			String cn = vc.stringTable[i][vc.fieldNumByName("SOURCE_NAME")];
			String pn = vc.stringTable[i][vc.fieldNumByName("TARGET_NAME")];;
			if(!pn.equals("-")){
				Vector<String> plist = complexMap.get(cn);
				if(plist==null) plist = new Vector<String>();
				plist.add(pn);
				complexMap.put(cn, plist);
				Vector<String> clist = proteinComplexMap.get(pn);
				if(clist==null) clist = new Vector<String>();
				clist.add(cn);
				proteinComplexMap.put(pn, clist);
			}
		}
		Iterator<String> keys = complexMap.keySet().iterator();
		Vector<String> keyToRemove = new Vector<String>();
		while(keys.hasNext()){
			String key = keys.next();
			if(complexMap.get(key).size()>complexMaximumSize)
				keyToRemove.add(key);
		}
		for(int i=0;i<keyToRemove.size();i++)
			complexMap.remove(keyToRemove.get(i));
		keys = complexMap.keySet().iterator();
		while(keys.hasNext()){
			String key = keys.next();
			System.out.println(key+"\t"+complexMap.get(key).size());
		}
		
	}
	
	public void addComplexesToNetworkAsClicks(){
		Iterator<String> it = complexMap.keySet().iterator();
		while(it.hasNext()){
			String cn = it.next();
			Vector<String> plist = complexMap.get(cn);
			for(int i=0;i<plist.size();i++)
				for(int j=i+1;j<plist.size();j++){
					//System.out.println(""+i+"_"+j);
					String si = plist.get(i);
					String sj = plist.get(j);
					if(network.getNode(si)==null){
						System.out.println("Node "+si+" not found in the network. Added.");
						network.getCreateNode(si);
					}
					if(network.getNode(sj)==null){
						System.out.println("Node "+sj+" not found in the network. Added.");
						network.getCreateNode(sj);
					}
					//Vector<Edge> ve = network.getEdge(network.getNode(si), network.getNode(sj), false);
					//if((ve.size()==0)||(ve==null)){
						Edge e = network.getCreateEdge(si+"_incomplex_"+sj);
						e.Node1 = network.getNode(si);
						e.Node2 = network.getNode(sj);
						e.Attributes.add(new Attribute("EDGE_TYPE","INCOMPLEX"));
					//}
					}
		}
	}
	

	public void addComplexesToNetworksAsNodes(){
		Iterator<String> it = complexMap.keySet().iterator();
		while(it.hasNext()){
			String cn = it.next();
			Node complexNode = network.getCreateNode(cn);
			complexNode.Attributes.add(new Attribute("NODE_TYPE","COMPLEX"));
			Vector<String> plist = complexMap.get(cn);
			for(int i=0;i<plist.size();i++){
					String si = plist.get(i);
					if(network.getNode(si)==null){
						System.out.println("Node "+si+" not found in the network. Added.");
						network.getCreateNode(si);
					}
					Edge e = network.getCreateEdge(cn+"_contains_"+si);
					e.Node1 = complexNode;
					e.Node2 = network.getNode(si);
					e.Attributes.add(new Attribute("TYPE","INCOMPLEX"));
					}
		}		
	}	
	
	/*public void removeComplexesFromSubnetwork(){
		
	}
	
	public void collapseComplexesInSubnetwork(){
		
	}
	
	public Node collapseSetOfNodes(Vector<String> names){
		Node metaNode = null;
		return metaNode;
	}*/
	
	public void removeDoubleEdges(){
		network.calcNodesInOut();
		Vector<Edge> edgesToRemove = new Vector<Edge>();		
		for(int i=0;i<network.Nodes.size();i++){
			//if(i==(int)((float)i*0.01f)*100)
			//	System.out.print(i+"\t");
			Node n = network.Nodes.get(i);
			int j=0;
			Vector<Edge> edges = new Vector<Edge>();
			for(int k=0;k<n.outcomingEdges.size();k++)
				edges.add(n.outcomingEdges.get(k));
			for(int k=0;k<n.incomingEdges.size();k++)
				edges.add(n.incomingEdges.get(k));
			
			while(j<edges.size()){
				Edge e = edges.get(j);
				//System.out.println(e.Node1.Id+"->"+e.Node2.Id+"("+e.Id+")");
				for(int k=j+1;k<edges.size();k++){
					Edge ek = edges.get(k);
					//System.out.println("\t"+ek.Node1.Id+"->"+ek.Node2.Id+"("+ek.Id+")");
					String et = e.getFirstAttributeValue("EDGE_TYPE");
					String etk = ek.getFirstAttributeValue("EDGE_TYPE");						
					
					if(e.Node1.Id.equals(ek.Node1.Id)&&e.Node2.Id.equals(ek.Node2.Id)){
						//network.removeEdge(ek.Id);
						if((etk!=null)&&etk.equals("INCOMPLEX")){						
							if(edgesToRemove.indexOf(ek)<0)
								edgesToRemove.add(ek);
						}else{
						if((et!=null)&&et.equals("INCOMPLEX")){
							if(edgesToRemove.indexOf(e)<0)
								edgesToRemove.add(e);
						}}
						//System.out.println(ek.Id+" removed");
					}
					if(e.Node1.Id.equals(ek.Node2.Id)&&e.Node2.Id.equals(ek.Node1.Id)){
						//network.removeEdge(ek.Id);
						if((etk!=null)&&etk.equals("INCOMPLEX")){						
							if(edgesToRemove.indexOf(ek)<0)
								edgesToRemove.add(ek);
						}else{
						if((et!=null)&&et.equals("INCOMPLEX")){
							if(edgesToRemove.indexOf(e)<0)
								edgesToRemove.add(e);
						}}
						//System.out.println(ek.Id+" removed");
					}					
				}
				j++;
			}
		}
		System.out.println(edgesToRemove.size()+" edges marked for removal.");
		/*Iterator it = network.EdgeHash.keySet().iterator();
		while(it.hasNext()){
			System.out.println("key = "+(String)it.next());
		}*/
		
		for(int i=0;i<edgesToRemove.size();i++){
			network.removeEdge(edgesToRemove.get(i).Id);
			//System.out.println("Remove "+edgesToRemove.get(i).Id+" "+network.Edges.size()+" edges");
		}
	}
	
	public void makeComplexProfile(String fn) throws Exception{
		FileWriter fw = new FileWriter(fn);
		subnetwork.calcNodesInOut();
		for(int i=0;i<subnetwork.Nodes.size();i++){
			Node n = subnetwork.Nodes.get(i);
			if(n.getFirstAttributeValue("NODE_TYPE").equals("COMPLEX")){
				fw.write(n.Id+"\t"+(n.incomingEdges.size()+n.outcomingEdges.size())+"\n");
			}
		}
		fw.close();
	}
	
	public void produceConnectionsSecondOrder(Graph graph, String distMatrixFile){
		graph.calcNodesInOut();
		System.out.print("Loading distance matrix... ");
		int fullMat[][] = VDatReadWrite.LoadIntegerMassifTabDelimited(distMatrixFile);
		networkDistanceMatrix = fullMat;
		System.out.println(" loaded.");
		int numNewEdges = 0;
		for(int i=0;i<graph.Nodes.size();i++){
			if(i==(int)((float)i*0.005f)*200)
				System.out.print(i+"\t");
			for(int j=i+1;j<graph.Nodes.size();j++){
				Node ni = graph.Nodes.get(i);
				Node nj = graph.Nodes.get(j);
				int ki = network.getNodeIndex(ni.Id);
				int kj = network.getNodeIndex(nj.Id);
				int dist = fullMat[ki][kj];
				if(dist==2){
					Edge e = new Edge();
					e.Node1 = ni; e.Node2 = nj;
					e.Id = ni.Id+"_"+nj.Id+"_secondorder";
					e.Attributes.add(new Attribute("EDGE_TYPE","SECOND_ORDER"));
					graph.addEdge(e);
					numNewEdges++;
				}
			}
		}
		System.out.println("\nAdded "+numNewEdges+" edges");
	}
	
	public void produceConnectionsSecondOrderFromDistMatrix(Graph graph){
		int numNewEdges = 0;
		int fullMat[][] = networkDistanceMatrix;
		
		int allindices[] = new int[graph.Nodes.size()];
		for(int i=0;i<graph.Nodes.size();i++){
			Node ni = graph.Nodes.get(i);
			int ki = network.getNodeIndex(ni.Id);
			allindices[i] = ki;
		}
		
		for(int i=0;i<graph.Nodes.size();i++){
			if(i==(int)((float)i*0.005f)*200)
				System.out.print(i+"\t");
			for(int j=i+1;j<graph.Nodes.size();j++){
				/*Node ni = graph.Nodes.get(i);
				Node nj = graph.Nodes.get(j);
				int ki = network.getNodeIndex(ni.Id);
				int kj = network.getNodeIndex(nj.Id);*/
				int ki = allindices[i];
				int kj = allindices[j];
				int dist = fullMat[ki][kj];
				if(dist==2){
					Node ni = graph.Nodes.get(i);
					Node nj = graph.Nodes.get(j);					
					Edge e = new Edge();
					e.Node1 = ni; e.Node2 = nj;
					e.Id = ni.Id+"_"+nj.Id+"_secondorder";
					e.Attributes.add(new Attribute("EDGE_TYPE","SECOND_ORDER"));
					graph.addEdge(e);
					numNewEdges++;
				}
			}
		}
		System.out.println("\nAdded "+numNewEdges+" edges");
	}
	
	public static String calcSignificanceVsNumberOfGenes(Graph network, Vector<String> rankedListOfProteins, int numberOfPermutations, int nga[]) throws Exception{
		return calcSignificanceVsNumberOfGenes(network, rankedListOfProteins, numberOfPermutations, nga, true);
	}
	
	public static String calcSignificanceVsNumberOfGenes(Graph network, Vector<String> rankedListOfProteins, int numberOfPermutations, int nga[], boolean verbose) throws Exception{
		String report = "";
		Vector<Integer> numberOfGenes = new Vector<Integer>();
		Vector<Integer> genesSelected = new Vector<Integer>();
		Vector<Integer> genesInTheBiggestConnectedComponent = new Vector<Integer>();
		Vector<Float> averageSizeOfRandomBiggestComponent = new Vector<Float>();
		Vector<Float> 	significanceOfTheBiggestComponent = new Vector<Float>();
		SubnetworkProperties SP = new SubnetworkProperties();
		SP.modeOfSubNetworkConstruction = SP.SIMPLY_CONNECT;
		SP.network = network;
		//int nga[] = {100,150,200,250,300,350,400,450,500,550,600,700,800,1000,1100,1200,1300,1400,1500,1600,1700,1800,1900,2000,2200,2400,2700,3000,3500,4000,5000,7000,9000}; 
		//int nga[] = {100,150,200,250,260,270,280,290,300,310,320,330,340,350,360,370,380,390,400,420,440,460,480,500,520,540,560,580,600,620,640,660,680,700,1000,1500,2000,4000,6000,9000};
		//int nga[] = {100,150,200,250,260,270,280,290,300,310,320,330,340,350,360,370,380,390,400,420,440,460,480,500,520,540,560,580,600,620,640,660,680,700,1000};
		for(int k=0;k<nga.length;k++){
			int ng = nga[k];
			if(verbose){
			System.out.println("================================");
			System.out.println("NUMBER OF GENES TO SELECT = "+ng);
			System.out.println("================================");
			}
			Vector<String> list = new Vector<String>();
			for(int i=0;i<ng;i++)
				list.add(rankedListOfProteins.get(i));
			SP.selectNodesFromList(list);
			SP.subnetwork.addConnections(SP.network);
			
			StructureAnalysisUtils.Option options = new StructureAnalysisUtils.Option();
			SP.calcDegreeDistribution(SP.network, SP.degreeDistribution, SP.degrees, verbose);
			Vector<String> fixedNodes = options.fixedNodeList;
			String reportCompactness = SP.makeTestOfConnectivity(numberOfPermutations, true, null, 0, fixedNodes, verbose);

			if(SP.distributionOfConnectedComponentSizes.length>0){
				numberOfGenes.add(ng);
				genesSelected.add(SP.subnetwork.Nodes.size());
				genesInTheBiggestConnectedComponent.add(SP.distributionOfConnectedComponentSizes[SP.distributionOfConnectedComponentSizes.length-1][0]); 
				significanceOfTheBiggestComponent.add(SP.significanceOfConnectedComponents[SP.significanceOfConnectedComponents.length-1]);
				averageSizeOfRandomBiggestComponent.add(SP.averageSizeOfRandomBiggestComponent);
			}
		}
		//report+="================================================\n";
		if(verbose)
			System.out.println("================================================");
		report+="NGENES\tSELECTED\tLARGESTCOMPONENT\tSIGNIFICANCE\tLARGESTRANDOMCOMPONENT\tSCORE\n";
		if(verbose)
			System.out.println("NGENES\tSELECTED\tLARGESTCOMPONENT\tSIGNIFICANCE\tLARGESTRANDOMCOMPONENT\tSCORE");
		for(int i=0;i<numberOfGenes.size();i++){
			float score = (float)(genesInTheBiggestConnectedComponent.get(i)-averageSizeOfRandomBiggestComponent.get(i))/genesSelected.get(i);
			report+=numberOfGenes.get(i)+"\t"+genesSelected.get(i)+"\t"+genesInTheBiggestConnectedComponent.get(i)+"\t"+significanceOfTheBiggestComponent.get(i)+"\t"+averageSizeOfRandomBiggestComponent.get(i)+"\t"+score+"\n";
			if(verbose)
				System.out.println(numberOfGenes.get(i)+"\t"+genesSelected.get(i)+"\t"+genesInTheBiggestConnectedComponent.get(i)+"\t"+significanceOfTheBiggestComponent.get(i)+"\t"+averageSizeOfRandomBiggestComponent.get(i)+"\t"+score);
			//System.out.println(numberOfGenes.get(i)+"\t"+genesSelected.get(i));
		}
		return report;
	}
	
	public void calcSubnetworkComplexProfile(){
		SubnetworkComplexProfile.clear();
		for(int i=0;i<subnetwork.Nodes.size();i++){
			Node n = subnetwork.Nodes.get(i);
			Vector<String> comps = proteinComplexMap.get(n.Id);
			if(comps!=null)
			for(int j=0;j<comps.size();j++){
				String comp = comps.get(j);
				Integer N = SubnetworkComplexProfile.get(comp);
				if(N==null) N = new Integer(0);
				N++;
				SubnetworkComplexProfile.put(comp, N);
			}
		}
		Iterator<String> it = SubnetworkComplexProfile.keySet().iterator();
		try{
		FileWriter fw = new FileWriter(this.path+"complexprofile");
		while(it.hasNext()){
			String key = it.next();
			fw.write(key+"\t"+SubnetworkComplexProfile.get(key)+"\n");
		}
		fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void calcPercolationThreshold(Graph graph){
		int numberOfSampling = 100;
		Random r = new Random();
		System.out.println("Before "+graph.Nodes.size());
		
		/*Vector<Graph> compsGlobal = GraphAlgorithms.ConnectedComponents(graph);
		int maxsizeGlobal = -1;
		int imaxGlobal = -1;
		for(int j=0;j<compsGlobal.size();j++)
			if(maxsizeGlobal<compsGlobal.get(j).Nodes.size()){
				maxsizeGlobal = compsGlobal.get(j).Nodes.size();
				imaxGlobal = j;
		}
		graph = compsGlobal.get(imaxGlobal);*/
		System.out.println("After "+graph.Nodes.size());
		System.out.println("NNODES\tAV_SIZE\tPERCENTAGE_CONNECTED\tGLOBALLY_CONNECTED");
		for(int size=100;size<=graph.Nodes.size();size+=300){
			int compSize = 0;
			for(int i=0;i<numberOfSampling;i++){
				graph.selectedIds.clear();
				int j=0;
				while(j<size){
					String randomId = graph.Nodes.get(r.nextInt(graph.Nodes.size())).Id;
					if(!graph.selectedIds.contains(randomId)){
						graph.selectedIds.add(randomId);
						j++;
					}
				}Graph subnetwork = graph.getSelectedNodes();
				subnetwork.addConnections(graph);
				Vector<Graph> comps = GraphAlgorithms.ConnectedComponents(subnetwork);
				int maxsize = -1;
				for(j=0;j<comps.size();j++)
					if(maxsize<comps.get(j).Nodes.size())
						maxsize = comps.get(j).Nodes.size();
				compSize+=maxsize;
			}
			float averageSize = (float)compSize/(float)numberOfSampling;
			System.out.println(size+"\t"+averageSize+"\t"+averageSize/size+"\t"+averageSize/graph.Nodes.size());
		}
	}
		
}
