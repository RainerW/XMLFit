package net.sourceforge.xmlfit.reader;

import java.util.HashMap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.openarchitectureware.workflow.WorkflowComponent;
import org.openarchitectureware.workflow.WorkflowContext;
import org.openarchitectureware.workflow.ast.parser.Location;
import org.openarchitectureware.workflow.container.CompositeComponent;
import org.openarchitectureware.workflow.issues.Issues;
import org.openarchitectureware.workflow.monitor.ProgressMonitor;
import org.openarchitectureware.xsd.XMLReaderImpl;
import org.openarchitectureware.xsd.XSDMetaModel;

public class DynamicXMLReader implements WorkflowComponent
{

	private String name;
	
	private CompositeComponent container;
	
	private Location location;
	
	private static XSDMetaModel metaModel;
	
	public static String inputDirectory = "";
	
	public XSDMetaModel getMetaModel() {
		return metaModel;
	}

	public void setMetaModel(XSDMetaModel metaModel) {
		DynamicXMLReader.metaModel = metaModel;
	}

	public static EObject loadModelFromXmlFile(String fileName)
	{
		XMLReaderImpl reader = new XMLReaderImpl(new ResourceSetImpl(), metaModel);
		reader.setUri(inputDirectory + fileName);
		reader.setUseDocumentRoot(false);
		reader.getOptions().putAll(new HashMap<String, Object>());
		EObject model = reader.readXML();
		
		return model;
	}

	public void checkConfiguration(Issues issues) {
	}

	public String getComponentName() {
		return name;
	}

	public CompositeComponent getContainer() {
		return container;
	}

	public Location getLocation() {
		return location;
	}

	public void invoke(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
	}

	public void setContainer(CompositeComponent container) {
		this.container = container;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
