package de.msg.xt.mdt.base;

import java.util.Set;

public interface EquivalenceClass {

	Object getValue();

	Set<Tag> getClassTags();

	String getName();
}
