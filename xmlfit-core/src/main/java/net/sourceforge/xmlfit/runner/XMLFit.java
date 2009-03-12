
package net.sourceforge.xmlfit.runner;

import generated.Call;
import generated.Testgroup;
import generated.Testsuite;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;


/**XMLFit 
 *@author faigle
 */
public class XMLFit  
{
 
  private static Logger logger = Logger.getLogger(XMLFit.class);
  
  private String testdir = "testfiles";
  private String filedir = "xmlfiles";
  private String outputdir;
  private String inputDir;
  private String testsuite;
  private String cssfile;
  private String mode;
  private List<String> tests = new ArrayList<String>();
  private URL transformSuite;
  private URL transformFiles;
  
  public XMLFit()
  {
  }
 
  public static void run(XMLFitRunner runner)
  {
    String method = "run() : ";
    logger.debug(method + "Start");
    
    XMLFit xmlfit = new XMLFit();
    xmlfit.setOutputDir(runner.getOutputDir());
    logger.info("Setting output directory to: " + runner.getOutputDir());
    xmlfit.setInputDir(runner.getInputDir());
    logger.info("Setting input directory to: " + runner.getInputDir());
    xmlfit.setTestsuite(runner.getTestsuite());
    logger.info("Setting testsuite file to: " + runner.getTestsuite());
    xmlfit.setCssfile(runner.getStylesheet());
    xmlfit.setMode(runner.getMode());
   
    try
    {
      xmlfit.generate();
    }
    catch (Exception e)
    {
      logger.error(method + "generate fit tests failed", e);
      logger.info("Generate FIT tests fails");
    }
    
    logger.debug(method + "End");
  }
  
  
  public void generate() throws Exception 
  {
   net.sourceforge.xmlfit.xslt.Validate validator = new net.sourceforge.xmlfit.xslt.Validate();
   net.sourceforge.xmlfit.xslt.Transform transformer = new net.sourceforge.xmlfit.xslt.Transform();
   net.sourceforge.xmlfit.util.Util util = new net.sourceforge.xmlfit.util.Util();
    
   File dir = new File(outputdir);
    if(!dir.exists()) 
    {
      logger.info("OutputDirectory does not exist. XMLFit will create it.");
      dir.mkdirs();
    }
    
    this.getTestcasesToValidate(testsuite);
    
    validator.validate(testsuite, tests);
    
    
    
    if(mode != null)
    {
     transformSuite = this.getClass().getResource("/" + mode+"/Transform.xsl");
     transformFiles = this.getClass().getResource("/base/TransformFiles.xsl");
    }
    else 
    {
     transformSuite = this.getClass().getResource("/fit/Transform.xsl");
     transformFiles = this.getClass().getResource("/base/TransformFiles.xsl");
  
  }
   
   if(cssfile != null) 
   {
     String filename = new File(cssfile).getName();
     transformer.setCssfile(filename); 
     logger.info("Setting Stylesheet to " + cssfile);
   }
   else
   {
     transformer.setCssfile("suite.css"); 
     logger.info("Using default Stylesheet");
   }
   
   if(this.getTestsuites(testsuite).isEmpty()) 
   {
     transformer.transform(new File(testsuite), transformSuite, 
     outputdir, testdir, filedir, inputDir, 0);
     transformer.transform(new File(testsuite), transformFiles, 
     outputdir, testdir, filedir, inputDir, 1);
    }
   else 
   {
     for(String name : this.getTestsuites(testsuite)) 
     {
       transformer.transform(new File(name), transformSuite, outputdir, testdir, filedir, inputDir, 0);
       transformer.transform(new File(name), transformFiles, outputdir, testdir, filedir, inputDir, 1);
     }
  }
   
   
   
  
   
   
   new File(outputdir+"/css/images").mkdirs();
   new File(outputdir +"/"+ testdir +"/css/images").mkdirs();
   new File(outputdir +"/"+ filedir +"/css/images").mkdirs();
   
   
   if(cssfile == null)
   {
     util.copyOutOfJar("/base/css/suite.css", outputdir+ "/css/suite.css"); 
     util.copyOutOfJar("/base/css/suite.css", outputdir +"/"+ testdir +"/css/suite.css");
     util.copyOutOfJar("/base/css/design.css", outputdir +"/"+ filedir +"/css/design.css");
     util.copyOutOfJar("/base/css/images/logo.jpg", outputdir+ "/css/images/logo.jpg"); 
     util.copyOutOfJar("/base/css/images/XMLFitLogo.jpg", outputdir+ "/css/images/XMLFitLogo.jpg");
     util.copyOutOfJar("/base/css/images/logo.jpg", outputdir+ "/"+ testdir +"/css/images/logo.jpg"); 
     util.copyOutOfJar("/base/css/images/XMLFitLogo.jpg", outputdir+ "/"+ testdir +"/css/images/XMLFitLogo.jpg"); 
   }
   else
   {
     String filename = new File(cssfile).getName();
     util.copy(new File(cssfile), new File(outputdir+"/css/"+filename));
     util.copy(new File(cssfile), new File(outputdir+ "/"+ testdir+ "/css/"+filename));
     util.copyOutOfJar("/base/css/design.css", outputdir +"/"+ filedir +"/css/design.css");
   }
}

