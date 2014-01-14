package org.testosterone4j.base;

public class TestDescriptor<U> {

    private final int testNumber;

    private final U testCase;

    public TestDescriptor(int testNumber, U testCase) {
        super();
        this.testNumber = testNumber;
        this.testCase = testCase;
    }

    public int getTestNumber() {
        return this.testNumber;
    }

    public U getTestCase() {
        return this.testCase;
    }
}
