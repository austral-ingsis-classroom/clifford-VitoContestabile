package edu.austral.ingsis.clifford;

import java.util.ArrayList;

public final class File implements Files {

  private String name;
  private ArrayList<String> pathNames;

  public File(String name, ArrayList<String> pathNames) {
    this.name = name;
    this.pathNames = pathNames;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public ArrayList<String> pathNames() {
    return new ArrayList<>(pathNames);
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public ArrayList<Files> getChildren() {
    return null;
  }
}
