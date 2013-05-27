package bne3.activities;

import bne3.activities.StdtoolkitActivityAdapter;

public interface OpenViewDialogAdapter extends StdtoolkitActivityAdapter {
  public abstract Object selectView(final String category, final String viewId);
  
  public abstract Object ok();
  
  public abstract Object cancel();
}
