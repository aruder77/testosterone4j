package bne3.activities;

import bne3.activities.StdtoolkitActivityAdapter;

public interface EditorActivityAdapter extends StdtoolkitActivityAdapter {
  public abstract Object saveAndClose();
  
  public abstract Object close();
  
  public abstract Object isDirty();
  
  public abstract Object returnToMain();
}
