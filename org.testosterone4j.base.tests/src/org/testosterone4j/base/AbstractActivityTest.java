package org.testosterone4j.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractActivityTest {
	
	AbstractActivity activity = null;
	private SampleActivity sampleActivity;
	private SampleSuperActivity sampleSuperActivity;
	private SampleChildActivity sampleChildActivity;
	private SampleActivityAdapter sampleActivityAdapter;

	@Before
	public void setUp() throws Exception {
		activity = new AbstractActivity() {		
			@Override
			public Set<ControlField> getFields() {
				return null;
			}
		};
		sampleActivity = new SampleActivity();
		sampleSuperActivity = new SampleSuperActivity();
		sampleChildActivity = new SampleChildActivity();
		sampleActivityAdapter = new SampleActivityAdapter();
	}

	/**
	 * tests, that if the adapter returns an activity which is of the same class as expected, it is returned
	 * no matter what the other parameters are.
	 */
	@Test
	public void testAdapterReturnsNewActivitySame() {
		parameterizedAdapterReturnsNewActivity("abc", SampleActivity.class, null, true);
		parameterizedAdapterReturnsNewActivity("abc", SampleActivity.class, null, false);
		parameterizedAdapterReturnsNewActivity("abc", SampleActivity.class, SampleActivityAdapter.class, true);
		parameterizedAdapterReturnsNewActivity("abc", SampleActivity.class, SampleActivityAdapter.class, false);
		parameterizedAdapterReturnsNewActivity(null, SampleActivity.class, null, true);
		parameterizedAdapterReturnsNewActivity(null, SampleActivity.class, null, false);
		parameterizedAdapterReturnsNewActivity(null, SampleActivity.class, SampleActivityAdapter.class, true);
		parameterizedAdapterReturnsNewActivity(null, SampleActivity.class, SampleActivityAdapter.class, false);
	}
	
	
	public void parameterizedAdapterReturnsNewActivity(String expectedId, Class<? extends AbstractActivity> expectedActivity, Class<? extends ActivityAdapter> expectedAdapterClass, boolean expectIrregularNextActivity) {
		// setup
		Function0<Object> call = Mockito.mock(Function0.class);
		when(call.apply()).thenReturn(sampleActivity);
		activity.adapter = sampleActivityAdapter;
		
		// execute
		AbstractActivity actualActivity = activity.callContextAdapter(call, expectedId, expectedActivity, expectedAdapterClass, expectIrregularNextActivity);
		
		// verify
		assertSame(sampleActivity, actualActivity);
	}
	
	/**
	 * tests, that if the adapter returns an activity which class is a child of the expected class, it is returned
	 * no matter what the other parameters are.
	 */
	@Test
	public void testAdapterReturnsNewActivityChild() {
			parameterizedAdapterReturnsNewActivity("abc", SampleSuperActivity.class, null, true);
			parameterizedAdapterReturnsNewActivity("abc", SampleSuperActivity.class, null, false);
			parameterizedAdapterReturnsNewActivity("abc", SampleSuperActivity.class, SampleActivityAdapter.class, true);
			parameterizedAdapterReturnsNewActivity("abc", SampleSuperActivity.class, SampleActivityAdapter.class, false);
			parameterizedAdapterReturnsNewActivity(null, SampleSuperActivity.class, null, true);
			parameterizedAdapterReturnsNewActivity(null, SampleSuperActivity.class, null, false);
			parameterizedAdapterReturnsNewActivity(null, SampleSuperActivity.class, SampleActivityAdapter.class, true);
			parameterizedAdapterReturnsNewActivity(null, SampleSuperActivity.class, SampleActivityAdapter.class, false);
	}

	/**
	 * tests that when an activity is returned by the adapter that is not a descendant of the expected 
	 * activity class, it is interpreted as context and a RuntimeException is thrown.
	 */
	@Test(expected=RuntimeException.class)
	public void testAdapterReturnsNewActivitySuper() {
		// setup
		Function0<Object> call = Mockito.mock(Function0.class);
		when(call.apply()).thenReturn(sampleActivity);
		activity.adapter = sampleActivityAdapter;
		sampleActivityAdapter.setContext(new IActivityContext() {
			@Override
			public String getId() {
				return "someId";
			}
		});
		
		// execute
		AbstractActivity actualActivity = activity.callContextAdapter(call, "abc", SampleChildActivity.class, SampleActivityAdapter.class, false);
		
		// verify RuntimeException since SampleActivity does not implement IActivityContext
	}
	
	
	
	static class SampleSuperActivity extends AbstractActivity {
		@Override
		public Set<ControlField> getFields() {
			return null;
		}
		
	}
	
	static class SampleActivity extends SampleSuperActivity {
	}
	
	static class SampleChildActivity extends SampleActivity {
	}
	
	static class SampleActivityAdapter implements ActivityAdapter {
		private Object context = null;
		
		@Override
		public void setContext(Object context) {
			this.context = context;
		}

		@Override
		public Object getContext() {
			return context;
		}
		
	}
}
