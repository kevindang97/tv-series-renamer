package kevindang97.tvSeriesRenamer;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kevindang97.tvSeriesRenamer.model.SeriesRenamer;
import kevindang97.tvSeriesRenamer.view.MainWindowController;

public class MainApp extends Application {

  Stage primaryStage;
  BorderPane rootLayout;

  SeriesRenamer seriesRenamer;

  @Override
  public void start(Stage primaryStage) {
    seriesRenamer = new SeriesRenamer();

    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Series Renamer");

    try {
      // Initialise the MainWindow layout
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/MainWindow.fxml"));
      rootLayout = (BorderPane) loader.load();

      MainWindowController controller = loader.getController();
      controller.setMainApp(this);

      Scene scene = new Scene(rootLayout);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public SeriesRenamer getSeriesRenamer() {
    return seriesRenamer;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
