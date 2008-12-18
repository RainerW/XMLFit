package net.sourceforge.xmlfit.xslt;


import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;


import org.apache.log4j.Logger;
import org.apache.xalan.processor.TransformerFactoryImpl;





import java.io.File;
import java.net.URL;


/** Class to transform XML Files with XSLT
 * 
 * @author faigle
 *
 */
public class Transform  
{

 
 private static Logger logger = Logger.getLogger(Transform.class);
  
 private String cssfile;
  
 public void transform(File testsuite, URL xsltFile, String outputSrc, String testDir, 
     String fileDir, String inputDir, int choice) 
 {
   String method = "transform() : ";
   logger.debug(method + "Start");
  
   Source xsltSource = new StreamSource(xsltFile.toString());
   TransformerFactory transFact = TransformerFactory.newInstance();
   if (transFact instanceof TransformerFactoryImpl) 
   {
     transFact.setAttribute(TransformerFactoryImpl.FEATURE_SOURCE_LOCATION, 
                        Boolean.TRUE);
   }
   try
   {
   Transformer trans = transFact.newTransformer(xsltSource);
   trans.setParameter("test_dir", testDir);
   trans.setParameter("base_dir", testDir);
   trans.setParameter("file_dir", fileDir);
   trans.setParameter("input_dir", inputDir);
   trans.setParameter("css_file", cssfile);
   Source xmlSource = new StreamSource(testsuite);
   
   
   if(choice == 0)
   {
    logger.info("Processing " + testsuite.getName() + " with xslt file " + xsltFile.getFile());
    trans.transform(xmlSource, new StreamResult(new File(outputSrc+"/"
       +testsuite.getName().replaceAll(".xml", ".html"))));
   }
   else if(choice == 1)
   {
    logger.info("Processing " + testsuite.getName() + " with xslt file " + xsltFile.getFile());
     trans.transform(xmlSource, new StreamResult(new File(outputSrc+"/"+ "allFiles"
        +testsuite.getName().replaceAll(".xml", ".html"))));
    
    logger.info("Deleting temporary File " + outputSrc+"/"+ "allFiles" 
        +testsuite.getName().replaceAll(".xml", ".html"));
     new File(outputSrc+"/"+ "allFiles"
        +testsuite.getName().replaceAll(".xml", ".html")).delete();
  
    }
   }
   catch(Exception e)
   {
     logger.info("Validation failed see xmlfit.log for more details");
     logger.error("Transformation failed " + e.getMessage());
   }
   
   logger.debug(method + "End");
 
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





