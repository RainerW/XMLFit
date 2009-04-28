package net.sourceforge.xmlfit.data;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDataSetImpl implements DataSet {

	private Node dataSetNode;
	
	public XMLDataSetImpl(Node node) 
	{
		this.dataSetNode = node;
	}
	
	public String getPropertyValue(String name) 
	{
		NodeList childNodes = dataSetNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if(node.getNodeName().equals(name))
			{
				return node.getTextContent();
			}
		}
		return null;
	}

}
