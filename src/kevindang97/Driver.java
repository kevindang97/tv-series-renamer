package kevindang97;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {

  public static void main(String[] args) {
    Path p = Paths.get("C:\\Users\\kevin\\Documents\\test");

    SeriesRenamer sr = new SeriesRenamer();
    sr.setSeriesName("Series");
    sr.setSeasonNumber(3);
    sr.openFolder(p);

    for (int i = 0; i < sr.getNumFiles(); i++) {
      System.out.format("File: %s, target: %s%n", sr.getCurrentFilename(i),
          sr.getTargetFilename(i));
    }

    sr.performRename();
  }
}
