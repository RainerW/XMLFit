package org.codehaus.mojo.fit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.DirectoryScanner;

import fit.Counts;
import fit.FileRunner;

/**
 * Maven Fit Runner.
 * 
 * @author Mauro Talevi
 * @goal run
 * @phase integration-test
 * @requiresDependencyResolution test
 */
public class FitRunnerMojo extends AbstractMojo
{

  private static final String ENV_RUN_TEST_WITH_ID = "fitTest";

  private static final String COMMA = ",";

  private static final String EXECUTION_PARAMETERS = "sourceDirectory={0}, caseSensitive={1},"
      + " sourceIncludes={2}, sourceExcludes={3}, parseTags={4}, outputDirectory={5}, ignoreFailures={6}";

  /**
   * Classpath.
   * 
   * @parameter expression="${project.testClasspathElements}"
   * @required
   */
  List classpathElements;

  /**
   * The source directory containing the Fit fixtures
   * @parameter
   * @required
   */
  String sourceDirectory;

  /**
   * Flag to indicate that path names are case sensitive
   * @parameter default-value="false"
   */
  boolean caseSensitive;

  /**
   * The filter for source file includes, relative to the source
   * directory, as CSV patterns.
   * @parameter
   */
  String sourceIncludes;

  /**
   * The filter for source file excludes, relative to the source
   * directory, as CSV patterns.
   * @parameter
   */
  String sourceExcludes;

  /**
   * The parsee tags used to identify the Fit tables.
   * @parameter
   */
  String[] parseTags = new String[] {"table", "tr", "td"};

  /**
   * The output directory where the results of Fit processing is
   * written to
   * @parameter
   * @required
   */
  String outputDirectory;

  /**
   * The option to ignore fixture failures
   * 
   * @parameter default-value="false"
   */
  boolean ignoreFailures;

  /**
   * The scanner to list files
   */
  private DirectoryScanner scanner = new DirectoryScanner();

  public Counts counts = new Counts();

