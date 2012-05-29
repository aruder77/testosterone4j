package test.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import test.usecase.SampleUseCase;
import de.msg.xt.mdt.StringDT;
import de.msg.xt.mdt.StringDTEquivalenceClass;

public class SampleTestGenerator implements Generator {
	
	Map<String, Stack<Object>> remainingValuesPerId = new HashMap<String, Stack<Object>>();
	
	Set<String> unsatisfiedCoverageIds = new HashSet<String>();
	
	public List<SampleUseCase> generate() {
		List<SampleUseCase> testCases = new ArrayList<SampleUseCase>();
		while (!isFinished()) {
			SampleUseCase testCase = new SampleUseCase(this);
			testCase.run();
			testCases.add(testCase);
		}
		return testCases;
	}

	@Override
	public StringDT generateStringDT(String id) {
		Stack<Object> remainingValues = remainingValuesPerId.get(id);
		if (remainingValues == null) {
			unsatisfiedCoverageIds.add(id);
			remainingValues = new Stack<Object>();
			remainingValues.addAll(StringDTEquivalenceClass.INSTANCE.values());
			remainingValuesPerId.put(id, remainingValues);
		}
		StringDTEquivalenceClass clazz = (StringDTEquivalenceClass) remainingValues.pop();
		if (remainingValues.isEmpty()) {
			unsatisfiedCoverageIds.remove(id);
			remainingValues.addAll(StringDTEquivalenceClass.INSTANCE.values());
		}
		return new StringDT(clazz.getValue(), clazz);
	}
	
	public boolean isFinished() {
		return unsatisfiedCoverageIds.isEmpty() && !remainingValuesPerId.isEmpty();
	}

}
