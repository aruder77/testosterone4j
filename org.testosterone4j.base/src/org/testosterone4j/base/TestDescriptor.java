package org.testosterone4j.base;

public class TestDescriptor {

	private final int testNumber;

	private final BaseUseCase testCase;

	public TestDescriptor(int testNumber, BaseUseCase testCase) {
		super();
		this.testNumber = testNumber;
		this.testCase = testCase;
	}

	public int getTestNumber() {
		return this.testNumber;
	}

	public BaseUseCase getTestCase() {
		return this.testCase;
	}
}
