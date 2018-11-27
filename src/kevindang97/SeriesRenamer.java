package kevindang97;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SeriesRenamer {

  private List<Path> files;
  private String format;
  private String seriesName;
  private int seasonNumber;
  private int episodeNumDigits;
  private List<String> episodeName;

  public SeriesRenamer() {
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
      return files.get(index).toString();
    } else {
      System.err.println("ERROR: getCurrentFilename(), index " + index + " out of bounds");
      return "";
    }
  }

  private int getNumDigits(int num) {
    int numDigits = 0;
    while (num > 0) {
      num /= 10;
      numDigits++;
    }

    return numDigits;
  }

  public String getTargetFilename(int index) {
    // TODO implement support for different formats
    if (indexInBounds(index)) {
      if (getEpisodeName(index).equals("")) {
        return String.format("%s - s%02de%0" + episodeNumDigits + "d", seriesName, seasonNumber,
            index);
      } else {
        return String.format("%s - s%02de%0" + episodeNumDigits + "d - %s", seriesName,
            seasonNumber, index, getEpisodeName(index));
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
    // TODO
    return false;
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


  }

  public boolean performRename() {
    return false;
  }

  private boolean indexInBounds(int index) {
    return index >= 0 && index < getNumFiles();
  }
}
