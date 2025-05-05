package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FileSystem {

  Directory root;
  Directory current;

  public FileSystem() {
    this.root = new Directory("/", new ArrayList<>());
    this.current = this.root;
  }

  public FileSystem(Directory root, Directory current) {
    this.root = root;
    this.current = current;
  }

  public Result moveToDirAbove() {
    if (current.isRoot()) {
      return new Result(new FileSystem(root, current), "moved to directory '/'");
    }
    ArrayList<String> newPathNames =
        new ArrayList<>(current.pathNames().subList(0, current.pathNames().size() - 1));

    return new Result(
        new FileSystem(root, findDirectoryWithPath(root, newPathNames)),
        "moved to directory '"
            + (newPathNames.isEmpty() ? "/" : newPathNames.get(newPathNames.size() - 1))
            + "'");
  }

  private Directory findDirectoryWithPath(Directory root, ArrayList<String> pathNames) {

    Directory current = root;
    while (!pathNames.isEmpty()) {
      current = getSon(current, pathNames.remove(0));
    }
    return current;
  }

  private Directory getSon(Directory current, String name) {

    for (Files directory : current.getChildren()) {
      if (directory.isDirectory() && directory.name().equals(name)) {
        return (Directory) directory;
      }
    }
    return new Directory("Invalid Path", new ArrayList<>());
  }

  public Result moveToDir(String path) {
    if (isPathFromRoot(path)) {
      return moveCurrentToSon(path, root);
    }
    return moveCurrentToSon(path, current);
  }

  private Result moveCurrentToSon(String path, Directory from) {
    Directory newCurrent = findDirectoryWithPath(from, parsePath(path));
    if (newCurrent.name().equals("Invalid Path")) {
      return new Result(new FileSystem(root, current), "'" + path + "' directory does not exist");
    }
    return new Result(
        new FileSystem(root, newCurrent), "moved to directory '" + newCurrent.name() + "'");
  }

  private boolean isPathFromRoot(String s) {
    return s.startsWith("/");
  }

  private ArrayList<String> parsePath(String path) {
    return new ArrayList<>(Arrays.asList(path.split("/")));
  }

  public String pathFromRoot() {

    String path = "/";
    for (String names : current.pathNames()) {
      path += names + "/";
    }
    if (current.isRoot()) {
      return path;
    }
    return path.substring(0, path.length() - 1);
  }

  public Result ls() {
    return new Result(this, listAsString(current.getChildren()));
  }

  public Result ls(String flag) {
    ArrayList<Files> copy;
    switch (flag) {
      case "--ord=desc":
        copy = copy(current.getChildren());
        sortDescendant(copy);
        return new Result(this, listAsString(copy));
      case "--ord=asc":
        copy = copy(current.getChildren());
        sortAscendant(copy);
        return new Result(this, listAsString(copy));
      default:
        return new Result(this, "Invalid Order");
    }
  }

  private ArrayList<Files> copy(ArrayList<Files> list) {
    ArrayList<Files> copy = new ArrayList<>();
    for (Files s : list) {
      if (s != null) {
        copy.add(s);
      }
    }
    return copy;
  }

  private void sortDescendant(ArrayList<Files> list) {
    list.sort(Comparator.comparing(Files::name));
    Collections.reverse(list);
  }

  private void sortAscendant(ArrayList<Files> list) {
    list.sort(Comparator.comparing(Files::name));
  }

  private String listAsString(ArrayList<Files> list) {
    String string = "";

    for (Files f : list) {
      if (f != null) {
        string += f.name() + " ";
      }
    }
    if (!string.isEmpty()) {
      return string.substring(0, string.length() - 1);
    }
    return string;
  }

  public Result createNewDirectory(String name) {

    Directory newRoot = setNewRootToAddDir(root, current, name);
    Directory newCurrent = findCurrent(newRoot, current.pathNames());
    return new Result(new FileSystem(newRoot, newCurrent), "'" + name + "' directory created");
  }

  public Result createNewFile(String name) {

    Directory newRoot = setNewRootToAddFile(root, current, name);
    Directory newCurrent = findCurrent(newRoot, current.pathNames());
    return new Result(new FileSystem(newRoot, newCurrent), "'" + name + "' file created");
  }

  private Directory findCurrent(Directory newRoot, ArrayList<String> pathNames) {

    Directory current = newRoot;
    while (!pathNames.isEmpty()) {
      current = getSon(current, pathNames.remove(0));
    }
    return current;
  }

  private Directory setNewRootToAddDir(Directory root, Directory current, String name) {

    return setNewRootRecursiveToAddDir(root, name, current.pathNames());
  }

  private Directory setNewRootRecursiveToAddDir(
      Directory root, String name, ArrayList<String> pathNames) {

    if (pathNames.equals(root.pathNames())) {
      ArrayList<Files> newSons = root.getChildren();
      ArrayList<String> newPathNames = root.pathNames();
      newPathNames.add(name);
      newSons.add(new Directory(name, newPathNames));
      return new Directory(root.name(), root.pathNames(), newSons);
    } else {
      ArrayList<Files> newSons = new ArrayList<>();

      for (Files file : root.getChildren()) {
        if (pathNames.contains(file.name()) && file.isDirectory()) {
          newSons.add(setNewRootRecursiveToAddDir((Directory) file, name, pathNames));
        } else {
          newSons.add(file);
        }
      }
      return new Directory(root.name(), root.pathNames(), newSons);
    }
  }

  private Directory setNewRootToAddFile(Directory root, Directory current, String name) {

    return setNewRootRecursiveToAddFile(root, name, current.pathNames());
  }

  private Directory setNewRootRecursiveToAddFile(
      Directory root, String name, ArrayList<String> pathNames) {

    if (pathNames.equals(root.pathNames())) {
      ArrayList<Files> newSons = root.getChildren();
      ArrayList<String> newPathNames = root.pathNames();
      newPathNames.add(name);
      newSons.add(new File(name, newPathNames));
      return new Directory(root.name(), root.pathNames(), newSons);
    } else {
      ArrayList<Files> newSons = new ArrayList<>();

      for (Files file : root.getChildren()) {
        if (pathNames.contains(file.name()) && file.isDirectory()) {
          newSons.add(setNewRootRecursiveToAddFile((Directory) file, name, pathNames));
        } else {
          newSons.add(file);
        }
      }
      return new Directory(root.name(), root.pathNames(), newSons);
    }
  }

  private Directory getDirSon(Directory current, String name) {

    for (Files directory : current.getChildren()) {
      if (directory.isDirectory() && directory.name().equals(name)) {
        return (Directory) directory;
      }
    }
    return new Directory("Invalid Path", new ArrayList<>());
  }

  private File getFileSon(Directory current, String name) {
    for (Files directory : current.getChildren()) {
      if (!directory.isDirectory() && directory.name().equals(name)) {
        return (File) directory;
      }
    }
    return new File("Invalid Path", new ArrayList<>());
  }

  private ArrayList<Files> removeDirFrom(ArrayList<Files> children, String name) {
    ArrayList<Files> newSons = new ArrayList<>();
    for (Files file : children) {
      if (name.equals(file.name()) && file.isDirectory()) {
        continue;
      }
      newSons.add(file);
    }
    return newSons;
  }

  private ArrayList<Files> removeFileFrom(ArrayList<Files> children, String name) {
    ArrayList<Files> newSons = new ArrayList<>();
    for (Files file : children) {
      if (name.equals(file.name()) && !file.isDirectory()) {
        continue;
      }
      newSons.add(file);
    }
    return newSons;
  }

  private Directory setNewRoot(Directory root, Directory current, String name, Class<?> type) {

    return setNewRootRecursive(root, name, current.pathNames(), type);
  }

  private Directory setNewRootRecursive(
      Directory root, String name, ArrayList<String> pathNames, Class<?> type) {

    if (pathNames.equals(root.pathNames())) {
      ArrayList<Files> newSons =
          type.equals(Directory.class)
              ? removeDirFrom(root.getChildren(), name)
              : removeFileFrom(root.getChildren(), name);
      return new Directory(root.name(), root.pathNames(), newSons);
    } else {
      ArrayList<Files> newSons = new ArrayList<>();

      for (Files file : root.getChildren()) {
        if (pathNames.contains(file.name()) && file.isDirectory()) {
          newSons.add(setNewRootRecursive((Directory) file, name, pathNames, type));
        } else {
          newSons.add(file);
        }
      }
      return new Directory(root.name(), root.pathNames(), newSons);
    }
  }

  public Result removeDirectory(String fileName) {
    if (getDirSon(current, fileName).name().equals("Invalid Path")) {
      return new Result(this, "cannot remove '" + fileName + "'");
    }
    Directory newRoot = setNewRoot(root, current, fileName, Directory.class);
    Directory newCurrent = findCurrent(newRoot, current.pathNames());
    return new Result(new FileSystem(newRoot, newCurrent), "'" + fileName + "' removed");
  }

  public Result removeFile(String fileName) {
    if (!getDirSon(current, fileName).name().equals("Invalid Path")) {
      return new Result(this, "cannot remove '" + fileName + "', is a directory");
    }
    if (getFileSon(current, fileName).name().equals("Invalid Path")) {
      return new Result(this, "cannot remove '" + fileName + "'");
    }
    Directory newRoot = setNewRoot(root, current, fileName, File.class);
    Directory newCurrent = findCurrent(newRoot, current.pathNames());
    return new Result(new FileSystem(newRoot, newCurrent), "'" + fileName + "' removed");
  }
}
