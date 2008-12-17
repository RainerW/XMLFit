
package net.sourceforge.xmlfit.runner;

import generated.Call;
import generated.Testgroup;
import generated.Testsuite;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

/**XMLFit 
 *@author faigle
 */
public class XMLFit  
{
 
  private String testdir = "testfiles";
  private String filedir = "xmlfiles";
  private String outputdir;
  private String inputDir;
  private String testsuite;
  private String cssfile;
 
  private static final int BUFFER = 4096;
  private   List<String> tests = new ArrayList<String>();
 
  
  public static void run(XMLFitRunner runner)
  {
    
    XMLFit xmlfit = new XMLFit();
    xmlfit.setOutputDir(runner.getOutputDir());
    xmlfit.setInputDir(runner.getInputDir());
    xmlfit.setTestsuite(runner.getTestsuite());
    xmlfit.setCssfile(runner.getStylesheet());
    try
    {
      xmlfit.generate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  @SuppressWarnings("unchecked")
  public void generate() throws Exception 
  {
   net.sourceforge.xmlfit.xslt.Validate validator = new net.sourceforge.xmlfit.xslt.Validate();
   net.sourceforge.xmlfit.xslt.Transform transformer = new net.sourceforge.xmlfit.xslt.Transform();
   
    File dir = new File(outputdir);
    if(!dir.exists()) 
    {
      System.out.println("OutputDirectory does not exist. XMLFit will create it.");
      dir.mkdir();
    }
    
    File[] files = dir.listFiles();
    
    if(files != null)
    {  
      for(int i=0; i < files.length; i++)
      {
        files[i].delete();
      }
    }
  
    

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
          tests.add(inputDir +"/"+ calls.get(i).getTest());
        }
    }
    
    validator.validate(testsuite, tests);
    
    
   //getting XSLT Files out of the jar
   URL transformSuite = this.getClass().getResource("/TransformSuite.xsl");
   URL transformFiles = this.getClass().getResource("/TransformFiles.xsl");
   
   

   
   if(cssfile != null) 
   {
     String filename = new File(cssfile).getName();
     transformer.setCssfile(filename); 
   }
   else
   {
     transformer.setCssfile("suite.css"); 
   
   }
   
   transformer.transform(new File(testsuite), transformSuite, 
        outputdir, testdir, filedir, inputDir, 0);
   transformer.transform(new File(testsuite), transformFiles, 
        outputdir, filedir, testdir, inputDir, 1);
   
   
   new File(outputdir+"/css").mkdir();
   new File(outputdir+"/css/images").mkdir();
   new File(outputdir +"/"+ testdir +"/css").mkdir();
   new File(outputdir +"/"+ testdir +"/css/images").mkdir();
   new File(outputdir +"/"+ filedir +"/css").mkdir();
   new File(outputdir +"/"+ filedir +"/css/images").mkdir();
   
   
   if(cssfile == null)
   {
     copy("/css/suite.css", outputdir+ "/css/suite.css"); 
     copy("/css/suite.css", outputdir +"/"+ testdir +"/css/suite.css");
     copy("/css/design.css", outputdir +"/"+ filedir +"/css/design.css");
     copy("/css/images/logo2.jpg", outputdir+ "/css/images/logo.jpg"); 
     copy("/css/images/XMLFitLogo.jpg", outputdir+ "/css/images/XMLFitLogo.jpg");
     copy("/css/images/logo2.jpg", outputdir+ "/"+ testdir +"/css/images/logo.jpg"); 
     copy("/css/images/XMLFitLogo.jpg", outputdir+ "/"+ testdir +"/css/images/XMLFitLogo.jpg"); 
   }
   else
   {
     String filename = new File(cssfile).getName();
     copyDirectory(new File(cssfile), new File(outputdir+"/css/"+filename));
     copyDirectory(new File(cssfile), new File(outputdir+ "/"+ testdir+ "/css/"+filename));
     copy("/css/design.css", outputdir +"/"+ filedir +"/css/design.css");
   }
}

 //Utility method to copy directorys and files
 public void copyDirectory(File sourceLocation , File targetLocation)
  throws IOException 
  {
      
      if (sourceLocation.isDirectory())
      {
          if (!targetLocation.exists()) 
          {
              targetLocation.mkdir();
          }
          
          String[] children = sourceLocation.list();
          for (int i=0; i<children.length; i++) 
          {
              copyDirectory(new File(sourceLocation, children[i]),
                      new File(targetLocation, children[i]));
          }
      } 
      else 
      {
          
          InputStream in = new FileInputStream(sourceLocation);
          OutputStream out = new FileOutputStream(targetLocation);
        
          byte[] buf = new byte[BUFFER];
          int len;
          while ((len = in.read(buf)) > 0) 
          {
              out.write(buf, 0, len);
          }
          in.close();
          out.close();
      }
  }
  
//Utility method to copy files out of the jar
  public void copy(String input, String output)
  {
    InputStream in = getClass().getResourceAsStream(input);
    BufferedInputStream bufIn = new BufferedInputStream(in);

    BufferedOutputStream bufOut = null;

  try 
  {
    bufOut = new BufferedOutputStream(new FileOutputStream(output));
  } 
  catch (FileNotFoundException e1)
  {
    e1.printStackTrace();
  }

  byte[] inByte = new byte[BUFFER];
  int count = -1;
  try 
  {
    while ((count = bufIn.read(inByte))!=-1) 
    {
      bufOut.write(inByte, 0, count);
    }
  } 
  catch (IOException e) 
  {
    e.printStackTrace();
  }

  try 
  {
    bufOut.close();
  }
   catch (IOException e) 
  {
     e.printStackTrace();
 
  }
  try 
  {
    bufIn.close();
  } 
    catch (IOException e) 
  {
      e.printStackTrace();
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
   

