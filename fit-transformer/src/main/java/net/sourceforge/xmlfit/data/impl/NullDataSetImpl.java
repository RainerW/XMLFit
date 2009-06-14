package net.sourceforge.xmlfit.data.impl;

import net.sourceforge.xmlfit.data.DataSet;

/**
 * NullDataSet can be used when no DataSet is initialized.
 */
public class NullDataSetImpl implements DataSet {

	/**
	 * Always returns null.
	 */
	public String getPropertyValue(String name) {
		return null;
	}

}
