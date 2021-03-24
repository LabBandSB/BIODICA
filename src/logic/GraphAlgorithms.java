/*
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


package logic;

import java.util.*;
import java.io.*;

import vdaoengine.utils.VSimpleProcedures;

/**
 * Implementation of graph algorithms without use of any node or edge semantics 
 */
public class GraphAlgorithms { 

	/**
	 * 
	 * @param graph
	 * @return List of connected components
	 */
	
	public static boolean verbose = true;
	
	public static Vector<Graph> ConnectedComponents(Graph graph){ 
		Vector<Graph> comps = new Vector<Graph>();
	    graph.addNodesFromEdges();
	    Graph gr = graph.makeCopy();
	    while(gr.Nodes.size()>0){
	      Graph comp = GetConnectedComponent(graph,(Node)gr.Nodes.elementAt(0));
	      gr.subtractNodes(comp);
	      gr.removeObsoleteEdges();
	      //comp.addConnections(this);
	      comps.add(comp);
	    }
	    return comps;
	}
	
	/**
	 * 
	 * @param graph
	 * @param minimumComponentSize
	 * @return List of strongly connected components
	 */
	public static Vector<Graph> StronglyConnectedComponentsTarjan(Graph graph,int minimumComponentSize){ 
		Vector<Graph> scc = new Vector<Graph>();
	
		Graph grcopy = graph.makeCopy();
		//System.out.println(graph.Nodes.size()+"/"+graph.Edges.size()+" "+grcopy.Nodes.size()+"/"+grcopy.Edges.size());
		int k=1;
		/*Node n = (Node)grcopy.Nodes.get(4);
		System.out.println("\nStarting from "+n.Id);
		Vector<Graph> extractedScc = RunTarjan(grcopy,n);
		for(int i=0;i<extractedScc.size();i++){
			Graph gr = extractedScc.get(i);
			gr.name = "Scc"+(k++);
			gr.addConnections(graph);
			scc.add(gr);
		}	*/	
		
		while(grcopy.Nodes.size()>0){
			Node n = (Node)grcopy.Nodes.get(0);
			//System.out.println("\nStarting from "+n.Id);
			grcopy.calcNodesInOut();
			Vector<Graph> extractedScc = RunTarjan(grcopy,n);
			for(int i=0;i<extractedScc.size();i++){
				Graph gr = extractedScc.get(i);
				if(gr.Nodes.size()>=minimumComponentSize){
					gr.addConnections(graph);
					gr.name = "Scc"+(k++);
					scc.add(gr);
				}
				grcopy.removeNodes(gr);
				grcopy.removeObsoleteEdges();
			}
			
		}
		//System.out.println("");
		return scc;
	}
	
	private static Vector<Graph> RunTarjan(Graph graph, Node n){
		Vector<Graph> scc = new Vector<Graph>();
		Set unvisited = new HashSet();
		for(int i=0;i<graph.Nodes.size();i++) unvisited.add(graph.Nodes.get(i));
		Stack<Node> stack = new Stack<Node>();
		int max_dfs = 0;
		HashMap<Node,Integer> dfsMap = new HashMap<Node,Integer>();
		HashMap<Node,Integer> lowlinkMap = new HashMap<Node,Integer>();
		Tarjan(graph,n, unvisited, stack, max_dfs,dfsMap,lowlinkMap,scc);
		return scc;
	}
	
	private static void Tarjan(Graph graph, Node v, Set unvisited, Stack<Node> stack, int max_dfs, HashMap<Node,Integer> dfsMap, HashMap<Node,Integer> lowlinkMap, Vector<Graph> scc){
		dfsMap.put(v, new Integer(max_dfs));
		lowlinkMap.put(v, new Integer(max_dfs));
		max_dfs++;
		stack.push(v);
		unvisited.remove(v);
		for(int i=0;i<v.outcomingEdges.size();i++){
			Node vs = ((Edge)v.outcomingEdges.get(i)).Node2;
			if(unvisited.contains(vs)){
				Tarjan(graph,vs,unvisited,stack,max_dfs,dfsMap,lowlinkMap,scc);
				lowlinkMap.put(v, Math.min(lowlinkMap.get(v), lowlinkMap.get(vs)));
			}else if(stack.search(vs)!=-1){
				lowlinkMap.put(v, Math.min(lowlinkMap.get(v), dfsMap.get(vs)));
			}
		}
		if(lowlinkMap.get(v).equals(dfsMap.get(v))){
			//System.out.print("\nSZK : ");
			Graph gr = new Graph();
			Node vs = null;
			do{
				vs = stack.pop();
				//System.out.print(vs.Id+"\t");
				gr.addNode(vs);
			}while(!v.equals(vs));
			scc.add(gr);
		}
		
/*		Input: Graph G = (V, E), Start node v0

		max_dfs := 0  // Counter for dfs
		U := V        // Collection of unvisited nodes
		S := {}       // An initially empty stack
		tarjan(v0)    // Call the function with the start node

		procedure tarjan(v)
		v.dfs := max_dfs;          // Set the depth index
		v.lowlink := max_dfs;      // v.lowlink <= v.dfs
		max_dfs := max_dfs + 1;    // Increment the counter
		S.push(v);                 // Place v on the stack
		U := U \ {v};              // Separate v from U
		forall (v, v') in E do     // Consider the neighboring nodes
		  if (v' in U)
		    tarjan(v');            // recursive call
		    v.lowlink := min(v.lowlink, v'.lowlink);
		  // Ask whether v' is on the stack 
		  // by a clever linear time method
		  // (for example, setting a flag on the node when it is pushed or popped) 
		  elseif (v' in S)
		    v.lowlink := min(v.lowlink, v'.dfs);
		  end if
		end for
		if (v.lowlink = v.dfs)     // the root of a strongly connected component
		  print "SZK:";
		  repeat
		    v' := S.pop;
		    print v';
		  until (v' = v);
		end if
		*/
	}
	
	/**
	 * Graph pruning (eliminataing IN and OUT layers)
	 * @param graph is modified after the procedure and contains the cyclic part of the graph
	 * @return Two subgraphs - IN-layer, OUT-layer
	 */
	public static Vector<Graph> PruneGraph(Graph graph, boolean includeInterface){
		
		Graph fullCopy = graph.makeCopy();
		
		Vector<Graph> v = new Vector<Graph>();
		Vector interfaceIn = new Vector();
	    Graph inbound = SelectInBoundary(graph,interfaceIn);
	    graph.subtractNodes(inbound);
	    graph.removeObsoleteEdges();

	    Vector interfaceOut = new Vector();
	    Graph outbound = SelectOutBoundary(graph,interfaceOut);
	    graph.subtractNodes(outbound);
	    graph.removeObsoleteEdges();
	    
	    if(includeInterface){
	    	for(int i=0;i<interfaceIn.size();i++)
	    		inbound.addNode((Node)interfaceIn.get(i));
	    	inbound.addConnections(fullCopy);
	    	for(int i=0;i<interfaceOut.size();i++)
	    		outbound.addNode((Node)interfaceOut.get(i));
	    	outbound.addConnections(fullCopy);
	    }

	    v.add(inbound);
	    v.add(outbound);
	    return v;
	}

