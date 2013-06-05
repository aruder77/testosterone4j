package de.msg.xt.mdt.base;


public interface ITestProtocol {

	void newTest(String identifier);

	void append(String str);

	void appendControlOperationCall(String activityName, String fieldName,
			String fieldControlName, String operationName, String returnValue,
			String... parameter);

	void appendActivityOperationCall(String activityName, String operationName,
			String returnValue, String... parameter);

	void appendSummary();

	public void openGenerationFile();

	public void closeGenerationFile();

}
