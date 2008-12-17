package net.sourceforge.xmlfit.xslt;


import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

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

 
  
 private String cssfile;
  
 public void transform(File testsuite, URL xsltFile, String outputSrc, String testDir, 
     String fileDir, String inputDir, int choice) 
 throws Exception 
 {
   Source xsltSource = new StreamSource(xsltFile.toString());
   TransformerFactory transFact = TransformerFactory.newInstance();
   if (transFact instanceof TransformerFactoryImpl) 
   {
     transFact.setAttribute(TransformerFactoryImpl.FEATURE_SOURCE_LOCATION, 
                        Boolean.TRUE);
   }
   Transformer trans = transFact.newTransformer(xsltSource);
   trans.setParameter("test_dir", testDir);
   trans.setParameter("base_dir", testDir);
   trans.setParameter("file_dir", fileDir);
   trans.setParameter("input_dir", inputDir);
   trans.setParameter("css_file", cssfile);
   Source xmlSource = new StreamSource(testsuite);
   
   
   if(choice == 0)
   {
   trans.transform(xmlSource, new StreamResult(new File(outputSrc+"/"
       +testsuite.getName().replaceAll(".xml", ".html"))));
   }
   else if(choice == 1)
   {
    trans.transform(xmlSource, new StreamResult(new File(outputSrc+"/"+ "allFiles"
        +testsuite.getName().replaceAll(".xml", ".html"))));
    
    new File(outputSrc+"/"+ "allFiles"
        +testsuite.getName().replaceAll(".xml", ".html")).delete();
   }
 
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





