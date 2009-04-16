package net.sourceforge.xmlfit.data;

public class DataSourceFactory {

	public static String inputDirectory;
	
	public static DataSource createDataSource(String fileName)
	{
		return new XMLDataSourceImpl(inputDirectory+fileName);
	}
	
}
