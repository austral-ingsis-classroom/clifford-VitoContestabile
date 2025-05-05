package edu.austral.ingsis.clifford;

public class Terminal {

  private FileSystem fs;
  private OrderParser orderParser;

  public Terminal(FileSystem fs) {
    this.fs = fs;
    orderParser = new OrderParser();
  }

  public String takeOrders(String order) {
    Result result = orderParser.parse(order, fs);
    this.fs = result.fileSystem();
    return result.message();
  }
}
