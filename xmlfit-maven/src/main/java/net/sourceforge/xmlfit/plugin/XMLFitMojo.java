package net.sourceforge.xmlfit.plugin;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.xmlfit.runner.TestRunner;
import net.sourceforge.xmlfit.runner.TestSuiteRunner;

import org.apache.log4j.PropertyConfigurator;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.DirectoryScanner;

/**
 * Transform XML testfiles to HTML.
 * @goal transform
 */
public class XMLFitMojo extends AbstractMojo
{

  private static final String[] DEFAULT_INCLUDE_PATTERN = new String[] {
                                          "**/Test*.xml",
                                          "**/*Test.xml",
                                          "**/*TestCase.xml"
                                       };

  /**
   * The output folder.
   * 
   * @parameter expression="${project.build.directory}/fit/"
   */
  private String outputDirectory;

  /**
   * The source directory containing the Fit fixtures
   * 
   * @parameter expression="${basedir}/src/test/xmlfit/"
   * @required
   */
  String inputDirectory;

  /**
   * Flag to indicate that path names are case sensitive
   * @parameter default-value="false"
   */
  boolean caseSensitive;

  /**
   * The filter for source file includes, relative to the source
   * directory, as CSV patterns. defaultValue *Test.xml or Test*.xml
   * @parameter
   */
  List<String> includes;

  /**
   * The filter for source file excludes, relative to the source
   * directory, as CSV patterns.
   * @parameter
   */
  List<String> excludes;

  /**
   * The custom CSS File.
   * 
   * @parameter stylesheet
   */
  private String stylesheet;

  /**
   * The Maven project this plugin runs in.
   * 
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * The xmlfit mode.
   * 
   * @parameter mode
   */
  private String mode;

  private DirectoryScanner scanner = new DirectoryScanner();

  private XMLFitMojoRunner runner = new XMLFitMojoRunner();

  private static final int DELAY = 6000;

  private static final String EXECUTION_PARAMETERS = "sourceDirectory={0}, caseSensitive={1},"
      + " sourceIncludes={2}, sourceExcludes={3}, outputDirectory={4}";

  public void execute()
  {
    PropertyConfigurator.configureAndWatch("log4j.properties", DELAY);
    try
    {
      getLog().info("Start XMLFit Maven Plugin");

      prepareIncludesDefaultValue();

      debugPrintParameters();

      runner.setInputDir("");
      runner.setMode(getMode());
      runner.setOutputDir(getOutputDir());

      addInputDirectoryAsMavenResourceFolder();

      runner.setStylesheet(getStylesheet());

      String[] files = listFiles(inputDirectory, caseSensitive, includes,
          excludes);

      for (int i = 0; i < files.length; i++)
      {
        String inputPath = toPath(inputDirectory, files[i]);
        runXmlFitOnFile(runner, inputPath);
      }
      
      getLog().info("End XMLFit Maven Plugin");
    }
    catch (Exception e)
    {
      getLog().error(e);
    }

  }

  private void debugPrintParameters()
  {
    final String executionParameters = MessageFormat.format(
        EXECUTION_PARAMETERS,
        new Object[] {inputDirectory, Boolean.valueOf(caseSensitive), includes,
            excludes, outputDirectory});
    getLog().debug(
        "Executing XMLFit Mojo with parameters " + executionParameters);
  }

  @SuppressWarnings("unchecked")
  private void prepareIncludesDefaultValue()
  {
    if (includes == null || includes.size() == 0)
    {
      includes = new ArrayList(Arrays.asList(DEFAULT_INCLUDE_PATTERN));
    }
  }

  /**
   * 
   */
  protected void addInputDirectoryAsMavenResourceFolder()
  {
    Resource res = new Resource();
    res.setDirectory(inputDirectory);
    res.addExclude("**/*.java");
    res.addInclude("*.ignoreAll");
    project.addTestResource(res);
  }

  /**
   * Transform given directory + filename to Path
   * @param directory
   * @param name
   * @return
   */
  protected String toPath(String directory, String name)
  {
    return new File(directory, name).getPath();
  }

  /**
   * Produces a List of Files which match the given patterns.
   * 
   * @param sourceDirectory Root folder of all files
   * 
   * @param caseSensitive if filenames should be case sensitive
   * 
   * @param sourceIncludes include filename patterns
   * 
   * @param sourceExcludes exclude filename patterns
   * 
   * @return List of files matching include and exclude pattern.
   */
  protected String[] listFiles(String sourceDirectory, boolean caseSensitive,
      List<String> sourceIncludes, List<String> sourceExcludes)
  {
    scanner.setBasedir(new File(sourceDirectory));
    getLog().debug("Listing files from directory " + sourceDirectory);
    getLog().debug("Setting case sensitive " + caseSensitive);
    scanner.setCaseSensitive(caseSensitive);
    if (sourceIncludes != null)
    {
      getLog().debug("Setting includes " + sourceIncludes);
      scanner.setIncludes(sourceIncludes.toArray(new String[] {}));
    }
    if (sourceExcludes != null)
    {
      getLog().debug("Setting excludes " + sourceExcludes);
      scanner.setExcludes(sourceExcludes.toArray(new String[] {}));
    }
    scanner.scan();
    String[] files = scanner.getIncludedFiles();
    getLog().debug("Files listed " + Arrays.asList(files));
    return files;
  }

  private void runXmlFitOnFile(XMLFitMojoRunner runner, String testFile)
  {
    getLog().info("Running XMLFit on file : " + testFile);
    TestRunner.INSTANCE.run(testFile, outputDirectory, inputDirectory);
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

  // public String[] getTestsuite()
  // {
  // return this.testsuites;
  // }
  //
  // public void setTestsuites(String[] testsuite)
  // {
  // this.testsuites = testsuite;
  // }
  //
  // public String getInputDir()
  // {
  // return inputDir;
  // }
  //
  // public void setInputDir(String inputDir)
  // {
  // this.inputDir = inputDir;
  // }

  public String getOutputDir()
  {
    return outputDirectory;
  }

  public void setOutputDir(String outputDir)
  {
    this.outputDirectory = outputDir;
  }

}
