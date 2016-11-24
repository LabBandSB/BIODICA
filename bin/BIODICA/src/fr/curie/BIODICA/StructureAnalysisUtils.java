/*
   BiNoM Cytoscape Plugin
   Copyright (C) 2006-2007 Curie Institute, 26 rue d'Ulm, 75005 Paris - FRANCE

   BiNoM Cytoscape Plugin is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   BiNoM Cytoscape plugin is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
*/

/*
  BiNoM authors:
	Andrei Zinovyev : http://www.ihes.fr/~zinovyev
	Eric Viara : http://www.sysra.com/viara
	Laurence Calzone :	http://leibniz.biol.vt.edu/people/laurence/laurence.html
*/

package fr.curie.BIODICA;

import java.util.*;
import java.io.*;

import edu.rpi.cs.xgmml.*;

/**
 * Connector class between GraphAlgorithms and BiographUtils and Cytoscape interface 
 *
 */
public class StructureAnalysisUtils {

  /**
   * Options container 
   */
  public static class Option {
    public float intersectionThreshold = 0.35f;
    public int smallestCycleSize = 0;
    // path analysis options
    public static int ALL_SHORTEST_PATHS = 1;
    public static int SUBOPTIMAL_SHORTEST_PATHS = 2;
    public static int ALL_PATHS = 3;
    public int pathFindMode = ALL_SHORTEST_PATHS;
    public boolean directedGraph = true;
    public double searchRadius = 10;
    // subnetwork extraction options
    public int methodOfSubnetworkExtraction = SubnetworkProperties.SIMPLY_CONNECT;
    public boolean checkComponentSignificance = false;
    public int numberOfPermutations = 100;
    public boolean makeConnectivityTable = false;
    public boolean makeSizeSignificanceTest = false;
    public int numberOfPermutationsForSizeTest = 100;
    public String sizesToTest = null;
    public Vector<String> fixedNodeList = null;
  }
  
  public static Vector getPrunedGraph(GraphDocument graph, boolean includeInterface){
	    Vector res = new Vector();

	    Graph gr = XGMML.convertXGMMLToGraph(graph);

	    Vector v = GraphAlgorithms.PruneGraph(gr,includeInterface);
	    GraphDocument xinbound = XGMML.convertGraphToXGMML((Graph)v.get(0));
	    xinbound.getGraph().setName(gr.name+"_in");
	    GraphDocument xoutbound = XGMML.convertGraphToXGMML((Graph)v.get(1));
	    xoutbound.getGraph().setName(gr.name+"_out");

	    res.add(xinbound);
	    res.add(xoutbound);
	    gr.name = gr.name+"_scc";
	    GraphDocument xgr = XGMML.convertGraphToXGMML(gr);
	    res.add(xgr);

	    return res;
	  }
  

  public static Vector getStronglyConnectedComponents(GraphDocument graph){
    Vector res = new Vector();

    Graph gr = XGMML.convertXGMMLToGraph(graph);

    Vector<Graph> comps = GraphAlgorithms.StronglyConnectedComponentsTarjan(gr,2);
    for(int i=0;i<comps.size();i++){
        Graph gr1 = (Graph)comps.elementAt(i);
        gr1.name = gr.name+"_scc"+(i+1);
        GraphDocument xgr = XGMML.convertGraphToXGMML(gr1);
        res.add(xgr);
      }

    return res;
  }

  public static Vector getConnectedComponents(GraphDocument graph){
    Vector res = new Vector();
    Graph gr = XGMML.convertXGMMLToGraph(graph);
    Vector comps = GraphAlgorithms.ConnectedComponents(gr);
    for(int i=0;i<comps.size();i++){
      Graph gr1 = (Graph)comps.elementAt(i);
      gr1.name = gr.name+"_cc"+(i+1);
      GraphDocument xgr = XGMML.convertGraphToXGMML(gr1);
      res.add(xgr);
    }
    return res;
  }

  public static Vector getCyclicComponents(GraphDocument graph, StructureAnalysisUtils.Option options){
    Vector res = new Vector();
    Graph gr = XGMML.convertXGMMLToGraph(graph);
    Vector comps = GraphAlgorithms.CycleDecomposition(gr, options.smallestCycleSize); //getCyclicMaterialComponents(gr,options);
    for(int i=0;i<comps.size();i++){
      Graph gr1 = (Graph)comps.elementAt(i);
      GraphDocument xgr = XGMML.convertGraphToXGMML(gr1);
      res.add(xgr);
    }
    return res;
  }

