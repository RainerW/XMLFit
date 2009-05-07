package net.sourceforge.xmlfit.data.impl;

import java.lang.annotation.Annotation;

import net.sourceforge.xmlfit.configuration.Configuration;
import net.sourceforge.xmlfit.data.DataSource;
import net.sourceforge.xmlfit.data.DataSourceFactory;
import net.sourceforge.xmlfit.data.DataSourceProvider;

/**
 * 
 */
public enum DataSourceFactoryImpl implements DataSourceFactory {

	INSTANCE;
	
	private Configuration configuration = Configuration.INSTANCE;
	
	public DataSource createDataSource(String fileName)
	{
		
		return new XMLDataSourceImpl(configuration.getInputDirectory()+fileName);
	}

	protected String extractFileExtension(String fileName) {
		int lastIndexOf = fileName.lastIndexOf('.');
		String extension = fileName.substring(lastIndexOf+1, fileName.length());
		return extension.toUpperCase();
	}

	protected Class getClassForExension(String extension) {
		return null;
	}
	
}
