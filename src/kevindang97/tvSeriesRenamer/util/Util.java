package kevindang97.tvSeriesRenamer.util;

import javafx.scene.control.TextFormatter;

public class Util {

  private static final String illegalFilenameChars = "/\\?%*:|\"<>.";

  public static TextFormatter<String> getRestricterIllegalFilenameChars() {
    return new TextFormatter<String>(change -> {
      String illegalCharPattern = "[\\Q" + illegalFilenameChars + "\\E]";

      String text = change.getText();
      String reducedText = text.replaceAll(illegalCharPattern, "");

      change.setText(reducedText);

      return change;
    });
  }

  public static String stripIllegalFilenameChars(String string) {
    String illegalCharPattern = "[\\Q" + illegalFilenameChars + "\\E]";
    return string.replaceAll(illegalCharPattern, "");
  }

}
