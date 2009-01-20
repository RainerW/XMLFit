package net.sourceforge.xmlfit.plugin;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.maven.plugin.AbstractMojo;

import net.sourceforge.xmlfit.runner.XMLFit;
import net.sourceforge.xmlfit.runner.XMLFitCommandLineRunner;
import net.sourceforge.xmlfit.runner.XMLFitRunner;


/**
 * Transform XML testfiles to HTML.
 * @goal transform
 */
public class XMLFitMojo extends AbstractMojo implements XMLFitRunner
{
  /**
   * The path to the testsuite.
   *
   * @parameter testsuite
   */
  private String testsuite;
  
  /**
   * The input folder.
   *
   * @parameter inputDir
   */
  private String inputDir;
  
  /**
   * The output folder.
   *
   * @parameter outputDir
   */
  private String outputDir;
  
  /**
   * The custom CSS File.
   *
   * @parameter stylesheet
   */
  private String stylesheet;
  
  private static final int DELAY = 6000;
  private static Logger logger = Logger.getLogger(XMLFitCommandLineRunner.class);
  
  public void execute()
  {
    PropertyConfigurator.configureAndWatch("log4j.properties", DELAY);
    try
    {
     logger.info("Start XMLFit");
     XMLFit.run(this);
     logger.info("End XMLFit");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
   
  }
  public String getStylesheet()
  {
    if(!(stylesheet == null))
    {
      return stylesheet;
    }
    return null;
  }
  
  public void setStylesheet(String stylesheet)
  {
    this.stylesheet = stylesheet;
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


  public String getOutputDir()
  {
    return outputDir;
  }
  
  public void setOutputDir(String outputDir)
  {
    this.outputDir = outputDir;
  }
}
