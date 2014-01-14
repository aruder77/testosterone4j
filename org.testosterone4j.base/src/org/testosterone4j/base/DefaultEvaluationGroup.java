package org.testosterone4j.base;

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
