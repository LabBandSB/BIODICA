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

package fr.curie.BIODICA;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.*;
import java.math.BigInteger;
import java.util.*;


import vdaoengine.data.VDataTable;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.VSimpleFunctions;
import edu.rpi.cs.xgmml.*;


import java.net.*;


/**
 * Set of simple functions
 * @author Andrei Zinovyev
 *
 */
@SuppressWarnings("unchecked")
public class Utils {
	
	  public static String replaceString(String source,String shabl,String val){
	        int i=0;
	        String s1 = new String(source);
	        StringBuffer sb = new StringBuffer(s1);
	        if(!shabl.equals(val)){
	        while((i=sb.indexOf(shabl))>=0){
	                sb.replace(i,i+shabl.length(),val);
	        }
	        }
	        s1 = sb.toString();
	        return s1;
	  }

	  public static void separatePositiveAndNegativeValues(VDataTable vt){
			Vector<String> colNames = new Vector<String>();
			for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL)if(!vt.fieldNames[i].endsWith("_ps"))if(!vt.fieldNames[i].endsWith("_ng")){
				String fn = vt.fieldNames[i];
				colNames.add(fn);
			}
			
			
			
			for(int i=0;i<colNames.size();i++){
				String fn = colNames.get(i);
				if(vt.fieldNumByName(fn+"_ps")<0)if(vt.fieldNumByName(fn+"_ng")<0){
				vt.addNewColumn(fn+"_ps", "", "", vt.NUMERICAL, "N/A");
				vt.addNewColumn(fn+"_ng", "", "", vt.NUMERICAL, "N/A");
				for(int j=0;j<vt.rowCount;j++){
					String val = vt.stringTable[j][vt.fieldNumByName(fn)];
					if(!val.toLowerCase().equals("na"))if(!val.toLowerCase().equals("n/a"))if(!val.toLowerCase().equals("@"))if(!val.toLowerCase().equals("_")){
						float f = Float.parseFloat(val);
						if(f>0) vt.stringTable[j][vt.fieldNumByName(fn+"_ps")] = ""+f;
						if(f<0) vt.stringTable[j][vt.fieldNumByName(fn+"_ng")] = ""+f;
					}
				}
				}
			}
			
		}
	  
	  public static VDataTable separatePositiveOrNegativeValues(VDataTable vt, boolean positive){
			Vector<String> colNames = new Vector<String>();
			for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL){
				String fn = vt.fieldNames[i];
				colNames.add(fn);
			}
			
			VDataTable vt1 = new VDataTable();
			vt1.copyHeader(vt);
			vt1.stringTable = new String[vt.rowCount][vt.colCount];
			for(int i=0;i<vt.rowCount;i++)for(int j=0;j<vt1.colCount;j++)
				vt1.stringTable[i][j] = vt.stringTable[i][j];
			
			for(int i=0;i<colNames.size();i++){
				String fn = colNames.get(i);
				for(int j=0;j<vt1.rowCount;j++){
					String val = vt1.stringTable[j][vt.fieldNumByName(fn)];
						vt1.stringTable[j][vt.fieldNumByName(fn)] = "N/A";
						if(!val.equals("@")){
						float f = Float.parseFloat(val);
						if(f>0)if(positive) vt1.stringTable[j][vt1.fieldNumByName(fn)] = ""+f;
						if(f<0)if(!positive) vt1.stringTable[j][vt1.fieldNumByName(fn)] = ""+f;
						}
				}
			}
			
			return vt1;
		}
	  

	  public static AttDocument.Att getFirstAttribute(GraphicNode n,String name){
			AttDocument.Att at = null;
			for(int i=0;i<n.getAttArray().length;i++){
				if(n.getAttArray()[i].getName().equals(name)){
					at = n.getAttArray()[i]; break;
				}
			}
			return at;
		}

	  public static void addAttribute(GraphicNode n,String label,String name, String value, ObjectType.Enum typ){
		  AttDocument.Att at = n.addNewAtt();
		  at.setName(name);
		  at.setValue(value);
		  at.setLabel(label);
		  at.setType(typ);
		}
	  public static void addAttribute(GraphicEdge n,String label,String name, String value, ObjectType.Enum typ){
		  AttDocument.Att at = n.addNewAtt();
		  at.setName(name);
		  at.setValue(value);
		  at.setLabel(label);
		  at.setType(typ);
		}

		public static void findAllNumericalColumns(VDataTable vt){
			for(int i=0;i<vt.colCount;i++){
				boolean isNumerical = true;
				for(int j=0;j<vt.rowCount;j++){
					String s = vt.stringTable[j][i].trim();
					if(s.equals("\"\"")||s.equals("NA")||s.equals("")||s.equals("_")||s.equals("N/A")){
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
		
		public static Vector<String> loadStringListFromFile(String fn){
			Vector<String> list = new Vector<String>(); 
			try{
			LineNumberReader lr = new LineNumberReader(new FileReader(fn));
			String s = null;
			while((s=lr.readLine())!=null){
			  list.add(s.trim());
			}
			lr.close();
			}catch(Exception e){
			  e.printStackTrace();
			}
			return list;
			}

		public static void CorrectCytoscapeNodeIds(Graph graph){
			for(int i=0;i<graph.Nodes.size();i++){
				Node n = (Node)graph.Nodes.get(i);
				n.Id = n.NodeLabel;
			}
			graph.recreateNodeEdgeHash();
		}

		public static Set UnionOfSets(Set set1, Set set2){
		    Iterator it = set2.iterator();
		    while(it.hasNext()){
		      Object o = it.next();
		      if(!set1.contains(o))
		        set1.add(o);
		    }
		    return set1;
		  }

  public static int getEdgeSign(Edge e){
	  int sign_in = 0;
      for(int kk=0;kk<e.Attributes.size();kk++){
      	Attribute at = (Attribute)e.Attributes.get(kk);
      	if(at.value!=null){
      	if((at.value.toLowerCase().indexOf("effect")>=0)||(at.name.toLowerCase().indexOf("interaction")>=0)||(at.name.toLowerCase().indexOf("effect")>=0)){
      		if(at.value.toLowerCase().indexOf("activation")>=0) sign_in = 1;
      		if(at.value.toLowerCase().indexOf("expression")>=0) sign_in = 1;        		
      		if(at.value.toLowerCase().indexOf("inhibition")>=0) sign_in = -1;
      		if(at.value.toLowerCase().indexOf("suppression")>=0) sign_in = -1;
      		if(at.value.toLowerCase().indexOf("repression")>=0) sign_in = -1;
      	}
      	}
      }
      return sign_in;
  }

public static int[] SortMass(float cais[]){
	  int res[]=new int[cais.length];
	  for (int i = 0; i < res.length; i++) res[i]=i;

	  int i,j,k,inc,n=cais.length;
	  float v;

	  inc=1;
	  do {
	  	inc *= 3;
	  	inc++;
	  } while (inc <= n);

	  do {
	  	inc /= 3;
	  	for (i=inc+1;i<=n;i++) {
	  		v=cais[res[i-1]];
	  		j=i;
	                  k=res[i-1];
	  		while (cais[res[j-inc-1]]>v) {
	  			//cais[j]=cais[j-inc];
	                          res[j-1]=res[j-inc-1];
	  			j -= inc;
	  			if (j <= inc) break;
	  		}
	  		//cais[j]=v;
	                  res[j-1]=k;
	  	}
	  } while (inc > 0);

	  return res;
	}

  public static int getPathSign(Path path){
	  int res = 0;
	  Vector<Edge> edges = path.getAsEdgeVector();
	  for(int i=0;i<edges.size();i++)
		  res*=getEdgeSign((Edge)edges.get(i));
	  return res;
  }
  
  public static int getPathSign(Graph graph, String sourceNodeId){
	  Path path = new Path(graph,sourceNodeId);
	  return getPathSign(path);
  }  

  public static Graph MergeNetworkAndFilter(Vector<Graph> graphs, float pourcentage){
	  Graph graph = new Graph();
	  HashMap<String,Integer> nodeCounts = new HashMap<String,Integer>();
	  HashMap<String,Integer> edgeCounts = new HashMap<String,Integer>();
	  for(int i=0;i<graphs.size();i++){
		  Graph gr = graphs.get(i);
		  for(int j=0;j<gr.Nodes.size();j++){
			  Node n = gr.Nodes.get(j);
			  if(graph.getNode(n.Id)!=null){
				  int count = nodeCounts.get(n.Id);
				  nodeCounts.put(n.Id, count+1);
			  }else{
				  graph.addNode(n);
				  nodeCounts.put(n.Id, 1);
			  }
		  }
		  for(int j=0;j<gr.Edges.size();j++){
			  Edge e = gr.Edges.get(j);
			  if(graph.getEdge(e.Id)!=null){
				  int count = edgeCounts.get(e.Id);
				  edgeCounts.put(e.Id, count+1);
			  }else{
				  Node node1 = graph.getNode(e.Node1.Id);
				  Node node2 = graph.getNode(e.Node2.Id);
				  if(node1==null)
					  System.out.println("ERROR in MergeNetworkAndFilter: edge "+e.Id+" Node1=null ("+e.Node1.Id+")");
				  if(node2==null)
					  System.out.println("ERROR in MergeNetworkAndFilter: edge "+e.Id+" Node2=null ("+e.Node2.Id+")");
				  e.Node1 = node1;
				  e.Node2 = node2;
				  graph.addEdge(e);
				  edgeCounts.put(e.Id, 1);
			  }
		  }
	  }
	  Iterator<String> keys = nodeCounts.keySet().iterator();
	  while(keys.hasNext()){
		  String id = keys.next();
		  Node n = graph.getNode(id);
		  n.Attributes.add(new Attribute("COUNT",""+nodeCounts.get(id)));
	  }
	  keys = edgeCounts.keySet().iterator();
	  while(keys.hasNext()){
		  String id = keys.next();
		  Edge e = graph.getEdge(id);
		  e.Attributes.add(new Attribute("COUNT",""+edgeCounts.get(id)));
	  }
	  keys = nodeCounts.keySet().iterator();
	  while(keys.hasNext()){
		  String id = keys.next();
		  Node n = graph.getNode(id);
		  float f = (float)nodeCounts.get(id)/(float)graphs.size();
		  if(f<pourcentage)
			  graph.removeNode(id);
	  }	  
	  graph.removeObsoleteEdges();
	  return graph;
  }
  
  public static int compareTwoSets(Vector v1, Vector v2){
	  int inters = 0;
	  for(int i=0;i<v1.size();i++){
	    if(v2.indexOf(v1.get(i))>=0)
	      inters++;
	    //else
	    // System.out.println(v1.get(i)+" from list1 not found in list2");
	  }
	  //for(int i=0;i<v2.size();i++){
	  //  if(v1.indexOf(v2.get(i))<0)
	  //    System.out.println(v2.get(i)+" from list2 not found in list1");
	  //}
	  return inters;
	 }

  public static Vector UnionOfVectors(Vector set1, Vector set2){
	    Iterator it = set2.iterator();
	    while(it.hasNext()){
	      Object o = it.next();
	      if(!set1.contains(o))
	        set1.add(o);
	    }
	    return set1;
	  }
 public static void writeStringToFile(String s, String fileName){
	 try{
		 FileWriter fw = new FileWriter(fileName);
		 fw.write(s);
		 fw.close();
	 }catch(Exception e){
		 e.printStackTrace();
	 }
 }
  

	
}