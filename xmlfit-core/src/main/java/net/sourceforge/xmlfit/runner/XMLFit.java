
package net.sourceforge.xmlfit.runner;

import generated.Call;
import generated.Testgroup;
import generated.Testsuite;



import java.io.File;
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
  private List<String> tests = new ArrayList<String>();

 
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
    
    
   //getting XSLT Files out of the jar
   URL transformSuite = this.getClass().getResource("/TransformSuite.xsl");
   URL transformFiles = this.getClass().getResource("/TransformFiles.xsl");
   

   
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
   
     transformer.transform(new File(testsuite), transformSuite, 
     outputdir, testdir, filedir, inputDir, 0);
     transformer.transform(new File(testsuite), transformFiles, 
     outputdir, testdir, filedir, inputDir, 1);
   
   
   new File(outputdir+"/css/images").mkdirs();
   new File(outputdir +"/"+ testdir +"/css/images").mkdirs();
   new File(outputdir +"/"+ filedir +"/css/images").mkdirs();
   
   
   if(cssfile == null)
   {
     util.copyOutOfJar("/css/suite.css", outputdir+ "/css/suite.css"); 
     util.copyOutOfJar("/css/suite.css", outputdir +"/"+ testdir +"/css/suite.css");
     util.copyOutOfJar("/css/design.css", outputdir +"/"+ filedir +"/css/design.css");
     util.copyOutOfJar("/css/images/logo2.jpg", outputdir+ "/css/images/logo.jpg"); 
     util.copyOutOfJar("/css/images/XMLFitLogo.jpg", outputdir+ "/css/images/XMLFitLogo.jpg");
     util.copyOutOfJar("/css/images/logo2.jpg", outputdir+ "/"+ testdir +"/css/images/logo.jpg"); 
     util.copyOutOfJar("/css/images/XMLFitLogo.jpg", outputdir+ "/"+ testdir +"/css/images/XMLFitLogo.jpg"); 
   }
   else
   {
     String filename = new File(cssfile).getName();
     util.copy(new File(cssfile), new File(outputdir+"/css/"+filename));
     util.copy(new File(cssfile), new File(outputdir+ "/"+ testdir+ "/css/"+filename));
     util.copyOutOfJar("/css/design.css", outputdir +"/"+ filedir +"/css/design.css");
   }
}

  @SuppressWarnings("unchecked")
  public void getTestcasesToValidate(String testsuite) throws JAXBException
  {
    JAXBContext jc = JAXBContext.newInstance("generated");
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    Object unmarshal = unmarshaller.unmarshal(new File(testsuite));
    JAXBElement<Testsuite> el = new JAXBElement(new QName("generated"), this.getClass(), unmarshal);
    Testsuite suite = el.getValue();
    List<Testgroup> testgroups = suite.getTestgroup();
    
    for (Testgroup testgroup : testgroups)
    {
      List<Call> calls = testgroup.getCall();
     
      for (int i = 0; i < calls.size(); i++)
        {
          if(calls.get(i).getTestsuite() != null && calls.get(i).getTestgroup() == null)
          {
            this.getTestcasesToValidate(inputDir + "/"+calls.get(i).getTestsuite());
          }
          
          if(calls.get(i).getTest() != null)
          {
          tests.add(inputDir +"/"+ calls.get(i).getTest());
          }
       }
    }
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
}
   

