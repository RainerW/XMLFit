package net.sourceforge.xmlfit;

import java.util.HashMap;

import net.sourceforge.xmlfit.data.DataSourceFactory;

import org.openarchitectureware.workflow.WorkflowRunner;
import org.openarchitectureware.workflow.monitor.NullProgressMonitor;

public class TestRunner 
{
	
	private static final String workFlowFile = "net/sourceforge/xmlfit/xmlfit.oaw";
	
	public static void run(String testFile, String outputDir, String inputDirectory)
	{
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