	/**
	 * 
	 * @param graph
	 * @param sizeThreshold
	 * @return List of minimal cycles
	 */
	public static Vector<Graph> CycleDecomposition(Graph graph, int sizeThreshold){
		Vector<Graph> elementary = new Vector<Graph>();
		CycleDecompositionRecursive(graph, sizeThreshold, elementary);		
		return elementary;
	}
	
	
	private static Vector<Graph> CycleDecompositionRecursive(Graph graph, int sizeThreshold, Vector<Graph>elementary){
		Vector<Graph> comps = new Vector<Graph>();
	    graph.calcNodesInOut();
	    for(int i=0;i<graph.Nodes.size();i++){
	      Node n = (Node)graph.Nodes.elementAt(i);
	      Vector cyc = new Vector();
	      GetAllCycles(graph,n,cyc);
	      for(int j=0;j<cyc.size();j++){
	        Graph c = (Graph)cyc.elementAt(j);
	        boolean exists = false;
	        if(c.identicalNodes(graph))
	        	exists = true;
	        for(int k=0;k<comps.size();k++){
	          Graph c1 = (Graph)comps.elementAt(k);
	          if(c.identicalNodes(c1)){
	            exists = true;
	            break;
	          }
	        }
	        if((!exists)&&(c.Nodes.size()>=sizeThreshold)){
	          c.name = "cycle"+(comps.size()+1);
	          comps.add(c);
	        }
	      }
	    }
	    
	    /*for(int i=0;i<comps.size();i++){
	    	Graph gr = comps.get(i);
	    	elementary.add(gr);
	    }*/
	    
	    for(int i=0;i<comps.size();i++){
	    	Graph gr = comps.get(i);
	    	Vector<Graph> vec = CycleDecompositionRecursive(gr,sizeThreshold,elementary);
	    	if(vec.size()==0){
	    		boolean exists = false;
		        for(int k=0;k<elementary.size();k++){
			          Graph c1 = (Graph)elementary.elementAt(k);
			          if(gr.identicalNodes(c1)){
			            exists = true;
			            break;
			          }
			    }	    		
	    		if(!exists){
	    		   gr.name = "cycle"+(elementary.size()+1);
	    		   elementary.add(gr);
	    		}
	    	}
	    }
		return comps;
	}

	/**
	 * 
	 * @param graph
	 * @param source
	 * @param target
	 * @param directedGraph
	 * @param suboptimal
	 * @return List of shortest or suboptimal shortest paths from source to target
	 */
	public static Vector<Graph> Dijkstra(Graph graph, Node source, Node target, boolean directedGraph, boolean suboptimal, double searchRadius){
		if(!suboptimal)
			return DijkstraAlgorithm(graph,source,target,directedGraph, searchRadius);
		else
			{
			   Vector<Graph> v = DijkstraAlgorithm(graph,source,target,directedGraph, searchRadius);
			   
			   Graph paths[] = new Graph[v.size()];
			   v.copyInto(paths);
			   for(int i=0;i<paths.length;i++){
				   Graph gr = paths[i];
				   for(int j=0;j<gr.Nodes.size()-1;j++){
					   //System.out.println("Getting "+gr.Nodes.get(j).Id+"->"+gr.Nodes.get(j+1).Id);
					   Vector<Edge> ev = gr.getEdge((Node)gr.Nodes.get(j),(Node)gr.Nodes.get(j+1),directedGraph);
					   if(ev.size()>0){
					   System.out.print("Removing...");
					   for(int ss=0;ss<ev.size();ss++){
						   System.out.print(ev.get(ss).Node1.Id+"->"+ev.get(ss).Node2.Id);
					   }
					   System.out.println();
					   double oldweights[] = new double[ev.size()];
					   for(int ie=0;ie<ev.size();ie++){
						   oldweights[ie] = ev.get(ie).weight;
						   ev.get(ie).weight = Double.POSITIVE_INFINITY;
					   }
					   Vector<Graph> v1 = DijkstraAlgorithm(graph,source,target,directedGraph,searchRadius);
					   for(int k=0;k<v1.size();k++){
						   boolean alreadyExists = false;
						   for(int kk=0;kk<v.size();kk++){
							   if(v1.get(k).identicalNodes(v.get(kk))){
								   alreadyExists = true; break;
							   }
						   }
						   if(!alreadyExists)
							   v.add(v1.get(k));
					   }
					   for(int ie=0;ie<ev.size();ie++)
						   ev.get(ie).weight = oldweights[ie];
					   }
				   }
			   }
			   return v; 
			}
	}
	
	/**
	 * Note: searchRadius here is the number of edges from source (edge weights are not considered) 
	 * @param graph
	 * @param source
	 * @param target
	 * @param directedGraph
	 * @return List of all non-intersecting paths from source to target
	 */
	public static Vector<Graph> FindAllPaths(Graph graph, Node source, Set<Node> targets, boolean directedGraph, double searchRadius){
		return FindAllPaths(graph,source,targets,directedGraph,searchRadius,false);
	}
	
	public static Vector<Graph> FindAllPaths(Graph graph, Node source, Set<Node> targets, boolean directedGraph, double searchRadius, boolean finalizePathIfJoinsExisting){
		Vector<Graph> pathf = new Vector<Graph>();
		Vector<Vector<Integer>> path = new Vector<Vector<Integer>>();
	    Vector<Vector<Edge>> neighbours = neighboursOfNodeHash(graph,directedGraph);
	    Vector<Integer> visited = new Vector<Integer>();
	    Vector<Integer> participateInPath = new Vector<Integer>();
	    Graph currentPath = new Graph();
	    currentPath.addNode(source);
	    
	    int isource = graph.getNodeIndex(source.Id);
	    visited.add(new Integer(isource));
	    SearchAllPaths(graph, targets, visited, participateInPath, currentPath, neighbours, directedGraph, pathf, searchRadius,finalizePathIfJoinsExisting);
	    
	    Vector<Graph> listOfPaths = new Vector<Graph>();
	    for(int i=0;i<pathf.size();i++){
	    	Graph gr = pathf.get(i);
	    	boolean found =false;
	    	for(int k=0;k<listOfPaths.size();k++){
	    		Graph gr2 = listOfPaths.get(k);
	    		if(gr2.identicalTo(gr))
	    			found = true;
	    	}
	    	if(!found)
	    		listOfPaths.add(gr);
	    }
	    
		return listOfPaths;
	}
	
	private static void SearchAllPaths(Graph graph, Set<Node> targets, Vector<Integer> visited, Vector<Integer> participateInPath, Graph currentPath, Vector<Vector<Edge>> neighbours, boolean directedGraph, Vector<Graph> pathf, double searchRadius, boolean finalizePathIfJoinsExisting){
		Node startNode = (Node)currentPath.Nodes.get(currentPath.Nodes.size()-1);
		int istart = graph.getNodeIndex(startNode.Id);
		Vector<Edge> nedges = neighbours.get(istart);
		boolean continued = false;

		//int kk = visited.size();
		//if(kk==((int)(kk*0.001f))*1000)
		//	System.out.println(kk+"\t");
		int kk = pathf.size();
		if(kk>0)
		if(kk==((int)(kk*0.01f))*100){
			float size = 0;
			for(int i=0;i<pathf.size();i++) size+=pathf.get(i).Nodes.size();
			size/=kk;
			System.out.println("Found "+kk+" paths (average length = "+size+")\t");
		}
		

		for(int i=0;i<nedges.size();i++){
			Edge ed = (Edge)nedges.get(i);
			/*System.out.print((i+1)+"\t"); 
			for(int l=0;l<currentPath.Edges.size();l++) 
				//System.out.print(currentPath.Edges.get(l).Node1.Id+"-"); 
				System.out.print(currentPath.Edges.get(l)+" ");
			System.out.print(ed+"\t\t[");
			for(int l=0;l<visited.size();l++) System.out.print(graph.Nodes.get(visited.get(l)).Id+",");
			System.out.println("]");*/
			Node nextNode = null;
			if(startNode.Id.equals(ed.Node1.Id))
				nextNode = ed.Node2;
			else
				nextNode = ed.Node1;
			int inextNode = graph.getNodeIndex(nextNode.Id);
			if(visited.indexOf(new Integer(inextNode))<0){ // do something if the node was not visited			
				//if(nextNode.Id.equals(target.Id)){ // Finalize path if target is found
				if(targets.contains(nextNode)){ // Finalize path if a target is found
					Graph copyPath = currentPath.makeCopy();
					currentPath.addNode(nextNode);
					currentPath.addEdge(ed);
					pathf.add(currentPath);
					/*for(int j=0;j<currentPath.Nodes.size();j++){ // Mark all nodes in the path in participateInPath
						Node n = (Node)currentPath.Nodes.get(j);
						int in = graph.getNodeIndex(n.Id);
						if(participateInPath.indexOf(new Integer(in))<0)
							participateInPath.add(new Integer(in));
					}*/
					currentPath = copyPath;
					//System.out.println("Hit the target!");
				}else // end finalize
				/*	if(finalizePathIfJoinsExisting&participateInPath.indexOf(new Integer(inextNode))>=0){ // Finalize if joins already found path
						pathf.add(currentPath);
						for(int j=0;j<currentPath.Nodes.size();j++){ // Mark all nodes in the path in participateInPath
							Node n = (Node)currentPath.Nodes.get(j);
							int in = graph.getNodeIndex(n.Id);
							if(participateInPath.indexOf(new Integer(in))<0)
								participateInPath.add(new Integer(in));
						} System.out.println("Hit existing path!");
					}else // end finalize */
					{
						visited.add(new Integer(inextNode));
						if(nedges.size()==1){
							currentPath.addNode(nextNode);
							currentPath.addEdge(ed);
							if(currentPath.Nodes.size()<=searchRadius){
								SearchAllPaths(graph, targets, visited, participateInPath, currentPath, neighbours, directedGraph, pathf, searchRadius,finalizePathIfJoinsExisting);
								continued = true;
							}
						}else{
							Graph newcurrentPath = currentPath.makeCopy();
							Vector<Integer> newvisited = new Vector<Integer>();
							for(int k=0;k<visited.size();k++)
								newvisited.add(visited.get(k));
							newcurrentPath.addNode(nextNode);
							newcurrentPath.addEdge(ed);
							if(newcurrentPath.Nodes.size()<=searchRadius){            	   
								SearchAllPaths(graph, targets, newvisited, participateInPath, newcurrentPath, neighbours, directedGraph, pathf, searchRadius,finalizePathIfJoinsExisting);
							}
						}
						visited.remove(visited.size()-1);
					}
			}
		}
	}
	
