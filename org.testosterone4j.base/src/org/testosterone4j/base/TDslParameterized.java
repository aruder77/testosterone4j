package org.testosterone4j.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * <p>
 * The custom runner <code>Parameterized</code> implements parameterized tests. When running a parameterized test class, instances
 * are created for the cross-product of the test methods and the test data elements.
 * </p>
 * 
 * For example, to test a Fibonacci function, write:
 * 
 * <pre>
 * &#064;RunWith(Parameterized.class)
 * public class FibonacciTest {
 * 	&#064;Parameters
 * 	public static List&lt;Object[]&gt; data() {
 * 		return Arrays.asList(new Object[][] { { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 } });
 * 	}
 * 
 * 	private int fInput;
 * 
 * 	private int fExpected;
 * 
 * 	public FibonacciTest(int input, int expected) {
 * 		fInput = input;
 * 		fExpected = expected;
 * 	}
 * 
 * 	&#064;Test
 * 	public void test() {
 * 		assertEquals(fExpected, Fibonacci.compute(fInput));
 * 	}
 * }
 * </pre>
 * 
 * <p>
 * Each instance of <code>FibonacciTest</code> will be constructed using the two-argument constructor and the data values in the
 * <code>&#064;Parameters</code> method.
 * </p>
 */
public class TDslParameterized extends Suite {

	private class TestClassRunnerForParameters extends BlockJUnit4ClassRunner {
		private final int fParameterSetNumber;

		private final List<Object[]> fParameterList;

		TestClassRunnerForParameters(final Class<?> type, final List<Object[]> parameterList, final int i)
				throws InitializationError {
			super(type);
			fParameterList = parameterList;
			fParameterSetNumber = i;
		}

		@Override
		public Object createTest() throws Exception {
			return getTestClass().getOnlyConstructor().newInstance(computeParams());
		}

		private Object[] computeParams() throws Exception {
			try {
				return fParameterList.get(fParameterSetNumber);
			} catch (final ClassCastException e) {
				throw new Exception(String.format("%s.%s() must return a Collection of arrays.", getTestClass().getName(),
						getParametersMethod(getTestClass()).getName()));
			}
		}

		@Override
		protected String getName() {
			return String.format("[%s]", fParameterSetNumber);
		}

		@Override
		protected String testName(final FrameworkMethod method) {
			return String.format("%s[%s]", method.getName(), fParameterSetNumber);
		}

		@Override
		protected void validateConstructor(final List<Throwable> errors) {
			validateOnlyOneConstructor(errors);
		}

		@Override
		protected Statement classBlock(final RunNotifier notifier) {
			return childrenInvoker(notifier);
		}

		@Override
		protected Annotation[] getRunnerAnnotations() {
			return new Annotation[0];
		}

		@Override
		public void run(final RunNotifier notifier) {
			final RunListener failureSpy = new ScreenshotCaptureListener();
			notifier.removeListener(failureSpy); // remove existing listeners that could be added by suite or class runners
			notifier.addListener(failureSpy);
			try {
				super.run(notifier);
			} finally {
				notifier.removeListener(failureSpy);
			}
		}
	}

	private final ArrayList<Runner> runners = new ArrayList<Runner>();

	/**
	 * Only called reflectively. Do not use programmatically.
	 */
	public TDslParameterized(final Class<?> klass) throws Throwable {
		super(klass, Collections.<Runner> emptyList());
		final List<Object[]> parametersList = getParametersList(getTestClass());
		for (int i = 0; i < parametersList.size(); i++) {
			runners.add(new TestClassRunnerForParameters(getTestClass().getJavaClass(), parametersList, i));
		}
	}

	@Override
	protected List<Runner> getChildren() {
		return runners;
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getParametersList(final TestClass klass) throws Throwable {
		return (List<Object[]>) getParametersMethod(klass).invokeExplosively(null);
	}

	private FrameworkMethod getParametersMethod(final TestClass testClass) throws Exception {
		final List<FrameworkMethod> methods = testClass.getAnnotatedMethods(Parameters.class);
		for (final FrameworkMethod each : methods) {
			final int modifiers = each.getMethod().getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
				return each;
			}
		}

		throw new Exception("No public static parameters method on class " + testClass.getName());
	}

}
