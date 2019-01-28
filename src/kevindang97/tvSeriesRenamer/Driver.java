package kevindang97.tvSeriesRenamer;

import java.util.List;
import kevindang97.tvSeriesRenamer.model.HttpClient;
import kevindang97.tvSeriesRenamer.model.SeriesRenamer;

public class Driver {

  public static void main(String[] args) throws Exception {
    SeriesRenamer sr = new SeriesRenamer();

    // Path p = Paths.get("D:\\Documents\\test");
    //
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

    HttpClient client = new HttpClient(sr);
    List<String> episodeNames = client.getEpisodeNames("Modern Family", 9);

    for (String s : episodeNames) {
      System.out.println(s);
    }
  }
}
