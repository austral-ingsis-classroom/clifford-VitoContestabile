package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;
import java.util.ArrayList;

public record Ls() implements Command {
  @Override
  public Result execute(FileSystem fs, ArrayList<String> order) {

    switch (order.get(0)) {
      case "--ord=desc":
        return fs.ls("--ord=desc");
      case "--ord=asc":
        return fs.ls("--ord=asc");
      case "":
        return fs.ls();
      default:
        return new Result(fs, "Invalid Order");
    }
  }
}
