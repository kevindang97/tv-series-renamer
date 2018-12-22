package kevindang97.tvSeriesRenamer.view;

import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EpisodeNameInputController {

  @FXML
  private TextArea textArea;

  private boolean okClicked = false;
  private Stage dialogStage;

  public List<String> getEpisodeNames() {
    String[] array = textArea.getText().split("\n");
    return Arrays.asList(array);
  }

  public boolean isOkClicked() {
    return okClicked;
  }

  public void setDialogStage(Stage stage) {
    dialogStage = stage;
  }

  @FXML
  private void handleOk() {
    okClicked = true;
    dialogStage.close();
  }

  @FXML
  private void handleCancel() {
    dialogStage.close();
  }
}
