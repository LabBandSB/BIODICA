package fr.curie.BIODICA;

import java.io.*;
import java.util.*;

public class Path {

	// This should be set up
	public Graph graph = null;
	public int source = -1;
	public int target = -1;
	
	public Vector<Node> nodeSequence = new Vector<Node>();

	// This is calculated by calcPathProperties
	public int length = 0;
	public float influence = 0;
	public String label = null;
	
	// This is calculated by getPathConsistencyAndSummaryActivity
	public int consistency = 0; // 1 - all active nodes on the pathway has the same influence type, -1 - not the same, 0 - not checked 
	public float summaryActivity = 0; // sum of all influences of active nodes along the path
	public int numberOfActiveNodes = 0; // sum of all influences of active nodes along the path
	
	
	public boolean selected = true;
	
	public Path(){
		
	}
	
	public Path(Graph _graph, String sourceNodeId){
		graph = _graph;
		source = graph.getNodeIndex(sourceNodeId);
	}
	
	public Vector<Edge> getAsEdgeVector(){
		Vector<Edge> edges = new Vector<Edge>();
		Node node = graph.Nodes.get(source);
		boolean nocontinuation = false;
		graph.calcNodesInOut();
		Edge e = null;
		while(!nocontinuation){
			if(node.outcomingEdges.size()==0){
				nocontinuation = true;
				if(e!=null)
					target = graph.getNodeIndex(e.Node2.Id);
			}else{
				e = (Edge)node.outcomingEdges.get(0);
				node = e.Node2;
				edges.add(e);
			}
		}
		return edges;
	}
	
	public String toString(){
		String res = "";
		Vector<Edge> edges = getAsEdgeVector();
		for(int i=0;i<edges.size();i++){
			res+=((Edge)edges.get(i)).Node1.Id;
			if(fr.curie.BIODICA.Utils.getEdgeSign((Edge)edges.get(i))==1)
				res+="->";
			if(fr.curie.BIODICA.Utils.getEdgeSign((Edge)edges.get(i))==-1)
				res+="-|";
			if(fr.curie.BIODICA.Utils.getEdgeSign((Edge)edges.get(i))==0)
				res+="-.";
		}
		if(edges.size()>0)
			res+=((Edge)edges.get(edges.size()-1)).Node2.Id;
		return res;
	}
	

}