	public static Vector<Graph> DijkstraAlgorithm(Graph graph, Node source, Node target, boolean directedGraph, double searchRadius){
		Vector<Graph> pathf = new Vector<Graph>();
		Vector<Vector<Integer>> path = new Vector<Vector<Integer>>();
	    double d[] = new double[graph.Nodes.size()];
	    Vector<Vector<Integer>> previous = new Vector<Vector<Integer>>(); // Vector of Vectors (previous)
	    for(int i=0;i<graph.Nodes.size();i++)
	      previous.add(new Vector<Integer>());
	    int isource = graph.getNodeIndex(source.Id);
	    int itarget = graph.getNodeIndex(target.Id);
	    Vector<Integer> itargets = new Vector<Integer>();
	    itargets.add(itarget);
	    DijkstraProcedure(graph, isource, itargets, d, previous, directedGraph, searchRadius);	    	    
	    // now extract recursively all shortest paths
	    Vector<Integer> prev = previous.get(itarget);
	    Vector<Integer> pth = new Vector<Integer>();
	    pth.add(new Integer(itarget));
	    prolongatePath(pth,previous,path);
	    //pth.add(new Integer(isource));
	    if(pth.size()>1)
	    	path.add(pth);
	    // reverse all paths (from source to end)
	    for(int i=0;i<path.size();i++){
	      pth = path.get(i);
	      Graph pthr = new Graph();	    
	      //System.out.print("Adding path: ");
	      for(int j=pth.size()-1;j>=0;j--){
	        pthr.addNode((Node)graph.Nodes.get(pth.get(j).intValue()));
		    //System.out.print(graph.Nodes.get(pth.get(j).intValue()).Id+"-");
	      }
	      //pthr.addConnections(graph);
	      pthr.addConnectionsAlongSequence(graph);
	      //System.out.println(" , "+pthr.Edges.size()+"edges");
	      pathf.add(pthr);
	    }
		return pathf;
	}
	
	/** returns list of paths (as Vector<Integer>) for each target separately
	 * 
	 * @param graph
	 * @param source
	 * @param targets
	 * @param directedGraph
	 * @param searchRadius
	 * @return
	 */
	public static Vector<Vector<Vector<Integer>>> DijkstraAlgorithmReturnIndexReverseOrder(Graph graph, Node source, Vector<Node> targets, boolean directedGraph, double searchRadius){
		Vector<Vector<Vector<Integer>>> pathAll = new Vector<Vector<Vector<Integer>>>();
	    double d[] = new double[graph.Nodes.size()];
	    Vector<Vector<Integer>> previous = new Vector<Vector<Integer>>(); // Vector of Vectors (previous)
	    for(int i=0;i<graph.Nodes.size();i++)
	      previous.add(new Vector<Integer>());
	    int isource = graph.getNodeIndex(source.Id);
	    Vector<Integer> itargets = new Vector<Integer>();
	    for(int i=0;i<targets.size();i++)
	      itargets.add(graph.getNodeIndex(targets.get(i).Id));
	    DijkstraProcedure(graph, isource, itargets, d, previous, directedGraph, searchRadius);
	    // now extract recursively all shortest paths
	    for(int i=0;i<itargets.size();i++){
			Vector<Vector<Integer>> path = new Vector<Vector<Integer>>();	    	
	    	Vector<Integer> prev = previous.get(itargets.get(i));
	    	Vector<Integer> pth = new Vector<Integer>();
	    	pth.add(itargets.get(i));
	    	prolongatePath(pth,previous,path);
	    //	pth.add(new Integer(isource));
	    	if(pth.size()>1)
	    		path.add(pth);
	    	//System.out.println(source.Id+"->"+graph.Nodes.get(itargets.get(i)).Id+"\t"+path.size()+" paths");
    		pathAll.add(path);
	 	}
		return pathAll;
	}
	
	
	public static Vector<Graph> DijkstraAlgorithm(Graph graph, Node source, Vector<Node> targets, boolean directedGraph, double searchRadius){
		Vector<Graph> pathf = new Vector<Graph>();
		Vector<Vector<Vector<Integer>>> pathByTarget = new Vector<Vector<Vector<Integer>>>();
		Vector<Vector<Integer>> path = new Vector<Vector<Integer>>();
		pathByTarget = DijkstraAlgorithmReturnIndexReverseOrder(graph,source,targets,directedGraph,searchRadius);
		// merge all targets in one list
		for(int i=0;i<pathByTarget.size();i++){
			for(int j=0;j<pathByTarget.get(i).size();j++){
				Vector<Integer> pth = pathByTarget.get(i).get(j);
				path.add(pth);
			}
		}
	    // reverse all paths (from source to end)
	    for(int i=0;i<path.size();i++){
	      //System.out.println(""+i+"/"+path.size());
	      Vector<Integer>  pth = path.get(i);
	      Graph pthr = new Graph();	      
	      for(int j=pth.size()-1;j>=0;j--){
	        pthr.addNode((Node)graph.Nodes.get(pth.get(j).intValue()));
	      }
	      //pthr.addConnections(graph);
	      pthr.addConnectionsAlongSequence(graph);
	      pathf.add(pthr);
	    }
		return pathf;
	}
	
		

