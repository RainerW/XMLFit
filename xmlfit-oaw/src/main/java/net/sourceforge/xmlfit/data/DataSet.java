package net.sourceforge.xmlfit.data;

import net.sourceforge.xmlfit.data.impl.NullDataSetImpl;
import net.sourceforge.xmlfit.data.impl.XMLDataSetImpl;

/**
 * 
 * Interface for a XMLFit DataSet.
 * 
 * @see XMLDataSetImpl for a example implementation for a DataSet.
 * 
 */
public interface DataSet 
{
	
	/**
	 * The Value of a null DataSet variable.
	 * Use NULL_DATASET instead of null for a DataSet.
	 */
	public static final DataSet NULL_DATASET = new NullDataSetImpl();
	
	/**
	 * Get property value by name.
	 * @param name the name of the property.
	 * @return value of the property or null when property with name 
	 * exists in the DataSet.
	 */
	public String getPropertyValue(String name);
	
}
