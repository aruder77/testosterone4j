package org.testosterone4j.base;

import java.util.List;

public interface ITestProtocol {

	void newTest(String identifier);

	void append(String str);

	void appendControlOperationCall(String activityName, String fieldName,
			String fieldControlName, String operationName, String returnValue,
			String... parameter);

	void appendActivityOperationCall(String activityName, String operationName,
			String returnValue, String... parameter);

	void appendSummary();

	void increaseIndentation();

	void decreaseIndentation();

	public void openGenerationFile();

	public void closeGenerationFile();

	public void addCoverageSummaryHeader();

	public void addCoverageSummary(int numberOfTests, double classCoverage,
			double matchingClassCoverage);

	public void addDataValueCoverageSummary(String dataValueId,
			int totalClasses, int totalMatchingClasses, int coveredClasses,
			Object[] values);

	<E extends Object, T extends EquivalenceClass> void addDataValueCoverageSummary(
			String dataValueId, int totalClasses, int totalMatchingClasses,
			int coveredClasses, List<DataType<E, T>> values);

}
