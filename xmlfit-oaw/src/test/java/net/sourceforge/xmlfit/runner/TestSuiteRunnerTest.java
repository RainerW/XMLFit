package net.sourceforge.xmlfit.runner;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSuiteRunnerTest {
	
	
	
	@Test
	public void testRun() {
		TestSuiteRunner.INSTANCE.run("example-suite.xml", "target/xmlfit", "src/test/resources/");
		assertEquals(2, TestSuiteRunner.INSTANCE.counter.success);
		assertEquals(0, TestSuiteRunner.INSTANCE.counter.failed);
	}

}