	  /**
	   * Sub-routine for Dijkstra's algorithm
	   */
	  private static void prolongatePath(Vector<Integer> pth, Vector<Vector<Integer>> previous, Vector<Vector<Integer>> paths){
		    int k = ((Integer)pth.get(pth.size()-1)).intValue();
		    Vector<Integer> prev = previous.get(k);
		    if(prev.size()>0){
		      pth.add(prev.get(0));
		    for(int i=1;i<prev.size();i++){
		      Vector<Integer> pthb = new Vector<Integer>();
		      for(int j=0;j<pth.size()-1;j++)
		        pthb.add(pth.get(j));
		      pthb.add(prev.get(i));
		      prolongatePath(pthb,previous,paths);
		      paths.add(pthb);
		    }
		      prolongatePath(pth,previous,paths);
		    }
		  }
	  	  /**
	  	   * Principal Dijkstra procedure
	  	   *    2     for each vertex v in V[G]                        // Initializations
		   *  	3           d[v] := infinity                           // Unknown distance function from s to v
		   *  	4           previous[v] := undefined
		   *  	5     d[s] := 0                                        // Distance from s to s
		   *  	6     S := empty set                                   // Set of all visited vertices
		   *  	7     Q := V[G]                                        // Set of all unvisited vertices
		   *  	8     while Q is not an empty set                      // The algorithm itself
		   *  	9           u := Extract_Min(Q)                        // Remove best vertex from priority queue
		   * 	10           S := S union {u}                           // Mark it 'visited'
		   * 	11           for each edge (u,v) outgoing from u
		   * 	12                  if d[u] + w(u,v) < d[v]             // Relax (u,v)
		   * 	13                        d[v] := d[u] + w(u,v)
		   *	14                        previous[v] := u
	  	   * 
	  	   */
		  public static void DijkstraProcedure(Graph graph, int isource, Vector<Integer> iends, double d[], Vector<Vector<Integer>> previous, boolean directedGraph, double searchRadius){
		    Vector<Vector<Edge>> neighbours = neighboursOfNodeHash(graph,directedGraph);
		    graph.createIndexNodeHash();

		    Set S = new HashSet();
		    Set Q = new HashSet();
		    for(int i=0;i<graph.Nodes.size();i++){
		      d[i] = Double.POSITIVE_INFINITY;
		      Q.add(new Integer(i));
		    }
		    HashSet<Integer> non_empties = new HashSet<Integer>();
		    d[isource] = 0;
		    non_empties.add(new Integer(isource));
		    
		    long extractingNodesTime = 0;
		    long unionOfSetsTime = 0;
		    long time = (new Date()).getTime();
		    Vector temp = new Vector();
		    
		    if(verbose)
		    	System.out.print(graph.Nodes.get(isource).Id+"\t");
		    
		    while(Q.size()>0){
		      if(Q.size()==((int)(Q.size()*0.001))*1000){
		    	 if(verbose) 
		    		 System.out.print(Q.size()+"\t");
		         //System.out.flush();
		      }
		      Date ttemp = new Date();
		      Set u = extractClosestNodes(Q,d,non_empties,temp, searchRadius);
		      extractingNodesTime+=((new Date()).getTime()-ttemp.getTime());
		      //System.out.println(Q.size()+"\t"+u.size());
		      if(u.size()==0) break;
		      S = logic.Utils.UnionOfSets(S,u);
		      int best = ((Integer)u.iterator().next()).intValue();
		      Vector<Edge> neigh = neighbours.get(best);
		      for(int i=0;i<neigh.size();i++){
		         Edge e = neigh.get(i);
		         int v = -1;
		         if(e.Node1.Id.equals(((Node)graph.Nodes.get(best)).Id))
		           v = graph.getNodeIndex(e.Node2.Id); 
		         else
		           v = graph.getNodeIndex(e.Node1.Id);
		         double weight = e.weight;
		         if(d[v]>d[best]+weight+1e-8){ // new path
		           Vector<Integer> prev = previous.get(v);
		           prev.clear();
		           prev.add(new Integer(best));
		           d[v] = d[best]+weight;
		           non_empties.add(v);
		         }else
		         if(Math.abs(d[v]-d[best]-weight)<1e-8){ // equivalent path
		           Vector<Integer> prev = previous.get(v);
		           prev.add(new Integer(best));
		           d[v] = d[best]+weight;
		           non_empties.add(v);
		         }
		      }
		    }
		    if(verbose)
		    	System.out.print("Total time: "+((new Date()).getTime()-time)+"\t");
		    if(iends.size()==1)
		    	if(verbose)
		    		System.out.println("Distance: "+graph.Nodes.get(iends.get(0)).Id+"\t"+":"+d[iends.get(0)]);
		    else{
		    	if(verbose)
		    		System.out.print("Distances: ");
		    	for(int k=0;k<iends.size();k++)
		    		if(verbose)
		    			System.out.print(graph.Nodes.get(iends.get(k)).Id+":"+d[iends.get(k)]+"\t");
		    	if(verbose)
		    		System.out.println();
		    }
		    //System.out.println("extractingNodesTime:"+extractingNodesTime);
		    /*try{
		    FileWriter fw = new FileWriter("c:/datas/binomtest/temp.xls");
		    for(int kk=0;kk<temp.size();kk++)
		    	fw.write(""+((Long)temp.get(kk)).intValue()+"\n");
		    fw.close();
		    }catch(Exception e){
		    	e.printStackTrace();
		    }*/
		  }
		  
		  /**
		   * Sub-routine for Dijkstra's algorithm
		   * Returns vector (of length number of nodes) of vectors of outgoing edges numbers
		   */
		  public static Vector<Vector<Edge>> neighboursOfNodeHash(Graph graph, boolean directedGraph){
		    Vector<Vector<Edge>> v = new Vector<Vector<Edge>>();
		    graph.calcNodesInOut();
		    for(int i=0;i<graph.Nodes.size();i++){
		      Node n = (Node)graph.Nodes.get(i);
		      Vector<Edge> neigh = new Vector<Edge>();
		      v.add(neigh);
		      for(int j=0;j<n.outcomingEdges.size();j++){ 
		    	  Edge e = (Edge)n.outcomingEdges.get(j);
		    	  neigh.add(e);
		      }
		      if(!directedGraph)
			      for(int j=0;j<n.incomingEdges.size();j++){ 
			    	  Edge e = (Edge)n.incomingEdges.get(j);
			    	  neigh.add(e);
			      }		    	  
		    }
		    return v;
		  }
		  /**
		   * Sub-routine for Dijkstra algorithm
		   * search for an items with minimum d[item]  and removes from Q
		   */
		  public static Set extractClosestNodes(Set Q, double d[], HashSet<Integer> non_empties, Vector temp, double searchRadius){
			Date time = new Date();
		    // here we extract only one minimum at random!!!
			//Integer q[] = new Integer[Q.size()];
			Integer ne[] = new Integer[non_empties.size()];
			Vector res = new Vector();
			//Q.toArray(q);
			non_empties.toArray(ne);
			double minv = Double.POSITIVE_INFINITY;
			//HashSet non_empties1 = new HashSet();			
			//Iterator<Integer> it = non_empties.iterator();

			//for(int i=0;i<q.length;i++){
			//while(it.hasNext()){
			//temp.add(q.length);

			for(int i=0;i<ne.length;i++){
				  Integer I = ne[i];
			      if(d[I.intValue()]<minv)
			        minv = d[I.intValue()];
			      //if(d[I.intValue()]<Double.POSITIVE_INFINITY)
			      //  non_empties.add(I);
			}
		    //it = non_empties.iterator();
			
			if(minv>searchRadius){ // finish the search
				//System.out.println("Search radius is achived "+minv);
				Q.clear();
			}
			//while(it.hasNext()){
			for(int i=0;i<ne.length;i++){
				  Integer I = ne[i];
			      if(Math.abs(d[I.intValue()]-minv)<1e-8)
			        { res.add(I); break; }
			}
			
			HashSet res_set = new HashSet();
			for(int i=0;i<res.size();i++){
		      Q.remove(res.get(i));
		      non_empties.remove(res.get(i));
		      res_set.add(res.get(i));
			}
			temp.add(new Long((new Date()).getTime()-time.getTime()));			
		    return res_set;
		  }
	
		  /**
		   * 
		   * @param graph
		   * @param n
		   * @return Connected component containing node n
		   */
		  public static Graph GetConnectedComponent(Graph graph,Node n){
		    Graph gr = new Graph();
		    gr.Nodes.add(n);
		    //System.out.println(n.NodeLabel);
		    GetNodeNeighbours(graph,n,gr);
		    return gr;
		  }

