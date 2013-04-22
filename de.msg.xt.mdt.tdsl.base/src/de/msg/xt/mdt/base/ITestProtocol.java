package de.msg.xt.mdt.base;


public interface ITestProtocol {

	void newTest(String identifier);

	void newTest(String identifier, boolean generationLog);

	void append(String str);

	void append(String str, boolean generationLog);

	void appendControlOperationCall(String activityName, String fieldName, String fieldControlName, String operationName,
			String returnValue, String... parameter);

	void appendControlOperationCall(String activityName, String fieldName, String fieldControlName, String operationName,
			String returnValue, boolean generationLog, String... parameter);

	void appendActivityOperationCall(String activityName, String operationName, String returnValue, String... parameter);

	void appendActivityOperationCall(String activityName, String operationName, String returnValue, boolean generationLog,
			String... parameter);
}
