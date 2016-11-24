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
import java.awt.*;

public class Node extends Element{

	/**
	 * Node unique id
	 */
	public String Id = "";
	public String NodeLabel = "";
	public int NodeShape = 0;
	public static int RECTANGLE = 0;
	public static int ELLIPSE = 1;
	public static int RECTANGLE_ROUNDED = 2;
	public static int DIAMOND = 3;
	public static int HEXAGON = 4;
	public static int OCTAGON = 5;
	public static int PARALLELOGRAM = 6;
	public static int TRIANGLE = 7;
	public Color NodeColor = Color.white;
	public Color NodeBorderColor = Color.black;
	public int NodeBorderWidth = 1;
	public int NodeSize = 5;
	/**
	 *   0 - species
	 *   1 - reaction
	 *   2 - cycle
	 *   3 - metanode
	 */
	public int NodeClass = 0;

	/**
	 * Auxiliary property for analysis of conservation laws
	 */
	public float coefficientInConservationLaw = 1f;
	/**
	 * Auxiliary property for analysis of conservation laws
	 */
	public boolean includedInConservationLaw = false;
	public String NodeInfo = "";
	public String NodeClassInfo = "";
	/**
	 * Node position x
	 */
	public float x = 0f;
	/**
	 * Node position y
	 */
	public float y = 0f;
	/**
	 * Node width
	 */
	public float w = 30f;
	/**
	 * Node height
	 */
	public float h = 30f;
	/**
	 * List of all in-going edges. Filled by Graph.CalcInOut function
	 */
	public Vector<Edge> incomingEdges = new Vector<Edge>();
	/**
	 * List of all out-going edges. Filled by Graph.CalcInOut function
	 */
	public Vector<Edge> outcomingEdges = new Vector<Edge>();
	/**
	 * Some object associated. For example, if it is a metanode, then a subgraph is associated
	 */
	public Object link = null;
	//public String label = "";

	/**
	 * Clones the node 
	 */
	public Node makeCopy(){
		Node n = new Node();
		n.coefficientInConservationLaw = this.coefficientInConservationLaw;
		n.includedInConservationLaw = this.includedInConservationLaw;
		n.x = this.x;
		n.y = this.y;
		n.w = this.w;
		n.h = this.h;
		n.Id = this.Id;
		n.NodeLabel = this.NodeLabel;
		n.link = this.link;
		n.NodeBorderColor = this.NodeBorderColor;
		n.NodeShape = this.NodeShape;
		n.NodeBorderWidth = this.NodeBorderWidth;
		n.NodeClass = this.NodeClass;
		n.NodeColor = this.NodeColor;
		n.NodeInfo = this.NodeInfo;
		n.NodeSize = this.NodeSize;
		for(int i=0;i<Attributes.size();i++){
			Attribute at = (Attribute)Attributes.get(i);
			n.Attributes.add(new Attribute(at.name,at.value));
		}
		return n;
	}

	public Node() {
	}
}