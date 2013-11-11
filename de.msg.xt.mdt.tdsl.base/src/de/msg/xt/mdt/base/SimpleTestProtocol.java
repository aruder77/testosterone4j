package de.msg.xt.mdt.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

public class SimpleTestProtocol implements ITestProtocol {

	private static final int INDENT_STEP = 4;

	String testId;

	private PrintWriter printWriter;

	private static final Logger LOG = Logger.getLogger(SimpleTestProtocol.class
			.getName());
	private static final Logger GENERATION_LOG = Logger
			.getLogger("de.msg.xt.mdt.GenerationLog");

	private static StringBuffer buffer = null;

	private PrintWriter generationLogger;

	private boolean generationMode = false;

	private int indent = 0;

	public SimpleTestProtocol(final String testId) {
		this.testId = testId;
	}

	@Override
	public void newTest(final String identifier) {
		buffer = new StringBuffer();
		log("\n==================================================================================");
		log("Testcase " + testId + " : " + identifier + "\n");
	}

	@Override
	public void append(final String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indent; i++) {
			sb.append(' ');
		}
		log(sb.toString() + str);
	}

	@Override
	public void appendControlOperationCall(final String activityName,
			final String fieldName, final String fieldControlName,
			final String operationName, final String returnValue,
			final String... parameter) {
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
			final String... parameter) {
		appendControlOperationCall(activityName, null, null, operationName,
				returnValue, parameter);
	}

	private void log(final String str) {
		if (this.generationMode) {
			GENERATION_LOG.fine(str + "\n");
			generationLogger.println(str);
		} else {
			if (buffer != null) {
				buffer.append(str + "\n");
			}
			LOG.fine(str + "\n");
		}
	}

	@Override
	public void appendSummary() {
		append("==================================================================================\n");
		LOG.info("TEST CASE SUMMARY:\n");
		LOG.info(buffer.toString());
	}

	@Override
	public void openGenerationFile() {
		try {
			this.generationMode = true;
			this.generationLogger = new PrintWriter(new FileWriter(new File(
					testId + ".txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeGenerationFile() {
		generationLogger.close();
		this.generationMode = false;
	}

	@Override
	public void addCoverageSummaryHeader() {
		append("\n\n============= Coverage summary ================");
	}

	@Override
	public void addCoverageSummary(int numberOfTests, double classCoverage,
			double matchingClassCoverage) {
		append("Number of tests generated: " + numberOfTests);
		append("Total equivalence class coverage: "
				+ ((int) (classCoverage * 100)) + "%");
		append("Total matching equivalence class coverage: "
				+ ((int) (matchingClassCoverage * 100)) + "%");
	}

	@Override
	public <E extends Object, T extends EquivalenceClass> void addDataValueCoverageSummary(
			String dataValueId, int totalClasses, int totalMatchingClasses,
			int coveredClasses, List<DataType<E, T>> values) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < values.size(); i++) {
			sb.append("\"" + values.get(i).getValue() + "\"("
					+ values.get(i).getEquivalenceClass().getName() + ")");
			if (i != (values.size() - 1)) {
				sb.append(",");
			}
		}
		append("Data field coverage for " + dataValueId + " " + coveredClasses
				+ "/" + totalMatchingClasses + "/" + totalClasses + ": ["
				+ sb.toString() + "]");
	}

	@Override
	public void addDataValueCoverageSummary(String dataValueId,
			int totalClasses, int totalMatchingClasses, int coveredClasses,
			Object[] values) {
		// TODO Auto-generated method stub

	}

	@Override
	public void increaseIndentation() {
		indent = indent + INDENT_STEP;
	}

	@Override
	public void decreaseIndentation() {
		indent = indent - INDENT_STEP;
	}

}
