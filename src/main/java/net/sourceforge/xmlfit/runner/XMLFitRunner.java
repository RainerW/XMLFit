package net.sourceforge.xmlfit.runner;

import java.io.File;



/**XMLFit Runner
 *@author faigle
 */
public final class XMLFitRunner
{
  private static final int MAX_ARGS = 4;
  private static final int ARGS = 3;
  private XMLFitRunner() 
  {
  
  }
  
 
  public static void main(String[] args) throws Exception
  {  
   
    
      if(args.length >= ARGS && new File(args[0]).isFile() && new File(args[1]).isDirectory())
        {
        XMLFit xmlfit = new XMLFit();
        xmlfit.setOutputDir(args[2]);
        xmlfit.setInputDir(args[1]);
        xmlfit.setTestsuite(args[0]);
        
          if(args.length >= MAX_ARGS)
          {
            xmlfit.setCssfile(args[ARGS]);
          }
      
        System.out.println("START Transformation");
        System.out.println("to directory: " + args[2]);
        System.out.println("from directory: " + args[1]);
        System.out.println("with testsuite file: " + args[0]);
        xmlfit.generate();
        System.out.println("END Transformation");
        }
      else
      {
      System.err.println("Usage: java -jar XMLFit.jar [xmlSuite] [inputSrc] [outputsrc] [cssfile (optional)]");
      System.exit(1);
      }
    }
  }

