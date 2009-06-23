package fit;

import java.util.HashMap;
import java.util.Stack;

public class ContextFixture extends Fixture
{

  private static HashMap<String, Fixture> contexts = new HashMap<String, Fixture>();

  private static Stack<String> activeContextStack = new Stack<String>();

  @Override
  public void doCells(Parse cells)
  {   
    if (cells.text().equals("begin context"))
    {
      beginContext(cells);
    }
    else if (cells.text().equals("complete context"))
    {
      completeContext(cells);
    }
    else if (cells.text().equals("select context"))
    {
      selectContext(cells);
    }
    else
    {
      Fixture fixture = getActiveFixture();
      fixture.doCells(cells);
    }
  }

  public void beginContext(Parse cells)
  {
    String context = cells.more.text();
    String fixtureName = cells.more.more.text();
    try
    {
      Fixture fixture = (Fixture) getClass().forName(fixtureName).newInstance();
      fixture.counts = this.counts;

      activeContextStack.push(context);
      contexts.put(context, fixture);
      right(cells);
      right(cells.more);
      right(cells.more.more);
    }
    catch (Exception e)
    {
      exception(cells, e);
    }
  }

  public void selectContext(Parse cells)
  {
    String context = cells.more.text();
    activeContextStack.remove(context);
    activeContextStack.push(context);
    right(cells);
    right(cells.more);
  }

  public void completeContext(Parse cells)
  {
    String context = cells.more.text();
    if (activeContextStack.lastElement().equals(context))
    {
      String active = activeContextStack.pop();
      Fixture fixture = contexts.remove(active);
      right(cells);
      right(cells.more);
      counts.right += fixture.counts.right;
      counts.wrong += fixture.counts.wrong;
      counts.ignores += fixture.counts.ignores;
      counts.exceptions += fixture.counts.ignores;
    }
    else
    {
      wrong(cells, "Wrong context, the context is not active!");
    }
  }

  protected Fixture getActiveFixture()
  {
    String activeContext = activeContextStack.lastElement();
    Fixture fixture = contexts.get(activeContext);
    return fixture;
  }

}
