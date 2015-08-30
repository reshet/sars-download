package com.mresearch.spsstools.cup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class MSS_RQ_XML
{
  private DocumentBuilderFactory D_Factory;
  private DocumentBuilder D_Builder;
  private Document DOC_XML;
  private String XML_STR;

  public MSS_RQ_XML()
  {
    try
    {
      this.D_Factory = DocumentBuilderFactory.newInstance();
      this.D_Builder = this.D_Factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException ex)
    {
      Logger.getLogger(MSS_RQ_XML.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public ArrayList<MSS_RQ_XML_Pattern> parseXML(String XML_str)
  {
    ArrayList<MSS_RQ_XML_Pattern> Arr = new ArrayList(20);
    try
    {
      if ((XML_str != null) && (XML_str.length() != 0))
      {
        InputStream stream = new ByteArrayInputStream(XML_str.getBytes("UTF-8"));

        this.DOC_XML = this.D_Builder.parse(stream);
        Element root = this.DOC_XML.getDocumentElement();
        for (Node childNode = root.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
          if ((childNode instanceof Element))
          {
            MSS_RQ_XML_Pattern Elem = new MSS_RQ_XML_Pattern();
            Elem.setNodename(childNode.getNodeName());
            NamedNodeMap attr = childNode.getAttributes();
            for (int i = 0; i < attr.getLength(); i++)
            {
              Node attr_node = attr.item(i);
              Elem.getAttributes().add(new MSS_Pair(attr_node.getNodeName(), attr_node.getNodeValue()));
            }
            Arr.add(Elem);
          }
        }
      }
    }
    catch (SAXException ex)
    {
      Logger.getLogger(MSS_RQ_XML.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IOException ex)
    {
      Logger.getLogger(MSS_RQ_XML.class.getName()).log(Level.SEVERE, null, ex);
    }
    return Arr;
  }

  public ArrayList<MSS_RQ_XML_Pattern> parseXML_Root(String XML_str)
  {
    ArrayList<MSS_RQ_XML_Pattern> Arr = new ArrayList(20);
    try
    {
      if ((XML_str != null) && (XML_str.length() != 0))
      {
        InputStream stream = new ByteArrayInputStream(XML_str.getBytes("UTF-8"));
        this.DOC_XML = this.D_Builder.parse(stream);
        Element root = this.DOC_XML.getDocumentElement();
        if ((root instanceof Element))
        {
          MSS_RQ_XML_Pattern Elem = new MSS_RQ_XML_Pattern();
          Elem.setNodename(root.getNodeName());
          NamedNodeMap attr = root.getAttributes();
          for (int i = 0; i < attr.getLength(); i++)
          {
            Node attr_node = attr.item(i);
            Elem.getAttributes().add(new MSS_Pair(attr_node.getNodeName(), attr_node.getNodeValue()));
          }
          Arr.add(Elem);
        }
      }
    }
    catch (SAXException ex)
    {
      Logger.getLogger(MSS_RQ_XML.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IOException ex)
    {
      Logger.getLogger(MSS_RQ_XML.class.getName()).log(Level.SEVERE, null, ex);
    }
    return Arr;
  }
}
