package com.gmail.kevindang97.tvseriesrenamer.test;

import static org.junit.Assert.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gmail.kevindang97.tvseriesrenamer.src.FileRenamer;

public class FileRenamerTest {

  private static final Path WORKING_DIRECTORY = Paths.get("");

  private static Path firstTestFile;
  private static Path secondTestFile;
  private static String firstOriginalFilename;
  private static String secondOriginalFilename;

  /**
   * Creates test file used in these tests
   * 
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    firstTestFile = Files.createTempFile("FileRenamerTest", null);
    firstOriginalFilename = firstTestFile.getFileName().toString();

    secondTestFile = Files.createTempFile("FileRenamerTest_", null);
    secondOriginalFilename = secondTestFile.getFileName().toString();
  }

  /**
   * Deletes the test file
   * 
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    assertTrue(Files.deleteIfExists(firstTestFile));
    assertTrue(Files.deleteIfExists(secondTestFile));
  }

  @Test
  public void testFilename() {

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
   */
  @Test
  public void testRenameAndRevert() {
    FileRenamer fr = new FileRenamer(firstTestFile, "firstFilename");

    assertEquals(firstTestFile, fr.getSourceFile());
    assertEquals(firstOriginalFilename, fr.getOriginalFilename());
    assertEquals("firstFilename", fr.getEndFilename());

    assertEquals(firstOriginalFilename, fr.getSourceFile().getFileName().toString());

    fr.rename();

    assertEquals("firstFilename", fr.getSourceFile().getFileName().toFile());

    fr.revert();

    assertEquals(firstOriginalFilename, fr.getSourceFile().getFileName().toString());
  }

}
