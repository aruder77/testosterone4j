package org.testosterone4j.base;

/*
 * #%L
 * org.testosterone4j.base
 * %%
 * Copyright (C) 2015 Axel Ruder
 * %%
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * #L%
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class Predicates {

	private static boolean evaluatePredicate(final ControlField field, final Class<?> predicateClass) {
		try {
			final Method evaluateMethod = predicateClass.getMethod("evaluate", ControlField.class);
			return (Boolean) evaluateMethod.invoke(null, field);
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean all(final IEvaluationGroup group, final Class<?> predicateClass) {
		for (final ControlField field : group.getFields()) {
			if (!evaluatePredicate(field, predicateClass)) {
				return false;
			}
		}
		return true;
	}

	public static boolean atLeastOne(final IEvaluationGroup group, final Class<?> predicateClass) {
		for (final ControlField field : group.getFields()) {
			if (evaluatePredicate(field, predicateClass)) {
				return true;
			}
		}
		return false;
	}

	public static Set<ControlField> fieldValuesWithTag(final IEvaluationGroup group, final Tag tag) {
		final Set<ControlField> fields = new HashSet<ControlField>();
		for (final ControlField field : group.getFields()) {
			final DataType<?, ? extends EquivalenceClass> lastEnteredValue = field.getLastEnteredValue();
			if ((lastEnteredValue != null) && (lastEnteredValue.getEquivalenceClass() != null)
					&& lastEnteredValue.getEquivalenceClass().getClassTags().contains(tag)) {
				fields.add(field);
			}
		}
		return fields;
	}

	public static boolean hasFieldValueWithTag(final IEvaluationGroup group, final Tag tag) {
		return !fieldValuesWithTag(group, tag).isEmpty();
	}
}
