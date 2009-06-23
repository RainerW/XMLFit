package fit;

import java.lang.reflect.Field;

/**
 * Simplified Fixture for setting fields directly without setter
 * methods.
 */
public class FieldActionFixture extends ActionFixture
{

  @Override
  public void doCells(Parse arg0)
  {
     super.doCells(arg0);
  }
  
  @Override
  public void enter() throws Exception
  {
    String fieldName = cells.more.text();
    String value = cells.more.more.text();

    Field field = actor.getClass().getDeclaredField(fieldName);
    if (field != null)
    {
      field.setAccessible(true);
      field.set(actor, value);
    }
    else
    {
      super.enter();
    }
  }
}
