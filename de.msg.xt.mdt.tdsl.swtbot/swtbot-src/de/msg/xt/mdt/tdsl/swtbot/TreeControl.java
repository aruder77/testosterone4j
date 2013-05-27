package de.msg.xt.mdt.tdsl.swtbot;

public interface TreeControl {
  public abstract void selectNode(final String nodePath);
  
  public abstract void doubleClickNode(final String nodePath);
  
  public abstract Boolean isEnabled();
  
  public abstract void invokeContextMenu(final String nodePath, final String contextMenuEntry);
  
  public abstract Boolean isContextMenuEnabled(final String nodePath, final String contextMenuEntry);
  
  public abstract Boolean hasChildNode(final String nodePath, final String nodePattern, final Boolean recursive);
}
