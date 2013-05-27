package bne3.activities;

import bne3.activities.StdtoolkitActivityAdapter;

public interface ViewActivityAdapter extends StdtoolkitActivityAdapter {
  public abstract Object close();
  
  public abstract Object returnToMain();
}
