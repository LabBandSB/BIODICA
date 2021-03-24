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

package logic;

import java.io.*;
import java.util.*;

public class Graph {

  public String name = "no_name";

  public boolean useIDsForOutput = false;
  public boolean writeHangingNodes = true;

  public String startId = "";
  
  public String globalComment = "";

  public Vector<Node> Nodes = new Vector();
  public Vector<Edge> Edges = new Vector();

  public HashMap<String, Node> NodeHash = new HashMap();
  public HashMap<String, Integer> NodeIndexHash = null;
  public HashMap<String, Edge> EdgeHash = new HashMap();

  public HashSet selectedIds = new HashSet();

  public static boolean countGraphs = false;
  public static Vector allGraphs = new Vector();

  public Vector metaNodes = new Vector();

  public Graph() {
    if(countGraphs)
      allGraphs.add(this);
  }

  public static void main(String[] args) {
    Graph graph1 = new Graph();
  }


  public void saveAsCytoscapeSif(String fn){
    try{
        FileWriter fw = new FileWriter(fn);
        for(int i=0;i<Edges.size();i++){
          Edge e = (Edge)Edges.elementAt(i);
          if(!useIDsForOutput)
            fw.write(e.Node1.NodeLabel+"\t"+e.EdgeLabel+"\t"+e.Node2.NodeLabel+"\r\n");
          else
            fw.write(e.Node1.Id+"\t"+e.EdgeLabel+"\t"+e.Node2.Id+"\r\n");
        }
        if(writeHangingNodes){
          Graph gr = getHangingNodes();
          for(int i=0;i<gr.Nodes.size();i++)
            if(!useIDsForOutput)
              fw.write(((Node)gr.Nodes.elementAt(i)).NodeLabel+"\r\n");
            else
              fw.write(((Node)gr.Nodes.elementAt(i)).Id+"\r\n");
        }
        fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }

  }
  
  public String correct(String id){
	  String res = id;
	  res = Utils.replaceString(res, "\"", "");
	  res = Utils.replaceString(res, "&", "and");
	  res = Utils.replaceString(res, "<", "&lt;");
	  res = Utils.replaceString(res, ">", "&gt;");
	  res = Utils.replaceString(res, "'", "prime");
	  return res;
  }

