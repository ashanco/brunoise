package co.ashan.querycli;

import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.Test;
import org.olap4j.CellSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.h2.engine.Constants.UTF8;
import static org.junit.Assert.*;

public class OlapContextTest {

  private OlapContext context = null;

  @Before
  public void setContext() throws Exception {

    ClassLoader loader = getClass().getClassLoader();

    File source = new File(loader.getResource("testsource.properties").getFile());
    DataSourceProperties properties = new DataSourceProperties();

    try (InputStream resource = new FileInputStream(source.getPath())) {
      properties.load(resource);
    }

    File schema = new File(loader.getResource("schema.xml").getFile());
    properties.setProperty("mondrian.schema.path", schema.getAbsolutePath());

    String url = properties.getProperty("database.url");
    String user = properties.getProperty("database.user");
    String password = properties.getProperty("database.password");

    File ddlScript = new File(loader.getResource("db_schema.sql").getFile());

    RunScript.execute(url, user, password, ddlScript.getPath(), UTF8, false);

    context = new OlapContext(properties);

  }

  @Test
  public void testSchemaName() {
    assertEquals("testschema", context.schemaName());
  }

  @Test
  public void testExecute() {
    MdxQuery query = new MdxQuery("SELECT [Measures].[Amount] ON COLUMNS, [Customer].[CustomerNumber].Members ON ROWS FROM [Sales]");
    CellSet cells = context.execute(query);
    assertEquals(3, cells.getAxes().get(1).getPositionCount());
  }
}
