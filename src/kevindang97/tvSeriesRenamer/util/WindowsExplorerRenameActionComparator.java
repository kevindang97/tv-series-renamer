package kevindang97.tvSeriesRenamer.util;

import kevindang97.tvSeriesRenamer.model.RenameAction;

import java.util.Comparator;

public class WindowsExplorerRenameActionComparator implements Comparator<RenameAction> {

  private final Comparator<String> stringComparator;

  public WindowsExplorerRenameActionComparator(Comparator<String> stringComparator) {
    this.stringComparator = stringComparator;
  }

  public int compare(RenameAction renameAction1, RenameAction renameAction2) {
    return stringComparator
        .compare(renameAction1.getBeforeFilename(), renameAction2.getBeforeFilename());
  }
}
