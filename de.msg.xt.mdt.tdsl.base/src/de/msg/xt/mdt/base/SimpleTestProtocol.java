package de.msg.xt.mdt.base;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class SimpleTestProtocol implements ITestProtocol {

	String testId;

	private PrintWriter printWriter;

	private static final Logger LOG = Logger.getLogger(SimpleTestProtocol.class
			.getName());
	private static final Logger GENERATION_LOG = Logger
			.getLogger("de.msg.xt.mdt.GenerationLog");

	private static StringBuffer buffer = null;

	public SimpleTestProtocol(final String testId) {
		this.testId = testId;
	}

	@Override
	public void newTest(final String identifier, final boolean generationLog) {
		buffer = new StringBuffer();
		log("\n==================================================================================",
				generationLog);
		log("Testcase " + testId + " : " + identifier + "\n", generationLog);
	}

	@Override
	public void append(final String str, final boolean generationLog) {
		log(str, generationLog);
	}

	@Override
	public void appendControlOperationCall(final String activityName,
			final String fieldName, final String fieldControlName,
			final String operationName, final String returnValue,
			final boolean generationLog, final String... parameter) {
		final StringBuffer sb = new StringBuffer();
		sb.append("[").append(activityName).append("]     ");

		if (fieldControlName != null) {
			sb.append(fieldName).append(" [").append(fieldControlName)
					.append("].");
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
	public void appendActivityOperationCall(final String activityName,
			final String operationName, final String returnValue,
			final boolean generationLog, final String... parameter) {
		appendControlOperationCall(activityName, null, null, operationName,
				returnValue, parameter);
	}

	private void log(final String str, final boolean generationLog) {
		if (generationLog) {
			GENERATION_LOG.fine(str + "\n");
		} else {
			if (buffer != null) {
				buffer.append(str + "\n");
			}
			LOG.fine(str + "\n");
		}
	}

	@Override
	public void newTest(final String identifier) {
		newTest(identifier, false);
	}

	@Override
	public void append(final String str) {
		append(str, false);
	}

	@Override
	public void appendControlOperationCall(final String activityName,
			final String fieldName, final String fieldControlName,
			final String operationName, final String returnValue,
			final String... parameter) {
		appendControlOperationCall(activityName, fieldName, fieldControlName,
				operationName, returnValue, false, parameter);
	}

	@Override
	public void appendActivityOperationCall(final String activityName,
			final String operationName, final String returnValue,
			final String... parameter) {
		appendActivityOperationCall(activityName, operationName, returnValue,
				false, parameter);
	}

	@Override
	public void appendSummary() {
		append("==================================================================================\n");
		LOG.info("TEST CASE SUMMARY:\n");
		LOG.info(buffer.toString());
	}

}
