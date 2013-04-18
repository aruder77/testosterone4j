package de.msg.xt.mdt.base;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class SimpleTestProtocol implements ITestProtocol {

	String testId;

	private PrintWriter printWriter;

	private static final Logger LOG = Logger.getLogger(SimpleTestProtocol.class.getName());

	public SimpleTestProtocol(final String testId) {
		this.testId = testId;
	}

	@Override
	public void open() throws IOException {
		printWriter = new PrintWriter(new File(testId + ".txt"));
	}

	@Override
	public void newTest(final String identifier) {
		log("\n==================================================================================");
		log("Testcase " + testId + " : " + identifier + "\n");
	}

	@Override
	public void close() {
		printWriter.close();
		printWriter = null;
	}

	@Override
	public void append(final String str) {
		log(str);
	}

	@Override
	public void appendControlOperationCall(final String activityName, final String fieldName, final String fieldControlName,
			final String operationName, final String returnValue, final String... parameter) {
		final StringBuffer sb = new StringBuffer();
		sb.append("[").append(activityName).append("]     ");

		if (fieldControlName != null) {
			sb.append(fieldName).append(" [").append(fieldControlName).append("].");
		}

		sb.append(operationName).append("(");

		int idx = 0;
		for (final Object param : parameter) {
			if (idx++ != 0) {
				sb.append(", ");
			}
			sb.append("\"").append(param).append("\"");
		}

		sb.append(")");
		if (returnValue != null) {
			sb.append(": \"").append(returnValue).append("\"");
		}
		append(sb.toString());
	}

	@Override
	public void appendActivityOperationCall(final String activityName, final String operationName, final String returnValue,
			final String... parameter) {
		appendControlOperationCall(activityName, null, null, operationName, returnValue, parameter);
	}

	private void log(final String str) {
		LOG.info(str + "\n");
	}

}
