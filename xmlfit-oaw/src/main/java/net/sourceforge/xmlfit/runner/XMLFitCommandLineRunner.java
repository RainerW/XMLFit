package net.sourceforge.xmlfit.runner;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * XMLFit Runner
 * 
 * @author faigle
 */
public final class XMLFitCommandLineRunner {

	private static Logger logger = Logger
			.getLogger(XMLFitCommandLineRunner.class);

	@Option(name = "-outputDir", usage = "The output dir for the FIT tests. (required)", required = true)
	private String outputDir;

	@Option(name = "-inputDir", usage = "The input folder of the XML tests. (required)", required = true)
	private String inputDir;

	@Option(name = "-testSuite", usage = "The XML test suite.", required = false)
	private String testsuite;

	@Option(name = "-test", usage = "XML test.", required = false)
	private String test;

	@Option(name = "-style", required = false, usage = "The CSS stylesheet for the HTML FIT tests, for own skin in the generated FIT tests.")
	private String stylesheet;

	@Option(name = "-mode", required = false, usage = "Transformation Mode")
	private String mode;

	private XMLFitCommandLineRunner() {

	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getStylesheet() {
		if (!(stylesheet == null)) {
			return stylesheet;
		}
		return null;
	}

	public String getMode() {
		if (!(mode == null)) {
			return mode;
		}
		return null;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public String getInputDir() {
		return inputDir;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public String getTestsuite() {
		return testsuite;
	}

	public void setTestsuite(String testsuite) {
		this.testsuite = testsuite;
	}

	public static void main(String[] args) throws Exception {
		XMLFitCommandLineRunner runner = new XMLFitCommandLineRunner();
		CmdLineParser parser = new CmdLineParser(runner);
		try {
			parser.parseArgument(args);
			logger.info("Start XMLFit");
			if (runner.testsuite != null) {
				TestSuiteRunner.INSTANCE.run(runner.testsuite,
						runner.outputDir, runner.inputDir);
			} else if (runner.test != null) {
				TestRunner.INSTANCE.run(runner.test, runner.outputDir,
						runner.inputDir);
			}
			logger.info("End XMLFit");
		} catch (CmdLineException e) {
			logger.error(e.getMessage());
		}
	}
}
