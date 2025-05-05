package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.*;
import java.util.ArrayList;

public record Remove() implements Command {
  @Override
  public Result execute(FileSystem fs, ArrayList<String> order) {

    switch (order.get(0)) {
      case "--recursive":
        return fs.removeDirectory(order.get(1));
      default:
        return fs.removeFile(order.get(0));
    }
  }
}
