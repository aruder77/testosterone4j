package org.testosterone4j.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class Predicates {

	private static boolean evaluatePredicate(ControlField field, Class<?> predicateClass) {
		try {
			Method evaluateMethod = predicateClass.getMethod("evaluate",
					ControlField.class);
			return (Boolean) evaluateMethod.invoke(null, field);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean all(IEvaluationGroup group, Class<?> predicateClass) {
		for (ControlField field : group.getFields()) {
			if (!evaluatePredicate(field, predicateClass)) {
				return false;
			}
		}
		return true;
	}

	public static boolean atLeastOne(IEvaluationGroup group, Class<?> predicateClass) {
		for (ControlField field : group.getFields()) {
			if (evaluatePredicate(field, predicateClass)) {
				return true;
			}
		}
		return false;
	}
	
	public static Set<ControlField> fieldValuesWithTag(IEvaluationGroup group, Tag tag) {
		Set<ControlField> fields = new HashSet<ControlField>();
		for (ControlField field: group.getFields()) {
			DataType<?,? extends EquivalenceClass> lastEnteredValue = field.getLastEnteredValue();
			if (lastEnteredValue != null && lastEnteredValue.getEquivalenceClass().getClassTags().contains(tag)) {
				fields.add(field);
			}
		}
		return fields;
	}
	
	public static boolean hasFieldValueWithTag(IEvaluationGroup group, Tag tag) {
		return !fieldValuesWithTag(group, tag).isEmpty();
	}
}
