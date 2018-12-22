package kevindang97.tvSeriesRenamer.util;

import javafx.scene.control.TextFormatter;

public class Util {

  private static String illegalFilenameChars = "/\\?%*:|\"<>.";

  public static TextFormatter<String> getRestricterIllegalFilenameChars() {
    return new TextFormatter<String>(change -> {
      String illegalCharPattern = "[\\Q" + illegalFilenameChars + "\\E]";

      String text = change.getText();
      String reducedText = text.replaceAll(illegalCharPattern, "");

      change.setText(reducedText);

      return change;
    });
  }

}
