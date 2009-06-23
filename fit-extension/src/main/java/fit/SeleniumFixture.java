package fit;


public class SeleniumFixture extends Fixture
{

  private final static ContextSeleniumFixture seleniumFixture = new ContextSeleniumFixture();

  public SeleniumFixture()
  {
  }

  @Override
  public void doTables(Parse tables)
  {
    super.doTables(tables);
  }

  @Override
  public void doRow(Parse row)
  {
    super.doRow(row);
  }

  @Override
  public void doCells(Parse cells)
  {
    seleniumFixture.counts = this.counts;
    seleniumFixture.doCells(cells);
  }
}
