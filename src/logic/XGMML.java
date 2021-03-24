package logic;

import java.io.*;
import java.util.*;

import edu.rpi.cs.xgmml.*;

/**
 * Wrapper of XGMML content represented by edu.rpi.cs.xgmml.GraphDocument object
 * @author Andrei Zinovyev
 *
 */
public class XGMML {

  /**
   * The GraphDocument object (XMLBeans mapping of XGMML)
   */
  public GraphDocument graphDoc = null;
  //GraphDocument.
  //edu.rpi.cs.xgmml.GraphicGraph graph = null;

  public XGMML() {
  }

  public static void main(String[] args) {

    try{
    /*XGMML xge = new XGMML();

    xge.graphDoc = GraphDocument.Factory.newInstance();
    xge.graph = xge.graphDoc.addNewGraph();
    GraphicNode nod1 = xge.graph.addNewNode();
    nod1.setId("n1");
    nod1.setLabel("Node1Label");

    AttDocument.Att attn1 = nod1.addNewAtt();
    attn1.setLabel("BIOPAX_NODE_TYPE");
    attn1.setName("BIOPAX_NODE_TYPE");
    attn1.setValue("catalysis");
    attn1.setType(ObjectType.STRING);

    GraphicNode nod2 = xge.graph.addNewNode();
    nod2.setId("n2");
    nod2.setLabel("Node2Label");

    AttDocument.Att attn2 = nod2.addNewAtt();
    attn2.setLabel("BIOPAX_NODE_TYPE");
    attn2.setName("BIOPAX_NODE_TYPE");
    attn2.setValue("smallMolecule");
    attn2.setType(ObjectType.STRING);

    GraphicNode nod3 = xge.graph.addNewNode();
    nod3.setId("n3");
    nod3.setLabel("Node3Label");

    AttDocument.Att attn3 = nod3.addNewAtt();
    attn3.setLabel("BIOPAX_NODE_TYPE");
    attn3.setName("BIOPAX_NODE_TYPE");
    attn3.setValue("protein");
    attn3.setType(ObjectType.STRING);

    GraphicEdge edge1 = xge.graph.addNewEdge();
    edge1.setId("Edge1");
    edge1.setLabel("Edge1Label");
    edge1.setSource(nod1.getId());
    edge1.setTarget(nod2.getId());

    AttDocument.Att att1 = edge1.addNewAtt();
    att1.setLabel("BIOPAX_EDGE_TYPE");
    att1.setName("BIOPAX_EDGE_TYPE");
    att1.setValue("CONTAINS");
    att1.setType(ObjectType.STRING);


    GraphicEdge edge2 = xge.graph.addNewEdge();
    edge2.setId("Edge2");
    edge2.setLabel("Edge2Label");
    edge2.setSource(nod1.getId());
    edge2.setTarget(nod3.getId());

    AttDocument.Att att2 = edge2.addNewAtt();
    att2.setLabel("BIOPAX_EDGE_TYPE");
    att2.setName("BIOPAX_EDGE_TYPE");
    att2.setValue("ACTIVATION");
    att2.setType(ObjectType.STRING);


    //xge.graph.save(new File("test.xgmml"));
    System.out.println(xge.graphDoc.toString());

    saveToXMGML(xge.graphDoc,"test.xgmml");*/

    }catch(Exception e){
      e.printStackTrace();
    }

  }

  /**
   * Loads XGMML from file
   * @param fn
   * @return
   * @throws Exception
   */
  public static GraphDocument loadFromXMGML(String fn) throws Exception{
    GraphDocument gr = null;
    gr = GraphDocument.Factory.parse(new File(fn));
    return gr;
  }
  /**
   * Saves XGMML to fil
   * @param graph
   * @param fn
   * @throws Exception
   */
  public static void saveToXGMML(GraphDocument graph, String fn) throws Exception{
    try{
      FileWriter fw = new FileWriter(fn);
      String s = graph.toString();
      s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\n"+s;
  fw.write(s);
  fw.close();
}catch(Exception e){
  e.printStackTrace();
}

  }
  
