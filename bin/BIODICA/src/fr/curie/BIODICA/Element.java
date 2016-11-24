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

import java.util.Vector;

/**
 * Abstract class for attributed graph elements (Nodes, Edges, HyperEdges, etc.)
 *
 */
public class Element {

	  public Vector Attributes = new Vector();	
	
	  /**
	   * Returns all attributes attached with a given name
	   * @param nam
	   * @return
	   */
	  public Vector getAttributeValues(String nam){
	    Vector res = new Vector();
	    for(int i=0;i<Attributes.size();i++){
	      Attribute attr = (Attribute)Attributes.get(i);
	      if(attr.value!=null)
	      if(attr.name.equals(nam))
	        res.add(attr.value);
	    }
	    return res;
	  }
	  /**
	   * Finds first in the list attribute with a given name
	   * @param nam
	   * @return
	   */
	  public String getFirstAttributeValue(String nam){
	    String s = null;
	    Vector res = getAttributeValues(nam);
	    if(res.size()>0)
	      s = (String)res.get(0);
	    return s;
	  }
	  
	  public Attribute getFirstAttribute(String nam){
		  Attribute s = null;
		    Vector res = new Vector();
		    for(int i=0;i<Attributes.size();i++)
		    	if(((Attribute)Attributes.get(i)).name.equals(nam))
		    		res.add(Attributes.get(i));
		    if(res.size()>0)
		      s = (Attribute)res.get(0);
		    return s;
		  }
	  

	  /**
	   * Checks if the attribute with such a name already attached and if yes,
	   * then changes its value, if no then creates a new one
	   * @param nam
	   * @param val
	   */
	  public void setAttributeValueUnique(String nam, String val, int type){
	    boolean found = false;
	    for(int i=0;i<Attributes.size();i++){
	      Attribute attr = (Attribute)Attributes.get(i);
	      if(attr.name.equals(nam)){
	        found = true;
	        attr.value = val;
	      }
	    }
	    if(!found)
	      Attributes.add(new Attribute(nam,val,type));
	  }
	  /**
	   * Finds all attributes in whose name the substring is contained
	   * @param substring
	   * @return
	   */
	  public Vector<Attribute> getAttributesWithSubstringInName(String substring){
		  Vector res = new Vector();
		  for(int i=0;i<this.Attributes.size();i++){
			  Attribute at = (Attribute)this.Attributes.get(i);
			  if(at.name.toLowerCase().indexOf(substring.toLowerCase())>=0)
				  res.add(at);
		  }
		  return res;
	  }
	  
	  public String getFirstAttributeValueWithSubstringInName(String substring){
		  String res = null;
		  Vector<Attribute> atts = getAttributesWithSubstringInName(substring);
		  if(atts.size()>0)
			  res = atts.get(0).value;
		  return res;
	  }
	  

}