  public void saveAsCytoscapeXGMML(String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<graph xmlns=\"http://www.cs.rpi.edu/XGMML\" id=\""+name+"\" name=\""+name+"\" label=\""+name+"\">\n");
      for(int i=0;i<Nodes.size();i++){
    	  Node n = (Node)Nodes.get(i);
    	  fw.write("\t<node id=\""+correct(n.Id)+"\" name=\""+correct(n.Id)+"\" label=\""+correct(n.NodeLabel)+"\">\n");
    	  for(int j=0;j<n.Attributes.size();j++){
    		  Attribute at = (Attribute)n.Attributes.get(j);
    		  fw.write("\t\t<att name=\""+correct(at.name)+"\" value=\""+correct(at.value)+"\" type=\"string\"/>\n");
    	  }
    	  fw.write("\t\t<graphics x=\""+n.x+"\" y=\""+n.y+"\"/>\n");
    	  fw.write("\t</node>\n");
      }
      for(int i=0;i<Edges.size();i++){
    	  Edge e = (Edge)Edges.get(i);
    	  fw.write("\t<edge id=\""+correct(e.Id)+"\" label=\""+correct(e.EdgeLabel)+"\" source=\""+correct(e.Node1.Id)+"\" target=\""+correct(e.Node2.Id)+"\">\n");
    	  for(int j=0;j<e.Attributes.size();j++){
    		  Attribute at = (Attribute)e.Attributes.get(j);
    		  fw.write("\t\t<att name=\""+correct(at.name)+"\" value=\""+correct(at.value)+"\" type=\"string\"/>\n");    		  
    	  }
    	  fw.write("\t</edge>\n");
      }
      fw.write("</graph>");
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void saveAsCytoscapeGML(String fn){
    try{
        FileWriter fw = new FileWriter(fn);
        fw.write("Creator\"Cytoscape\"\r\n");
        fw.write("Version	1.0\r\n");
        fw.write("graph\t[\r\n");
        for(int i=0;i<Nodes.size();i++){
          Node n = (Node)Nodes.elementAt(i);
          fw.write("\tnode\t[\r\n");
          fw.write("\t\troot_index\t"+i+"\r\n");
          fw.write("\t\tid\t"+i+"\r\n");
          fw.write("\t\tgraphics\t[\r\n");
          fw.write("\t\t\tx\t"+n.x+"\r\n");
          fw.write("\t\t\ty\t"+n.y+"\r\n");
          fw.write("\t\t\tw\t"+n.w+"\r\n");
          fw.write("\t\t\th\t"+n.h+"\r\n");

          int rc = n.NodeColor.getRed();
          int gc = n.NodeColor.getGreen();
          int bc = n.NodeColor.getBlue();
          String rcs = Integer.toHexString(rc); if(rcs.length()==1) rcs="0"+rcs;
          String gcs = Integer.toHexString(gc); if(gcs.length()==1) gcs="0"+gcs;
          String bcs = Integer.toHexString(bc); if(bcs.length()==1) bcs="0"+bcs;
          fw.write("\t\t\tfill\t\"#"+rcs+gcs+bcs+"\"\r\n");

          String nodeType = "RECTANGLE_ROUNDED";
          switch(n.NodeShape){
            case 0: nodeType = "rectangle"; break;
            case 1: nodeType = "ellipse"; break;
            case 2: nodeType = "RECTANGLE_ROUNDED"; break;
            case 3: nodeType = "diamond"; break;
            case 4: nodeType = "hexagon"; break;
            case 5: nodeType = "octagon"; break;
            case 6: nodeType = "parallelogram"; break;
            case 7: nodeType = "triangle"; break;
          }
          if(!nodeType.equals("RECTANGLE_ROUNDED"))
            fw.write("\t\t\ttype\t\""+nodeType+"\"\r\n");

          rc = n.NodeBorderColor.getRed();
          gc = n.NodeBorderColor.getGreen();
          bc = n.NodeBorderColor.getBlue();
          rcs = Integer.toHexString(rc); if(rcs.length()==1) rcs="0"+rcs;
          gcs = Integer.toHexString(gc); if(gcs.length()==1) gcs="0"+gcs;
          bcs = Integer.toHexString(bc); if(bcs.length()==1) bcs="0"+bcs;
          fw.write("\t\t\toutline\t\"#"+rcs+gcs+bcs+"\"\r\n");

          fw.write("\t\t\toutline_width\t"+n.NodeBorderWidth+"\r\n");
          fw.write("\t\t]\r\n");
          fw.write("\t\tlabel\t\""+n.NodeLabel+"\"\r\n");
          fw.write("\t]\r\n");
        }

        for(int i=0;i<Edges.size();i++){
          Edge e = (Edge)Edges.elementAt(i);
          fw.write("\tedge\t[\r\n");
          fw.write("\t\troot_index\t"+(-i-1)+"\r\n");
          fw.write("\t\ttarget\t"+getNodeIndex(e.Node2.Id)+"\r\n");
          fw.write("\t\tsource\t"+getNodeIndex(e.Node1.Id)+"\r\n");
          fw.write("\t\tlabel\t\""+e.EdgeLabel+"\"\r\n");
          fw.write("\t]\r\n");
        }

        fw.write("]\r\n");
        fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }

  }


  public Node getNode(String id){
    /*Node n = null;
    for(int i=0;i<Nodes.size();i++){
      Node nn = (Node)Nodes.elementAt(i);
      if(nn.Id.equals(id))
        n = nn;
    }*/
    Node n = (Node)NodeHash.get(id);
    return n;
  }

  public Edge getEdge(String id){
	    /*Node n = null;
	    for(int i=0;i<Nodes.size();i++){
	      Node nn = (Node)Nodes.elementAt(i);
	      if(nn.Id.equals(id))
	        n = nn;
	    }*/
	    Edge e = (Edge)EdgeHash.get(id);
	    return e;
	  }

