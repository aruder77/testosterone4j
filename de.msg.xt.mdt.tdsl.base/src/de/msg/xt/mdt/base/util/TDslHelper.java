package de.msg.xt.mdt.base.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.inject.Injector;

import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityAdapter;

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
}
