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
    logger.debug(method + "Start");
    logger.debug("Copy " + input + " to " + output);
    InputStream in = getClass().getResourceAsStream(input);
    BufferedInputStream bufIn = new BufferedInputStream(in);

    BufferedOutputStream bufOut = null;

  try 
  {
    bufOut = new BufferedOutputStream(new FileOutputStream(output));
  } 
  catch (FileNotFoundException e1)
  {
    e1.printStackTrace();
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
    e.printStackTrace();
  }

  try 
  {
    bufOut.close();
  }
   catch (IOException e) 
  {
     e.printStackTrace();
 
  }
  try 
  {
    bufIn.close();
  } 
    catch (IOException e) 
  {
      e.printStackTrace();
  }
   
  logger.debug(method + "End");
  
}
 public void copy(File sourceLocation , File targetLocation)
 throws IOException 
 {
    String method = "copy() : ";
    logger.debug(method + "Start");
 
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
     logger.debug(method + "End");
 }

}
