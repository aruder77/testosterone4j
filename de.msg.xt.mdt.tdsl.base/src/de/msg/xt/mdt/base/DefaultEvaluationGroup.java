package de.msg.xt.mdt.base;

import java.util.HashSet;
import java.util.Set;

public class DefaultEvaluationGroup implements IEvalutaionGroup {

	private final Set<ControlField> fields;

	public DefaultEvaluationGroup() {
		this(new HashSet<ControlField>());
	}

	public DefaultEvaluationGroup(Set<ControlField> fields) {
		super();
		this.fields = fields;
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
