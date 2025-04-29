package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import java.util.ArrayList;

public sealed interface Command permits Cd, Ls, Mkdir, Touch, Pwd, Remove {

  public FileSystem execute(FileSystem fs, ArrayList<String> order);
}
