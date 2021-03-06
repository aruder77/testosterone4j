package bne3.datatypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;
import de.msg.xt.mdt.base.util.TDslHelper;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

public enum DoubleStringDTEquivalenceClass implements EquivalenceClass {
  string,

  negative,

  zero,

  positive;
  
  public String getValue() {
    String value = null;
    switch (this) {
      case string:
        value = "ABC";
        break;
      case negative:
        Iterable<String> negativeIterable = new Function0<Iterable<String>>() {
          public Iterable<String> apply() {
            ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList("-0.5", "-100.3", "-1.0354");
            return _newArrayList;
          }
        }.apply();
        value = TDslHelper.selectRandom(negativeIterable.iterator());;
        break;
      case zero:
        value = "0.0";
        break;
      case positive:
        Iterable<String> positiveIterable = new Function0<Iterable<String>>() {
          public Iterable<String> apply() {
            ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList("1.0345", "53.3453", "12346754.0978908");
            return _newArrayList;
          }
        }.apply();
        value = TDslHelper.selectRandom(positiveIterable.iterator());;
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case string:
    		tags = new Tag[] { de.msg.xt.mdt.tdsl.basictypes.Tags.Invalid };
    		break;
    	case negative:
    		tags = new Tag[] {  };
    		break;
    	case zero:
    		tags = new Tag[] {  };
    		break;
    	case positive:
    		tags = new Tag[] {  };
    		break;
    }
    return tags;
  }
  
  public static DoubleStringDTEquivalenceClass getByValue(final String value) {
    bne3.datatypes.DoubleStringDTEquivalenceClass clazz = null;
    if (value != null) {
      if (value.equals("ABC")) {
      	return string;
      }
      Iterable<String> negativeIterable = new Function0<Iterable<String>>() {
        public Iterable<String> apply() {
          ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList("-0.5", "-100.3", "-1.0354");
          return _newArrayList;
        }
      }.apply();
      Iterator<String> negativeIterator = negativeIterable.iterator();while(negativeIterator.hasNext()) {
      	if (value.equals(negativeIterator.next())) {
      		return negative;
      	}
      }if (value.equals("0.0")) {
      	return zero;
      }
      Iterable<String> positiveIterable = new Function0<Iterable<String>>() {
        public Iterable<String> apply() {
          ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList("1.0345", "53.3453", "12346754.0978908");
          return _newArrayList;
        }
      }.apply();
      Iterator<String> positiveIterator = positiveIterable.iterator();while(positiveIterator.hasNext()) {
      	if (value.equals(positiveIterator.next())) {
      		return positive;
      	}
      }
    }
    return null;
  }
}
