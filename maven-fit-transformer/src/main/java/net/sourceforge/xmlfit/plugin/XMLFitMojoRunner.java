package net.sourceforge.xmlfit.plugin;

/**
 * Parameter Object to run multiple XMLFit tests
 */
public class XMLFitMojoRunner
{

  private String inputDir;

  private String mode;

  private String outputDir;

  private String stylesheet;

  private String testSuite;

  public String getInputDir()
  {
    return inputDir;
  }

  public String getMode()
  {
    return mode;
  }

  public String getOutputDir()
  {
    return outputDir;
  }

  public String getStylesheet()
  {
    return stylesheet;
  }

  public String getTestsuite()
  {
    return testSuite;
  }

  public void setTestsuite(String testsuite)
  {
    testSuite = testsuite;
  }

  public void setInputDir(String inputDir)
  {
    this.inputDir = inputDir;
  }

  public void setMode(String mode)
  {
    this.mode = mode;
  }

  public void setOutputDir(String outputDir)
  {
    this.outputDir = outputDir;
  }

  public void setStylesheet(String stylesheet)
  {
    this.stylesheet = stylesheet;
  }

}