   public Vector<Edge> getEdge(Node source, Node target, boolean directedGraph){
	   Vector<Edge> v = new Vector<Edge>();
	   calcNodesInOut();
	   for(int i=0;i<source.outcomingEdges.size();i++){
		   if(source.outcomingEdges.get(i).Node2.Id.equals(target.Id))
			   v.add(source.outcomingEdges.get(i));
	   }
	   if(!directedGraph){
		   for(int i=0;i<source.incomingEdges.size();i++)
			   if(source.incomingEdges.get(i).Node1.Id.equals(target.Id))
				   v.add(source.incomingEdges.get(i));
	   }
	   /*for(int i=0;i<Edges.size();i++){
		   Edge e = (Edge)Edges.get(i);
		   if((e.Node1.Id.equals(source.Id))&&(e.Node2.Id.equals(target.Id)))
			  v.add(e);
		   if(!directedGraph)
			   if((e.Node2.Id.equals(source.Id))&&(e.Node1.Id.equals(target.Id)))
					  v.add(e);
	   }*/
       return v;
   }
  

  public Node getNodeByLabel(String label){
    Node n = null;
    for(int i=0;i<Nodes.size();i++){
      Node nn = (Node)Nodes.elementAt(i);
      if(nn.NodeLabel.equals(label))
        n = nn;
    }
    return n;
  }

  public Graph getNodesByLabelInclusion(String label){
    Graph gr = new Graph();
    for(int i=0;i<Nodes.size();i++){
      Node nn = (Node)Nodes.elementAt(i);
      if(nn.NodeLabel.toLowerCase().indexOf(label.toLowerCase())>=0)
        gr.addNode(nn);
    }
    gr.addConnections(this);
    return gr;
  }

  public int getNodeIndex(String id){
    int k = -1;
    if(NodeIndexHash!=null){
    if(NodeIndexHash.get(id)!=null){
    	k = ((Integer)NodeIndexHash.get(id)).intValue();
    }}else
    for(int i=0;i<Nodes.size();i++){
      Node nn = (Node)Nodes.elementAt(i);
      if(nn.Id.equals(id))
        k = i;
    }
    return k;
  }

  public int getEdgeIndex(String id){
    int k = -1;
    for(int i=0;i<Edges.size();i++){
      Edge nn = (Edge)Edges.elementAt(i);
      if(nn.Id==null)
    	  System.out.println("nn.Id is null for "+id);
      if(nn.Id.equals(id))
        k = i;
    }
    return k;
  }

  public Node getCreateNode(String id){
    /*Node n = null;
    for(int i=0;i<Nodes.size();i++){
      Node nn = (Node)Nodes.elementAt(i);
      if(nn.Id.equals(id))
        n = nn;
    }*/
    Node n = (Node)NodeHash.get(id);
    if(n==null){
      n = new Node();
      n.Id = id;
      n.NodeLabel = id;
      Nodes.add(n);
      NodeHash.put(id,n);
    }
    return n;
  }

  public Edge getCreateEdge(String id){
    /*Edge n = null;
    for(int i=0;i<Edges.size();i++){
      Edge nn = (Edge)Edges.elementAt(i);
      if(nn.Id.equals(id))
        n = nn;
    }*/
    Edge n = (Edge)EdgeHash.get(id);
    if(n==null){
      n = new Edge();
      n.Id = id;
      n.EdgeLabel = id;
      Edges.add(n);
      EdgeHash.put(id,n);
    }
    return n;
  }


  public Graph makeCopy(){
    Graph gr = new Graph();
    for(int i=0;i<Nodes.size();i++){
      gr.addNode((Node)this.Nodes.elementAt(i));
    }
    for(int i=0;i<Edges.size();i++){
      gr.addEdge((Edge)this.Edges.elementAt(i));
    }
    gr.name = this.name;
    gr.startId = this.startId;
    Iterator it = this.selectedIds.iterator();
    while(it.hasNext()){
      gr.selectedIds.add(it.next());
    }
    return gr;
  }



