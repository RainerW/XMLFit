package net.sourceforge.xmlfit.configuration.impl;

import net.sourceforge.xmlfit.configuration.Configuration;

/**
 * Default Implementation of {@link Configuration}
 */
public enum ConfigurationImpl implements Configuration {

	INSTANCE;
	
	private String inputDirectory;
	
	private String outputDirectory;
	
	public String getInputDirectory() {
		return inputDirectory;
	}

	public void setInputDirectory(String inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

}
