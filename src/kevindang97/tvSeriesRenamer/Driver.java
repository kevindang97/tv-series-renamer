package kevindang97.tvSeriesRenamer;

import java.util.List;
import kevindang97.tvSeriesRenamer.model.HttpClient;

public class Driver {

  public static void main(String[] args) throws Exception {
    // Path p = Paths.get("D:\\Documents\\test");
    //
    // SeriesRenamer sr = new SeriesRenamer();
    // sr.setSeriesName("Series");
    // sr.setSeasonNumber(3);
    // sr.openFolder(p);
    //
    // for (RenameAction renameAction : sr.getRenameActions()) {
    // System.out.format("File: %s, target: %s%n", renameAction.getBeforeFilename(),
    // renameAction.getAfterFilename());
    // }
    //
    // sr.performRename();

    List<String> episodeNames = HttpClient.getEpisodeNames("Modern Family", 9);

    for (String s : episodeNames) {
      System.out.println(s);
    }
  }
}
