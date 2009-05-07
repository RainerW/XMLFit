package net.sourceforge.xmlfit.configuration;

import net.sourceforge.xmlfit.configuration.impl.ConfigurationImpl;

/**
 * Interface for the configuration component of xmlfit.
 * Here are the global settings stored for one xmlfit run. 
 */
public interface Configuration {
	
	/**
	 * Instance of the configuration component, singelton.
	 */
	public static Configuration INSTANCE = ConfigurationImpl.INSTANCE;
	
	/**
	 * Setter for the input directory.
	 * @param inputDirectory the root directory of the xmlfit tests.
	 */
	public void setInputDirectory(String inputDirectory);
	
	/**
	 * Getter for the input directory.
	 * @return the root directory of the xmlfit tests.
	 */
	public String getInputDirectory();
	
	/**
	 * Setter for output directory of the fit tests.
	 * @param outputDirectory the directory for the fit tests.
	 */
	public void setOutputDirectory(String outputDirectory);
	
	/**
	 * Getter for the output directory of the fit tests.
	 * @return the directory of the generated fit tests.
	 */
	public String getOutputDirectory();
	
}
