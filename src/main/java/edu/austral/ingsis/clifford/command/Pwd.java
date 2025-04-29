package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.ArrayList;

public record Pwd() implements Command {
  @Override
  public FileSystem execute(FileSystem fs, ArrayList<String> order) {
    return new FileSystem(fs.getRoot(), fs.getCurrent(), pathFromRootAsString(fs));
  }

  private String pathFromRootAsString(FileSystem fs) {

    String path = "/";
    for (String names : fs.getCurrent().pathNames()) {
      path += names + "/";
    }
    if (fs.getCurrent().isRoot()) {
      return path;
    }
    return path.substring(0, path.length() - 1);
  }
}
