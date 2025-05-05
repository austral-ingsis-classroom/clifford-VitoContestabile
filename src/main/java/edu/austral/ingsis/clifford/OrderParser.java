package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.command.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OrderParser {

  Map<String, Command> map = new HashMap<>();

  public OrderParser() {
    map.put("cd", new Cd());
    map.put("ls", new Ls());
    map.put("touch", new Touch());
    map.put("mkdir", new Mkdir());
    map.put("pwd", new Pwd());
    map.put("rm", new Remove());
  }

  public Result parse(String order, FileSystem fs) {

    ArrayList<String> orderAsArray = new ArrayList<>(Arrays.asList(order.split(" ")));

    if (!orderAsArray.isEmpty() && map.containsKey(orderAsArray.get(0))) {
      Command command = map.get(orderAsArray.get(0));
      orderAsArray.remove(0);

      if (orderAsArray.isEmpty()) {
        orderAsArray.add("");
      }

      return command.execute(fs, orderAsArray);
    }
    return new Result(fs, "Invalid Order");
  }
}