		  private static void GetNodeNeighbours(Graph graph, Node n, Graph exclude){
		    //Graph v = new Graph();
		    for(int i=0;i<graph.Edges.size();i++){
		      Edge e = (Edge)graph.Edges.elementAt(i);
		      if(e.Node1.Id.equals(n.Id)){
		        int ek = exclude.getEdgeIndex(e.Id);
		        if(ek<0){
		          exclude.Edges.add(e);
		          //System.out.println(e.EdgeLabel);
		        }
		        int nk = exclude.getNodeIndex(e.Node2.Id);
		        if(nk<0){
		          exclude.Nodes.add(e.Node2);
		          //System.out.println(e.Node2.NodeLabel);
		          GetNodeNeighbours(graph,e.Node2,exclude);
		        }
		      }
		      if(e.Node2.Id.equals(n.Id)){
		        int ek = exclude.getEdgeIndex(e.Id);
		        if(ek<0){
		          exclude.Edges.add(e);
		          //System.out.println(e.EdgeLabel);
		        }
		        int nk = exclude.getNodeIndex(e.Node1.Id);
		        if(nk<0){
		          exclude.Nodes.add(e.Node1);
		          //System.out.println(e.Node1.NodeLabel);
		          GetNodeNeighbours(graph,e.Node1,exclude);
		        }
		      }
		    }
		    //return v;
		  }
		  
		  private static Graph SelectInBoundaryLayer(Graph graph){
			    Graph gr = new Graph();
			    for(int i=0;i<graph.Nodes.size();i++){
			      Node n = (Node)graph.Nodes.elementAt(i);
			      int incoming = -1;
			      for(int j=0;j<graph.Edges.size();j++){
			        Edge e = (Edge)graph.Edges.elementAt(j);
			        if((incoming!=1)&&(e.Node1.Id.equals(n.Id)))
			          incoming = 0;
			        //if((incoming!=1)&&(e.Node1.Id.equals(n.Id)))
			        //  incoming = 0;
			        if(e.Node2.Id.equals(n.Id))
			          incoming = 1;
			      }
			      if(incoming==0)
			        gr.Nodes.add(n);
			    }
			    return gr;
			  }

			  private static Graph SelectInBoundary(Graph graph,Vector interfaceNodes){
			    Graph grf = graph.makeCopy();
			    Graph gr = new Graph();
			    Graph inl = null;
			    while((inl=SelectInBoundaryLayer(grf)).Nodes.size()>0){
			      gr.addNodes(inl);
			      grf.subtractNodes(inl);
			      grf.removeObsoleteEdges();
			      Graph grh = grf.getHangingNodes();
			      grf.subtractNodes(grh);
			      gr.addNodes(grh);
			      grf.removeObsoleteEdges();
			    }
			    GetNextNodes(gr,graph,true,interfaceNodes);
			    gr.addConnections(graph);
			    return gr;
			  }
			  
			  private static void GetNextNodes(Graph nodes, Graph graph, boolean incoming, Vector interfaceNodes){
				  graph.calcNodesInOut();
				  for(int i=0;i<nodes.Nodes.size();i++){
					  Node n = (Node)nodes.Nodes.get(i);
					  Node ng = graph.getNode(n.Id);
					  if(incoming){
					  for(int j=0;j<ng.outcomingEdges.size();j++){
						  Edge e = (Edge)ng.outcomingEdges.get(j);
						  interfaceNodes.add(e.Node2);
					  }
					  }else{
					  for(int j=0;j<ng.incomingEdges.size();j++){
						  Edge e = (Edge)ng.incomingEdges.get(j);
						  interfaceNodes.add(e.Node1);
					  }						  
					  }
				  }
			  }

			  private static Graph SelectOutBoundaryLayer(Graph graph){
			    Graph gr = new Graph();
			    for(int i=0;i<graph.Nodes.size();i++){
			      Node n = (Node)graph.Nodes.elementAt(i);
			      //if(n.NodeLabel.equals("Kinase"))
			      //  System.out.println(n.NodeLabel);
			      int outcoming = -1;
			      for(int j=0;j<graph.Edges.size();j++){
			        Edge e = (Edge)graph.Edges.elementAt(j);
			        if((outcoming!=1)&&(e.Node2.Id.equals(n.Id)))
			          outcoming = 0;
			        //if((incoming!=1)&&(e.Node1.Id.equals(n.Id)))
			        //  incoming = 0;
			        if(e.Node1.Id.equals(n.Id))
			          outcoming = 1;
			      }
			      if(outcoming==0)
			        gr.Nodes.add(n);
			    }
			    return gr;
			  }

			  private static Graph SelectOutBoundary(Graph graph, Vector interfaceNodes){
			    Graph grf = graph.makeCopy();
			    Graph gr = new Graph();
			    Graph inl = null;
			    while((inl=SelectOutBoundaryLayer(grf)).Nodes.size()>0){
			      gr.addNodes(inl);
			      grf.subtractNodes(inl);
			      grf.removeObsoleteEdges();
			      Graph grh = grf.getHangingNodes();
			      grf.subtractNodes(grh);
			      gr.addNodes(grh);
			      grf.removeObsoleteEdges();
			    }
			    GetNextNodes(gr,graph,false,interfaceNodes);
			    gr.addConnections(graph);
			    return gr;
			  }
		  

				  private static void GetAllCycles(Graph graph, Node n, Vector cyc){
				    for(int i=0;i<n.outcomingEdges.size();i++){
				      Edge e = (Edge)n.outcomingEdges.elementAt(i);
				      Graph gc = graph.makeCopy();
				      //gc.name+=""+i;
				      gc.startId = n.Id;
				      System.out.println("Start "+n.NodeLabel);
				      FollowCycle(gc,e.Node2,cyc);
				      }
				  }

				  private static void FollowCycle(Graph graph, Node n, Vector cyc){
				    graph.selectedIds.add(n.Id);
				    System.out.print("\t"+n.Id);
				    if(n.Id.equals(graph.startId)){
				      cyc.add(graph.getSelectedNodes());
				      System.out.println("\tEnd "+n.NodeLabel);
				    }else{

				    Vector contNum = new Vector();
				    for(int i=0;i<n.outcomingEdges.size();i++){
				      Edge e = (Edge)n.outcomingEdges.elementAt(i);
				      if(!(graph.selectedIds.contains(e.Node2.Id)))
				        contNum.add(e);
				    }
				    /*if(contNum.size()==1)
				      System.out.println("Follow "+n.NodeLabel+" sel."+selectedIds.size()+" "+name);
				    else
				      System.out.println("Fork"+contNum.size()+": Follow "+n.NodeLabel);
				    printGraphList();*/
				    if(contNum.size()>0){
				      Edge e = null;
				      for(int i=1;i<contNum.size();i++){
				        e = (Edge)contNum.elementAt(i);
				        Graph grc = graph.makeCopy();
				        //grc.name+=""+i;
				        //System.out.println("\nStarting new cycle");
				        FollowCycle(grc,e.Node2,cyc);
				      }
				      e = (Edge)contNum.elementAt(0);
				      FollowCycle(graph,e.Node2,cyc);
				    }
				    }
				  }
	
				  private static int[][] InclusionMatrix(Vector graphs){
					    int inclusionMatrix[][] = new int[graphs.size()][graphs.size()];
					    for(int i=0;i<graphs.size();i++){
					      Graph gi = (Graph)graphs.elementAt(i);
					      for(int j=0;j<graphs.size();j++){
					        Graph gj = (Graph)graphs.elementAt(j);
					        inclusionMatrix[i][j] = gi.includesNodes(gj).size();
					        if(gi.identicalNodes(gj))
					          inclusionMatrix[i][j] = -1;
					      }
					  }
					   return inclusionMatrix;
					  }

