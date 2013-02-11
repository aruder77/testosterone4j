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

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SampleTestGenerator implements Generator {

    public static final Logger LOG = Logger.getLogger(SampleTestGenerator.class.getName());

    Map<String, Stack<Object>> remainingValuesPerId = new HashMap<String, Stack<Object>>();

    Set<String> unsatisfiedCoverageIds = new HashSet<String>();

    @Override
    public <E extends Runnable> List<E> generate(Class<E> clazz) {
        List<E> testCases = new ArrayList<E>();
        while (!this.unsatisfiedCoverageIds.isEmpty() || testCases.isEmpty()) {
            try {
                Constructor<E> constructor = clazz.getConstructor(Generator.class);
                E testCase = constructor.newInstance(this);
                testCase.run();
                testCases.add(testCase);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
                    | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return testCases;
    }

    @Override
    public <T extends DataType> T generateDataTypeValue(Class<T> clazz, String id, Tag[] tags) {
        Stack<Object> remainingValues = this.remainingValuesPerId.get(id);
        T dataType = null;
        try {
            dataType = clazz.newInstance();
            if (remainingValues == null) {
                this.unsatisfiedCoverageIds.add(id);
                remainingValues = new Stack<Object>();
                List<Object> list = Arrays.asList(getEquivalenceClasses(dataType));
                Collections.shuffle(list, new Random(System.currentTimeMillis()));
                remainingValues.addAll(list);
                this.remainingValuesPerId.put(id, remainingValues);
            }
            EquivalenceClass equivalenceClass = (EquivalenceClass) remainingValues.pop();
            dataType.setEquivalenceClass(equivalenceClass);
            dataType.setValue(equivalenceClass.getValue());
            if (remainingValues.isEmpty()) {
                this.unsatisfiedCoverageIds.remove(id);
                remainingValues.addAll(Arrays.asList(getEquivalenceClasses(dataType)));
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        LOG.fine("generatedValue[id=\"" + id + "\"]:" + dataType.getValue());
        return dataType;
    }

    private <T extends DataType> Object[] getEquivalenceClasses(T dataType) {
        Object[] values = null;
        try {
            Method valuesMethod = dataType.getEquivalenceClassEnum().getMethod("values", (Class[]) null);
            return (Object[]) valuesMethod.invoke(null, (Object[]) null);
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return values;
    }
}
