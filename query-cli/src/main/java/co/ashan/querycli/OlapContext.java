package co.ashan.querycli;

import mondrian.olap.MondrianException;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.mdx.SelectNode;
import org.olap4j.mdx.parser.MdxParser;
import org.olap4j.mdx.parser.MdxParserFactory;
import org.olap4j.mdx.parser.MdxValidator;

import java.sql.DriverManager;
import java.sql.SQLException;

public class OlapContext {

  private OlapConnection connection;
  private final MdxParser parser;
  private final MdxValidator validator;

  public OlapContext(DataSourceProperties properties) {
    try {
      Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(cnfe);
    }
    try {
      connection = DriverManager.getConnection(properties.toString()).unwrap(OlapConnection.class);
    } catch (SQLException sqle) {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException ce) {
          // Log this
        }
      }
      throw new RuntimeException(sqle);
    }
    MdxParserFactory parserFactory = connection.getParserFactory();
    parser = parserFactory.createMdxParser(connection);
    validator = parserFactory.createMdxValidator(connection);
  }

  public String schemaName() {
    try {
      return connection.getSchema();
    } catch (OlapException oe) {
      throw new RuntimeException(oe);
    }
  }

  public MdxQuery query(String queryString) {
    if (!isValidQuery(queryString))
      throw new IllegalArgumentException(queryString + " is not a valid MDX query");
    return new MdxQuery(queryString);
  }

  public boolean isValidQuery(String queryString) {
    try {
      SelectNode parseTree = parser.parseSelect(queryString);
      validator.validateSelect(parseTree);
    } catch (MondrianException | OlapException parseException) {
      return false;
    }
    return true;
  }


  public CellSet execute(MdxQuery query) {
    CellSet result = null;
    try {
      result = connection.createStatement().executeOlapQuery(query.queryString());
    } catch (OlapException oe) {
      throw new RuntimeException(oe);
    }
    return result;
  }

  public void clear() {
    try {
      if (connection != null) {
        connection.close();
        connection = null;
      }
    } catch (SQLException sqle) {
      throw new RuntimeException(sqle);
    }
  }

}
