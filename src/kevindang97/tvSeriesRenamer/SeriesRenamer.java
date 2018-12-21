package kevindang97.tvSeriesRenamer;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SeriesRenamer {

  private Path folder;
  // private List<Path> files;
  private String format;
  private String seriesName;
  private int seasonNumber;
  private int episodeNumDigits;
  // private List<String> episodeName;
  private ObservableList<RenameAction> renameActions;

  public SeriesRenamer() {
    folder = null;
    // files = new ArrayList<Path>();
    format = "[series-name] - s[season-num]e[episode-num] - [episode-name]";
    seriesName = "";
    seasonNumber = 0;
    episodeNumDigits = 2;
    // episodeName = new ArrayList<String>();
    renameActions = FXCollections.observableArrayList();
  }

  public int getNumFiles() {
    return renameActions.size();
  }

  public String getFormatString() {
    return format;
  }

  // public String getCurrentFilename(int index) {
  // if (indexInBounds(index)) {
  // return files.get(index).getFileName().toString();
  // } else {
  // System.err.println("ERROR: getCurrentFilename(), index " + index + " out of bounds");
  // return "";
  // }
  // }

  // public String getTargetFilename(int index) {
  // // TODO implement support for different formats
  // // get extension from original filename
  // String extension = "";
  // String[] split = getCurrentFilename(index).split("\\.");
  // if (split.length > 1) {
  // // if the file extension actually exists
  // extension = "." + split[split.length - 1];
  // }
  //
  // if (indexInBounds(index)) {
  // if (getEpisodeName(index).equals("")) {
  // return String.format("%s - s%02de%0" + episodeNumDigits + "d%s", seriesName, seasonNumber,
  // index + 1, extension);
  // } else {
  // return String.format("%s - s%02de%0" + episodeNumDigits + "d - %s%s", seriesName,
  // seasonNumber, index + 1, getEpisodeName(index), extension);
  // }
  // } else {
  // System.err.println("ERROR: getTargetFilename(), index " + index + " out of bounds");
  // return "";
  // }
  // }

  public ObservableList<RenameAction> getRenameActions() {
    return renameActions;
  }

  private boolean regenerateAfterFilename(int index) {
    if (index < 0 || index >= getNumFiles()) {
      return false;
    }

    RenameAction renameAction = renameActions.get(index);

    // get extension from beforeFilename
    String extension = "";
    String[] split = renameAction.getBeforeFilename().split("\\.");
    if (split.length > 1) {
      // if the file extension actually exists
      extension = "." + split[split.length - 1];
    }

    if (renameAction.getEpisodeName().equals("")) {
      renameAction.setAfterFilename(String.format("%s - s%02de%0" + episodeNumDigits + "d%s",
          seriesName, seasonNumber, index + 1, extension));
    } else {
      renameAction.setAfterFilename(String.format("%s - s%02de%0" + episodeNumDigits + "d - %s%s",
          seriesName, seasonNumber, index + 1, renameAction.getEpisodeName(), extension));
    }

    return true;
  }

  private void regenerateAllAfterFilenames() {
    for (int i = 0; i < getNumFiles(); i++) {
      regenerateAfterFilename(i);
    }
  }

  public String getSeriesName() {
    return seriesName;
  }

  public int getSeasonNumber() {
    return seasonNumber;
  }

  // public String getEpisodeName(int index) {
  // if (indexInBounds(index)) {
  // return episodeName.get(index);
  // } else {
  // System.err.println("ERROR: getEpisodeName(), index " + index + " out of bounds");
  // return "";
  // }
  // }

  public void setSeriesName(String seriesName) {
    this.seriesName = seriesName;
    regenerateAllAfterFilenames();
  }

  public void setSeasonNumber(int seasonNum) {
    this.seasonNumber = seasonNum;
    regenerateAllAfterFilenames();
  }

  public void setEpisodeName(int index, String newEpisodeName) {
    if (!renameActions.get(index).getEpisodeName().equals(newEpisodeName)) {
      renameActions.get(index).setEpisodeName(newEpisodeName);
      regenerateAfterFilename(index);
    }
  }

  public void clearEpisodeNames() {
    for (int i = 0; i < getNumFiles(); i++) {
      setEpisodeName(i, "");
    }
  }

  /**
   * Reads all the files in the specified folder
   * 
   * @param folder
   * @return
   */
  public boolean openFolder(Path folder) {
    // clear existing files and episodeName lists
    renameActions.clear();

    // opens the folder directory and stores files in private list
    this.folder = folder;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
      for (Path path : stream) {
        // files.add(path);
        // episodeName.add("");
        renameActions.add(new RenameAction(path.getFileName().toString()));
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

    regenerateAllAfterFilenames();

    return true;
  }

  /**
   * Swaps the beforeFilename properties of the two RenameActions
   * 
   * @param index1
   * @param index2
   */
  public void moveBeforeFilenameSwap(int index1, int index2) {
    if (!indexInBounds(index1) || !indexInBounds(index2)) {
      System.err
          .println("ERROR: moveFileSwap(), indexes " + index1 + " or " + index2 + " out of bounds");
      return;
    }

    String temp = renameActions.get(index1).getBeforeFilename();
    renameActions.get(index1).setBeforeFilename(renameActions.get(index2).getBeforeFilename());
    renameActions.get(index2).setBeforeFilename(temp);

    // have to regenerate afterFilename as the extensions of the before files might be different,
    // requiring the after filenames to change extensions as well
    regenerateAfterFilename(index1);
    regenerateAfterFilename(index2);
  }

  /**
   * Moves the beforeFilename properties of the two RenameActions by inserting index1 directly
   * above/below index2. This is the same as the process used in insertion sort.
   * 
   * @param index1
   * @param index2
   */
  public void moveBeforeFilenameInsert(int index1, int index2) {
    if (!indexInBounds(index1) || !indexInBounds(index2)) {
      System.err.println(
          "ERROR: moveFileInsert(), indexes " + index1 + " or " + index2 + " out of bounds");
      return;
    }

    if (index1 < index2) {
      while (index1 < index2) {
        moveBeforeFilenameSwap(index1, index1 + 1);
        index1++;
      }
    } else if (index1 > index2) {
      while (index1 > index2) {
        moveBeforeFilenameSwap(index1, index1 - 1);
        index1--;
      }
    }
  }

  public boolean performRename() {
    // TODO Can't handle case where original and target filenames overlap
    for (int i = 0; i < getNumFiles(); i++) {
      try {
        Files.move(folder.resolve(renameActions.get(i).getBeforeFilename()),
            folder.resolve(renameActions.get(i).getAfterFilename()));
      } catch (IOException e) {
        System.err.format("Failed trying to rename %s to %s, printing stack trace:%n",
            folder.resolve(renameActions.get(i).getBeforeFilename()),
            folder.resolve(renameActions.get(i).getAfterFilename()));
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