  public void execute() throws MojoExecutionException, MojoFailureException
  {
    final String executionParameters = MessageFormat.format(
        EXECUTION_PARAMETERS, new Object[] {sourceDirectory,
            Boolean.valueOf(caseSensitive), sourceIncludes, sourceExcludes,
            Arrays.asList(parseTags), outputDirectory,
            Boolean.valueOf(ignoreFailures)});
    getLog()
        .debug("Executing FitRunner with parameters " + executionParameters);

    String runOnlyID = System.getProperty(ENV_RUN_TEST_WITH_ID);
    if (runOnlyID != null)
    {
      getLog().info(
          "Detected environment Variable " + ENV_RUN_TEST_WITH_ID + " is "
              + runOnlyID);
      sourceIncludes = "**/*" + runOnlyID + "*";
      getLog().info("Therefore sourceIncludes are now = " + sourceIncludes);
    }
    try
    {
      getLog().debug("sourceDirectory : " + sourceDirectory);
      getLog().debug("sourceIncludes : " + sourceIncludes);
      getLog().debug("sourceExcludes : " + sourceExcludes);
      getLog().debug("outputDirectory : " + outputDirectory);
      // run( sourceDirectory, caseSensitive, sourceIncludes,
      // sourceExcludes, outputDirectory );
      String[] listFiles = listFiles(sourceDirectory, caseSensitive,
          sourceIncludes, sourceExcludes);

      createOutputDirectory();

      FixtureClassLoader classLoader = createClassLoader();
      Thread.currentThread().setContextClassLoader(classLoader);

      StringBuffer resultHtml = new StringBuffer();

      for (int i = 0; i < listFiles.length; i++)
      {
        String fileName = listFiles[i];
        FileRunner fileRunner = new FileRunner();

        getLog().info("Run FIT : ");
        getLog().info(fileName);
        String[] argv = new String[] {sourceDirectory + "/" + fileName,
            outputDirectory + "/" + fileName};
        fileRunner.args(argv);
        fileRunner.process();
        fileRunner.output.close();

        counts.tally(fileRunner.fixture.counts);

        resultHtml.append("<tr>");
        resultHtml.append("<td>");
        resultHtml.append("<a href=\"" + fileName + "\">" + outputDirectory
            + "/" + fileName + "</a>");
        resultHtml.append("</td>");

        if (fileRunner.fixture.counts.exceptions > 0)
        {
          // gelb
          resultHtml.append("<td bgcolor=\"#ffffcf\">");
        }
        else if (fileRunner.fixture.counts.wrong > 0)
        {
          // rot
          resultHtml.append("<td bgcolor=\"#ffcfcf\">");
        }
        else
        {
          // grün
          resultHtml.append("<td bgcolor=\"#cfffcf\" >");
        }

        resultHtml.append(fileRunner.fixture.counts());
        resultHtml.append("</td>");
        resultHtml.append("</tr>");

        getLog().info(fileRunner.fixture.counts());
      }
      FileWriter fileWriter = new FileWriter(outputDirectory + "/result.html",
          false);
      fileWriter.append("<html>");
      fileWriter.append("<body>");
      fileWriter.append("<h1> Results </h1>");
      fileWriter.append("<table border=\"1\">");
      fileWriter.append("<tr>");
      if (counts.exceptions > 0)
      {
        // gelb
        fileWriter.append("<td bgcolor=\"#ffffcf\">");
      }
      else if (counts.wrong > 0)
      {
        // rot
        fileWriter.append("<td bgcolor=\"#ffcfcf\">");
      }
      else
      {
        // grün
        fileWriter.append("<td bgcolor=\"#cfffcf\" >");
      }

      fileWriter.append(counts.toString());
      fileWriter.append("</td>");
      fileWriter.append("</tr>");
      fileWriter.append("</table>");

      fileWriter.append("<h1> Details </h1>");
      fileWriter.append("<table border=\"1\">");
      fileWriter.append(resultHtml.toString());
      fileWriter.append("</table>");

      fileWriter.append("</body>");
      fileWriter.close();
    }
    catch (Exception e)
    {
      throw new MojoExecutionException(
          "Failed to execute FitRunner with parameters " + executionParameters,
          e);
    }

    getLog().info("");
    getLog().info("----------------------------------------");
    getLog().info("");
    getLog().info("Result - FIT TESTS : ");
    getLog().info("");
    getLog().info(counts.toString());
    getLog().info("");
    getLog().info("----------------------------------------");
    getLog().info("");

    if (counts.exceptions > 0 || counts.wrong > 0)
    {
      throw new MojoFailureException("Errors in FIT Tests");
    }

  }

  private void createOutputDirectory()
  {
    File file = new File(outputDirectory);
    file.mkdirs();
  }

  protected FixtureClassLoader createClassLoader() throws MalformedURLException
  {
    FixtureClassLoader fixtureClassLoader = new FixtureClassLoader(
        classpathElements);
    return fixtureClassLoader;
  }

  protected String toPath(String directory, String name)
  {
    return new File(directory, name).getPath();
  }

  protected String[] listFiles(String sourceDirectory, boolean caseSensitive,
      String sourceIncludes, String sourceExcludes)
  {
    scanner.setBasedir(new File(sourceDirectory));
    getLog().debug("Listing files from directory " + sourceDirectory);
    getLog().debug("Setting case sensitive " + caseSensitive);
    scanner.setCaseSensitive(caseSensitive);
    if (sourceIncludes != null)
    {
      getLog().debug("Setting includes " + sourceIncludes);
      scanner.setIncludes(sourceIncludes.split(COMMA));
    }
    if (sourceExcludes != null)
    {
      getLog().debug("Setting excludes " + sourceExcludes);
      scanner.setExcludes(sourceExcludes.split(COMMA));
    }
    scanner.scan();
    String[] files = scanner.getIncludedFiles();
    getLog().debug("Files listed " + Arrays.asList(files));
    return files;
  }

  protected void ensureDirectoryExists(String path) throws IOException
  {
    File file = new File(path);
    if (!file.exists())
    {
      getLog().debug("Creating directory " + file);
      file.mkdirs();
    }
  }

}
