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

import java.util.HashSet;
import java.util.Set;

public class DefaultEvaluationGroup implements IEvaluationGroup {

	private final Set<ControlField> fields;

	public DefaultEvaluationGroup() {
		this(new HashSet<ControlField>());
	}

	public DefaultEvaluationGroup(Set<ControlField> fields) {
		super();
		this.fields = fields;
	}

	public DefaultEvaluationGroup(IEvaluationGroup... groups) {
		this();
		for (IEvaluationGroup group: groups) {
			addAll(group.getFields());
		}
	}

	public void addField(ControlField field) {
		fields.add(field);
	}

	@Override
	public Set<ControlField> getFields() {
		return fields;
	}

	public void addAll(Set<ControlField> fields) {
		this.fields.addAll(fields);
	}
}
