package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;
import java.util.ArrayList;

public sealed interface Command permits Cd, Ls, Mkdir, Touch, Pwd, Remove {

  public Result execute(FileSystem fs, ArrayList<String> order);
}
