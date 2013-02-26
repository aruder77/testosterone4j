package de.msg.xt.mdt.base;

import java.io.IOException;

public interface ITestProtocol {

    void open() throws IOException;

    void openLog(int testNumber) throws IOException;

    void close();

    void newTest(String identifier);

    void append(String str);

    void appendControlOperationCall(String activityName, String fieldName, String fieldControlName, String operationName,
            String returnValue, String... parameter);

    void appendActivityOperationCall(String activityName, String operationName, String returnValue, String... parameter);
}
