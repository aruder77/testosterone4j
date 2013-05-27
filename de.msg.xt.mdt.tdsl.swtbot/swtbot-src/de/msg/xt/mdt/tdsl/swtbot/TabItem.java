package de.msg.xt.mdt.tdsl.swtbot;

public interface TabItem {
  public abstract void push();
  
  public abstract Boolean isEnabled();
  
  public abstract Boolean isSelected();
}
