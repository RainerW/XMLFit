package fit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Adds additional target "state"
 *
 * Print's the State of a Method or Field into the
 * Testresult. Useful for showing a finally build URL 
 * 
 * 
 * If target contains "()" chars, a "String youMethodName()/1" 
 * will be used. Otherwise the code tries to find the Field 
 * "String youFieldName".   
 */
public class ActionStateFixture extends ActionFixture
{
  public void state() throws Exception
  {
    Parse cell = cells.more;
    String target = cell.text();
    String state;
    if (target.contains("()"))
    {
      state = getStateFromMethod(target.replace("()",""));
    }
    else
    {
      state = getStateFromField(target);
    }
    cell.body = cell.body.replace(target, target + " = " + state);
  }

  protected String getStateFromMethod(String target) throws Exception
  {
    Method method = getMethodFromDelegate(target);
    return (String) method.invoke(actor, empty);
  }

  protected Field getFieldFromDelegate(String target) throws Exception
  {
    return actor.getClass().getDeclaredField(target);
  }

  protected String getStateFromField(String target) throws Exception
  {
    Field field = getFieldFromDelegate(target);
    return (String) field.get(actor);
    
  }

  protected Method getMethodFromDelegate(String target) throws Exception
  {
    return actor.getClass().getMethod(target, empty);
  }

}
