package net.sourceforge.xmlfit.runner;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;



/**XMLFit Runner
 *@author faigle
 */
public final class XMLFitCommandLineRunner implements XMLFitRunner
{
  
  private static Logger logger = Logger.getLogger(XMLFitCommandLineRunner.class);
  
  @Option(
      name="-outputDir", 
      usage="The output dir for the FIT tests. (required)",
      required=true)
  private String outputDir;

  @Option(
      name="-inputDir", 
      usage="The input folder of the XML tests. (required)",
      required=true)
  private String inputDir;

  @Option(
      name="-testsuite", 
      usage="The XML test suite. (required)",
      required=true)
  private String testsuite;

  @Option(
      name="-style",
      required=false,
      usage="The CSS stylesheet for the HTML FIT tests, for own skin in the generated FIT tests.")
  private String stylesheet;

  private XMLFitCommandLineRunner() 
  {
  
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
  
  public String getOutputDir()
  {
    return outputDir;
  }


  public void setOutputDir(String outputDir)
  {
    this.outputDir = outputDir;
  }


  public String getInputDir()
  {
    return inputDir;
  }


  public void setInputDir(String inputDir)
  {
    this.inputDir = inputDir;
  }


  public String getTestsuite()
  {
    return testsuite;
  }


  public void setTestsuite(String testsuite)
  {
    this.testsuite = testsuite;
  }

 
  public static void main(String[] args) throws Exception
  {  
    Logger logger = Logger.getLogger("log");
    XMLFitCommandLineRunner runner = new XMLFitCommandLineRunner();
    CmdLineParser parser = new CmdLineParser(runner);
    try 
    {
      parser.parseArgument(args);
      logger.info("Starting XMLFit");
      XMLFit.run(runner);
    } 
    catch (CmdLineException e) 
    {
          logger.error(e.getMessage());
          // handling of wrong arguments
          System.err.println(e.getMessage());
          parser.printUsage(System.err);
    }
   }
 }

