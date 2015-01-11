package org.testosterone4j.base.util;

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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.testosterone4j.base.AbstractActivity;
import org.testosterone4j.base.ActivityAdapter;

import com.google.inject.Injector;

public class TDslHelper {

	public static <T> T selectRandom(final Iterator<T> iterator) {
		final List<T> list = new ArrayList<T>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list.get(new Random(System.currentTimeMillis()).nextInt(list.size()));
	}

	public static <T extends AbstractActivity, E extends T, F extends ActivityAdapter> E castActivity(final Injector injector,
			final T activity, final Class<E> activityClass, final Class<F> adapterClass) {
		if (activityClass.isAssignableFrom(activity.getClass())) {
			return (E) activity;
		}

		// cast EditorActivity to PaketEditor
		F adapter = null;
		if (activity.getAdapter() != null) {
			final Object o = activity.getAdapter().getContext();
			adapter = injector.getInstance(adapterClass);
			adapter.setContext(o);
		}
		T newActivity = null;
		try {
			newActivity = activityClass.getConstructor(adapterClass).newInstance(adapter);
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		}
		return (E) newActivity;
	}
	
	public static <T extends AbstractActivity> String extractId(Class<T> activityClass) {
		String id = null;
		try {
			Field idField = activityClass.getDeclaredField("ID");
			id = (String) idField.get(null);
			if (id != null && id.trim().isEmpty()) {
				id = null;
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return id;
	}
}
