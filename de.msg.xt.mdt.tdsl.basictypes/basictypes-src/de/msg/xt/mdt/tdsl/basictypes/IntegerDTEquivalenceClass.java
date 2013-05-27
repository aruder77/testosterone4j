package de.msg.xt.mdt.tdsl.basictypes;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;
import de.msg.xt.mdt.base.util.TDslHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;

public enum IntegerDTEquivalenceClass implements EquivalenceClass {
  minusMinus,

  minus,

  zero,

  small,

  big;
  
  public Integer getValue() {
    Integer value = null;
    switch (this) {
      case minusMinus:
        Iterable<Integer> minusMinusIterable = new Function0<Iterable<Integer>>() {
          public Iterable<Integer> apply() {
            int _minus = (-1000);
            int _minus_1 = (-1500);
            int _minus_2 = (-100);
            ArrayList<Integer> _newArrayList = CollectionLiterals.<Integer>newArrayList(Integer.valueOf(_minus), Integer.valueOf(_minus_1), Integer.valueOf(_minus_2));
            return _newArrayList;
          }
        }.apply();
        value = TDslHelper.selectRandom(minusMinusIterable.iterator());;
        break;
      case minus:
        Iterable<Integer> minusIterable = new Function0<Iterable<Integer>>() {
          public Iterable<Integer> apply() {
            int _minus = (-99);
            int _minus_1 = (-1);
            IntegerRange _upTo = new IntegerRange(_minus, _minus_1);
            return _upTo;
          }
        }.apply();
        value = TDslHelper.selectRandom(minusIterable.iterator());;
        break;
      case zero:
        value = 0;
        break;
      case small:
        value = new Function0<Integer>() {
          public Integer apply() {
            long _currentTimeMillis = System.currentTimeMillis();
            Random _random = new Random(_currentTimeMillis);
            int _nextInt = _random.nextInt(99);
            int _plus = (_nextInt + 1);
            return _plus;
          }
        }.apply();
        break;
      case big:
        value = 310052345;
        break;
    }
    return value;
  }
  
  public Tag[] getTags() {
    Tag[]tags = null;
    switch (this) {
    	case minusMinus:
    		tags = new Tag[] {  };
    		break;
    	case minus:
    		tags = new Tag[] {  };
    		break;
    	case zero:
    		tags = new Tag[] {  };
    		break;
    	case small:
    		tags = new Tag[] {  };
    		break;
    	case big:
    		tags = new Tag[] {  };
    		break;
    }
    return tags;
  }
  
  public static IntegerDTEquivalenceClass getByValue(final Integer value) {
    de.msg.xt.mdt.tdsl.basictypes.IntegerDTEquivalenceClass clazz = null;
    if (value != null) {
      Iterable<Integer> minusMinusIterable = new Function0<Iterable<Integer>>() {
        public Iterable<Integer> apply() {
          int _minus = (-1000);
          int _minus_1 = (-1500);
          int _minus_2 = (-100);
          ArrayList<Integer> _newArrayList = CollectionLiterals.<Integer>newArrayList(Integer.valueOf(_minus), Integer.valueOf(_minus_1), Integer.valueOf(_minus_2));
          return _newArrayList;
        }
      }.apply();
      Iterator<Integer> minusMinusIterator = minusMinusIterable.iterator();while(minusMinusIterator.hasNext()) {
      	if (value.equals(minusMinusIterator.next())) {
      		return minusMinus;
      	}
      }Iterable<Integer> minusIterable = new Function0<Iterable<Integer>>() {
        public Iterable<Integer> apply() {
          int _minus = (-99);
          int _minus_1 = (-1);
          IntegerRange _upTo = new IntegerRange(_minus, _minus_1);
          return _upTo;
        }
      }.apply();
      Iterator<Integer> minusIterator = minusIterable.iterator();while(minusIterator.hasNext()) {
      	if (value.equals(minusIterator.next())) {
      		return minus;
      	}
      }if (value.equals(0)) {
      	return zero;
      }
      if (new Function0<Function1<Integer,Boolean>>() {
        public Function1<Integer,Boolean> apply() {
          final Function1<Integer,Boolean> _function = new Function1<Integer,Boolean>() {
              public Boolean apply(final Integer e) {
                boolean _and = false;
                boolean _greaterThan = ((e).intValue() > 0);
                if (!_greaterThan) {
                  _and = false;
                } else {
                  boolean _lessThan = ((e).intValue() < 100);
                  _and = (_greaterThan && _lessThan);
                }
                return _and;
              }
            };
          return _function;
        }
      }.apply().apply(value)) {
      	return small;
      }if (value.equals(310052345)) {
      	return big;
      }
      
    }
    return null;
  }
}
