package bne3.datatypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;

public enum LongNameEquivalenceClass implements EquivalenceClass {
  emptyLongName,

  regularLongName,

  umlautLongName,

  longLongName;
  
  public String getValue() {
    String value = null;
    switch (this) {
      case emptyLongName:
        value = "";
        break;
      case regularLongName:
        value = "Shortname";
        break;
      case umlautLongName:
        value = "Shortname 256 $/__!";
        break;
      case longLongName:
        value = "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongShortname";
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case emptyLongName:
    		tags = new Tag[] { de.msg.xt.mdt.tdsl.basictypes.Tags.Empty };
    		break;
    	case regularLongName:
    		tags = new Tag[] {  };
    		break;
    	case umlautLongName:
    		tags = new Tag[] { de.msg.xt.mdt.tdsl.basictypes.Tags.Invalid };
    		break;
    	case longLongName:
    		tags = new Tag[] {  };
    		break;
    }
    return tags;
  }
  
  public static LongNameEquivalenceClass getByValue(final String value) {
    bne3.datatypes.LongNameEquivalenceClass clazz = null;
    if (value != null) {
      if (value.equals("")) {
      	return emptyLongName;
      }
      if (value.equals("Shortname")) {
      	return regularLongName;
      }
      if (value.equals("Shortname 256 $/__!")) {
      	return umlautLongName;
      }
      if (value.equals("LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongShortname")) {
      	return longLongName;
      }
      
    }
    return null;
  }
}
