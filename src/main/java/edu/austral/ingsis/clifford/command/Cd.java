package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Files;
import java.util.ArrayList;
import java.util.Arrays;

public record Cd() implements Command {
  @Override
  public FileSystem execute(FileSystem fs, ArrayList<String> order) {

    switch (order.get(0)) {
      case "..":
        return cdPointPoint(fs);
      case ".":
        return cdPoint(fs);
      default:
        if (isPathFromRoot(order.get(0))) {
          return cdFromRoot(fs, order.get(0));
        }
        return cdNormal(fs, order.get(0));
    }
  }

  private boolean isPathFromRoot(String s) {
    return s.startsWith("/");
  }

  private FileSystem cdPointPoint(FileSystem fs) {
    if (fs.getCurrent().isRoot()) {
      return new FileSystem(fs.getRoot(), fs.getCurrent(), "moved to directory '/'");
    }
    ArrayList<String> newPathNames =
        new ArrayList<>(
            fs.getCurrent().pathNames().subList(0, fs.getCurrent().pathNames().size() - 1));
    return new FileSystem(
        fs.getRoot(),
        findCurrent(fs.getRoot(), newPathNames),
        "moved to directory '"
            + (newPathNames.isEmpty() ? "/" : newPathNames.get(newPathNames.size() - 1))
            + "'");
  }

  private FileSystem cdPoint(FileSystem fs) {
    return new FileSystem(
        fs.getRoot(), fs.getCurrent(), "Stayed in diretory '" + fs.getCurrent().name() + "'");
  }

  private FileSystem cdNormal(FileSystem fs, String path) {
    Directory newCurrent = findCurrent(fs.getCurrent(), parsePath(path));
    if (newCurrent.name().equals("Invalid Path")) {
      return new FileSystem(
          fs.getRoot(), fs.getCurrent(), "'" + path + "' directory does not exist");
    }
    return new FileSystem(
        fs.getRoot(), newCurrent, "moved to directory '" + newCurrent.name() + "'");
  }

  private FileSystem cdFromRoot(FileSystem fs, String path) {
    Directory newCurrent = findCurrent(fs.getRoot(), parsePath(path));
    if (newCurrent.name().equals("Invalid Path")) {
      return new FileSystem(fs.getRoot(), fs.getCurrent(), "Invalid Path");
    }
    return new FileSystem(
        fs.getRoot(), newCurrent, "moved to directory '" + newCurrent.name() + "'");
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

  private ArrayList<String> parsePath(String path) {
    return new ArrayList<>(Arrays.asList(path.split("/")));
  }
}
