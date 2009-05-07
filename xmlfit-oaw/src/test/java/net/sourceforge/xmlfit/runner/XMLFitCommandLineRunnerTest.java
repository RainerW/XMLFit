package net.sourceforge.xmlfit.runner;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;


public class XMLFitCommandLineRunnerTest {
	
	private static final String OUTPUT_DIR = "target/xmlfit/";
	private static final String GENERATED_TESTFILE = 
		OUTPUT_DIR + "001-LoginTest.html";

	@Before
	public void setUp()
	{
		File testFile = new File(OUTPUT_DIR);
		if(testFile.exists())
		{
			testFile.delete();
		}
	}
	
	@Test
	public void testMain_Test() throws Exception {
		String[] args = {
				"-outputDir", "target/xmlfit/",
				"-inputDir" ,"src/test/resources/",
				"-test" ,"example-test.xml"};
		XMLFitCommandLineRunner.main(args);
		File testFile = new File(GENERATED_TESTFILE);
		assertTrue(testFile.exists());
	}
	
	@Test
	public void testMain_Suite() throws Exception {
		String[] args = {
				"-outputDir", "target/xmlfit/",
				"-inputDir" ,"src/test/resources/",
				"-testSuite" ,"example-suite.xml"};
		XMLFitCommandLineRunner.main(args);
		File testFile = new File(GENERATED_TESTFILE);
		assertTrue(testFile.exists());
		testFile = new File(OUTPUT_DIR + "002-LoginTest.html");
		assertTrue(testFile.exists());
	}

}
