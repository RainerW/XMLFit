
package runner;


import java.io.IOException;
import java.text.ParseException;
import fitlibrary.runner.FolderRunner;


/** FitRunner
 * 
 * @author faigle
 *
 */
public class Runner
{

  public Runner(String testDir, String reportDir) throws IOException,
      ParseException
  {
    String[] args = new String[] {testDir, reportDir};
    FolderRunner fit = new FolderRunner(args);
    fit.run();
  }

}
