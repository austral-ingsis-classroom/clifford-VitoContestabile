package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public record Ls() implements Command {
  @Override
  public FileSystem execute(FileSystem fs, ArrayList<String> order) {

    switch (order.get(0)) {
      case "--ord=desc":
        return descendantLs(fs);
      case "--ord=asc":
        return ascendantLs(fs);
      case "":
        return normalLs(fs);
      default:
        return new FileSystem(fs.getRoot(), fs.getCurrent(), "Invalid order");
    }
  }

  private FileSystem normalLs(FileSystem fs) {
    return new FileSystem(
        fs.getRoot(), fs.getCurrent(), listAsString(fs.getCurrent().getChildren()));
  }

  private FileSystem ascendantLs(FileSystem fs) {
    ArrayList<Files> copy = copy(fs.getCurrent().getChildren());
    sortAscendant(copy);
    return new FileSystem(fs.getRoot(), fs.getCurrent(), listAsString(copy));
  }

  private FileSystem descendantLs(FileSystem fs) {
    ArrayList<Files> copy = copy(fs.getCurrent().getChildren());
    sortDescendant(copy);
    return new FileSystem(fs.getRoot(), fs.getCurrent(), listAsString(copy));
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
}
