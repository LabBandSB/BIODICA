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

/**
 * Simple internal for BiNoM implementation of node and edge attribute 
 *
 */
public class Attribute {

  public static int ATTRIBUTE_TYPE_STRING = 0;
  public static int ATTRIBUTE_TYPE_REAL = 1;
	
  public String name = "";
  public String value = "";
  public int type = ATTRIBUTE_TYPE_STRING;

  public Attribute(String _name, String _val, int _type){
    name = _name;
    value = _val;
    type = _type;
  }
  public Attribute(String _name, String _val){
	    name = _name;
	    value = _val;
	    type = ATTRIBUTE_TYPE_STRING;
  }
  

}