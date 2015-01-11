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
