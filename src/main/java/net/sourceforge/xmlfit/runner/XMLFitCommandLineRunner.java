package net.sourceforge.xmlfit.runner;

import java.io.File;

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
  private File outputDir;

  @Option(
      name="-inputDir", 
      usage="The input folder of the XML tests. (required)",
      required=true)
  private File inputDir;

  @Option(
      name="-testsuite", 
      usage="The XML test suite. (required)",
      required=true)
  private File testsuite;

  @Option(
      name="-style",
      required=false,
      usage="The CSS stylesheet for the HTML FIT tests, for own skin in the generated FIT tests.")
  private File stylesheet;

  private XMLFitCommandLineRunner() 
  {
  
  }
  
  public String getStylesheet()
  {
    return stylesheet.getAbsolutePath();
  }

  public void setStylesheet(File stylesheet)
  {
    this.stylesheet = stylesheet;
  }
  
  public String getOutputDir()
  {
    return outputDir.getAbsolutePath();
  }


  public void setOutputDir(File outputDir)
  {
    this.outputDir = outputDir;
  }


  public String getInputDir()
  {
    return inputDir.getAbsolutePath();
  }


  public void setInputDir(File inputDir)
  {
    this.inputDir = inputDir;
  }


  public String getTestsuite()
  {
    return testsuite.getAbsolutePath();
  }


  public void setTestsuite(File testsuite)
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

