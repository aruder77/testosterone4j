package bne3.datatypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;
import de.msg.xt.mdt.base.util.TDslHelper;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

public enum KategorieDTEquivalenceClass implements EquivalenceClass {
  wert;
  
  public String getValue() {
    String value = null;
    switch (this) {
      case wert:
        Iterable<String> wertIterable = new Function0<Iterable<String>>() {
          public Iterable<String> apply() {
            ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList("END_OF_PDU", "LEADING_LENGTH_INFO_TYPE", "MIN_MAX_LENGTH_TYPE", "STANDARD_LENGTH_TYPE");
            return _newArrayList;
          }
        }.apply();
        value = TDslHelper.selectRandom(wertIterable.iterator());;
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case wert:
    		tags = new Tag[] {  };
    		break;
    }
    return tags;
  }
  
  public static KategorieDTEquivalenceClass getByValue(final String value) {
    bne3.datatypes.KategorieDTEquivalenceClass clazz = null;
    if (value != null) {
      Iterable<String> wertIterable = new Function0<Iterable<String>>() {
        public Iterable<String> apply() {
          ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList("END_OF_PDU", "LEADING_LENGTH_INFO_TYPE", "MIN_MAX_LENGTH_TYPE", "STANDARD_LENGTH_TYPE");
          return _newArrayList;
        }
      }.apply();
      Iterator<String> wertIterator = wertIterable.iterator();while(wertIterator.hasNext()) {
      	if (value.equals(wertIterator.next())) {
      		return wert;
      	}
      }
    }
    return null;
  }
}
