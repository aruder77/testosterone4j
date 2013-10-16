package de.msg.xt.mdt.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Predicates {

	private static boolean evaluatePredicate(Class<?> predicateClass,
			ControlField field) {
		try {
			Method evaluateMethod = predicateClass.getMethod("evaluate",
					ControlField.class);
			return (Boolean) evaluateMethod.invoke(field);
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

	public static boolean all(Class<?> predicateClass, IEvalutaionGroup group) {
		for (ControlField field : group.getFields()) {
			if (!evaluatePredicate(predicateClass, field)) {
				return false;
			}
		}
		return true;
	}

	public static boolean atLeastOne(Class<?> predicateClass,
			IEvalutaionGroup group) {
		for (ControlField field : group.getFields()) {
			if (evaluatePredicate(predicateClass, field)) {
				return true;
			}
		}
		return false;
	}
}