  public static void saveToXGMML(Graph graph, String fn) throws Exception{
	  GraphDocument grDoc = XGMML.convertGraphToXGMML(graph);
	  saveToXGMML(grDoc,fn);
	  }
  

  /**
   * 
   * @param n
   * @param name
   * @return GraphicNode attribute value
   */
  public static String getAttValue(GraphicNode n, String name){
    String s = null;
    AttDocument.Att atl[] = n.getAttArray();
    for(int i=0;i<atl.length;i++){
      AttDocument.Att at = atl[i];
      if(at.getName().equals(name)){
        s = at.getValue();
      }
    }
    return s;
  }

  /**
   * Converts GraphDocument to internal BiNoM graph representation
   * (fr.curie.BiNoM.pathways.analysis.structure.Graph)
   * @param xgr
   * @return
   */
  public static Graph convertXGMMLToGraph(GraphDocument xgr){
    Graph gr = new Graph();
    //gr.name = xgr.getGraph().getId();
    gr.name = xgr.getGraph().getName();
    if(gr.name==null)
    	gr.name = xgr.getGraph().getLabel();
    //System.out.println("Nodes: "+xgr.getGraph().getNodeArray().length);
    Date tm1 = new Date();
    GraphicNode ar[] = xgr.getGraph().getNodeArray();
    int size = ar.length;
    //Utils.printUsedMemory();
    for(int i=0;i<size;i++){
      GraphicNode xn = ar[i];
      String id = xn.getId();
      AttDocument.Att canNameAtt = Utils.getFirstAttribute(xn, "canonicalName");
      String canName = "";
      if(canNameAtt!=null)
    	  canName = canNameAtt.getValue();
      /*if((!canName.equals(""))&&(!id.equals(canName)))
    	  id = canName;*/
      Node n = gr.getCreateNode(id);
      if((!canName.equals("")))
    	  n.NodeLabel = canName;
      else
         n.NodeLabel = xn.getLabel();
      //System.out.println("Id = "+n.Id+" canName = "+canName+" nodeLabel = "+n.NodeLabel);      
      n.NodeClass = 0;
      for(int j=0;j<xn.getAttArray().length;j++){
        String attname = xn.getAttArray(j).getName();
        String attvalue = xn.getAttArray(j).getValue();
        if(attname.indexOf("_REACTION")>=0)
          if((attvalue!=null)&&(!attvalue.equals("")))
            n.NodeClass = 1;
    	int att_type = Attribute.ATTRIBUTE_TYPE_STRING;
    	if(xn.getAttArray(j).getType()==ObjectType.REAL)
    		att_type = Attribute.ATTRIBUTE_TYPE_REAL;
        n.Attributes.add(new Attribute(xn.getAttArray(j).getName(),xn.getAttArray(j).getValue(),att_type));
      }
      if(xn.getGraphics()!=null){
        n.x = (float)xn.getGraphics().getX();
        n.y = (float)xn.getGraphics().getY();
      }
      //if(i==(int)(0.001f*i)*1000){
      //  System.out.print(i+"/");
      //  System.out.print(((new Date()).getTime()-tm1.getTime())+" ");
      //  tm1 = new Date();
      //}
    }
    //System.out.println("\nEdges: "+xgr.getGraph().getEdgeArray().length);
    GraphicEdge are[] = xgr.getGraph().getEdgeArray();
    size = are.length;
    //Edge e = new Edge();
    for(int i=0;i<size;i++){
      //if(i==(int)(0.001f*i)*1000){
      //  System.out.print(i+"/");
      //  System.out.print(((new Date()).getTime()-tm1.getTime())+" ");
      //  tm1 = new Date();
      //  System.gc();
      //  Utils.printUsedMemory();
      //}
      GraphicEdge xe = are[i];
      String id = xe.getId();
      if(id==null)
    	  id = xe.getSource()+"_"+xe.getTarget();
      Edge e = gr.getCreateEdge(id);
      e.EdgeLabel = xe.getLabel();
      e.Node1 = gr.getNode(xe.getSource());
      e.Node2 = gr.getNode(xe.getTarget());
      for(int j=0;j<xe.getAttArray().length;j++){
    	int att_type = Attribute.ATTRIBUTE_TYPE_STRING;
    	if(xe.getAttArray(j).getType()==ObjectType.REAL)
    		att_type = Attribute.ATTRIBUTE_TYPE_REAL;
        e.Attributes.add(new Attribute(xe.getAttArray(j).getName(),xe.getAttArray(j).getValue(),att_type));
      }
    }
    // correct node ids
    for(int i=0;i<gr.Nodes.size();i++){
    	Node n = (Node)gr.Nodes.get(i);
    	n.Id = n.NodeLabel;
    	gr.NodeHash.put(n.Id, n);
    }
    //System.out.println();
    return gr;
  }
  
