package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;
import java.util.ArrayList;

public record Pwd() implements Command {
  @Override
  public Result execute(FileSystem fs, ArrayList<String> order) {
    return new Result(fs, fs.pathFromRoot());
  }
}
