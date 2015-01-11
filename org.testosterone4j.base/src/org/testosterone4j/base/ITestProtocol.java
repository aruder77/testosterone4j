package org.testosterone4j.base;

/*
 * #%L
 * org.testosterone4j.base
 * %%
 * Copyright (C) 2015 Axel Ruder
 * %%
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * #L%
 */

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
