package kevindang97.tvSeriesRenamer.view;

import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import kevindang97.tvSeriesRenamer.model.SeriesRenamer;

public class SettingsWindowController {

  @FXML
  private TextField episodeNumberingStartTextField;
  @FXML
  private CheckBox autoSetSeriesInfoCheckBox;
  @FXML
  private TextField tvdbUsernameTextField;
  @FXML
  private TextField tvdbUniqueIdTextField;
  @FXML
  private TextField tvdbApiKeyTextField;

  private Stage dialogStage;
  private SeriesRenamer seriesRenamer;

  @FXML
  private void initialize() {
    // set up text formatter for season number text field to restrict input to only digit characters
    episodeNumberingStartTextField.setTextFormatter(new TextFormatter<>(change -> {
      if (Pattern.matches("\\d*", change.getText())) {
        return change;
      } else {
        return null;
      }
    }));
  }

  public void setDialogStage(Stage stage) {
    dialogStage = stage;
  }

  public void setSeriesRenamer(SeriesRenamer seriesRenamer) {
    this.seriesRenamer = seriesRenamer;

    updateSettings();
  }

  private void updateSettings() {
    String episodeNumberingStart = Integer.toString(seriesRenamer.getEpisodeNumberingStart());
    episodeNumberingStartTextField.setText(episodeNumberingStart);
    autoSetSeriesInfoCheckBox.setSelected(seriesRenamer.getAutoSetSeriesInfo());
    tvdbUsernameTextField.setText(seriesRenamer.getTvdbUsername());
    tvdbUniqueIdTextField.setText(seriesRenamer.getTvdbUniqueId());
    tvdbApiKeyTextField.setText(seriesRenamer.getTvdbApiKey());
  }

  @FXML
  private void handleApply() {
    int episodeNumberingStart = Integer.parseInt(episodeNumberingStartTextField.getText());
    seriesRenamer.setEpisodeNumberingStart(episodeNumberingStart);
    seriesRenamer.setAutoSetSeriesInfo(autoSetSeriesInfoCheckBox.isSelected());
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