  /**
   * Converts internal BiNoM graph representation to GraphDocument
   * @param gr
   * @return
   */
  public static GraphDocument convertGraphToXGMML(Graph gr){
    GraphDocument xgr = GraphDocument.Factory.newInstance();
    xgr.addNewGraph();
    xgr.getGraph().setId(gr.name);
    xgr.getGraph().setName(gr.name);
    xgr.getGraph().setLabel(gr.name);
    for(int i=0;i<gr.Nodes.size();i++){
      Node n = (Node)gr.Nodes.get(i);
      GraphicNode gn = xgr.getGraph().addNewNode();
      gn.setId(n.Id); gn.setName(n.NodeLabel); gn.setLabel(n.NodeLabel);
      for(int j=0;j<n.Attributes.size();j++){
        //AttDocument.Att at = gn.addNewAtt();
        Attribute ga = (Attribute)n.Attributes.get(j);
        if(!ga.name.equals("GRAPH_VIEW_ZOOM")){
        	if(ga.type==ga.ATTRIBUTE_TYPE_STRING)
        		Utils.addAttribute(gn,ga.name,ga.name,ga.value,ObjectType.STRING);
        	if(ga.type==ga.ATTRIBUTE_TYPE_REAL)
        		Utils.addAttribute(gn,ga.name,ga.name,ga.value,ObjectType.REAL);
        }
      }
      
      //if((n.x!=0)&&(n.y!=0))
      {
    	  GraphicsDocument.Graphics graph = gn.addNewGraphics();
    	  graph.setX(n.x);
    	  graph.setY(n.y);
      }
    }
    for(int i=0;i<gr.Edges.size();i++){
    	Edge n = null;
      try{
          n = (Edge)gr.Edges.get(i);
          GraphicEdge gn = xgr.getGraph().addNewEdge();
          gn.setId(n.Id); gn.setName(n.Id); gn.setLabel(n.EdgeLabel);
          gn.setSource(n.Node1.Id);
          gn.setTarget(n.Node2.Id);
      for(int j=0;j<n.Attributes.size();j++){
        //AttDocument.Att at = gn.addNewAtt();
        Attribute ga = (Attribute)n.Attributes.get(j);
        if(!ga.name.equals("GRAPH_VIEW_ZOOM")){
        	if(ga.type==ga.ATTRIBUTE_TYPE_STRING)
        		Utils.addAttribute(gn,ga.name,ga.name,ga.value,ObjectType.STRING);
        	if(ga.type==ga.ATTRIBUTE_TYPE_REAL)
        		Utils.addAttribute(gn,ga.name,ga.name,ga.value,ObjectType.REAL);
        }
      }
  	  //Utils.setAttribute(gn,"interaction","interaction",n.EdgeLabel,ObjectType.STRING);
      }catch(Exception e){
    	  String source = null;
    	  String target = null;
    	  if(n.Node1!=null)
    		  source = n.Node1.Id;
    	  if(n.Node2!=null)
    		  target = n.Node2.Id;
    	  System.out.println("ERROR: Bad edge "+n.Id+" source="+source+" target="+target);
      }
    }
    return xgr;
  }

}