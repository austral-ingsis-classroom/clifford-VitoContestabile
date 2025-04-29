package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Files;
import java.util.ArrayList;

public record Remove() implements Command {
  @Override
  public FileSystem execute(FileSystem fs, ArrayList<String> order) {

    switch (order.get(0)) {
      case "--recursive":
        return removeDirectory(fs, order.get(1), Directory.class);
      default:
        return removeFile(fs, order.get(0), File.class);
    }
  }

  private FileSystem removeDirectory(FileSystem fs, String fileName, Class<?> type) {
    if (getDirSon(fs.getCurrent(), fileName).name().equals("Invalid Path")) {
      return new FileSystem(fs.getRoot(), fs.getCurrent(), "cannot remove '" + fileName + "'");
    }
    Directory newRoot = setNewRoot(fs.getRoot(), fs.getCurrent(), fileName, type);
    Directory newCurrent = findCurrent(newRoot, fs.getCurrent().pathNames());
    return new FileSystem(newCurrent, newCurrent, "'" + fileName + "' removed");
  }

  private FileSystem removeFile(FileSystem fs, String fileName, Class<?> type) {
    if (getFileSon(fs.getCurrent(), fileName).name().equals("Invalid Path")) {
      return new FileSystem(
          fs.getRoot(), fs.getCurrent(), "cannot remove '" + fileName + "', is a directory");
    }
    Directory newRoot = setNewRoot(fs.getRoot(), fs.getCurrent(), fileName, type);
    Directory newCurrent = findCurrent(newRoot, fs.getCurrent().pathNames());
    return new FileSystem(newCurrent, newCurrent, "'" + fileName + "' removed");
  }

  private Directory findCurrent(Directory newRoot, ArrayList<String> pathNames) {

    Directory current = newRoot;
    while (!pathNames.isEmpty()) {
      current = getDirSon(current, pathNames.remove(0));
    }
    return current;
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
}
