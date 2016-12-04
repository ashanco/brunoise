package co.ashan.querycli;

public class MdxQuery {
  private final String queryString;

  MdxQuery(String queryString) {
    this.queryString = queryString;
  }

  public String queryString() {
    return queryString;
  }
}