					  private static Vector CombineIncludedGraphs(Vector graphs){
					    Vector newGraphs = new Vector();
					    Graph ident = new Graph();
					    int inclusionMatrix[][] = InclusionMatrix(graphs);
					    // First, we identify identical graphs and combine them
					    for(int k=0;k<graphs.size();k++){
					      Graph gk = (Graph)graphs.elementAt(k);
					      //System.out.println(""+k+":"+gk.name);
					      Node n = ident.getCreateNode(gk.name);
					      n.NodeLabel = gk.name;
					      n.NodeClass = 2;
					      n.link = gk;
					    }
					    for(int i=0;i<graphs.size();i++){
					      Graph gi = (Graph)graphs.elementAt(i);
					      for(int j=i+1;j<graphs.size();j++) if(i!=j) {
					         Graph gj = (Graph)graphs.elementAt(j);
					         if(inclusionMatrix[i][j]==-1){
					           Edge e = new Edge();
					           int ki = ident.getNodeIndex(gi.name);
					           int kj = ident.getNodeIndex(gj.name);
					           e.Node1 = (Node)ident.Nodes.elementAt(kj);
					           e.Node2 = (Node)ident.Nodes.elementAt(ki);
					           e.EdgeLabel = "IDENTICAL";
					           ident.Edges.add(e);
					         }
					      }
					    }
					    //ident.saveAsCytoscapeSif("c_ident.sif");
					    Vector comps = ConnectedComponents(ident);
					    Vector graphsUnique = new Vector();
					    for(int i=0;i<comps.size();i++){
					      Graph gr = (Graph)comps.elementAt(i);
					      Graph ung = (Graph)((Node)gr.Nodes.elementAt(0)).link;
					      ung.name = "";
					      for(int j=0;j<gr.Nodes.size();j++)
					        ung.name += ((Node)gr.Nodes.elementAt(j)).NodeLabel+"/";
					      ung.name = ung.name.substring(0,ung.name.length()-1);
					      graphsUnique.add(ung);
					    }

					    // Second, we get rid of all fully included graphs
					    Graph included = new Graph();
					    for(int k=0;k<graphsUnique.size();k++){
					      Graph gk = (Graph)graphsUnique.elementAt(k);
					      Node n = included.getCreateNode(gk.name);
					      n.NodeLabel = gk.name;
					      n.NodeClass = 2;
					      n.link = gk;
					    }
					    for(int i=0;i<graphsUnique.size();i++){
					      Graph gi = (Graph)graphsUnique.elementAt(i);
					      for(int j=0;j<graphsUnique.size();j++) if(i!=j) {
					         Graph gj = (Graph)graphsUnique.elementAt(j);
					         if(gj.includesNodes(gi).size()==gi.Nodes.size()){
					           Edge e = new Edge();
					           int ki = included.getNodeIndex(gi.name);
					           int kj = included.getNodeIndex(gj.name);
					           e.Node1 = (Node)included.Nodes.elementAt(ki);
					           e.Node2 = (Node)included.Nodes.elementAt(kj);
					           if(i>j)
					             e.EdgeLabel = "INCLUDED";
					           else
					             e.EdgeLabel = "INCLUDED_";
					           included.Edges.add(e);
					         }
					      }
					    }
					    //included.saveAsCytoscapeSif("c_included.sif");
					    int nodm = included.Nodes.size();
					    ContractGraph(included);
					    int nod = included.Nodes.size();
					    while(nodm!=nod){
					      ContractGraph(included);
					      nodm = nod;
					      nod = included.Nodes.size();
					    }

					    for(int i=0;i<included.Nodes.size();i++){
					      Node n = (Node)included.Nodes.elementAt(i);
					      newGraphs.add(n.link);
					    }

					    return newGraphs;
					  }

					  private static Graph ContractGraph(Graph graph){
					    graph.calcNodesInOut();
					    Graph copy = graph.makeCopy();
					    copy.Edges.clear();
					    for(int i=0;i<graph.Nodes.size();i++){
					      Node n = (Node)graph.Nodes.elementAt(i);
					      if((n.outcomingEdges.size()>0)&&(n.incomingEdges.size()==0)){
					        for(int j=0;j<n.outcomingEdges.size();j++){
					          Edge e = (Edge)n.outcomingEdges.elementAt(j);
					          e.Node2.NodeLabel+="/"+e.Node1.NodeLabel;
					          int k = copy.getNodeIndex(e.Node1.Id);
					          if(k>=0)
					            copy.Nodes.remove(k);
					        }
					      }
					    }
					    copy.removeObsoleteEdges();
					    copy.addConnections(graph);
					    return copy;
					  }

					  private static Graph ContractGraphOfGraph(Graph graph){
					    graph.calcNodesInOut();
					    Graph copy = graph.makeCopy();
					    copy.Edges.clear();
					    for(int i=0;i<graph.Nodes.size();i++){
					      Node n = (Node)graph.Nodes.elementAt(i);
					      if((n.outcomingEdges.size()>0)&&(n.incomingEdges.size()==0)){
					        for(int j=0;j<n.outcomingEdges.size();j++){
					          Edge e = (Edge)n.outcomingEdges.elementAt(j);
					          e.Node2.NodeLabel+="/"+e.Node1.NodeLabel;

					          Graph gr1 = (Graph)e.Node1.link;
					          Graph gr2 = (Graph)e.Node2.link;
					          gr2.addNodes(gr1);
					          gr2.addEdges(gr1);

					          int k = copy.getNodeIndex(e.Node1.Id);
					          if(k>=0){
					            copy.Nodes.remove(k);
					          }
					        }
					      }
					    }
					    copy.removeObsoleteEdges();
					    copy.addConnections(graph);
					    return copy;
					  }

