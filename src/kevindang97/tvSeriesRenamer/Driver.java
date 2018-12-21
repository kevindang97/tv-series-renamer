package kevindang97.tvSeriesRenamer;

import java.nio.file.Path;
import java.nio.file.Paths;

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
