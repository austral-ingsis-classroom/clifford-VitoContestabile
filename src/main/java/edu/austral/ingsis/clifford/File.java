package edu.austral.ingsis.clifford;

public class File implements Files {

  private String name;
  private Directory father;

  public File(String name, Directory father) {
    this.name = name;
    this.father = father;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Directory father() {
    return father;
  }
}
