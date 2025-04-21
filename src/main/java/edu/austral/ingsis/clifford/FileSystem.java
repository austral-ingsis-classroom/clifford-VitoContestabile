package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Arrays;

public class FileSystem {

  Directory root;
  Directory current;

  public FileSystem() {
    this.root = new Directory("/", null);
    this.current = this.root;
  }

  public String ls() {
    return current.ls();
  }

  public String ls(String order) {
    return current.ls(order);
  }

  public String mkdir(String name) {
    return current.mkdir(name);
  }

  public String touch(String name) {
    return current.touch(name);
  }

  public String cd(String order) {
    switch (order) {
      case "..":
        if (current != root) {
          current = current.father();
        }
        return "moved to directory '" + current.name() + "'";

      case ".":
        return "stayed in directory '" + current.name() + "'";

      default:
        if (isPathFromRoot(order) && findPath(order.substring(1), root)) {
          moveToPathFrom(order.substring(1), root);
          return "moved to directory '" + current.name() + "'";
        }

        if (findPath(order, current)) {
          moveToPathFrom(order, current);
          return "moved to directory '" + current.name() + "'";
        }
    }
    return "'" + order + "' directory does not exist";
  }

  private boolean isPathFromRoot(String path) {
    return path.charAt(0) == '/';
  }

  public String pwd() {
    Directory dir = current;
    String pwd = "";

    if (inRoot()) {
      return "/";
    }

    while (dir != root) {
      pwd = "/" + dir.name() + pwd;
      dir = dir.father();
    }
    return pwd;
  }

  private boolean inRoot() {
    return current == root;
  }

  public String rm(String order) {

    ArrayList<String> listOrder = new ArrayList<>(Arrays.asList(order.split(" ")));

    switch (listOrder.size()) {
      case 1:
        return current.rm(listOrder.get(0));

      case 2:
        if (listOrder.get(0).equals("--recursive")) {
          return current.rmRecursive(listOrder.get(1));
        }

      default:
        return "Not a valid order";
    }
  }

  private void moveToPathFrom(String order, Directory from) {
    ArrayList<String> path = new ArrayList<>(Arrays.asList(order.split("/")));
    int index = 0;
    current = from;
    while (index < path.size()) {
      current = setNewFinder(current, path.get(index));
      index++;
    }
  }

  private boolean findPath(String order, Directory from) {

    ArrayList<String> path = new ArrayList<>(Arrays.asList(order.split("/")));
    return findPath(path, from);
  }

  private Boolean findPath(ArrayList<String> path, Directory finder) {
    if (path.isEmpty()) {
      return true;
    }
    if (directoryHasChildren(finder, path.get(0))) {
      finder = setNewFinder(finder, path.get(0));
      return findPath(
          path.size() > 1 ? new ArrayList<>(path.subList(1, path.size())) : new ArrayList<>(),
          finder);
    } else {
      return false;
    }
  }

  private boolean directoryHasChildren(Directory dir, String name) {

    for (Files file : dir.sons()) {
      if (file.name().equals(name) && file instanceof Directory) {
        return true;
      }
    }
    return false;
  }

  private Directory setNewFinder(Directory dir, String name) {

    for (Files file : dir.sons()) {
      if (file.name().equals(name) && file instanceof Directory) {
        return (Directory) file;
      }
    }
    return null;
  }
}
