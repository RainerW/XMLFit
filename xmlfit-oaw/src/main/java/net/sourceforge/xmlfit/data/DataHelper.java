package net.sourceforge.xmlfit.data;

import java.util.ArrayList;
import java.util.Collection;

public class DataHelper {
	
	public static Collection<DataSet> createDataSource(String fileName)
	{
		DataSource dataSource = DataSourceFactory.createDataSource(fileName);
		ArrayList<DataSet> list = new ArrayList<DataSet>();
		for (DataSet dataSet : dataSource) {
			list.add(dataSet);
		}
		return list;
	}
	
}
