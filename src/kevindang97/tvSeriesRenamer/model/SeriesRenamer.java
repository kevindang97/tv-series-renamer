package kevindang97.tvSeriesRenamer.model;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kevindang97.tvSeriesRenamer.util.Util;

public class SeriesRenamer {

  private Path folder;
  private String format;
  private String seriesName;
  private int seasonNumber;
  private ObservableList<RenameAction> renameActions;
  private HttpClient httpClient;
  private Preferences prefs;

  // Implementation specific variables (Won't be exposed with getters/setters)
  private int episodeNumDigits;

  public SeriesRenamer() {
    folder = null;
    format = "[series-name] - s[season-num]e[episode-num] - [episode-name]";
    seriesName = "";
    seasonNumber = 0;
    episodeNumDigits = 2;
    renameActions = FXCollections.observableArrayList();
    httpClient = new HttpClient(this);
    prefs = Preferences.userNodeForPackage(SeriesRenamer.class);
  }

  public Path getFolder() {
    return folder;
  }

  public String getFormatString() {
    return format;
  }

  public String getSeriesName() {
    return seriesName;
  }

  public void setSeriesName(String seriesName) {
    this.seriesName = seriesName;
    regenerateAllAfterFilenames();
  }

  public int getSeasonNumber() {
    return seasonNumber;
  }

  public void setSeasonNumber(int seasonNum) {
    this.seasonNumber = seasonNum;
    regenerateAllAfterFilenames();
  }

  public int getNumFiles() {
    return renameActions.size();
  }

  public ObservableList<RenameAction> getRenameActions() {
    return renameActions;
  }

  public boolean getEpisodeNumberingZeroStart() {
    return prefs.getBoolean("episodeNumberingZeroStart", false);
  }

  public void setEpisodeNumberingZeroStart(boolean bool) {
    if (getEpisodeNumberingZeroStart() != bool) {
      prefs.putBoolean("episodeNumberingZeroStart", bool);
      regenerateAllAfterFilenames();
    }
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

    // get episode number
    int episodeNumber = getEpisodeNumberingZeroStart() ? index : index + 1;

    if (renameAction.getEpisodeName().equals("")) {
      renameAction.setAfterFilename(String.format("%s - s%02de%0" + episodeNumDigits + "d%s",
          seriesName, seasonNumber, episodeNumber, extension));
    } else {
      renameAction.setAfterFilename(String.format("%s - s%02de%0" + episodeNumDigits + "d - %s%s",
          seriesName, seasonNumber, episodeNumber, renameAction.getEpisodeName(), extension));
    }

    return true;
  }

  private void regenerateAllAfterFilenames() {
    for (int i = 0; i < getNumFiles(); i++) {
      regenerateAfterFilename(i);
    }
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

    // automatically set series name and season number based off of folder path
    if (getAutoSetSeriesInfo()) {
      String folderName = folder.getFileName().toString();
      String parentName = folder.getParent().getFileName().toString();

      // the assumption is that the folder structure is like so: SeriesName/Season#/EpisodesHere
      // strip all non number characters from season folder so that the number can be parsed
      String strippedFolderName = folderName.replaceAll("\\D", "");
      if (!strippedFolderName.equals("")) {
        int folderNum = Integer.parseInt(strippedFolderName);
        setSeasonNumber(folderNum);
      }
      setSeriesName(parentName);
    }

    // opens the folder directory and stores files in private list
    this.folder = folder;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
      for (Path path : stream) {
        if (path.toFile().isFile()) {
          renameActions.add(new RenameAction(path.getFileName().toString()));
        }
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

    // sort the renameActions before we generate the after filenames
    // this is because there's no guarantee that the directory stream reads the files in sorted
    // order
    Collections.sort(renameActions);

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
    // TODO Unused yet
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

  /**
   * Checks that the various fields have valid values, returning an error message for each invalid
   * field. An empty return string means there are no invalid fields.
   * 
   * @return
   */
  public String performValidation() {
    String errorMessage = "";

    if (getNumFiles() == 0) {
      errorMessage += "Please open a folder with at least one file to rename\n";
    }
    if (seriesName.equals("")) {
      errorMessage += "Series title field must not be empty\n";
    }
    if (seasonNumber <= 0) {
      errorMessage += "Season number must be a positive integer\n";
    }

    return errorMessage;
  }

  /**
   * Performs the full renaming operation on all the files in the renameActions list
   * 
   * @return
   */
  public void performRename() {
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

    openFolder(folder);
  }

  private int getNumDigits(int num) {
    int numDigits = 0;
    while (num > 0) {
      num /= 10;
      numDigits++;
    }

    return numDigits;
  }

  /**
   * Uses HttpClient to automatically retrieve and set the episode names for the current seriesName
   * and seasonNumber using the TVDB database. It will also strip any illegal filename characters
   * from the episode names it retrieves.
   */
  public void autoFillEpisodeNames() {
    if (getSeriesName().equals("") || getSeasonNumber() == 0) {
      return;
    }

    clearEpisodeNames();

    List<String> episodeNames = null;

    try {
      episodeNames = httpClient.getEpisodeNames(seriesName, seasonNumber);
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (int i = 0; i < getNumFiles(); i++) {
      setEpisodeName(i, Util.stripIllegalFilenameChars(episodeNames.get(i)));
    }
  }

  private boolean indexInBounds(int index) {
    return index >= 0 && index < getNumFiles();
  }

  public boolean getAutoSetSeriesInfo() {
    return prefs.getBoolean("autoSetSeriesInfo", true);
  }

  public void setAutoSetSeriesInfo(boolean bool) {
    prefs.putBoolean("autoSetSeriesInfo", bool);
  }

  public String getTvdbUsername() {
    return prefs.get("tvdbUsername", "");
  }

  public void setTvdbUsername(String username) {
    prefs.put("tvdbUsername", username);
  }

  public String getTvdbUniqueId() {
    return prefs.get("tvdbUniqueId", "");
  }

  public void setTvdbUniqueId(String uniqueId) {
    prefs.put("tvdbUniqueId", uniqueId);
  }

  public String getTvdbApiKey() {
    return prefs.get("tvdbApiKey", "");
  }

  public void setTvdbApiKey(String apiKey) {
    prefs.put("tvdbApiKey", apiKey);
  }
}
