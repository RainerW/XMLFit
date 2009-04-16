package net.sourceforge.xmlfit.data;

public class DataSourceFactory {

	public static DataSource createDataSource(String fileName)
	{
		return new XMLDataSourceImpl(fileName);
	}
	
}
