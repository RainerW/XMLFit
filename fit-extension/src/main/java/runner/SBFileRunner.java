package runner;

import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class SBFileRunner extends fit.FileRunner
{
  private File input;
  private static Logger logger = Logger.getLogger(SBFileRunner.class);
  private static final int DELAY = 6000;
 
  @Override
  public void exit()
  {
      output.close();  
      logger.info("Right: " + fixture.counts.right);
      
      
      if(fixture.counts.wrong > 0)
      {
        logger.warn("Errors: " + fixture.counts.wrong);
      }
      else
      {
        logger.info("Errors: " + fixture.counts.wrong);
      }
      
      
      if(fixture.counts.exceptions > 0)
      {
        logger.warn("Exceptions: " + fixture.counts.exceptions);
      }
      else
      {
        logger.info("Exceptions: " + fixture.counts.exceptions);
      }
      
  }
  @Override
  public void run (String argv[]) {
    PropertyConfigurator.configureAndWatch("log4j.properties", DELAY);
    input = new File(argv[1]);
    logger.info("=========================================");
    logger.info(input.getName());
    try{
      args(argv);
      process();
      exit();
    }catch(Exception e)
    {
      logger.warn(e.getMessage());
    }
}
  
  
  public int getErrorCount()
  {
    return fixture.counts.wrong;
  }
  
  public int getExceptionCount()
  {
    return fixture.counts.exceptions;
  }
  
  public int getIgnoredCount()
  {
    return fixture.counts.ignores;
  }
  
  public int getRightCount()
  {
    return fixture.counts.right;
  }
}