  public void subtractNodes(Graph gr){
    for(int i=0;i<gr.Nodes.size();i++){
      Node n = (Node)gr.Nodes.elementAt(i);
      int k = this.getNodeIndex(n.Id);
      //System.out.println(n.Id);
      if(k>=0){
    	NodeHash.remove(((Node)Nodes.get(k)).Id);
        Nodes.remove(k);
      }
    }
  }

  public void addNodes(Graph gr){
    for(int i=0;i<gr.Nodes.size();i++){
      int k = this.getNodeIndex(((Node)gr.Nodes.elementAt(i)).Id);
      if(k<0){
        Nodes.add((Node)gr.Nodes.elementAt(i));
        NodeHash.put(((Node)gr.Nodes.elementAt(i)).Id,(Node)gr.Nodes.elementAt(i));
      }
    }
  }

  public void addNode(Node n){
    if(NodeHash.get(n.Id)==null){
      Nodes.add(n);
      NodeHash.put(n.Id,n);
    }
  }

  public void addEdge(Edge e){
    if(EdgeHash.get(e.Id)==null){
      Edges.add(e);
      EdgeHash.put(e.Id,e);
    }
  }
  
  public void addEdgeIdUnique(Edge e){
	    if(EdgeHash.get(e.Id)==null){
	      Edges.add(e);
	      EdgeHash.put(e.Id,e);
	    }else{
	      Edge ei = getEdge(e.Id);
	      e.Id = e.Id+"'";
	      if((!e.Node1.equals(ei.Node1))||(!e.Node2.equals(ei.Node2)))
	    	  addEdgeIdUnique(e);
	    }
  }
  

  public void removeNodes(Graph gr){
	for(int i=0;i<gr.Nodes.size();i++){
      int k = this.getNodeIndex(((Node)gr.Nodes.elementAt(i)).Id);
      if(k>=0){
    	Nodes.remove(k);
        NodeHash.remove(((Node)gr.Nodes.elementAt(i)).Id);
      }
    }
  }

  public void removeNode(String id){
	      Node n = this.getNode(id);
	      if(n!=null){
	    	Nodes.remove(n);
	        NodeHash.remove(id);
	      }
	  }

  public void removeEdge(String id){
      Edge n = this.getEdge(id);
      if(n!=null){
    	Edges.remove(n);
        EdgeHash.remove(id);
      }
  }
  

  
  public void addEdges(Graph gr){
    for(int i=0;i<gr.Edges.size();i++){
      int k = this.getEdgeIndex(((Edge)gr.Edges.elementAt(i)).Id);
      if(k<0){
        Edges.add((Edge)gr.Edges.elementAt(i));
        EdgeHash.put(((Edge)gr.Edges.elementAt(i)).Id,(Edge)gr.Edges.elementAt(i));
      }
    }
  }

  public void removeObsoleteEdges(){
    //Graph gr = makeCopy();
    int i=0;
    //System.out.println(Edges.size()+" edges");
    while(i<Edges.size()){
      //if((i>0)&&(i==((int)(0.0001*i)*10000)))
      //	  System.out.print(i+"\t");
      Edge e = (Edge)Edges.elementAt(i);
      //int k1 = -1;
      //int k2 = -1;
      Node n1 = getNode(e.Node1.Id);
      Node n2 = getNode(e.Node2.Id);
      /*if(e.Node1!=null)
         k1 = getNodeIndex(e.Node1.Id);
      if(e.Node2!=null)      
         k2 = getNodeIndex(e.Node2.Id);
      if((k1<0)||(k2<0)){*/
      if((n1==null)||(n2==null)){
        //int kk = getEdgeIndex(e.Id);
        EdgeHash.remove(e.Id);
        Edges.remove(i);
      }else
    	  i++;
    }
    //System.out.println();
  }

  public void addNodesFromEdges(){
    for(int i=0;i<Edges.size();i++){
      Edge e = (Edge)Edges.elementAt(i);
      int k1 = getNodeIndex(e.Node1.Id);
      int k2 = getNodeIndex(e.Node2.Id);
      if(k1<0)
        addNode(e.Node1);
      if(k2<0)
    	addNode(e.Node2);
    }
  }