					  /**
					   * Clusters graphs with node overlap more than threshold
					   * @param graphs
					   * @param thresh
					   * @return List of graph clusters
					   */
					  public static Vector<Graph> CombineIncludedGraphsApprox(Vector<Graph> graphs, float thresh){
					    Vector<Graph> newGraphs = new Vector<Graph>();
					    Graph ident = new Graph();

					    int inclusionMatrix[][] = new int[graphs.size()][graphs.size()];
					    for(int i=0;i<graphs.size();i++){
					      Graph gi = (Graph)graphs.elementAt(i);
					      for(int j=0;j<graphs.size();j++){
					        Graph gj = (Graph)graphs.elementAt(j);
					        inclusionMatrix[i][j] = gi.includesNodes(gj).size();
					        if(gi.includesNodesPercentage(gj)>=thresh)
					          if(gj.includesNodesPercentage(gi)>=thresh)
					            inclusionMatrix[i][j] = -1;
					      }
					   }

					    // First, we identify approx. identical graphs and combine them
					    for(int k=0;k<graphs.size();k++){
					      Graph gk = (Graph)graphs.elementAt(k);
					      //System.out.println(""+k+":"+gk.name);
					      Node n = ident.getCreateNode(gk.name);
					      n.NodeLabel = gk.name;
					      n.NodeClass = 2;
					      n.link = gk;
					    }
					    for(int i=0;i<graphs.size();i++){
					      Graph gi = (Graph)graphs.elementAt(i);
					      for(int j=i+1;j<graphs.size();j++) if(i!=j) {
					         Graph gj = (Graph)graphs.elementAt(j);
					         if(inclusionMatrix[i][j]==-1){
					           Edge e = new Edge();
					           int ki = ident.getNodeIndex(gi.name);
					           int kj = ident.getNodeIndex(gj.name);
					           e.Node1 = (Node)ident.Nodes.elementAt(kj);
					           e.Node2 = (Node)ident.Nodes.elementAt(ki);
					           e.EdgeLabel = "IDENTICAL";
					           ident.Edges.add(e);
					         }
					      }
					    }
					    //ident.saveAsCytoscapeSif("c_ident1.sif");
					    Vector comps = ConnectedComponents(ident);
					    Vector graphsUnique = new Vector();
					    for(int i=0;i<comps.size();i++){
					      Graph gr = (Graph)comps.elementAt(i);
					      Graph ung = (Graph)((Node)gr.Nodes.elementAt(0)).link;
					      ung.name = "";
					      for(int j=0;j<gr.Nodes.size();j++)
					        ung.name += ((Node)gr.Nodes.elementAt(j)).NodeLabel+"/";
					      ung.name = ung.name.substring(0,ung.name.length()-1);
					      for(int j=1;j<gr.Nodes.size();j++){
					        Graph unga = (Graph)((Node)gr.Nodes.elementAt(j)).link;
					        ung.addNodes(unga);
					        ung.addEdges(unga);
					      }
					      graphsUnique.add(ung);
					    }

					    // Second, we get rid of all fully approx. included graphs
					    Graph included = new Graph();
					    for(int k=0;k<graphsUnique.size();k++){
					      Graph gk = (Graph)graphsUnique.elementAt(k);
					      Node n = included.getCreateNode(gk.name);
					      n.NodeLabel = gk.name;
					      n.NodeClass = 2;
					      n.link = gk;
					    }
					    for(int i=0;i<graphsUnique.size();i++){
					      Graph gi = (Graph)graphsUnique.elementAt(i);
					      for(int j=0;j<graphsUnique.size();j++) if(i!=j) {
					         Graph gj = (Graph)graphsUnique.elementAt(j);
					         if(gj.includesNodesPercentage(gi)>=thresh){
					           Edge e = new Edge();
					           int ki = included.getNodeIndex(gi.name);
					           int kj = included.getNodeIndex(gj.name);
					           e.Node1 = (Node)included.Nodes.elementAt(ki);
					           e.Node2 = (Node)included.Nodes.elementAt(kj);
					           if(i>j)
					             e.EdgeLabel = "INCLUDED";
					           else
					             e.EdgeLabel = "INCLUDED_";
					           included.Edges.add(e);
					         }
					      }
					    }
					    //included.saveAsCytoscapeSif("c_included2.sif");
					    int nodm = included.Nodes.size();
					    included = ContractGraphOfGraph(included);
					    //included.saveAsCytoscapeSif("c_included2c.sif");
					    int nod = included.Nodes.size();
					    while(nodm!=nod){
					      included = ContractGraphOfGraph(included);
					      nodm = nod;
					      nod = included.Nodes.size();
					    }

					    for(int i=0;i<included.Nodes.size();i++){
					      Node n = (Node)included.Nodes.elementAt(i);
					      Graph g = (Graph)n.link;
					      g.name = n.NodeLabel;
					      newGraphs.add((Graph)n.link);
					    }

					    return newGraphs;
					  }
					  
					  /**
					   * 
					   * @param v
					   * @return The biggest graph in the list v
					   */
					  public static Graph GetBiggestGraph(Vector v){
						    Graph res = null;
						    int maxs = -1;
						    for(int i=0;i<v.size();i++){
						      Graph gr = (Graph)v.elementAt(i);
						      if(gr.Nodes.size()>maxs){
						        res = gr;
						        maxs = gr.Nodes.size();
						      }
						    }
						    return res;
						  }
	  /**
	   * Floyd-Warshall algorithm for finding all shortest path simultaneously
	   * 
	   */
	  //public static 
	
	  /**
	   * Tresch's algorithm for transitive graph reduction
	   * From Tresch et al., JCompBiol V14, N9 (2007)
	   * 
	   * Takes as arguments a graph to be pruned, 'probabilities' of positive and negative regulations
	   * (as Vectors of Float in the order, corresponding to the graph Edges), depth is the depth of the
	   * directed path search (for example, if equals number of Edges then this is complete pruning, if equals
	   * 2 then it searches for edge 'explanations' with only one intermediate node)
	   * 
	   * The algorithm returns a pruned Graph, and also fills the Vector of 'explanations' (paths as graphs) in the order of the 
	   * initial graph edges
	   * 
	   */		  
	  public static Graph TreschAlgorithm(Graph graph, Vector<Float> pplus, Vector<Float> pminus, Vector<Graph> explanations, float delta){
		  
		  int N = graph.Nodes.size();
		  
		  float Pplus[][] = new float[N][N];
		  float Pminus[][] = new float[N][N];
		  float Pr[][] = new float[N][N];
		  float Pd[][] = new float[N][N];
		  float W[][] = new float[N][N];
		  float T[][] = new float[N][N];
		  
		  for(int i=0;i<graph.Edges.size();i++){
			  Edge e = (Edge)graph.Edges.get(i);
			  Pplus[graph.getNodeIndex(e.Node1.Id)][graph.getNodeIndex(e.Node2.Id)] = pplus.get(i);
			  Pminus[graph.getNodeIndex(e.Node1.Id)][graph.getNodeIndex(e.Node2.Id)] = pminus.get(i);
		  }
		  for(int i=0;i<N;i++)for(int j=0;j<N;j++){
			Pr[i][j] = Pplus[i][j]+Pminus[i][j];  
			Pd[i][j] = Pplus[i][j]-Pminus[i][j];
			if(Pr[i][j]>0)
				W[i][j] = -(float)Math.log(Pr[i][j]);
			else
				W[i][j] = Float.POSITIVE_INFINITY;
			if(Math.abs(Pd[i][j])>0)
				T[i][j] = -(float)Math.log(Math.abs(Pd[i][j]));
			else
				T[i][j] = Float.POSITIVE_INFINITY;
		  }
		  
		  int Pred[][] = new int[N][N];
		  float Wplus[][] = new float[N][N];
		  float Wminus[][] = new float[N][N];
		  float Tplus[][] = new float[N][N];
		  float Tminus[][] = new float[N][N];
		  
		  for(int j=0;j<N;j++)for(int k=0;k<N;k++){
			Pred[j][k] = j;
			if(Pd[j][k]>=0) Wplus[j][k] = W[j][k]; else Wplus[j][k] = Float.POSITIVE_INFINITY;
			if(Pd[j][k]<0)  Wminus[j][k] = W[j][k]; else Wminus[j][k] = Float.POSITIVE_INFINITY;
			
			if(Pd[j][k]>=0) Tplus[j][k] = T[j][k]; else Tplus[j][k] = Float.POSITIVE_INFINITY;
			if(Pd[j][k]<0)  Tminus[j][k] = T[j][k]; else Tminus[j][k] = Float.POSITIVE_INFINITY;
		  }
		  
		  
		  // now the modified Floyd-Warshall
		  for(int k=0;k<N;k++){
			  for(int j=0;j<N;j++)for(int l=0;l<N;l++)if(j!=l){
				  for(int s1=+1;s1<=1;s1+=2)for(int s2=+1;s2<=1;s2+=2){
					  int s = s1*s2;
					  float Ws[][] = s>0?Wplus:Wminus;
					  float Ws1[][] = s1>0?Wplus:Wminus;
					  float Ws2[][] = s2>0?Wplus:Wminus;
					  float Ts[][] = s>0?Tplus:Tminus;
					  float Ts1[][] = s1>0?Tplus:Tminus;
					  float Ts2[][] = s2>0?Tplus:Tminus;
					  if((Ws[j][l]>Ws1[j][k]+Ws2[k][l])&&(Ts[j][l]>Ts1[j][k]+Ts2[k][l])){
						  Ws[j][l] = Ws1[j][k]+Ws2[k][l];
						  Ts[j][l] = Ts1[j][k]+Ts2[k][l];
						  Pred[j][l] = k;
					  }
				  }
			  }
		  }
		  
		  boolean Res[][] = new boolean[N][N];
		  for(int j=0;j<N;j++)for(int k=0;k<N;k++)if(j!=k){
			  Res[j][k] = (W[j][k]<Float.POSITIVE_INFINITY && T[j][k]<Float.POSITIVE_INFINITY)&&
			              (
			              ((Pd[j][k]>0)&&(Wplus[j][k]<=W[j][k])&&(Tplus[j][k]<=T[j][k]))||	  
			              ((Pd[j][k]<0)&&(Wminus[j][k]<=W[j][k])&&(Tminus[j][k]<=T[j][k]))
			              );
		  }
		  
		  Graph resGraph = new Graph();
		  

		  for(int j=0;j<N;j++) System.out.print("\t"+graph.Nodes.get(j).Id); System.out.println(); 
		  for(int j=0;j<N;j++){System.out.print(graph.Nodes.get(j).Id+"\t");  {for(int k=0;k<N;k++){
			  System.out.print(Wplus[j][k]+"("+W[j][k]+")\t");
		  } System.out.println(); }}
		  
		  System.out.println();
		  
		  for(int j=0;j<N;j++) System.out.print("\t"+graph.Nodes.get(j).Id); System.out.println(); 
		  for(int j=0;j<N;j++){System.out.print(graph.Nodes.get(j).Id+"\t");  {for(int k=0;k<N;k++){
			  System.out.print(Res[j][k]+"\t");
		  } System.out.println(); }}
		  
		  for(int i=0;i<graph.Nodes.size();i++) resGraph.addNode((Node)graph.Nodes.get(i));
		  for(int i=0;i<graph.Edges.size();i++){
			  Edge e = (Edge)graph.Edges.get(i);
			  int k = graph.getNodeIndex(e.Node1.Id);
			  int l = graph.getNodeIndex(e.Node2.Id);
			  if(Res[k][l])
				  resGraph.addEdge(e);
		  }
		  
		  return resGraph;
	  }
	  
