package fit;

import java.io.File;

public class SeleniumAliasFixture extends Fixture
{
  private static String defaultMappingFile = "/element-alias-mapping.xml";

  private AliasFixture delegateToAliaseFixture;

  private SeleniumFixture decoratedSeleniumFixture;

  public SeleniumAliasFixture()
  {
    decoratedSeleniumFixture = new SeleniumFixture();

    delegateToAliaseFixture = new AliasFixture();
    delegateToAliaseFixture.loadMapping(defaultMappingFile);

    delegateToAliaseFixture.setDelegateFixture(decoratedSeleniumFixture);
  }

  @Override
  public void doCells(Parse cells)
  {
    if (cells.text().equals("#setDefaultMappingFile"))
    {
      String filename = cells.more.text();
        defaultMappingFile = filename;
        delegateToAliaseFixture.loadMapping(defaultMappingFile);
    }
    else
    {
      delegateToAliaseFixture.counts = this.counts;
      decoratedSeleniumFixture.counts = this.counts;
      delegateToAliaseFixture.doCells(cells);
    }
  }
}
