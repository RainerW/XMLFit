package net.sourceforge.xmlfit.fit;

import java.io.PrintWriter;
import java.text.ParseException;

import fit.Parse;

/**
 * Hack um Parse Klasse um text() Methode umzuschreiben. 
 */
public class ReplaceParser extends Parse implements IParseObjectDecorated
{
  private int fIndex=-1;

  public ReplaceParser(Parse delegateTo, IReplace  replacer,
      int index) throws ParseException
  { 
    super("<table><tr><td>" + "</td></tr></table>");
    fDelegate = delegateTo;
    fReplacer = replacer;
    fIndex = index;
    if (delegateTo.more == null)
    {
      more = null;
    }
    else
    {
      more = new ReplaceParser(delegateTo.more, replacer, index + 1);
    }
  }

  public static Parse wrap(Parse cells, IReplace replacer)
  {
    try
    {
      return new ReplaceParser(cells, replacer, 0);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  private Parse fDelegate = null;

  private IReplace fReplacer;

  public String text()
  {
    if (fReplacer != null)
    {
      String value = fDelegate.text();
      return fReplacer.replaceAll(value,fIndex);
    }
    else
    {
      return fDelegate.text();
    }
  }

  // / Delegates :

  public void addToBody(String arg0)
  {
    fDelegate.addToBody(arg0);
  }

  public void addToTag(String arg0)
  {
    fDelegate.addToTag(arg0);
  }

  public Parse at(int arg0, int arg1, int arg2)
  {
    return fDelegate.at(arg0, arg1, arg2);
  }

  public Parse at(int arg0, int arg1)
  {
    return fDelegate.at(arg0, arg1);
  }

  public Parse at(int arg0)
  {
    return fDelegate.at(arg0);
  }

  public boolean equals(Object obj)
  {
    return fDelegate.equals(obj);
  }

  public String footnote()
  {
    return fDelegate.footnote();
  }

  public int hashCode()
  {
    return fDelegate.hashCode();
  }

  public Parse last()
  {
    return fDelegate.last();
  }

  public Parse leaf()
  {
    return fDelegate.leaf();
  }

  public void print(PrintWriter arg0)
  {
    fDelegate.print(arg0);
  }

  public int size()
  {
    return fDelegate.size();
  }

  public String toString()
  {
    return fDelegate.toString();
  }

  public Parse getUndecoratedParseObject()
  {
    return fDelegate;
  }

}
