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


import java.io.*;
import java.util.*;

/**
 * Simple internal BiNoM implementation of Edge object.
 *
 */
public class Edge extends Element{

  /**
   * Source node
   */
  public Node Node1 = null;
  /**
   * Target node
   */
  public Node Node2 = null;

  /**
   * Edge unique id
   */
  public String Id = "";
  /**
   * Edge label
   */
  public String EdgeLabel = "";
  /**
   * Some edge information
   */  
  public String EdgeInfo = "";
  /**
   * Type of edge arrow (not used yet)
   */  
  public int EdgeArrowType = 0;
     public static int SHARP  = 0;
     public static int CIRCLE  = 1;
     public static int BAR  = 2;
     public static int SQUARE  = 3;

     /**
      * Edge weight
      */  
  public double weight = 1;

  public Edge() {
  }

  /**
   *
   * @return an edge clone
   */
  public Edge copy(){
	  Edge e = new Edge();
	  e.Id = Id;
	  e.EdgeLabel = EdgeLabel;
	  e.EdgeInfo = EdgeInfo;
	  e.EdgeArrowType = EdgeArrowType; 
	  e.Node1 = Node1;
	  e.Node2 = Node2;
	  e.Attributes = Attributes;
	  return e;
  }
  
  public String toString(){
	  String connector = "-.";
	  //if(BiographUtils.getEdgeSign(this)==1) connector = "->";
	  //if(BiographUtils.getEdgeSign(this)==-1) connector = "-|";
	  return Node1.Id+connector+Node2.Id;
  }
  
}