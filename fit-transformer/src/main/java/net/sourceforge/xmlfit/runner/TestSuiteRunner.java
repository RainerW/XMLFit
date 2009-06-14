package net.sourceforge.xmlfit.runner;

import java.util.HashMap;

import net.sourceforge.xmlfit.configuration.Configuration;

import org.openarchitectureware.workflow.WorkflowRunner;
import org.openarchitectureware.workflow.monitor.NullProgressMonitor;

public enum TestSuiteRunner {

	INSTANCE;

	private String workFlowFile = "net/sourceforge/xmlfit/xmlfit-suite.oaw";
	
	private Configuration configuration = Configuration.INSTANCE;

	public void run(String testSuite, String outputDirectory, String inputDirectory) {
		configuration.setInputDirectory(inputDirectory);
		configuration.setOutputDirectory(outputDirectory);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("testSuiteFile", inputDirectory + testSuite);
		params.put("outputDir", outputDirectory);
		WorkflowRunner workflowRunner = new WorkflowRunner();
		workflowRunner.run(workFlowFile, new NullProgressMonitor(), params,
				new HashMap<String, String>());
	}

	public static void runTestFromSuite(String testFile) {
		TestRunner.INSTANCE.run(
				Configuration.INSTANCE.getInputDirectory() + testFile,
				Configuration.INSTANCE.getOutputDirectory(), 
				Configuration.INSTANCE.getInputDirectory());
	}

}
