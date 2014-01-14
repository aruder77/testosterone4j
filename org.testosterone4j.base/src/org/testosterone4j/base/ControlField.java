package org.testosterone4j.base;

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