  @SuppressWarnings("unchecked")
  public void getTestcasesToValidate(String testsuite) throws JAXBException, IOException
  {
    String actualFolder = new File(testsuite).getAbsolutePath().replace(new File(testsuite).getName(), "");
    JAXBContext jc = JAXBContext.newInstance("generated");
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    Object unmarshal = unmarshaller.unmarshal(new File(testsuite));
    JAXBElement<Testsuite> el = new JAXBElement(new QName("generated"), this.getClass(), unmarshal);
    Testsuite suite = el.getValue();
    List<Testgroup> testgroups = suite.getTestgroup();
    
    for (Testgroup testgroup : testgroups)
    {
      List<Call> calls = testgroup.getCall();
      for (Call call : calls)
      {
        if(call.getTestsuite() != null && call.getTestgroup() == null)
        {
          this.getTestcasesToValidate(actualFolder + "/"+call.getTestsuite());
        }
        
        if(call.getTest() != null)
        {
          tests.add(actualFolder +"/"+ call.getTest());
        }
      }
      
    }
}
  @SuppressWarnings("unchecked")
  public List<String> getTestsuites(String testsuite) throws JAXBException
  {
    JAXBContext jc = JAXBContext.newInstance("generated");
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    Object unmarshal = unmarshaller.unmarshal(new File(testsuite));
    JAXBElement<Testsuite> el = new JAXBElement(new QName("generated"), this.getClass(), unmarshal);
    Testsuite suite = el.getValue();
    List<Call> testsuites = suite.getCall();
    List<String> testsuitenames = new ArrayList();
    for(Call call : testsuites)
    {
     testsuitenames.add(inputDir+"/"+call.getTestsuite());
    }
  return testsuitenames;
  }
   
  //Getter and Setter
  public String getTestdir()
  {
    return testdir;
  }

  public String getFiledir()
  {
    return filedir;
  }

  public String getOutputDir()
  {
    return outputdir;
  }

  public void setTestdir(String testdir)
  {
    this.testdir = testdir;
  }

  public void setFiledir(String filedir)
  {
    this.filedir = filedir;
  }

  public void setOutputDir(String outputDir)
  {
    this.outputdir = outputDir;
  }

  public String getTestsuite()
  {
    return testsuite;
  }
  
  public void setTestsuite(String testsuite)
  {
    this.testsuite = testsuite;
  }

  public String getInputDir()
  {
    return inputDir;
  }
  
  public void setInputDir(String inputDir)
  {
    this.inputDir = inputDir;
  }
  
  public String getCssfile()
  {
    return cssfile;
  }

  public void setCssfile(String cssfile)
  {
    this.cssfile = cssfile;
  }

  public void setMode(String mode)
  {
    this.mode = mode;
  }
  
 

}
   

