package org.codehaus.mojo.fit;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Extends URLClassLoader to instantiate Fixture classes.
 * 
 * @author Mauro Talevi
 */
public class FixtureClassLoader extends URLClassLoader
{

  public FixtureClassLoader()
  {
    this(new URL[] {});
  }

  public FixtureClassLoader(List classpathElements)
      throws MalformedURLException
  {
    this(toClasspathURLs(classpathElements));
  }

  public FixtureClassLoader(URL[] urls)
  {
    this(urls, Thread.currentThread().getContextClassLoader());
  }

  public FixtureClassLoader(URL[] urls, ClassLoader parent)
  {
    super(urls, parent);
  }

  protected static URL[] toClasspathURLs(List classpathElements)
      throws MalformedURLException
  {
    List urls = new ArrayList();
    if (classpathElements != null)
    {
      for (Iterator i = classpathElements.iterator(); i.hasNext();)
      {
        String classpathElement = (String) i.next();
        urls.add(new File(classpathElement).toURL());
      }
    }
    return (URL[]) urls.toArray(new URL[urls.size()]);
  }
  
  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException
  {
    return super.loadClass(name);
  }

}
