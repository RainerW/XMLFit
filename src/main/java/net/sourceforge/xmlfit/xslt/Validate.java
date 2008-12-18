package net.sourceforge.xmlfit.xslt;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;


/** Class to validate XML Files with XSD
 * 
 * @author faigle
 *
 */
public class Validate
{

  private static Logger logger = Logger.getLogger(Validate.class);
  
 public void validate(String src, List<String> tests) throws Exception
 { 
  
  SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
  URL suiteSchemaLocation = this.getClass().getResource("/SuiteSchema.xsd");
  URL fileSchemaLocation = this.getClass().getResource("/FileSchema.xsd");
  Schema suiteSchema = factory.newSchema(suiteSchemaLocation);
  Schema fileSchema = factory.newSchema(fileSchemaLocation);
  Validator fileValidator = fileSchema.newValidator();
  Validator suiteValidator = suiteSchema.newValidator();
  Source source = new StreamSource(src);
  
  try 
  {
    suiteValidator.validate(source);
    System.out.println(new File(src).toURI() + " is a valid XMLFit testsuite file.");
  }
  catch (SAXException ex) 
  {
    System.out.println(new File(src).toURI()+ " is not valid because ");
    System.out.println(ex.getMessage());
  }  
 
  for(int i = 0; i < tests.size(); i++)
  {
    Source sc = new StreamSource(tests.get(i));
    try
    {
      fileValidator.validate(sc);
      System.out.println(tests.get(i) + " is a valid XMLFit testfile ");
    }
    catch (SAXException ex)
    {
      System.out.println(tests.get(i) + " is not valid because ");
      System.out.println(ex.getMessage());
    }
   }
  }
}