  public void addConnections(Graph gr){
    for(int k=0;k<gr.Edges.size();k++){
      Edge e = (Edge)gr.Edges.elementAt(k);
      //int k1 = this.getNodeIndex(e.Node1.Id);
      //int k2 = this.getNodeIndex(e.Node2.Id);
      //if(e.Node1.Id.equals("Cdc2"))
      //	  System.out.println(e.Node1.Id+"->"+e.Node2.Id);
      Node n1 = this.getNode(e.Node1.Id);
      Node n2 = this.getNode(e.Node2.Id);
      //if((k1>=0)&&(k2>=0)){
      if((n1!=null)&&(n2!=null)){
        int kk = getEdgeIndex(e.Id);
        if(kk<0)
          addEdge(e);
      }
    }
  }
  
  public void addConnectionsAlongSequence(Graph gr){
	    for(int k=0;k<gr.Edges.size();k++){
	        Edge e = (Edge)gr.Edges.elementAt(k);
	        Node n1 = this.getNode(e.Node1.Id);
	        Node n2 = this.getNode(e.Node2.Id);
	        if(n1!=null)if(n2!=null)
	        for(int i=0;i<this.Nodes.size()-1;i++){
	        	if(this.Nodes.get(i).Id.equals(n1.Id))
		        	if(this.Nodes.get(i+1).Id.equals(n2.Id))
		        		addEdge(e);
	        }
	      }	  
  }
  
  public void addMetanodeConnections(Graph gr, boolean nodeIntersectionView, boolean showIntersections){
	    for(int i=0;i<Nodes.size();i++){
	        Node n1 = (Node)Nodes.elementAt(i);
	        if(n1.NodeClass==3)
	        for(int j=0;j<Nodes.size();j++) if(i!=j){
	          Node n2 = (Node)Nodes.elementAt(j);
	          if(n2.NodeClass==3){
	            Vector es = edgesConnectingSubGraphs((Graph)n1.link,(Graph)n2.link,gr,nodeIntersectionView);
	            for(int k=0;k<es.size();k++){
	              Edge e = (Edge)es.elementAt(k);
	              if(getEdgeByLabel(e.EdgeLabel)==null){
	              System.out.println("Added meta-meta connection "+e.Id);
	              e.Node1 = n1;
	              e.Node2 = n2;
	              if(((Graph)n2.link).Nodes.size()>((Graph)n1.link).Nodes.size())
	            	  if(e.getFirstAttributeValue("BIOPAX_EDGE_TYPE")!=null)
	            	  if(e.getFirstAttributeValue("BIOPAX_EDGE_TYPE").equals("INTERSECTION")){
	            		  e.Node2 = n1;
	            		  e.Node1 = n2;
	            	  }  		  
	              addEdge(e);
	              }
	            }
	          }
	        }
	      }  
  }
  
  public Edge getEdgeByLabel(String label){
	  Edge e = null;
	  for(int i=0;i<Edges.size();i++){
		  Edge ei = (Edge)Edges.get(i);
		  if(ei.EdgeLabel.equals(label))
			  e = ei;
	  }
	  return e;
  }
  
