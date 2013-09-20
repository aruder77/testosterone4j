package de.msg.xt.mdt.base;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.xtext.xbase.lib.Functions.Function0;

import com.google.inject.Injector;

public class AbstractActivity {

	protected ActivityAdapter adapter;

	public AbstractActivity() {
		this(null);
	}

	public AbstractActivity(final ActivityAdapter adapter) {
		this.adapter = adapter;
	}

	public ActivityAdapter getAdapter() {
		return adapter;
	}

	protected <T, E extends ActivityAdapter> T callContextAdapter(
			Function0<Object> call, Class<T> expectedResultType,
			Class<E> adapterClass) {
		Object o = null;
		if (adapter != null) {
			o = call.apply();
		}
		T nextActivity = null;
		if (expectedResultType.isAssignableFrom(o.getClass())) {
			nextActivity = (T) o;
		} else {
			E adapter = null;
			if (adapter != null) {
				adapter = getInjector().getInstance(adapterClass);
				adapter.setContext(o);
			}
			try {
				nextActivity = expectedResultType.getConstructor(adapterClass)
						.newInstance(adapter);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return nextActivity;
	}

	protected Injector getInjector() {
		throw new UnsupportedOperationException(
				"getInjector() must be implemented by implementing classes!");
	}

}
