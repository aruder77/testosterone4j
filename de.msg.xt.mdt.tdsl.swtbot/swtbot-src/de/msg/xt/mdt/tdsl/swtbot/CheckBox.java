package de.msg.xt.mdt.tdsl.swtbot;

public interface CheckBox {
  public abstract void toggle();
  
  public abstract void setSelected(final Boolean selected);
  
  public abstract Boolean isSelected();
  
  public abstract Boolean isEnabled();
}
