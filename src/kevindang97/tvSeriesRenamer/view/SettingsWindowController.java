package kevindang97.tvSeriesRenamer.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kevindang97.tvSeriesRenamer.model.SeriesRenamer;

public class SettingsWindowController {

  @FXML
  private CheckBox episodeNumberingZeroStartCheckBox;
  @FXML
  private TextField tvdbUsernameTextField;
  @FXML
  private TextField tvdbUniqueIdTextField;
  @FXML
  private TextField tvdbApiKeyTextField;

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
    tvdbUsernameTextField.setText(seriesRenamer.getTvdbUsername());
    tvdbUniqueIdTextField.setText(seriesRenamer.getTvdbUniqueId());
    tvdbApiKeyTextField.setText(seriesRenamer.getTvdbApiKey());
  }

  @FXML
  private void handleApply() {
    seriesRenamer.setEpisodeNumberingZeroStart(episodeNumberingZeroStartCheckBox.isSelected());
    seriesRenamer.setTvdbUsername(tvdbUsernameTextField.getText());
    seriesRenamer.setTvdbUniqueId(tvdbUniqueIdTextField.getText());
    seriesRenamer.setTvdbApiKey(tvdbApiKeyTextField.getText());

    dialogStage.close();
  }

  @FXML
  private void handleClose() {
    dialogStage.close();
  }
}
