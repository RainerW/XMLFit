package net.sourceforge.xmlfit.runner;

import java.util.HashMap;

import net.sourceforge.xmlfit.data.DataSourceFactory;
import net.sourceforge.xmlfit.reader.DynamicXMLReader;

import org.apache.log4j.Logger;
import org.openarchitectureware.workflow.WorkflowRunner;
import org.openarchitectureware.workflow.monitor.NullProgressMonitor;

public enum TestRunner 
{
	
	INSTANCE;
	
	private static Logger logger = Logger.getLogger(TestRunner.class);
	
	private static final String workFlowFile = "net/sourceforge/xmlfit/xmlfit.oaw";
		
	public void run(String testFile, String outputDir, String inputDirectory)
	{
		logger.info("");
		logger.info("--------------------------------------------------------");
		logger.info("Run XMLFit");
		logger.info("--------------------------------------------------------");
		logger.info("");
		logger.info("Start transformation for test file: "+ testFile);
		logger.info("ouput Dir: "+ outputDir);
		logger.info("input Dir: "+ inputDirectory);
		logger.info("");
		
		DynamicXMLReader.inputDirectory = inputDirectory;
		DataSourceFactory.inputDirectory = inputDirectory;
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("testFile", testFile);
		params.put("outputDir", outputDir);
		params.put("test", "none");
		WorkflowRunner workflowRunner = new WorkflowRunner();
		workflowRunner.run(
				workFlowFile, 
				new NullProgressMonitor(), 
				params, 
				new HashMap<String, String>());
	}
	
}
