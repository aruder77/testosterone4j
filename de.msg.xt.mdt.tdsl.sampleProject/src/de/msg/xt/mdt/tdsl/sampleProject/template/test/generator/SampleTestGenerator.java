package de.msg.xt.mdt.tdsl.sampleProject.template.test.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype.StringDT;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype.StringDTEquivalenceClass;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.usecase.SampleUseCase;

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
        Stack<Object> remainingValues = this.remainingValuesPerId.get(id);
        if (remainingValues == null) {
            this.unsatisfiedCoverageIds.add(id);
            remainingValues = new Stack<Object>();
            remainingValues.addAll(StringDTEquivalenceClass.INSTANCE.values());
            this.remainingValuesPerId.put(id, remainingValues);
        }
        StringDTEquivalenceClass clazz = (StringDTEquivalenceClass) remainingValues.pop();
        if (remainingValues.isEmpty()) {
            this.unsatisfiedCoverageIds.remove(id);
            remainingValues.addAll(StringDTEquivalenceClass.INSTANCE.values());
        }
        return new StringDT(clazz.getValue(), clazz);
    }

    public boolean isFinished() {
        return this.unsatisfiedCoverageIds.isEmpty() && !this.remainingValuesPerId.isEmpty();
    }

}
