package co.ashan.querycli;

import java.util.Properties;

public class DataSourceProperties extends Properties {

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("jdbc:mondrian:Jdbc=")
      .append(getProperty("database.url"))
      .append(";JdbcDrivers=")
      .append(getProperty("database.driver"))
      .append(";JdbcUser=")
      .append(getProperty("database.username"))
      .append(";JdbcPassword=")
      .append(getProperty("database.password"))
      .append(";Catalog=file://")
      .append(getProperty("mondrian.schema.path"));
    return sb.toString();
  }
}
