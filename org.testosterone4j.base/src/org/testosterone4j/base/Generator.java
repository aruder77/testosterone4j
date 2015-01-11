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


import java.util.List;
import java.util.Set;

public interface Generator {

	<E extends Runnable> List<E> generate(Class<E> clazz);

	<T extends DataType> T generateDataTypeValue(Class<T> clazz, String id,
			Set<Tag> tags);

	void setTags(Tag[] tags);

	void setExcludeTags(Tag[] excludeTags);

	void setMinTestCases(int min);

	void setMaxTestCases(int max);
}
