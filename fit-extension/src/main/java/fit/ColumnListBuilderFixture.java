package fit;

abstract public class ColumnListBuilderFixture extends
    ColumnSpecialValuesFixture
{

  @Override
  protected void beforeTable(Parse parse)
  {
    resetList();
  }

  @Override
  protected void afterRow(Parse parse)
  {
    addRowToList();
  }

  abstract protected void addRowToList();

  abstract protected void resetList();

  @Override
  public void doCells(Parse cells)
  {
    super.doCells(cells);
  }

  protected String processValue(String orgValue)
  {
    if (orgValue == null)
    {
      return null;
    }
    if (orgValue.equals(SPECIAL_EMPTY))
    {
      return "";
    }
    if (orgValue.equals(SPECIAL_NULL))
    {
      return null;
    }
    return orgValue;
  }
}
