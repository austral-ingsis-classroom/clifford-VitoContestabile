package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.*;
import java.util.ArrayList;

public record Mkdir() implements Command {
  @Override
  public FileSystem execute(FileSystem fs, ArrayList<String> order) {

    String name = order.get(0);
    if (name == null || name.isEmpty() || name.contains(" ") || name.contains("/")) {
      return new FileSystem(fs.getRoot(), fs.getCurrent(), "Invalid order");
    }
    Directory newRoot = setNewRoot(fs.getRoot(), fs.getCurrent(), name);
    Directory newCurrent = findCurrent(newRoot, fs.getCurrent().pathNames());
    return new FileSystem(newRoot, newCurrent, "'" + name + "' directory created");
  }

  private Directory findCurrent(Directory newRoot, ArrayList<String> pathNames) {

    Directory current = newRoot;
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

  private Directory setNewRoot(Directory root, Directory current, String name) {

    return setNewRootRecursive(root, name, current.pathNames());
  }

  private Directory setNewRootRecursive(Directory root, String name, ArrayList<String> pathNames) {

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
          newSons.add(setNewRootRecursive((Directory) file, name, pathNames));
        } else {
          newSons.add(file);
        }
      }
      return new Directory(root.name(), root.pathNames(), newSons);
    }
  }
}
