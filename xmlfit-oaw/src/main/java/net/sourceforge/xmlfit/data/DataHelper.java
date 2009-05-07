package net.sourceforge.xmlfit.data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Helper Class for use XMLFit DataSets in OAW Templates. 
 */
public class DataHelper {
	
	/**
	 * Create a list with DataSets.
	 * 
	 * @param fileName the DataSource file.
	 * 
	 * @return the list with DataSets.
	 */
	public static Collection<DataSet> createDataSource(String fileName)
	{
		DataSource dataSource = DataSourceFactory.INSTANCE.createDataSource(fileName);
		ArrayList<DataSet> list = new ArrayList<DataSet>();
		for (DataSet dataSet : dataSource) {
			list.add(dataSet);
		}
		return list;
	}
	
}
