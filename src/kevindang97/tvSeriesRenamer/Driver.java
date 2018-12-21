package kevindang97.tvSeriesRenamer;

import java.nio.file.Path;
import java.nio.file.Paths;
import kevindang97.tvSeriesRenamer.model.RenameAction;
import kevindang97.tvSeriesRenamer.model.SeriesRenamer;

public class Driver {

  public static void main(String[] args) {
    Path p = Paths.get("D:\\Documents\\test");

    SeriesRenamer sr = new SeriesRenamer();
    sr.setSeriesName("Series");
    sr.setSeasonNumber(3);
    sr.openFolder(p);

    for (RenameAction renameAction : sr.getRenameActions()) {
      System.out.format("File: %s, target: %s%n", renameAction.getBeforeFilename(),
          renameAction.getAfterFilename());
    }

    sr.performRename();
  }
}
