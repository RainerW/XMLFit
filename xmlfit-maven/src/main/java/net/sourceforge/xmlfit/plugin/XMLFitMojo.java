package net.sourceforge.xmlfit.plugin;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.maven.plugin.AbstractMojo;

import net.sourceforge.xmlfit.runner.XMLFit;
import net.sourceforge.xmlfit.runner.XMLFitCommandLineRunner;

/**
 * Transform XML testfiles to HTML.
 * @goal transform
 */
public class XMLFitMojo extends AbstractMojo
{
  /**
   * The path to the testsuites.
   * 
   * @parameter
   */
  private String[] testsuites;

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

  /**
   * The custom CSS File.
   * 
   * @parameter mode
   */
  private String mode;

  private static final int DELAY = 6000;

  private static Logger logger = Logger
      .getLogger(XMLFitCommandLineRunner.class);

  public void execute()
  {
    PropertyConfigurator.configureAndWatch("log4j.properties", DELAY);
    try
    {
      logger.info("Start XMLFit");
      XMLFitMojoRunner runner = new XMLFitMojoRunner();
      runner.setInputDir(getInputDir());
      runner.setMode(getMode());
      runner.setOutputDir(getOutputDir());
      runner.setStylesheet(getStylesheet());
      for (String suite : getTestsuite())
      {
        logger.info("XMLFit running next suite : " + suite);
        runner.setTestsuite(suite);
        XMLFit.run(runner);
      }
      logger.info("End XMLFit");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  public String getStylesheet()
  {
    if (!(stylesheet == null))
    {
      return stylesheet;
    }
    return null;
  }

  public String getMode()
  {
    if (!(mode == null))
    {
      return mode;
    }
    return null;
  }

  public void setMode(String mode)
  {
    this.mode = mode;
  }

  public void setStylesheet(String stylesheet)
  {
    this.stylesheet = stylesheet;
  }

  public String[] getTestsuite()
  {
    return this.testsuites;
  }

  public void setTestsuites(String[] testsuite)
  {
    this.testsuites = testsuite;
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
