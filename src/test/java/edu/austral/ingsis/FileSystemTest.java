package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.FileSystem;
import org.junit.jupiter.api.Test;

public class FileSystemTest {

  @Test
  public void test1() {
    FileSystem fs = new FileSystem();

    assertEquals("", fs.ls());
    assertEquals("'horace' directory created", fs.mkdir("horace"));
    assertEquals("Invalid name", fs.mkdir(" horace"));
    assertEquals("Invalid name", fs.touch(" horace"));
    assertEquals("horace", fs.ls());
    assertEquals("'emily' directory created", fs.mkdir("emily"));
    assertEquals("emily horace", fs.ls("--ord=asc"));
  }

  @Test
  public void test2() {
    FileSystem fs = new FileSystem();

    assertEquals("'horace' directory created", fs.mkdir("horace"));
    assertEquals("'emily' directory created", fs.mkdir("emily"));
    assertEquals("'jetta' directory created", fs.mkdir("jetta"));
    assertEquals("horace emily jetta", fs.ls());
    assertEquals("moved to directory 'emily'", fs.cd("emily"));
    assertEquals("/emily", fs.pwd());
    assertEquals("'elizabeth.txt' file created", fs.touch("elizabeth.txt"));
    assertEquals("'t-bone' directory created", fs.mkdir("t-bone"));
    assertEquals("elizabeth.txt t-bone", fs.ls());
  }

  @Test
  public void test3() {
    FileSystem fs = new FileSystem();

    assertEquals("'horace' directory created", fs.mkdir("horace"));
    assertEquals("'emily' directory created", fs.mkdir("emily"));
    assertEquals("'jetta' directory created", fs.mkdir("jetta"));
    assertEquals("stayed in directory '/'", fs.cd("."));
    assertEquals("moved to directory 'emily'", fs.cd("emily"));
    assertEquals("'elizabeth.txt' file created", fs.touch("elizabeth.txt"));
    assertEquals("'t-bone' directory created", fs.mkdir("t-bone"));
    assertEquals("elizabeth.txt t-bone", fs.ls());
    assertEquals("cannot remove 't-bone', is a directory", fs.rm("t-bone"));
    assertEquals("'t-bone' removed", fs.rm("--recursive t-bone"));
    assertEquals("elizabeth.txt", fs.ls());
    assertEquals("'elizabeth.txt' removed", fs.rm("elizabeth.txt"));
    assertEquals("'elizabeth.txt' not found", fs.rm("elizabeth.txt"));
    assertEquals("Not a valid order", fs.rm("elizabeth.txt --recursive a"));
    assertEquals("", fs.ls());
  }

  @Test
  public void test4() {
    FileSystem fs = new FileSystem();
    assertEquals("'emily' directory created", fs.mkdir("emily"));
    assertEquals("'horace' directory does not exist", fs.cd("horace"));
    assertEquals("moved to directory 'emily'", fs.cd("emily"));
    assertEquals("moved to directory '/'", fs.cd(".."));
    assertEquals("/", fs.pwd());
    assertEquals("moved to directory 'emily'", fs.cd("/emily"));
  }

  @Test
  public void test5() {
    FileSystem fs = new FileSystem();
    assertEquals("moved to directory '/'", fs.cd(".."));
  }

  @Test
  public void test6() {
    FileSystem fs = new FileSystem();

    assertEquals("'horace' directory created", fs.mkdir("horace"));
    assertEquals("moved to directory 'horace'", fs.cd("horace"));
    assertEquals("'emily.txt' file created", fs.touch("emily.txt"));
    assertEquals("'jetta.txt' file created", fs.touch("jetta.txt"));
    assertEquals("emily.txt jetta.txt", fs.ls());
    assertEquals("'emily.txt' removed", fs.rm("emily.txt"));
    assertEquals("jetta.txt", fs.ls());
  }

  @Test
  public void test7() {
    FileSystem fs = new FileSystem();
    assertEquals("'emily' directory created", fs.mkdir("emily"));
    assertEquals("moved to directory 'emily'", fs.cd("emily"));
    assertEquals("'emily' directory created", fs.mkdir("emily"));
    assertEquals("'emily.txt' file created", fs.touch("emily.txt"));
    assertEquals("'jetta.txt' file created", fs.touch("jetta.txt"));
    assertEquals("emily emily.txt jetta.txt", fs.ls());
    assertEquals("'emily' removed", fs.rm("--recursive emily"));
    assertEquals("emily.txt jetta.txt", fs.ls());
    assertEquals("jetta.txt emily.txt", fs.ls("--ord=desc"));
  }
}
