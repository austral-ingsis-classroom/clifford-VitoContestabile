package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.*;

public final class Directory implements Files {

  private String name;
  private ArrayList<Files> sons;
  private Directory father;

  public Directory(String name, Directory father) {
    this.name = name;
    this.father = father;
    this.sons = new ArrayList<>();
  }

  @Override
  public String name() {
    return name;
  }

  public ArrayList<Files> sons() {
    return sons;
  }

  @Override
  public Directory father() {
    return father;
  }

  public String ls() {
    String string = "";

    for (Files f : sons) {
      if (f != null) {
        string += f.name() + " ";
      }
    }
    if (!string.isEmpty()) {
      return string.substring(0, string.length() - 1);
    }
    return string;
  }

  public String ls(String order) {
    String string = "";
    ArrayList<Files> copy = copy(sons);

    if (order == "--ord=asc") {
      copy.sort(Comparator.comparing(Files::name));
    } else if (order == "--ord=desc") {
      copy.sort(Comparator.comparing(Files::name));
      Collections.reverse(copy);
    } else {
      return "Invalid order ";
    }

    for (Files f : copy) {
      if (f != null) {
        string += f.name() + " ";
      }
    }
    if (!string.isEmpty()) {
      return string.substring(0, string.length() - 1);
    }
    return string;
  }

  private ArrayList<Files> copy(ArrayList<Files> list) {
    ArrayList<Files> copy = new ArrayList<Files>();
    for (Files s : list) {
      if (s != null) {
        copy.add(s);
      }
    }
    return copy;
  }

  public String mkdir(String name) {
    if (name == null || name.isEmpty() || name.contains(" ") || name.contains("/")) {
      return "Invalid name";
    }
    sons.add(new Directory(name, this));
    return "'" + name + "' directory created";
  }

  public String touch(String name) {
    if (name == null || name.isEmpty() || name.contains(" ") || name.contains("/")) {
      return "Invalid name";
    }
    sons.add(new File(name, this));
    return "'" + name + "' file created";
  }

  public String rm(String fileName) {

    for (Files f : sons) {
      if (f.name().equals(fileName) && f instanceof File) {
        sons.remove(f);
        return "'" + fileName + "' removed";
      } else if (f.name().equals(fileName) && f instanceof Directory) {
        return "cannot remove '" + fileName + "', is a directory";
      }
    }
    return "'" + fileName + "' not found";
  }

  public String rmRecursive(String fileName) {
    for (Files f : sons) {
      if (f.name().equals(fileName) && f instanceof Directory) {
        ((Directory) f).rmRecursive();
        sons.remove(f);
        return "'" + fileName + "' removed";
      }
    }
    return "";
  }

  private void rmRecursive() {
    for (Files f : sons) {
      if (f instanceof Directory) {
        ((Directory) f).rmRecursive();
      }
    }
    sons.clear();
  }
}
