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


import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomExtensions {

	public static <T> T rand(List<T> list) {
		Random rand = new Random(System.currentTimeMillis());
		return list.get(rand.nextInt(list.size()));
	}

	public static <T> T rand(Collection<T> col) {
		Random rand = new Random(System.currentTimeMillis());
		int index = rand.nextInt(col.size());
		return (T) col.toArray()[index];
	}
}
