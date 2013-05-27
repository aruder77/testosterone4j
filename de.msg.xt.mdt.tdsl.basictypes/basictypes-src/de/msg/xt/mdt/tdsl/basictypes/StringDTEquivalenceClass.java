package de.msg.xt.mdt.tdsl.basictypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;

public enum StringDTEquivalenceClass implements EquivalenceClass {
  emptyString,

  shortString,

  longString;
  
  public String getValue() {
    String value = null;
    switch (this) {
      case emptyString:
        value = "";
        break;
      case shortString:
        value = "shortString";
        break;
      case longString:
        value = "longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglongString";
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case emptyString:
    		tags = new Tag[] { de.msg.xt.mdt.tdsl.basictypes.Tags.Empty };
    		break;
    	case shortString:
    		tags = new Tag[] {  };
    		break;
    	case longString:
    		tags = new Tag[] {  };
    		break;
    }
    return tags;
  }
  
  public static StringDTEquivalenceClass getByValue(final String value) {
    de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass clazz = null;
    if (value != null) {
      if (value.equals("")) {
      	return emptyString;
      }
      if (value.equals("shortString")) {
      	return shortString;
      }
      if (value.equals("longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglongString")) {
      	return longString;
      }
      
    }
    return null;
  }
}
