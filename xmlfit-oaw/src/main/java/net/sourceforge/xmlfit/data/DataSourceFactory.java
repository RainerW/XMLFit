package net.sourceforge.xmlfit.data;

import net.sourceforge.xmlfit.data.impl.DataSourceFactoryImpl;

/**
 * Factory for the XMLFit DataSources.
 */
public interface DataSourceFactory {
	
	/**
	 * The instance of the factory which will be use by XMLFit.
	 */
	// Design - Comment:
	// There never be more then one implementation so the default factory pattern is ok.
	// is not final because for testing we should be able to change the implementation.
	public static DataSourceFactory INSTANCE = DataSourceFactoryImpl.INSTANCE;

	/**
	 * Create a DataSource for file, the file extension is used to selected the DataSource.
	 * Example: File with the name *.xml a instance of a XMLDataSourceImpl will be return.
	 * @param fileName the name of the DataSource file. For Example a XML File.
	 * @return a instance of a DataSource for the given file type.
	 */
	public DataSource createDataSource(String fileName);
	
}
