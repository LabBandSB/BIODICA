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
import org.xml.sax.*;
import org.apache.xerces.parsers.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Simple XGMML parser
 * @author Andrei Zinovyev
 *
 */
public class GraphXGMMLParser  extends DefaultHandler{

  SAXParser parser = new SAXParser();
  CharArrayWriter text = new CharArrayWriter ();
  public Graph graph = new Graph();
  Node currentNode = null;
  Edge currentEdge = null;
  boolean firstPass = true;
  boolean inNode = false;
  boolean inEdge = false;

  /**
   * Parses fn file
   * @param fn
   * @throws SAXException
   * @throws IOException
   */
  public void parse (String fn) throws SAXException, IOException
  {
      parser.setContentHandler (this);
      FileInputStream fi = new FileInputStream(fn);
      firstPass = true; inNode = false; inEdge = false;
      parser.parse(new InputSource (fi));
      fi.close();
      fi = new FileInputStream(fn);
      firstPass = false; inNode = false; inEdge = false;
      parser.parse(new InputSource (fi));
  }

  public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) throws SAXException
  {
    text.reset();
    if(firstPass){

    if(qName.equals("node")){
      String id = attributes.getValue("id");
      String name = attributes.getValue("name");
      currentNode = graph.getCreateNode(id);
      currentNode.NodeLabel = attributes.getValue("label");;
    }

    }else{

    if(qName.equals("edge")){
      String id = attributes.getValue("id");
      String label = attributes.getValue("label");
      String source = attributes.getValue("source");
      String target = attributes.getValue("target");
      if(id==null) id = label;
      if(id.equals("")) id = label;
      currentEdge = graph.getCreateEdge(id);
      currentEdge.EdgeLabel = label;
      currentEdge.Node1 = graph.getNode(source);
      currentEdge.Node2 = graph.getNode(target);
    }
    }

    if(qName.equals("node")){
      inNode = true; inEdge = false;
    }
    if(qName.equals("edge")){
      inEdge = true; inNode = false;
    }

    if(qName.equals("att")){if(!firstPass&&inEdge){
      String name = attributes.getValue("name");
      String value = attributes.getValue("value");
      String label = attributes.getValue("label");
      Attribute at = new Attribute(name,value);
      currentEdge.Attributes.add(at);
    }else if(firstPass&&inNode){
      String name = attributes.getValue("name");
      String value = attributes.getValue("value");
      String label = attributes.getValue("label");
      Attribute at = new Attribute(name,value);
      currentNode.Attributes.add(at);
      currentNode.NodeClass = 0;
      if(name.indexOf("_REACTION")>=0)
        if((value!=null)&&(!value.equals("")))
          currentNode.NodeClass = 1;
    }}


  }

}