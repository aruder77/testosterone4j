package bne3.datatypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

public enum ChangeRequestTitleEquivalenceClass implements EquivalenceClass {
  normalTitle;
  
  public String getValue() {
    String value = null;
    switch (this) {
      case normalTitle:
        value = new Function0<String>() {
          public String apply() {
            long _currentTimeMillis = System.currentTimeMillis();
            String _plus = ("ChangeRequest" + Long.valueOf(_currentTimeMillis));
            return _plus;
          }
        }.apply();
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case normalTitle:
    		tags = new Tag[] {  };
    		break;
    }
    return tags;
  }
  
  public static ChangeRequestTitleEquivalenceClass getByValue(final String value) {
    bne3.datatypes.ChangeRequestTitleEquivalenceClass clazz = null;
    if (value != null) {
      if (new Function0<Function1<String,Boolean>>() {
        public Function1<String,Boolean> apply() {
          final Function1<String,Boolean> _function = new Function1<String,Boolean>() {
              public Boolean apply(final String e) {
                boolean _startsWith = e.startsWith("ChangeRequest");
                return _startsWith;
              }
            };
          return _function;
        }
      }.apply().apply(value)) {
      	return normalTitle;
      }
    }
    return null;
  }
}
