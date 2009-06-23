package fit;

import java.lang.reflect.Method;

public abstract class ArgumentFixture extends Fixture
{

  @SuppressWarnings("unchecked")
  protected static Class args[] = {Argument.class, Argument.class};

  private boolean stop = false;

  public boolean isStop()
  {
    return stop;
  }

  public void setStop(boolean stop)
  {
    // this.stop = stop;
  }

  public void doCells(Parse cells)
  {
    if (isStop())
    {
      ignore(cells);
    }
    else
    {
      doTest(cells);
    }
  }

  private void doTest(Parse cells)
  {
    try
    {
      Method action = getClass().getMethod(cells.text(), args);
      Argument argument;
      Argument selektor = new Argument(cells.more);
      if (cells.more != null)
      {
        argument = new Argument(cells.more.more);
      }
      else
      {
        argument = null;
      }
      action.invoke(this, new Object[] {selektor, argument});
    }
    catch (Exception e)
    {
      exception(cells, e);
    }
  }

  protected void exception(Argument arg, Exception e)
  {
    arg.setException(true);
    exception(arg.getCell(), e);
  }

  protected void wrong(Argument arg)
  {
    arg.setWrong(true);
    wrong(arg.getCell());
  }

  protected void wrong(Argument arg, String actual)
  {
    arg.setWrong(true);
    if (actual == null)
    {
      wrong(arg.getCell(), "null");
    }
    else
    {
      wrong(arg.getCell(), actual);
    }
  }

  /**
   * Markiert die gegebene Selenim Zelle (arg) als Falsch.
   * Ist actual {@code null} wird "null" weggeschrieben. In allen anderen 
   * fällen wird toString auf actual gerufen.   
   */
  protected void wrong(Argument arg, Object actual)
  {
    arg.setWrong(true);
    if (actual == null)
    {
      wrong(arg.getCell(), "null");
    }
    else
    {
      wrong(arg.getCell(), actual.toString());
    }

  }

  protected void right(Argument arg)
  {
    arg.setRight(true);
    right(arg.getCell());
  }

}
