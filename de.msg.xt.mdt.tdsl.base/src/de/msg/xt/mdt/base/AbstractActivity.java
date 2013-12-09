package de.msg.xt.mdt.base;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.xtext.xbase.lib.Functions.Function0;

import com.google.inject.Injector;

public abstract class AbstractActivity implements IEvalutaionGroup {

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

	protected <T extends AbstractActivity, E extends ActivityAdapter> T callContextAdapter(
			final Function0<Object> call, final Class<T> expectedResultType,
			final Class<E> adapterClass) {
		Object o = null;
		if (adapter != null) {
			o = call.apply();
		}
		T nextActivity = null;
		if ((o != null) && expectedResultType.isAssignableFrom(o.getClass())) {
			nextActivity = (T) o;
		} else {
			E adapter = null;
			if ((adapterClass != null) && (this.adapter != null)) {
				if (o == null) {
					adapter = getInjector().getInstance(ActivityLocator.class)
							.find(expectedResultType, adapterClass);
				} else {
					adapter = getInjector().getInstance(adapterClass);
					adapter.setContext(o);
				}
			}
			try {
				nextActivity = expectedResultType.getConstructor(adapterClass)
						.newInstance(adapter);
			} catch (final InstantiationException e) {
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
			} catch (final NoSuchMethodException e) {
				e.printStackTrace();
			} catch (final SecurityException e) {
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
