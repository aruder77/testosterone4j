package de.msg.xt.mdt.tdsl.swtbot;

public interface List {
  public abstract void addSelection(final String entry);
  
  public abstract void clearSelection();
  
  public abstract Boolean isSelected(final String entry);
  
  public abstract void doubleClick(final String entry);
  
  public abstract Boolean isEnabled();
}
