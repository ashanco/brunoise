package co.ashan.querycli;

public class QueryCli {
  private final OlapContext context;

  public QueryCli(OlapContext context) {
    this.context = context;
  }

  public String schemaName() {
    return context.schemaName();
  }

}
