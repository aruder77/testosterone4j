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

import java.io.Serializable;

public interface DataType<DT, EC extends EquivalenceClass> extends Serializable {

	DT getValue();

	void setValue(DT value);

	EC getEquivalenceClass();

	void setEquivalenceClass(EC equivalenceClass);

	Class<EC> getEquivalenceClassEnum();
}
