package net.sourceforge.xmlfit.data;

import static org.junit.Assert.*;

import net.sourceforge.xmlfit.configuration.Configuration;
import net.sourceforge.xmlfit.data.DataSource;
import net.sourceforge.xmlfit.data.impl.DataSourceFactoryImpl;

import org.junit.Test;

public class DataSourceFactoryTest {

	@Test
	public void testCreateDataSource() {
		Configuration.INSTANCE.setInputDirectory("src/test/resources/");
		DataSource dataSource = 
			DataSourceFactory.INSTANCE.createDataSource("example-data.xml");
		assertNotNull(dataSource);
	}

}
