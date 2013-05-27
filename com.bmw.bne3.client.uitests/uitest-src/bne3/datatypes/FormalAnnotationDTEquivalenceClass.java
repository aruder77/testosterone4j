package bne3.datatypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;

public enum FormalAnnotationDTEquivalenceClass implements EquivalenceClass {
  diagnostic,

  errorCode,

  infoCode;
  
  public String getValue() {
    String value = null;
    switch (this) {
      case diagnostic:
        value = "Diagnostics";
        break;
      case errorCode:
        value = "Error-Code";
        break;
      case infoCode:
        value = "Info-Code";
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case diagnostic:
    		tags = new Tag[] {  };
    		break;
    	case errorCode:
    		tags = new Tag[] {  };
    		break;
    	case infoCode:
    		tags = new Tag[] {  };
    		break;
    }
    return tags;
  }
  
  public static FormalAnnotationDTEquivalenceClass getByValue(final String value) {
    bne3.datatypes.FormalAnnotationDTEquivalenceClass clazz = null;
    if (value != null) {
      if (value.equals("Diagnostics")) {
      	return diagnostic;
      }
      if (value.equals("Error-Code")) {
      	return errorCode;
      }
      if (value.equals("Info-Code")) {
      	return infoCode;
      }
      
    }
    return null;
  }
}
