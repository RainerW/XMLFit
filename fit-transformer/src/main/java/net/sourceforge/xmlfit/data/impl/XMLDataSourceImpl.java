package net.sourceforge.xmlfit.data.impl;

import java.io.File;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.xmlfit.data.DataSet;
import net.sourceforge.xmlfit.data.DataSource;
import net.sourceforge.xmlfit.data.DataSourceProvider;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@DataSourceProvider(fileExtension="XML")
public class XMLDataSourceImpl implements DataSource, Iterator<DataSet> {

	private static Logger logger = 
		Logger.getLogger(XMLDataSourceImpl.class);
	
	private int actualIndex = 0;
	
	private Document document;

	private NodeList dataSets;

	public XMLDataSourceImpl(String fileName) {
		try {
			DocumentBuilderFactory factory = 
				DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			document = documentBuilder.parse(new File(fileName));
			dataSets = document.getElementsByTagName("dataSet");
			
		} catch (Exception e) {
			logger.error("Error on loading data file with name: " +fileName);
			logger.error(e);
			throw new XmlLoadingException(e);
		}
	}

	public Iterator<DataSet> iterator() {
		return this;
	}

	public boolean hasNext() {
		return actualIndex < dataSets.getLength();
	}

	public DataSet next() {
		Node dataSetNode = dataSets.item(actualIndex++);
		return new XMLDataSetImpl(dataSetNode);
	}

	public void remove() {
		
	}

}
