package de.msg.xt.mdt.base;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.xtext.xbase.lib.Functions.Function0;

import com.google.inject.Injector;

import de.msg.xt.mdt.base.util.TDslHelper;

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
			final Function0<Object> call, final String expectedId, final Class<T> expectedActivity,
			final Class<E> adapterClass) {
		Object o = null;
		boolean exceptionOccured = false;
		if (adapter != null) {
			try {
				o = call.apply();
			} catch (RuntimeException ex) {
				ex.printStackTrace();
				exceptionOccured = true;
			}
		}
		T nextActivity = null;
		if ((o != null) && expectedActivity.isAssignableFrom(o.getClass())) {
			nextActivity = (T) o;
		} else {
			E adapter = null;
			if ((adapterClass != null) && (this.adapter != null)) {
				if (o != null && !(o instanceof IActivityContext)) {
					throw new RuntimeException("context object must implement IActivityContext!");
				}
				IActivityContext context = (IActivityContext)o;
				
				if (context == null || context.getId() == null || exceptionOccured || (expectedId != null && !context.getId().equals(expectedId))) {
					adapter = getInjector().getInstance(ActivityLocator.class)
							.find(expectedActivity, adapterClass);
				} else {
					adapter = getInjector().getInstance(adapterClass);
					adapter.setContext(o);
				}
			}
			try {
				nextActivity = expectedActivity.getConstructor(adapterClass)
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
