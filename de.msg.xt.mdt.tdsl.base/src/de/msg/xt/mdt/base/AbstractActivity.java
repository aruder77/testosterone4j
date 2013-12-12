package de.msg.xt.mdt.base;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.xtext.xbase.lib.Functions.Function0;

import com.google.inject.Injector;

import de.msg.xt.mdt.base.util.TDslHelper;

public abstract class AbstractActivity implements IEvaluationGroup {

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

	/**
	 * Makes the call to the activity adapter and converts the {@link Object} returned to the expected activity.
	 * If the adapter returned object is already an {@link AbstractActivity}, it is returned directly.
	 * Otherwise, it is considered to be a context object for the activity adapter and wrapped in
	 * an appropriate adapter and activity by this method. The expected activity and adapter class must be
	 * given to this method as parameters. 
	 * If the given expectedId is not null, it is verified that the context object returned by the adapter
	 * matches this id. If not, the context object is ignored and the expected activity is looked for using
	 * {@link ActivityLocator}.
	 * 
	 * @param call			a callback to make the actual call to the activity adapter
	 * @param expectedId	the id of the activity expected as next activity for the call
	 * @param expectedActivity	the class of the activity expected as next activity 
	 * @param adapterClass	the adapter class of the activity expected as next activity
	 * @return the next activity 
	 */
	protected <T extends AbstractActivity, E extends ActivityAdapter> T callContextAdapter(
			final Function0<Object> call, final String expectedId, final Class<T> expectedActivity,
			final Class<E> adapterClass, boolean expectIrregularNextActivity) {
		Object o = null;
		RuntimeException exception = null;
		if (adapter != null) {
			try {
				o = call.apply();
			} catch (RuntimeException ex) {
				if (expectIrregularNextActivity) {
					// expect exception in this case, do nothing
					o = null;
				} else {
					throw ex;
				}
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
				
				if (context == null || context.getId() == null || expectIrregularNextActivity || (expectedId != null && !context.getId().equals(expectedId))) {
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
