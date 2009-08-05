package fit;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import net.sourceforge.xmlfit.fit.IReplace;
import net.sourceforge.xmlfit.fit.ReplaceParser;

import com.seitenbau.testing.aliasconfigurationfixture.Alias;
import com.seitenbau.testing.aliasconfigurationfixture.AliasConfiguration;
import com.seitenbau.testing.aliasconfigurationfixture.IndexAlias;

/**
 * Special Fixture defining a mapping for &lt;target/&gt; Values.
 */
public class AliasFixture extends Fixture
{
  private static final String CMD_LOAD_MAPPING = "#loadMapping";

  private static final String CMD_START = "start";

  private static final String SPECIAL_NULL = "#{NULL}";

  private static final String SPECIAL_EMPTY = "#{EMPTY}";

  private HashMap<String, String> fAliasMap = null;
  
  private static HashMap<String, String> fStaticAliasMap = null;

  private Fixture fDelegateFixture;

  public void setDelegateFixture(Fixture delegateFixture)
  {
    fDelegateFixture = delegateFixture;
  }

  public AliasFixture()
  {
  }

  @Override
  public void doTables(Parse tables)
  {
    super.doTables(tables);
  }

  @Override
  public void doCells(Parse cells)
  {
    if (cells.text().equals(CMD_LOAD_MAPPING))
    {
      loadMapping(cells);
    }
    else if (cells.text().equals(CMD_START) && fDelegateFixture == null)
    {
      start(cells);
    }
    else
    {
      // create wrapped Parse Object
      Parse mappedCells = ReplaceParser.wrap(cells, new IReplace()
      {
        public String replaceAll(String source, int index)
        {
          return replace(source, index);
        }
      });
      // DELEGATE CALLS
      fDelegateFixture.doCells(mappedCells);
    }
  }

  private void start(Parse cells)
  {
    String fixtureName = cells.more.text();
    try
    {
      Fixture fixture = (Fixture) Class.forName(fixtureName).newInstance();
      fDelegateFixture = fixture;

      String filename = cells.more.more.text();
      if (filename != null && filename.length() > 0)
      {
        loadMapping(filename);
      }
      fixture.counts = counts;
    }
    catch (Exception e)
    {
      exception(cells, e);
    }

  }

  /**
   * Callback when text() is called, and could be replaced
   * 
   * @param value
   * @param index
   * @return
   */
  public String replace(String value, int index)
  {
    if (index == 1) // Nur selekor ersetzen
    {
      String newValue = getMapping().get(value);
      if (newValue != null)
      {
        return newValue;
      }
    }
    if (value.equals(SPECIAL_EMPTY))
    {
      return "";
    }
    if (value.equals(SPECIAL_NULL))
    {
      return null;
    }
    return value;
  }
  
  public void loadMapping(Parse cells)
  {
    String filename = cells.more.text();
    loadMapping(filename);
  }

  public HashMap<String, String> getMapping()
  {
    if (fAliasMap == null)
    {
      return new HashMap<String, String>();
    }
    return fAliasMap;
  }

  @SuppressWarnings("unchecked")
  public void loadMapping(String filename)
  {
    fAliasMap = new HashMap<String, String>();
    fStaticAliasMap= new HashMap<String, String>();
    try
    {
      JAXBContext ctx = JAXBContext.newInstance(AliasConfiguration.class
          .getPackage().getName());
      Unmarshaller um = ctx.createUnmarshaller();
      InputStream stream = AliasConfiguration.class
          .getResourceAsStream(filename);
      JAXBElement<AliasConfiguration> obj = (JAXBElement<AliasConfiguration>) um
          .unmarshal(stream);
      AliasConfiguration cfg = obj.getValue();
      for (Alias alias : cfg.getAliasAndIndexAlias())
      {
        if (alias instanceof IndexAlias)
        {
        }
        else
        {
          String name = alias.getName();
          String value = alias.getValue();
          fAliasMap.put(name, value);
          fStaticAliasMap.put(name,value);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

}
