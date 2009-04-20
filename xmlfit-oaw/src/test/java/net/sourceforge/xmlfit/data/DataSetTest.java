package net.sourceforge.xmlfit.data;

import static org.junit.Assert.*;

import net.sourceforge.xmlfit.data.DataSet;
import net.sourceforge.xmlfit.data.DataSourceFactory;

import org.junit.Before;
import org.junit.Test;

public class DataSetTest {

	private DataSet dataSet;
	
	@Before
	public void setUp()
	{
		DataSourceFactory.inputDirectory = "src/test/resources/";
		dataSet = 
			DataSourceFactory.createDataSource("example-data.xml").iterator().next();
	}
	
	@Test
	public void testGetPropertyValue_username() {
		String propertyValue = dataSet.getPropertyValue("username");
		assertEquals("baranowsk01", propertyValue);
	}
	
	@Test
	public void testGetPropertyValue_password() {
		String propertyValue = dataSet.getPropertyValue("password");
		assertEquals("test01", propertyValue);
	}

}
