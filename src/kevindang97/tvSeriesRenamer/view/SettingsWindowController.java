package kevindang97.tvSeriesRenamer.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import kevindang97.tvSeriesRenamer.model.SeriesRenamer;

public class SettingsWindowController {

  @FXML
  private CheckBox episodeNumberingZeroStartCheckBox;

  private Stage dialogStage;
  private SeriesRenamer seriesRenamer;

  public void setDialogStage(Stage stage) {
    dialogStage = stage;
  }

  public void setSeriesRenamer(SeriesRenamer seriesRenamer) {
    this.seriesRenamer = seriesRenamer;

    updateSettings();
  }

  private void updateSettings() {
    episodeNumberingZeroStartCheckBox.setSelected(seriesRenamer.getEpisodeNumberingZeroStart());
  }

  @FXML
  private void handleApply() {
    seriesRenamer.setEpisodeNumberingZeroStart(episodeNumberingZeroStartCheckBox.isSelected());

    dialogStage.close();
  }

  @FXML
  private void handleClose() {
    dialogStage.close();
  }
}
