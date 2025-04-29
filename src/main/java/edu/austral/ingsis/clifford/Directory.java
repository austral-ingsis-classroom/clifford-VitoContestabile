package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import javax.swing.*;

public final class Directory implements Files {

  private String name;
  private ArrayList<Files> sons;
  private ArrayList<String> pathNames;

  public Directory(String name, ArrayList<String> pathNames) {
    this.name = name;
    this.sons = new ArrayList<>();
    this.pathNames = pathNames;
  }

  public Directory(String name, ArrayList<String> pathNames, ArrayList<Files> sons) {
    this.name = name;
    this.pathNames = pathNames;
    this.sons = sons;
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
    return true;
  }

  @Override
  public ArrayList<Files> getChildren() {
    return new ArrayList<>(sons);
  }

  public boolean isRoot() {
    return pathNames.isEmpty();
  }
}
