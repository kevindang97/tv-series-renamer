package kevindang97.tvSeriesRenamer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RenameAction {

  private final StringProperty beforeFilename;
  private final StringProperty afterFilename;
  private final StringProperty episodeName;

  public RenameAction(String beforeFilename) {
    this.beforeFilename = new SimpleStringProperty(beforeFilename);
    this.afterFilename = new SimpleStringProperty("");
    this.episodeName = new SimpleStringProperty("");
  }

  public String getBeforeFilename() {
    return beforeFilename.get();
  }

  public void setBeforeFilename(String filename) {
    beforeFilename.set(filename);
  }

  public StringProperty beforeFilenameProperty() {
    return beforeFilename;
  }

  public String getAfterFilename() {
    return afterFilename.get();
  }

  public void setAfterFilename(String filename) {
    afterFilename.set(filename);
  }

  public StringProperty afterFilenameProperty() {
    return afterFilename;
  }

  public String getEpisodeName() {
    return episodeName.get();
  }

  public void setEpisodeName(String episodeName) {
    this.episodeName.set(episodeName);
  }

  public StringProperty episodeNameProperty() {
    return episodeName;
  }
}