  /*public static Vector getCyclicMaterialComponents(Graph graph, StructureAnalysisUtils.Option options){
    Vector mcomps = BiographUtils.calcAllMaterialComponents(graph);
    Vector cyc = new Vector();
    for(int i=0;i<mcomps.size();i++){
      Graph gr = (Graph)mcomps.elementAt(i);
      Vector cycs = GraphAlgorithms.CycleDecomposition(gr,3);
      int k = 1;
      for(int j=0;j<cycs.size();j++){
        Graph grc = (Graph)cycs.elementAt(j);
        Graph grcs = BiographUtils.excludeUnaryReactions(grc);
        if(cycs.size()>1){
          grc.name+="c"+k;
          if(grcs.Nodes.size()>2){
            k++;
          }
        }
        if(grcs.Nodes.size()>2)
          cyc.add(grc);
      }
    }
    //cyc = Graph.combineIncludedGraphs(cyc);
    cyc = GraphAlgorithms.CombineIncludedGraphsApprox(cyc,options.intersectionThreshold);
    return cyc;

  }*/
  
  
  public static Vector<GraphDocument> getClusteredNetworks(Vector<GraphDocument> networks, float intersectionThreshold){
	  Vector<GraphDocument> v = new Vector<GraphDocument>();
	  Vector graphs = new Vector();
	  for(int i=0;i<networks.size();i++){
		  Graph module = XGMML.convertXGMMLToGraph(networks.get(i));
		  graphs.add(module);
	  }
	  Vector<Graph> clusters = GraphAlgorithms.CombineIncludedGraphsApprox(graphs,intersectionThreshold);
	  for(int i=0;i<clusters.size();i++){
		  v.add(XGMML.convertGraphToXGMML((Graph)clusters.get(i)));
	  }
  	  return v;
  }
  
  public static Set<String> findPaths(GraphDocument network, Vector<String> sources, Vector<String> targets, Option options){
	  Graph graph = XGMML.convertXGMMLToGraph(network);
	  return findPaths(graph, sources, targets, options); 
  }
  
  public static Set<String> findPaths(Graph network, Vector<String> sources, Vector<String> targets, Option options){
	System.out.println("Sources "+sources.size()+" Targets "+targets.size());
	Set<String> selected = new HashSet<String>();
	Graph graph = network;
	
	for(int i=0;i<sources.size();i++){

		HashSet<Node> targetSet = new HashSet();		
		for(int j=0;j<targets.size();j++){
			Node n = network.getNode(targets.get(j));
			if(!n.Id.equals(sources.get(i)))
			if(n!=null)
				targetSet.add(n);
		}
		
		Vector<Graph> paths = null;
		if(options.pathFindMode==options.ALL_PATHS){
			Node source = graph.getNode(sources.get(i));
			System.out.println(""+(i+1)+") Source "+source.Id);
			paths = GraphAlgorithms.FindAllPaths(graph, source, targetSet, options.directedGraph, options.searchRadius);
			if(paths!=null){
				System.out.println(""+paths.size()+" paths found");
				for(int k=0;k<paths.size();k++){
					Graph gr = paths.get(k);
					for(int kk=0;kk<gr.Nodes.size();kk++){
						//System.out.print(((Node)gr.Nodes.get(kk)).Id+"\t");
						selected.add(((Node)gr.Nodes.get(kk)).Id);
					}
					//System.out.println();
				}
			}
		}else
		for(int j=0;j<targets.size();j++){
			    {
				Node source = graph.getNode(sources.get(i));
				Node target = graph.getNode(targets.get(j));
				if(!source.Id.equals(target.Id)){
				System.out.println("Source "+source.Id+" Target "+target.Id);
				
				if((source!=null)&&(target!=null)){
				if(options.pathFindMode==options.ALL_SHORTEST_PATHS)
					paths = GraphAlgorithms.Dijkstra(graph, source, target, options.directedGraph, false, options.searchRadius);
				if(options.pathFindMode==options.SUBOPTIMAL_SHORTEST_PATHS)
					paths = GraphAlgorithms.Dijkstra(graph, source, target, options.directedGraph, true, options.searchRadius);
				}
				}
			}
				if(paths!=null){
					System.out.println(""+paths.size()+" paths found");
					for(int k=0;k<paths.size();k++){
						Graph gr = paths.get(k);
						for(int kk=0;kk<gr.Nodes.size();kk++){
							//System.out.print(((Node)gr.Nodes.get(kk)).Id+"\t");
							selected.add(((Node)gr.Nodes.get(kk)).Id);
						}
						//System.out.println();
					}
				}
		}
	}
	return selected;
  }
  
  public static Graph removeReciprocalEdges(Graph graph){
	  Graph gr = graph;
	  float connectivity[] = new float[graph.Nodes.size()];
	  gr.calcNodesInOut();
	  for(int i=0;i<graph.Nodes.size();i++){
		  connectivity[i] = 0f+graph.Nodes.get(i).incomingEdges.size()+graph.Nodes.get(i).outcomingEdges.size();
	  }
	  int ind[] = fr.curie.BIODICA.Utils.SortMass(connectivity);
	  for(int i=0;i<ind.length;i++){
		  Node n = graph.Nodes.get(ind[i]);
		  gr.calcNodesInOut();
		  for(int j=0;j<n.incomingEdges.size();j++)for(int k=0;k<n.outcomingEdges.size();k++){
			  Edge ej = n.incomingEdges.get(j);
			  Edge ek = n.outcomingEdges.get(k);	
			  if(ej.Node1==ek.Node2)if(ej.Node2==ek.Node1)
					 gr.removeEdge(ek.Id);
		  }
		  gr.calcNodesInOut();
		  for(int j=0;j<n.incomingEdges.size();j++)for(int k=j+1;k<n.incomingEdges.size();k++){
			  Edge ej = n.incomingEdges.get(j);
			  Edge ek = n.incomingEdges.get(k);	
			  if(ej.Node1==ek.Node1)if(ej.Node2==ek.Node2)
					 gr.removeEdge(ek.Id);
		  }
	  }
	  return gr;
  }

}