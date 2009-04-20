package net.sourceforge.xmlfit.runner;

import java.util.HashMap;

import net.sourceforge.xmlfit.data.DataSourceFactory;
import net.sourceforge.xmlfit.reader.DynamicXMLReader;

import org.openarchitectureware.workflow.WorkflowRunner;
import org.openarchitectureware.workflow.monitor.NullProgressMonitor;

public enum TestRunner 
{

	INSTANCE;
	
	private static final String workFlowFile = "net/sourceforge/xmlfit/xmlfit.oaw";
		
	public void run(String testFile, String outputDir, String inputDirectory)
	{
		DynamicXMLReader.inputDirectory = inputDirectory;
		DataSourceFactory.inputDirectory = inputDirectory;
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("testFile", testFile);
		params.put("outputDir", outputDir);
		WorkflowRunner workflowRunner = new WorkflowRunner();
		workflowRunner.run(
				workFlowFile, 
				new NullProgressMonitor(), 
				params, 
				new HashMap<String, String>());
	}
	
}
