package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.*;
import java.util.ArrayList;

public record Mkdir() implements Command {
  @Override
  public Result execute(FileSystem fs, ArrayList<String> order) {

    String name = order.get(0);
    if (name == null || name.isEmpty() || name.contains(" ") || name.contains("/")) {
      return new Result(fs, "Invalid Order");
    }
    return fs.createNewDirectory(name);
  }
}
