package de.msg.xt.mdt.tdsl.basictypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;

public enum BooleanDTEquivalenceClass implements EquivalenceClass {
  trueBoolean,

  falseBoolean;
  
  public Boolean getValue() {
    Boolean value = null;
    switch (this) {
      case trueBoolean:
        value = true;
        break;
      case falseBoolean:
        value = false;
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case trueBoolean:
    		tags = new Tag[] {  };
    		break;
    	case falseBoolean:
    		tags = new Tag[] {  };
    		break;
    }
    return tags;
  }
  
  public static BooleanDTEquivalenceClass getByValue(final Boolean value) {
    de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass clazz = null;
    if (value != null) {
      if (value.equals(true)) {
      	return trueBoolean;
      }
      if (value.equals(false)) {
      	return falseBoolean;
      }
      
    }
    return null;
  }
}
