package kevindang97;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SeriesRenamer {

  private Path folder;
  private List<Path> files;
  private String format;
  private String seriesName;
  private int seasonNumber;
  private int episodeNumDigits;
  private List<String> episodeName;

  public SeriesRenamer() {
    folder = null;
    files = new ArrayList<Path>();
    format = "[series-name] - s[season-num]e[episode-num] - [episode-name]";
    seriesName = "";
    seasonNumber = 0;
    episodeNumDigits = 2;
    episodeName = new ArrayList<String>();
  }

  public int getNumFiles() {
    return files.size();
  }

  public String getFormatString() {
    return format;
  }

  public String getCurrentFilename(int index) {
    if (indexInBounds(index)) {
      return files.get(index).getFileName().toString();
    } else {
      System.err.println("ERROR: getCurrentFilename(), index " + index + " out of bounds");
      return "";
    }
  }

  public String getTargetFilename(int index) {
    // TODO implement support for different formats
    // get extension from original filename
    String extension = "";
    String[] split = getCurrentFilename(index).split("\\.");
    if (split.length > 1) {
      // if the file extension actually exists
      extension = "." + split[split.length - 1];
    }

    if (indexInBounds(index)) {
      if (getEpisodeName(index).equals("")) {
        return String.format("%s - s%02de%0" + episodeNumDigits + "d%s", seriesName, seasonNumber,
            index + 1, extension);
      } else {
        return String.format("%s - s%02de%0" + episodeNumDigits + "d - %s%s", seriesName,
            seasonNumber, index + 1, getEpisodeName(index), extension);
      }
    } else {
      System.err.println("ERROR: getTargetFilename(), index " + index + " out of bounds");
      return "";
    }
  }

  public String getSeriesName() {
    return seriesName;
  }

  public int getSeasonNumber() {
    return seasonNumber;
  }

  public String getEpisodeName(int index) {
    if (indexInBounds(index)) {
      return episodeName.get(index);
    } else {
      System.err.println("ERROR: getEpisodeName(), index " + index + " out of bounds");
      return "";
    }
  }

  public void setSeriesName(String seriesName) {
    this.seriesName = seriesName;
  }

  public void setSeasonNumber(int seasonNum) {
    this.seasonNumber = seasonNum;
  }

  public void setEpisodeName(int index, String name) {
    this.episodeName.set(index, name);
  }

  public void clearEpisodeNames() {
    for (int i = 0; i < getNumFiles(); i++) {
      setEpisodeName(i, "");
    }
  }

  public boolean openFolder(Path folder) {
    // clear existing files and episodeName lists
    files.clear();
    episodeName.clear();

    // opens the folder directory and stores files in private list
    this.folder = folder;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
      for (Path path : stream) {
        files.add(path);
        episodeName.add("");
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    if (getNumFiles() > 99) {
      episodeNumDigits = getNumDigits(getNumFiles());
    } else {
      episodeNumDigits = 2;
    }

    return true;

  }

  public void moveFileSwap(int index1, int index2) {
    if (!indexInBounds(index1) || !indexInBounds(index2)) {
      System.err
          .println("ERROR: moveFileSwap(), indexes " + index1 + " or " + index2 + " out of bounds");
      return;
    }

    Path temp = files.get(index1);
    files.set(index1, files.get(index2));
    files.set(index2, temp);
  }

  public void moveFileInsert(int index1, int index2) {
    if (!indexInBounds(index1) || !indexInBounds(index2)) {
      System.err.println(
          "ERROR: moveFileInsert(), indexes " + index1 + " or " + index2 + " out of bounds");
      return;
    }

    if (index1 < index2) {
      while (index1 < index2) {
        moveFileSwap(index1, index1 + 1);
        index1++;
      }
    } else if (index1 > index2) {
      while (index1 > index2) {
        moveFileSwap(index1, index1 - 1);
        index1--;
      }
    }
  }

  public boolean performRename() {
    // TODO Can't handle case where original and target filenames overlap
    for (int i = 0; i < getNumFiles(); i++) {
      try {
        Files.move(folder.resolve(getCurrentFilename(i)), folder.resolve(getTargetFilename(i)));
      } catch (IOException e) {
        System.err.format("Failed trying to rename %s to %s, printing stack trace:%n",
            folder.resolve(getCurrentFilename(i)), folder.resolve(getTargetFilename(i)));
        e.printStackTrace();
      }
    }
    return false;
  }

  private int getNumDigits(int num) {
    int numDigits = 0;
    while (num > 0) {
      num /= 10;
      numDigits++;
    }

    return numDigits;
  }

  private boolean indexInBounds(int index) {
    return index >= 0 && index < getNumFiles();
  }
}
