package net.sourceforge.xmlfit.data;

import net.sourceforge.xmlfit.data.impl.XMLDataSourceImpl;

/**
 * Interface of a XMLFit DataSource.
 * 
 * A DataSource provides a ordered collection of DataSets.
 * 
 * @see DataSet for the DataSet Interface
 * @see XMLDataSourceImpl for a example implementation of a DataSource. 
 * @see Iterable
 */
public interface DataSource extends Iterable<DataSet>{
	
}
