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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ControlField {

	String name;

	Set<Tag> tags;

	DataType<?, ? extends EquivalenceClass> lastEnteredValue;

	public ControlField(String name, Set<Tag> tags) {
		super();
		this.name = name;
		this.tags = tags;
	}

	public ControlField(String name, Tag[] tags) {
		super();
		this.name = name;
		this.tags = new HashSet<Tag>();
		this.tags.addAll(Arrays.asList(tags));
	}

	public String getName() {
		return name;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public DataType<?, ? extends EquivalenceClass> getLastEnteredValue() {
		return lastEnteredValue;
	}

	public void setLastEnteredValue(
			DataType<?, ? extends EquivalenceClass> lastEnteredValue) {
		this.lastEnteredValue = lastEnteredValue;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + ", tags=" + tags + "]";
	}
}
