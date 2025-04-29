package edu.austral.ingsis;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Terminal;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {

  @Override
  public List<String> executeCommands(List<String> commands) {
    FileSystem fs = new FileSystem();
    Terminal terminal = new Terminal(fs);
    List<String> result = new ArrayList<>();
    for (String command : commands) {
      result.add(terminal.takeOrders(command));
    }
    return result;
  }
}
