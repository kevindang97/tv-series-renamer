package com.gmail.kevindang97.tvseriesrenamer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Abstract data structure used to keep all the data associated with a single renaming action
 * 	 in one convenient object
 */
public class FileRenamer {
	
	private Path sourceFile;
	private String endFilename;
	
	/**
	 * Private default constructor so that it can't be called
	 */
	@SuppressWarnings("unused")
	private FileRenamer() {}
	
	/**
	 * Creates a new FileRenamer object and sets both parameters
	 * @param sourceFile
	 * @param endFilename
	 */
	public FileRenamer(Path sourceFile, String endFilename) {
		
	}
	
	/**
	 * Returns the Path of the source file used in this object
	 * @return
	 */
	public Path getSourceFile() {
		return sourceFile;
	}
	
	/**
	 * Set a new source file path
	 * @param sourceFile
	 */
	public void setSourceFile(Path sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	/**
	 * Returns the String of the filename that the source file will be renamed to
	 * @return
	 */
	public String getEndFilename() {
		return endFilename;
	}
	
	/**
	 * Set a new end filename that the file will be renamed to 
	 * @param endFilename
	 */
	public void setEndFilename(String endFilename) {
		this.endFilename = endFilename;
	}
	
	/**
	 * Swaps the source files of two FileRenamer objects with each other
	 * @param other
	 */
	public void swapSourceFiles(FileRenamer other) {
		Path temp = this.getSourceFile();
		this.setSourceFile(other.getSourceFile());
		other.setSourceFile(temp);
	}
	
	/**
	 * Swaps the end filenames of two FileRenamer objects with each other
	 * @param other
	 */
	public void swapEndFilenames(FileRenamer other) {
		String temp = this.getEndFilename();
		this.setEndFilename(other.getEndFilename());
		other.setEndFilename(temp);
	}
	
	/**
	 * Does the renaming action, setting the new filename of the source file to endFilename
	 */
	public void rename() {
		try {
			Files.move(sourceFile, sourceFile.resolve(endFilename));
		} catch (IOException e) {
			System.out.println("IOException occurred in FileRenamer object's rename() method");
			e.printStackTrace();
		}
	}
	
}
