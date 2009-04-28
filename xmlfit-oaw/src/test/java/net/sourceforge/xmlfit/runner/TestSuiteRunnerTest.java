package net.sourceforge.xmlfit.runner;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class TestSuiteRunnerTest {
	
	
	@Before
	public void setUp()
	{
		File testFile = new File("target/xmlfit");
		if(testFile.exists())
		{
			testFile.delete();
		}
		TestSuiteRunner.INSTANCE.reset();
	}
	
	@Test
	public void testRun() {
		TestSuiteRunner.INSTANCE.run("example-suite.xml", "target/xmlfit", "src/test/resources/");
		assertEquals(2, TestSuiteRunner.INSTANCE.counter.success);
		assertEquals(0, TestSuiteRunner.INSTANCE.counter.failed);
	}

}
