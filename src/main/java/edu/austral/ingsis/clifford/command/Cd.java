package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;
import java.util.ArrayList;

public record Cd() implements Command {
  @Override
  public Result execute(FileSystem fs, ArrayList<String> order) {
    switch (order.get(0)) {
      case "..":
        return fs.moveToDirAbove();
      case ".":
        return new Result(fs, "Stayed in current Directory");
      default:
        return fs.moveToDir(order.get(0));
    }
  }
}
