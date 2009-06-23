package fit;

import fit.ColumnFixture;
import fit.Parse;
import fit.TypeAdapter;

public class ColumnSpecialValuesFixture extends ColumnFixture
{

  public static final String SPECIAL_NULL = "#{NULL}";
  public static final String SPECIAL_EMPTY = "#{EMPTY}";

  @Override
  public void doCells(Parse cells)
  {
    super.doCells(cells);
  }
  @Override
  public void check(Parse cell, TypeAdapter a)
  {
    String text = cell.text();
    if (text.equals(SPECIAL_EMPTY))
    {
      compareEmpty(cell, a, "");
    }
    else if (text.equals(SPECIAL_NULL))
    {
      compareNull(cell, a);
    }
    else
    {
      super.check(cell, a);
    }
  }

  public void compareNull(Parse cell, TypeAdapter a)
  {
    try
    {
      Object result = a.get();
      if (result == null)
      {
        right(cell);
      }
      else
      {
        wrong(cell, a.toString(result));
      }
    }
    catch (Exception e)
    {
      exception(cell, e);
    }
  }

  public void compareEmpty(Parse cell, TypeAdapter a, String text)
  {
    try
    {
      Object result = a.get();
      if (a.equals(a.parse(text), result))
      {
        right(cell);
      }
      else
      {
        wrong(cell, a.toString(result));
      }
    }
    catch (Exception e)
    {
      exception(cell, e);
    }
  }

}
