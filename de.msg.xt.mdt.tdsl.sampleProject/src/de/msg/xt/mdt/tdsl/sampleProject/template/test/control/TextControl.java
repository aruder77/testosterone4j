package de.msg.xt.mdt.tdsl.sampleProject.template.test.control;

public class TextControl {
  private String id;
  
  private String str;
  
  public TextControl(final String id) {
    this.id = id;
    
  }
  
  public void setText(final String str) {
    throw new UnsupportedOperationException("setTextis not implemented");
  }
  
  public String getText() {
    throw new UnsupportedOperationException("getTextis not implemented");
  }
  
  public void invokeAction() {
    throw new UnsupportedOperationException("invokeActionis not implemented");
  }
}
