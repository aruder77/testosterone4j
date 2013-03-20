package de.msg.xt.mdt.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SimpleTestProtocol implements ITestProtocol {

    String testId;

    private PrintWriter printWriter;

    public SimpleTestProtocol(String testId) {
        this.testId = testId;
    }

    @Override
    public void open() throws IOException {
        this.printWriter = new PrintWriter(new File(this.testId + ".txt"));
    }

    @Override
    public void openLog(int testNumber) throws IOException {
        boolean append = (testNumber != 1);
        this.printWriter = new PrintWriter(new FileWriter(new File(this.testId + ".log"), append));
    }

    @Override
    public void newTest(String identifier) {
        this.printWriter.println();
        this.printWriter.println("==================================================================================");
        this.printWriter.println("Testcase " + this.testId + " : " + identifier);
        this.printWriter.println();
    }

    @Override
    public void close() {
        this.printWriter.close();
        this.printWriter = null;
    }

    @Override
    public void append(String str) {
        this.printWriter.println(str);
    }

    @Override
    public void appendControlOperationCall(String activityName, String fieldName, String fieldControlName, String operationName,
            String returnValue, String... parameter) {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(activityName).append("]     ");

        if (fieldControlName != null) {
            sb.append(fieldName).append(" [").append(fieldControlName).append("].");
        }

        sb.append(operationName).append("(");

        int idx = 0;
        for (Object param : parameter) {
            if (idx++ != 0) {
                sb.append(", ");
            }
            sb.append("\"").append(param).append("\"");
        }

        sb.append(")");
        if (returnValue != null) {
            sb.append(": \"").append(returnValue).append("\"");
        }
        this.append(sb.toString());
    }

    @Override
    public void appendActivityOperationCall(String activityName, String operationName, String returnValue, String... parameter) {
        this.appendControlOperationCall(activityName, null, null, operationName, returnValue, parameter);
    }

}
