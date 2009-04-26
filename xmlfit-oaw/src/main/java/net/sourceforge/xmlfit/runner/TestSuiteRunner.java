package net.sourceforge.xmlfit.runner;

import java.util.HashMap;

import net.sourceforge.xmlfit.data.DataSourceFactory;

import org.openarchitectureware.workflow.WorkflowRunner;
import org.openarchitectureware.workflow.monitor.NullProgressMonitor;

public enum TestSuiteRunner {

	INSTANCE;
	
	private String workFlowFile = "net/sourceforge/xmlfit/xmlfit-suite.oaw";

	public String outputDir = "target";
	
	public String inputDirectory = "src/test/resources/";
	
	public Counter counter = new Counter();
	
	public void run(String testSuite, String outputDir, String inputDirectory)
	{
		this.outputDir = outputDir;
		this.inputDirectory = inputDirectory;
		
		DataSourceFactory.inputDirectory = inputDirectory;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("testSuiteFile", inputDirectory+testSuite);
		params.put("outputDir", outputDir);
		WorkflowRunner workflowRunner = new WorkflowRunner();
		workflowRunner.run(
				workFlowFile, 
				new NullProgressMonitor(), 
				params, 
				new HashMap<String, String>());
	}
	
	public static void runTestFromSuite(String testFile)
	{
		try
		{
			TestRunner.INSTANCE.run(
					INSTANCE.inputDirectory+testFile, 
					INSTANCE.outputDir, 
					INSTANCE.inputDirectory);
		}
		catch(Throwable exp)
		{
			INSTANCE.counter.failed++;
		}
		INSTANCE.counter.success++;
	}
	
}
