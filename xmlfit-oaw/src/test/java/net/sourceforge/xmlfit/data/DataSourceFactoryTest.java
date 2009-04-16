package net.sourceforge.xmlfit.data;

import static org.junit.Assert.*;

import net.sourceforge.xmlfit.data.DataSource;
import net.sourceforge.xmlfit.data.DataSourceFactory;

import org.junit.Test;

public class DataSourceFactoryTest {

	@Test
	public void testCreateDataSource() {
		DataSource dataSource = 
			DataSourceFactory.createDataSource("src/test/resources/example-data.xml");
		assertNotNull(dataSource);
	}

}
