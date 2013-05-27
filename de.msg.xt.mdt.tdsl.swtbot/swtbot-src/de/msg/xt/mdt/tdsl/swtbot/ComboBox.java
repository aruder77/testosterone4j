package de.msg.xt.mdt.tdsl.swtbot;

public interface ComboBox {
  public abstract void setText(final String str);
  
  public abstract String getText();
  
  public abstract Boolean isEnabled();
}
