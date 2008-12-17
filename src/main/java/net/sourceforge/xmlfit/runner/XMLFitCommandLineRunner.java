package net.sourceforge.xmlfit.runner;

import java.io.File;
import java.net.URL;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;



/**XMLFit Runner
 *@author faigle
 */
public final class XMLFitCommandLineRunner implements XMLFitRunner
{
  
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
    XMLFitCommandLineRunner runner = new XMLFitCommandLineRunner();
    CmdLineParser parser = new CmdLineParser(runner);
    try {
      parser.parseArgument(args);
      XMLFit.run(runner);
      } catch (CmdLineException e) {
          // handling of wrong arguments
          System.err.println(e.getMessage());
          parser.printUsage(System.err);
      }
    }
  }

