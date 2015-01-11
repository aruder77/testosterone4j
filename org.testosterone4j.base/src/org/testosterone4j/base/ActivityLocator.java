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

public interface ActivityLocator {

	Object beforeTest();

	Object afterTest();

	<T extends ActivityAdapter> T find(String id, Class<T> adapterClass);

	<E extends AbstractActivity, T extends ActivityAdapter> T find(
			Class<E> activityClass, Class<T> adapterClass);
}
