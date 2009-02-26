package net.sourceforge.xmlfit.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.Logger;


/**Utility class for XMLFit 
 *@author faigle
 */
public class Util
{

private static final int BUFFER = 4096;  
private static Logger logger = Logger.getLogger(Util.class);

  
 public void copyOutOfJar(String input, String output)
  {
    String method = "copyOutOfJar() : ";
    String error = "Error occured in "+ method +" . See xmlfit.log for details";
    logger.debug(method + "Start");
    logger.debug("Copy " + input + " to " + output);
    InputStream in = getClass().getResourceAsStream(input);
    BufferedInputStream bufIn = new BufferedInputStream(in);

    BufferedOutputStream bufOut = null;

  try 
  {
    bufOut = new BufferedOutputStream(new FileOutputStream(output));
  } 
  catch (FileNotFoundException e)
  {
    logger.info("File not found. See xmlfit.log for details");
    logger.error(e.getMessage());
  }

  byte[] inByte = new byte[BUFFER];
  int count = -1;
  try 
  {
    while ((count = bufIn.read(inByte))!=-1) 
    {
      bufOut.write(inByte, 0, count);
    }
  } 
  catch (IOException e) 
  {
    logger.info(error);
    logger.error(e.getMessage());
  }

  try 
  {
    bufOut.close();
  }
   catch (IOException e) 
  {
     logger.info(error);
     logger.error(e.getMessage());
  }
  try 
  {
    bufIn.close();
  } 
    catch (IOException e) 
  {
      logger.info(error);
      logger.error(e.getMessage());
  }
   
  logger.debug(method + "End");
  
}
 public void copy(File sourceLocation , File targetLocation)
 {
    String method = "copy() : ";
    logger.debug(method + "Start");
    String error = "Error occured in "+ method +" . See xmlfit.log for details";
     
     if (sourceLocation.isDirectory())
     {
         if (!targetLocation.exists()) 
         {
             logger.debug("Creating " +targetLocation);
             targetLocation.mkdir();
         }
         
         String[] children = sourceLocation.list();
         for (int i=0; i<children.length; i++) 
         {
             logger.debug("Copy " + children[i] + "to " + targetLocation);
             copy(new File(sourceLocation, children[i]),
                     new File(targetLocation, children[i]));
         }
     } 
     else 
     {
         try
         {
           InputStream in = new FileInputStream(sourceLocation);
           OutputStream out = new FileOutputStream(targetLocation);
       
           byte[] buf = new byte[BUFFER];
           int len;
           while ((len = in.read(buf)) > 0) 
           {
             out.write(buf, 0, len);
           }
           in.close();
           out.close();
         }
         catch(IOException e)
         {
           logger.info(error);
           logger.error(e.getMessage());
         }
         logger.debug(method + "End");
     }
   }
 

   

}
