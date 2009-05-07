package net.sourceforge.xmlfit.data.impl;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class DataSourceFactoryImplTest {

	private DataSourceFactoryImpl dataSourceFactoryImpl = 
		DataSourceFactoryImpl.INSTANCE;
	
	@Test
	public void testExtractFileExtension() throws Exception {
		String extension = dataSourceFactoryImpl.extractFileExtension(
				"testfile.html.xml");
		assertEquals("XML", extension);
	}
	
	@Test
	@Ignore
	public void testGetClassForExtension() throws Exception {
		Class clazz = dataSourceFactoryImpl.getClassForExension("XML");
		assertEquals(XMLDataSourceImpl.class, clazz);
	}
	
}