	  /**
	   * 
	   * 
	   * 
	   */
	  public static Vector<Float> SimpleEdgePruning(Graph graph, Vector<Float> pplus, Vector<Float> pminus, int depth, Vector<Graph> explanations){
		  Vector<Float> edgeRedundancies = new Vector<Float>();
		  float fedgeRedundancies[] = new float[graph.Edges.size()];
		  for(int i=0;i<graph.Edges.size();i++){
			  Edge e = (Edge)graph.Edges.get(i);
			  Node source = e.Node1;
			  Node target = e.Node2;
			  HashSet<Node> trgts = new HashSet<Node>();
			  trgts.add(target);
			  Vector<Graph> paths = FindAllPaths(graph, source, trgts, true, (double)depth,false);
			  System.out.println("Edge "+e.toString());
			  for(int k=0;k<paths.size();k++){
				  Graph gr = paths.get(k);
				  if(gr.Nodes.size()>2){
				  Path p = new Path(gr,source.Id);
				  System.out.println((k+1)+": "+p.toString());
				  if(logic.Utils.getPathSign(p)==logic.Utils.getEdgeSign(e))
					  fedgeRedundancies[i]++;
				  if(logic.Utils.getPathSign(p)*logic.Utils.getEdgeSign(e)<0)
					  fedgeRedundancies[i]--;
				  }
			  }
			  System.out.println("Edge "+e.toString()+" redundancy = "+fedgeRedundancies[i]);
		  }
		  edgeRedundancies.clear();
		  for(int i=0;i<fedgeRedundancies.length;i++)
			  edgeRedundancies.add(fedgeRedundancies[i]);
		  return edgeRedundancies;
	  }
	  
	  /**
	   * 
	   */
	  public static Graph PruneEdges(Graph graph, Vector<Float> edgeRedundancies, float thresh){
		  Graph res = new Graph();
		  res = graph.makeCopy();
		  for(int i=0;i<graph.Edges.size();i++){
			  if(edgeRedundancies.get(i)>=thresh)
				  res.removeEdge(graph.Edges.get(i).Id);
		  }
		  return res;
	  }
	  
	  public static Vector<Float> SimpleEdgePruningLOOCrossValidation(Graph graph, Vector<Float> pplus, Vector<Float> pminus, int depth, Vector<Graph> explanations){
		  Vector<Float> edgeRedundancies = new Vector<Float>();
		  float fedgeRedundancies[] = new float[graph.Edges.size()];
		  
		  return edgeRedundancies;
	  }
	  
	  
	  /**
	   * Find miminum hitting set for a graph with heuristic algorithm.
	   * From Vazquez, A. BMC Systems Biology 2009 3:81
	   * 
	   * @param gr graph (bipartite)
	   * @param sourceNodes list of source nodes
	   * @param targetNodes list of target nodes
	   * @return a minimal hitting set as a list of node IDs
	   */
	  public static HashSet<Node> HittingSetHighestDegreeFirst(Graph gr, ArrayList<Node> sourceNodes, ArrayList<Node> targetNodes) {
		  
		  // minimal cut set result
		  HashSet<Node> covered = new HashSet<Node>();

		  // set graph statistics
		  gr.calcNodesInOut();
		  
		  while (targetNodes.size()>0) {
			  //System.out.println("source size"+sourceNodes.size());

			  // determine source nodes having max degree
			  int maxDeg = 0;
			  for (Node n : sourceNodes) {
				  //Node n = g.getNode(id);
				  if (n.outcomingEdges.size()>maxDeg)
					  maxDeg=n.outcomingEdges.size();
			  }
			  //System.out.println("maxDeg="+maxDeg);

			  // select nodes having max degree
			  ArrayList<Node> selected = new ArrayList<Node>(); 
			  for (Node n : sourceNodes) {
				  //Node n = g.getNode(id);
				  if (n.outcomingEdges.size() == maxDeg) {
					  selected.add(n);
				  }
			  }
			  //System.out.println("Selection size: "+selected.size());

			  // select one of them randomly
			  Random rand = new Random();
			  int idx = rand.nextInt(selected.size());
			  //System.out.println("selected: "+selected.get(idx).Id);

			  // set it covered 
			  covered.add(selected.get(idx));
			  Node snode = gr.getNode(selected.get(idx).Id);

			  // remove targets of selected node from the graph
			  for (Edge e : snode.outcomingEdges) {
				  targetNodes.remove(targetNodes.indexOf(e.Node2));
				  gr.removeNode(e.Node2.Id);
			  }

			  // remove selected node
			  gr.removeNode(snode.Id);
			  sourceNodes.remove(sourceNodes.indexOf(snode));

			  // update graph
			  gr.removeObsoleteEdges();

			  // re-calculate stats
			  gr.calcNodesInOut();
			  
//			  System.out.println("--");
//			  for (Edge e : gr.Edges) {
//				  System.out.println(e.Node1.Id+":"+e.Node2.Id);
//			  }
//			  System.out.println("--");
			  
			  //System.out.println(sourceNodes.size());
			  //System.out.println(targetNodes.size());
		  }

		  return covered;
	  }
	  
	  public static int[][] GetAjacencyMatrix(Graph gr){
		  int am[][] = new int[gr.Nodes.size()][gr.Nodes.size()];
		  for(Edge e: gr.Edges){
			  int i = gr.Nodes.indexOf(e.Node1);
			  int j = gr.Nodes.indexOf(e.Node2);
			  am[i][j] = 1;
			  am[j][i] = 1;
		  }
		  return am;
	  }
	  
	  public static int[][] GraphLaplacian(int adjacencyMatrix[][]){
		  int L[][] = new int[adjacencyMatrix.length][adjacencyMatrix.length];
		  int connectivity[] = new int[adjacencyMatrix.length];
		  for(int i=0;i<adjacencyMatrix.length;i++){
			  for(int j=0;j<adjacencyMatrix.length;j++)
				  connectivity[i]+=adjacencyMatrix[i][j];
		  }
		  for(int i=0;i<L.length;i++)
			  for(int j=0;j<L.length;j++){
				  if(i==j)
					  L[i][j] = connectivity[i];
				  else
					  L[i][j] = -adjacencyMatrix[i][j];
			  }
		  return L;
	  }
	  

					  
}
