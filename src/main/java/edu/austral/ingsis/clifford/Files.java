package edu.austral.ingsis.clifford;

import java.util.ArrayList;

public sealed interface Files permits File, Directory {

  public String name();

  public ArrayList<String> pathNames();

  public boolean isDirectory();

  public ArrayList<Files> getChildren();
}