  public Vector edgesConnectingSubGraphs(Graph meta1, Graph meta2, Graph grglobal, boolean nodeIntersectionView){
	    Vector res = new Vector();
	    Graph inters = meta1.intersection(meta2);
	    for(int k=0;k<grglobal.Edges.size();k++){
	      Edge e = (Edge)grglobal.Edges.elementAt(k);
	      int k1 = meta1.getNodeIndex(e.Node1.Id);
	      int k2 = meta2.getNodeIndex(e.Node2.Id);
	      //if(e.Node1.Id.equals("CDC2_Thr161_pho:cyclin B1*"))
	      //	  if(e.Node2.Id.equals("re258"))
	      //		  System.out.println("edge found");
	      if((k1>=0)&&(k2>=0)){
	    	    boolean inIntersection = false;
	    	    if((inters.getNode(e.Node1.Id)!=null)||(inters.getNode(e.Node2.Id)!=null))
	    	    	inIntersection = true;
	    	    Edge en = e.copy();
		    	int kk = 1, ind = 1;
		    	String id = e.Id;
		    	while(kk>=0){
		          kk = getEdgeIndex(en.Id);
		          if(kk>=0)
		        	  en.Id = id+"("+ind+")";
		          ind++;
		    	}
		    	//en.EdgeLabel = en.Id;
		    	if((!nodeIntersectionView)||(!inIntersection))
		    		res.add(en);
	      }
	    }
    	if(nodeIntersectionView)if(meta1.name.compareTo(meta2.name)>0){
    		for(int i=0;i<inters.Nodes.size();i++){
    			Node n = (Node)inters.Nodes.get(i);
    			Edge ec = new Edge();
    			ec.Id = meta1.name+"(INTERSECT_"+n.Id+")"+meta2.name;
    			ec.EdgeLabel = ec.Id;
    			ec.setAttributeValueUnique("BIOPAX_EDGE_TYPE", "INTERSECTION",Attribute.ATTRIBUTE_TYPE_STRING);
    			ec.setAttributeValueUnique("CELLDESIGNER_EDGE_TYPE", "INTERSECTION",Attribute.ATTRIBUTE_TYPE_STRING);
    			res.add(ec);
    		}
    	}
	    return res;
	}  

  public void assignEdgeIds(){
    for(int i=0;i<Edges.size();i++){
      Edge e = (Edge)Edges.elementAt(i);
      e.Id = e.Node1.Id+"_"+e.Node2.Id;
    }
  }

  public Graph getHangingNodes(){
    Graph gr = new Graph();
    for(int i=0;i<Nodes.size();i++){
      Node n = (Node)Nodes.elementAt(i);
      //if(n.NodeLabel.startsWith("p18"))
      //  System.out.println(n.NodeLabel);
      boolean hanging = true;
      for(int j=0;j<Edges.size();j++){
        Edge e = (Edge)Edges.elementAt(j);
        if(e.Node1.Id.equals(n.Id)){
          hanging = false;
          break;
        }
        if(e.Node2.Id.equals(n.Id)){
          hanging = false;
          break;
        }
      }
      if(hanging)
        gr.addNode(n);
    }
    return gr;
  }


  public void calcNodesInOut(){
    for(int i=0;i<Nodes.size();i++){
      ((Node)Nodes.elementAt(i)).incomingEdges.clear();
      ((Node)Nodes.elementAt(i)).outcomingEdges.clear();
    }
    for(int i=0;i<Edges.size();i++){
      Edge e = (Edge)Edges.elementAt(i);
      if (e.Node1 != null && e.Node2 != null) {
	      e.Node1.outcomingEdges.add(e);
	      e.Node2.incomingEdges.add(e);
      }
    }
  }

  public boolean identicalNodes(Graph gr){
    boolean r = true;
    if(this.Nodes.size()!=gr.Nodes.size())
      r = false;
    else{
      if(this.includesNodes(gr).size()!=gr.Nodes.size()) r = false;
      else
      if(gr.includesNodes(this).size()!=this.Nodes.size()) r = false;
    }
    return r;
  }
  public boolean identicalEdges(Graph gr){
	    boolean r = true;
	    if(this.Edges.size()!=gr.Edges.size())
	      r = false;
	    else{
	      if(this.includesEdges(gr).size()!=gr.Edges.size()) r = false;
	      else
	      if(gr.includesEdges(this).size()!=this.Edges.size()) r = false;
	    }
	    return r;
}
  
  
  public boolean identicalTo(Graph gr){
	    boolean rn = identicalNodes(gr);
	    boolean re = identicalEdges(gr);
	    return rn&re;
	}  

  public Vector includesNodes(Graph gr){
    Vector v  = new Vector();
    for(int i=0;i<gr.Nodes.size();i++){
      Node n = (Node)gr.Nodes.elementAt(i);
      Node nk = this.getNode(n.Id);
      if(nk!=null)
        v.add(n);
    }
    return v;
  }
  public Vector includesEdges(Graph gr){
	    Vector v  = new Vector();
	    for(int i=0;i<gr.Edges.size();i++){
	      Edge n = gr.Edges.elementAt(i);
	      Edge nk = this.getEdge(n.Id);
	      if(nk!=null)
	        v.add(n);
	    }
	    return v;
  }  

