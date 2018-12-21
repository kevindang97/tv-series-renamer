package kevindang97.tvSeriesRenamer;

import java.io.File;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.DirectoryChooser;

public class MainWindowController {

  @FXML
  private TextField seriesNameTextField;
  @FXML
  private TextField seasonNumberTextField;

  @FXML
  private TableView<RenameAction> fileTable;
  @FXML
  private TableColumn<RenameAction, String> beforeFilenameColumn;
  @FXML
  private TableColumn<RenameAction, String> afterFilenameColumn;

  private MainApp mainApp;
  private final DirectoryChooser directoryChooser = new DirectoryChooser();

  @FXML
  private void initialize() {
    beforeFilenameColumn
        .setCellValueFactory(cellData -> cellData.getValue().beforeFilenameProperty());
    afterFilenameColumn
        .setCellValueFactory(cellData -> cellData.getValue().afterFilenameProperty());

    // set up change listener for series name and season number text fields
    seriesNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      mainApp.getSeriesRenamer().setSeriesName(newValue);
    });

    seasonNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      int seasonNum = 0;
      if (!newValue.equals("")) {
        seasonNum = Integer.parseInt(newValue);
      }
      mainApp.getSeriesRenamer().setSeasonNumber(seasonNum);
    });

    // set up text formatter for season number text field to restrict input to only digit characters
    seasonNumberTextField.setTextFormatter(new TextFormatter<>(change -> {
      if (Pattern.matches("\\d*", change.getText())) {
        return change;
      } else {
        return null;
      }
    }));
  }

  public void setMainApp(MainApp mainApp) {
    this.mainApp = mainApp;

    // add observable rename actions list to the table
    fileTable.setItems(mainApp.getSeriesRenamer().getRenameActions());
  }

  @FXML
  private void handleOpenFolder() {
    File directory = directoryChooser.showDialog(mainApp.getPrimaryStage());
    if (directory != null) {
      mainApp.getSeriesRenamer().openFolder(directory.toPath());
    }
  }
}
