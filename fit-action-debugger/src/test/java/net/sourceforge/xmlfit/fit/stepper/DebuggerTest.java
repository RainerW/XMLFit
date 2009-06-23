package net.sourceforge.xmlfit.fit.stepper;

import org.junit.Test;

import fit.FileRunner;


public class DebuggerTest
{
  
  @Test
  public void testDebugger() throws Exception
  {
    String args[] = new String[]{"src/test/resources/MusicExample.html","target/MusicExample.html"};
    FileRunner.main(args);
  }

}