  public Graph intersection(Graph gr){
    Graph gri = new Graph();
    for(int i=0;i<this.Nodes.size();i++){
      Node n = (Node)Nodes.elementAt(i);
      int k = gr.getNodeIndex(n.Id);
      if(k>=0)
        gri.addNode(n);
    }
    return gri;
  }

  public float includesNodesPercentage(Graph gr){
    float perc = 0f;
    int nn = 0;
    for(int i=0;i<gr.Nodes.size();i++){
      Node n = (Node)gr.Nodes.elementAt(i);
      int k = this.getNodeIndex(n.Id);
      if(k>=0)
        nn++;
    }
    perc = (float)nn/gr.Nodes.size();
    return perc;
  }


  public Graph getSelectedNodes(){
    Graph gr = new Graph();
    Iterator it = selectedIds.iterator();
    while(it.hasNext()){
      String id = (String)it.next();
      Node n = getNode(id);
      if(n!=null)
    	  gr.addNode(n);
    }
    gr.addConnections(this);
    gr.name = name;
    return gr;
  }


  public static void printGraphList(){
    for(int i=0;i<allGraphs.size();i++){
      Graph gr = (Graph)allGraphs.elementAt(i);
      System.out.println(gr.name+"\t"+gr.selectedIds.size());
    }
  }
  
  /**
   * Add an edge from edgeId.Target to edgeId.Source.
   * Requires method calcInOut pre-calculated.
   * @param edgeId
   */
  public void makeEdgeDoubleSense(String edgeId){
	Edge e = (Edge)getEdge(edgeId);
	Node source = e.Node1;
	Node target = e.Node2;
	boolean alreadyConnected = false;
	for(int i=0;i<target.outcomingEdges.size();i++){
		Edge ec = (Edge)target.outcomingEdges.get(i);
		if(ec.Node2.Id.equals(source.Id))
			alreadyConnected = true;
	}
	if(!alreadyConnected){
		String id = target.Id+" ("+e.EdgeLabel+") "+source.Id;
		Edge newEdge = e.copy();
		newEdge.Node1 = target;
		newEdge.Node2 = source;
		newEdge.Id = id;
		addEdge(newEdge);
	}
  }
  
  public void createIndexNodeHash(){
	  NodeIndexHash = new HashMap();
	  for(int i=0;i<Nodes.size();i++){
		  NodeIndexHash.put(((Node)Nodes.get(i)).Id,new Integer(i));  
	  }
  }
  
  public double[][] getIncidenceMatrix(){
	  double inc[][] = new double[Nodes.size()][Nodes.size()];
	  for(int i=0;i<Edges.size();i++){
		  int k1 = getNodeIndex(Edges.get(i).Node1.Id);
		  int k2 = getNodeIndex(Edges.get(i).Node2.Id);
		  double w = Edges.get(i).weight;
		  if(Math.abs(w)<1e-8) w = 1;
		  inc[k1][k2] = w;
	  }
	  return inc;
  }
  
  public String toString(){
	  double inc[][] = getIncidenceMatrix();
	  String s = "";
	  for(int i=0;i<Nodes.size();i++) s+="\t"+Nodes.get(i).Id; s+="\n";
	  for(int i=0;i<Nodes.size();i++){ 
		  s+=Nodes.get(i).Id+"\t";
		  for(int j=0;j<Nodes.size();j++) s+=inc[i][j]+"\t"; 
		  s+="\n";
	  }
	  return s;
  }
  
  public void recreateNodeEdgeHash(){
	  NodeHash.clear();
	  for(int i=0;i<Nodes.size();i++)
		  NodeHash.put(Nodes.get(i).Id, Nodes.get(i));
	  EdgeHash.clear();
	  for(int i=0;i<Edges.size();i++)
		  EdgeHash.put(Edges.get(i).Id, Edges.get(i));
  }

}

