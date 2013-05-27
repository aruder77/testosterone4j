package bne3.datatypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

public enum ShortNameEquivalenceClass implements EquivalenceClass {
  emptyShortName,

  regularShortName,

  umlautShortName,

  longShortName;
  
  public String getValue() {
    String value = null;
    switch (this) {
      case emptyShortName:
        value = "";
        break;
      case regularShortName:
        value = new Function0<String>() {
          public String apply() {
            long _currentTimeMillis = System.currentTimeMillis();
            String _plus = ("Shortname" + Long.valueOf(_currentTimeMillis));
            return _plus;
          }
        }.apply();
        break;
      case umlautShortName:
        value = "Shortname 256 $/__!";
        break;
      case longShortName:
        value = "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongShortname";
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case emptyShortName:
    		tags = new Tag[] { de.msg.xt.mdt.tdsl.basictypes.Tags.Empty };
    		break;
    	case regularShortName:
    		tags = new Tag[] {  };
    		break;
    	case umlautShortName:
    		tags = new Tag[] { de.msg.xt.mdt.tdsl.basictypes.Tags.Invalid };
    		break;
    	case longShortName:
    		tags = new Tag[] { de.msg.xt.mdt.tdsl.basictypes.Tags.Invalid };
    		break;
    }
    return tags;
  }
  
  public static ShortNameEquivalenceClass getByValue(final String value) {
    bne3.datatypes.ShortNameEquivalenceClass clazz = null;
    if (value != null) {
      if (value.equals("")) {
      	return emptyShortName;
      }
      if (new Function0<Function1<String,Boolean>>() {
        public Function1<String,Boolean> apply() {
          final Function1<String,Boolean> _function = new Function1<String,Boolean>() {
              public Boolean apply(final String e) {
                boolean _startsWith = e.startsWith("Shortname");
                return _startsWith;
              }
            };
          return _function;
        }
      }.apply().apply(value)) {
      	return regularShortName;
      }if (value.equals("Shortname 256 $/__!")) {
      	return umlautShortName;
      }
      if (value.equals("LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongShortname")) {
      	return longShortName;
      }
      
    }
    return null;
  }
}
