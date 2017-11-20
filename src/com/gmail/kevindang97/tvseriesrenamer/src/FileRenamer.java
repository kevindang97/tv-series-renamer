package com.gmail.kevindang97.tvseriesrenamer.src;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Abstract data structure used to keep all the data associated with a single renaming action in one
 * convenient object
 */
public class FileRenamer {

  private Path sourceFile;
  private String endFilename;
  private String originalFileName;

  /**
   * Creates a new FileRenamer object and sets both parameters
   * 
   * @param sourceFile
   * @param endFilename
   */
  public FileRenamer(Path sourceFile, String endFilename) {
    this.sourceFile = sourceFile;
    this.endFilename = endFilename;
    this.originalFileName = sourceFile.getFileName().toString();
  }

  /**
   * Returns the Path of the source file used in this object
   * 
   * @return
   */
  public Path getSourceFile() {
    return sourceFile;
  }

  /**
   * Set a new source file path
   * 
   * @param sourceFile
   */
  public void setSourceFile(Path sourceFile) {
    this.sourceFile = sourceFile;
    this.originalFileName = sourceFile.getFileName().toString();
  }

  /**
   * Returns the String of the filename that the source file will be renamed to. Does not include
   * file extension as the extension will remain unchanged by any of this class' renaming actions
   * 
   * @return
   */
  public String getEndFilename() {
    return endFilename;
  }

  /**
   * Set a new end filename that the file will be renamed to. Does not include file extension as the
   * extension will remain unchanged by any of this class' renaming actions
   * 
   * @param endFilename
   */
  public void setEndFilename(String endFilename) {
    this.endFilename = endFilename;
  }

  /**
   * Returns the String of the file's original filename. Does not include file extension as the
   * extension will remain unchanged by any of this class' renaming actions
   * 
   * @return
   */
  public String getOriginalFilename() {
    return originalFileName;
  }

  /**
   * Swaps the source files of two FileRenamer objects with each other
   * 
   * @param other
   */
  public void swapSourceFiles(FileRenamer other) {
    Path temp = this.getSourceFile();
    this.setSourceFile(other.getSourceFile());
    other.setSourceFile(temp);
  }

  /**
   * Swaps the end filenames of two FileRenamer objects with each other
   * 
   * @param other
   */
  public void swapEndFilenames(FileRenamer other) {
    String temp = this.getEndFilename();
    this.setEndFilename(other.getEndFilename());
    other.setEndFilename(temp);
  }

  /**
   * Does the renaming action, setting the new filename of the source file to endFilename. If it
   * encounters a file that already exists it will throw an error rather than overwrite it.
   * 
   * @throws IOException
   */
  public void rename() throws IOException, FileAlreadyExistsException {
    sourceFile = Files.move(sourceFile, sourceFile.resolveSibling(endFilename));
  }

  /**
   * Reverts any renaming action, so it sets file back to its original filename. Will overwrite
   * files that already exist because they shouldn't exist in the first place.
   * 
   * @throws IOException
   */
  public void revert() throws IOException {
    sourceFile = Files.move(sourceFile, sourceFile.resolveSibling(originalFileName),
        StandardCopyOption.REPLACE_EXISTING);
  }

}
