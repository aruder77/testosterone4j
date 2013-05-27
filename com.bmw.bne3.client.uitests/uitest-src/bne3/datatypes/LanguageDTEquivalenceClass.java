package bne3.datatypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;

public enum LanguageDTEquivalenceClass implements EquivalenceClass {
  de,

  en;
  
  public String getValue() {
    String value = null;
    switch (this) {
      case de:
        value = "DE";
        break;
      case en:
        value = "EN";
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case de:
    		tags = new Tag[] {  };
    		break;
    	case en:
    		tags = new Tag[] {  };
    		break;
    }
    return tags;
  }
  
  public static LanguageDTEquivalenceClass getByValue(final String value) {
    bne3.datatypes.LanguageDTEquivalenceClass clazz = null;
    if (value != null) {
      if (value.equals("DE")) {
      	return de;
      }
      if (value.equals("EN")) {
      	return en;
      }
      
    }
    return null;
  }
}
