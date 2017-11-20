package com.gmail.kevindang97.tvseriesrenamer.test;

import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.gmail.kevindang97.tvseriesrenamer.src.FileRenamer;

public class FileRenamerTest {

  private Path firstTestFile;
  private Path secondTestFile;
  private String firstOriginalFilename;
  private String secondOriginalFilename;

  /**
   * Creates test file used in each test
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    System.out.println("Setting up...");

    System.out.println("Creating temporary test files...");
    firstTestFile = Files.createTempFile("FileRenamerTest_", "");
    firstOriginalFilename = firstTestFile.getFileName().toString();

    secondTestFile = Files.createTempFile("FileRenamerTest_", "");
    secondOriginalFilename = secondTestFile.getFileName().toString();

    System.out.println("Finished setting up");
  }

  /**
   * Deletes the test files after each test
   * 
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    System.out.println("Tearing down...");

    if (!Files.deleteIfExists(firstTestFile)) {
      System.out.println("ERROR: Could not delete first test file");
    }

    if (!Files.deleteIfExists(secondTestFile)) {
      System.out.println("ERROR: Could not delete second test file");
    }

    System.out.println("Finished tearing down");
  }

  /**
   * Tests some of the standard methods along with getter and setter methods
   */
  @Test
  public void testFileRenamer() {
    FileRenamer fr = new FileRenamer(firstTestFile, "newFilename");

    assertNotNull(fr);
    assertEquals(firstTestFile, fr.getSourceFile());
    assertEquals(firstOriginalFilename, fr.getOriginalFilename());
    assertEquals("newFilename", fr.getEndFilename());

    fr.setSourceFile(secondTestFile);
    assertEquals(secondTestFile, fr.getSourceFile());
    assertEquals(secondOriginalFilename, fr.getOriginalFilename());
    assertEquals("newFilename", fr.getEndFilename());

    fr.setEndFilename("otherFilename");
    assertEquals("otherFilename", fr.getEndFilename());
  }

  /**
   * Testing the swap methods
   */
  @Test
  public void testSwap() {
    FileRenamer firstRenamer = new FileRenamer(firstTestFile, "firstFilename");
    FileRenamer secondRenamer = new FileRenamer(secondTestFile, "secondFilename");

    assertEquals(firstTestFile, firstRenamer.getSourceFile());
    assertEquals(firstOriginalFilename, firstRenamer.getOriginalFilename());
    assertEquals("firstFilename", firstRenamer.getEndFilename());

    assertEquals(secondTestFile, secondRenamer.getSourceFile());
    assertEquals(secondOriginalFilename, secondRenamer.getOriginalFilename());
    assertEquals("secondFilename", secondRenamer.getEndFilename());

    firstRenamer.swapSourceFiles(secondRenamer);

    assertEquals(secondTestFile, firstRenamer.getSourceFile());
    assertEquals(secondOriginalFilename, firstRenamer.getOriginalFilename());
    assertEquals("firstFilename", firstRenamer.getEndFilename());

    assertEquals(firstTestFile, secondRenamer.getSourceFile());
    assertEquals(firstOriginalFilename, secondRenamer.getOriginalFilename());
    assertEquals("secondFilename", secondRenamer.getEndFilename());

    secondRenamer.swapSourceFiles(firstRenamer);
    firstRenamer.swapEndFilenames(secondRenamer);

    assertEquals(firstTestFile, firstRenamer.getSourceFile());
    assertEquals(firstOriginalFilename, firstRenamer.getOriginalFilename());
    assertEquals("secondFilename", firstRenamer.getEndFilename());

    assertEquals(secondTestFile, secondRenamer.getSourceFile());
    assertEquals(secondOriginalFilename, secondRenamer.getOriginalFilename());
    assertEquals("firstFilename", secondRenamer.getEndFilename());

    // Testing what happens when the swap method is given itself as a parameter
    firstRenamer.swapSourceFiles(firstRenamer);
    firstRenamer.swapEndFilenames(firstRenamer);
    assertEquals(firstTestFile, firstRenamer.getSourceFile());
    assertEquals(firstOriginalFilename, firstRenamer.getOriginalFilename());
    assertEquals("secondFilename", firstRenamer.getEndFilename());
  }

  /**
   * Tests rename and revert methods
   * 
   * @throws IOException
   */
  @Test
  public void testRenameAndRevert() throws IOException {
    FileRenamer fr = new FileRenamer(firstTestFile, "firstFilename");

    assertEquals(firstTestFile, fr.getSourceFile());
    assertEquals(firstOriginalFilename, fr.getOriginalFilename());
    assertEquals("firstFilename", fr.getEndFilename());

    assertEquals(firstOriginalFilename, fr.getSourceFile().getFileName().toString());

    fr.rename();

    assertEquals("firstFilename", fr.getSourceFile().getFileName().toString());

    fr.revert();

    assertEquals(firstOriginalFilename, fr.getSourceFile().getFileName().toString());
  }

}
