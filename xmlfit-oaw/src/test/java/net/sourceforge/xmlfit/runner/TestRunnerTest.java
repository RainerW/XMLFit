package net.sourceforge.xmlfit.runner;

import static org.junit.Assert.*;

import java.io.File;

import net.sourceforge.xmlfit.runner.TestRunner;

import org.junit.Before;
import org.junit.Test;

public class TestRunnerTest {

	
	private static final String OUTPUT_DIR = "target/xmlfit/";
	private static final String GENERATED_TESTFILE = 
		OUTPUT_DIR + "001-LoginTest.html";

	@Before
	public void setUp()
	{
		File testFile = new File(GENERATED_TESTFILE);
		if(testFile.exists())
		{
			testFile.delete();
		}
	}
	
	@Test
	public void testRun() {
		TestRunner.INSTANCE.run("example-test.xml", OUTPUT_DIR, "src/test/resources/");
		File testFile = new File(GENERATED_TESTFILE);
		assertTrue(testFile.exists());
	}

}
