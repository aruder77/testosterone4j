package de.msg.xt.mdt.tdsl.swtbot;

public interface TableControl {
  public abstract void invokeContextMenu(final String contextMenuEntry);
  
  public abstract void invokeContextMenuOnRow(final Integer row, final String contextMenuEntry);
  
  public abstract void isContextMenuEnabled(final String contextMenuEntry);
  
  public abstract String getText(final Integer row, final Integer column);
  
  public abstract String getTextByName(final Integer row, final String columnName);
  
  public abstract void setText(final Integer row, final Integer column, final String textValue);
  
  public abstract void setTextByName(final Integer row, final String columnName, final String textValue);
  
  public abstract void selectValue(final Integer row, final Integer column, final String textValue);
  
  public abstract void selectValueByName(final Integer row, final String columnName, final String textValue);
  
  public abstract Integer getRowCount();
  
  public abstract void checkRow(final Integer row);
  
  public abstract void selectRow(final Integer row);
}
