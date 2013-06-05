package de.msg.xt.mdt.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import javax.inject.Inject;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SampleTestGenerator implements Generator {

	public static final Logger LOG = Logger.getLogger(SampleTestGenerator.class
			.getName());

	Map<String, Stack<EquivalenceClass>> remainingValuesPerId = new HashMap<String, Stack<EquivalenceClass>>();

	Set<String> unsatisfiedCoverageIds = new HashSet<String>();

	@Inject
	ITestProtocol protocol;

	@Inject
	ActivityLocator locator;

	private Set<Tag> tags;

	private Set<Tag> excludeTags;

	@Override
	public <E extends Runnable> List<E> generate(final Class<E> clazz) {
		final List<E> testCases = new ArrayList<E>();
		int idx = 1;
		protocol.openGenerationFile();
		while (!unsatisfiedCoverageIds.isEmpty() || testCases.isEmpty()) {
			try {
				final Constructor<E> constructor = clazz
						.getConstructor(Generator.class);
				final E testCase = constructor.newInstance(this);
				protocol.newTest(String.valueOf(idx++));

				locator.beforeTest();
				try {
					testCase.run();
				} finally {
					locator.afterTest();
				}

				testCases.add(testCase);
			} catch (final InstantiationException e) {
				e.printStackTrace();
			} catch (final SecurityException e) {
				e.printStackTrace();
			} catch (final NoSuchMethodException e) {
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		protocol.closeGenerationFile();
		return testCases;
	}

	@Override
	public <T extends DataType> T generateDataTypeValue(final Class<T> clazz,
			final String id, final Tag[] tags) {
		Stack<EquivalenceClass> remainingValues = remainingValuesPerId.get(id);
		T dataType = null;
		try {
			dataType = clazz.newInstance();
			if (remainingValues == null) {
				unsatisfiedCoverageIds.add(id);
				remainingValues = new Stack<EquivalenceClass>();
				determineEquivalenceClassList(remainingValues, dataType);
				remainingValuesPerId.put(id, remainingValues);
			}
			final EquivalenceClass equivalenceClass = remainingValues.pop();
			dataType.setEquivalenceClass(equivalenceClass);
			dataType.setValue(equivalenceClass.getValue());
			if (remainingValues.isEmpty()) {
				unsatisfiedCoverageIds.remove(id);
				determineEquivalenceClassList(remainingValues, dataType);
			}
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		LOG.fine("generatedValue[id=\"" + id + "\"]:" + dataType.getValue());
		return dataType;
	}

	private <T extends DataType> void determineEquivalenceClassList(
			final Stack<EquivalenceClass> remainingValues, final T dataType) {
		final List<Object> list = Arrays
				.asList(getEquivalenceClasses(dataType));
		Collections.shuffle(list, new Random(System.currentTimeMillis()));
		for (final Object o : list) {
			final EquivalenceClass ec = (EquivalenceClass) o;
			if (checkTagCompliance(ec.getTags())) {
				remainingValues.add(ec);
			}
		}
	}

	private boolean checkTagCompliance(final Tag[] tags) {
		boolean result = true;
		if (this.tags != null) {
			result = false;
			for (final Tag tag : tags) {
				if (this.tags.contains(tag)) {
					result = true;
					break;
				}
			}
		} else if (excludeTags != null) {
			for (final Tag tag : tags) {
				if (excludeTags.contains(tag)) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	private <T extends DataType> Object[] getEquivalenceClasses(final T dataType) {
		final Object[] values = null;
		try {
			final Method valuesMethod = dataType.getEquivalenceClassEnum()
					.getMethod("values", (Class[]) null);
			return (Object[]) valuesMethod.invoke(null, (Object[]) null);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}
		return values;
	}

	@Override
	public void setTags(final Tag[] tags) {
		this.tags = new HashSet<Tag>();
		this.tags.addAll(Arrays.asList(tags));
		excludeTags = null;
	}

	@Override
	public void setExcludeTags(final Tag[] excludeTags) {
		tags = null;
		this.excludeTags = new HashSet<Tag>();
		this.excludeTags.addAll(Arrays.asList(excludeTags));
	}
}
