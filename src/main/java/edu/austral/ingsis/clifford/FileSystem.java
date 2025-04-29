package edu.austral.ingsis.clifford;

import java.util.ArrayList;

public class FileSystem {

  Directory root;
  Directory current;
  String lastMessage;

  public FileSystem() {
    this.root = new Directory("/", new ArrayList<>());
    this.current = this.root;
    this.lastMessage = "";
  }

  public FileSystem(Directory root, Directory current, String lastMessage) {
    this.root = root;
    this.current = current;
    this.lastMessage = lastMessage;
  }

  public Directory getRoot() {
    return root;
  }

  public Directory getCurrent() {
    return current;
  }

  public String getLastMessage() {
    return lastMessage;
  }
}
