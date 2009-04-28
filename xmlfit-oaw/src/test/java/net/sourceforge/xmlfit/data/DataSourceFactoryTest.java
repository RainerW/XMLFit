package net.sourceforge.xmlfit.data;

import static org.junit.Assert.*;

import net.sourceforge.xmlfit.data.DataSource;
import net.sourceforge.xmlfit.data.DataSourceFactory;

import org.junit.Test;

public class DataSourceFactoryTest {

	@Test
	public void testCreateDataSource() {
		DataSourceFactory.inputDirectory = "src/test/resources/";
		DataSource dataSource = 
			DataSourceFactory.createDataSource("example-data.xml");
		assertNotNull(dataSource);
	}

}
