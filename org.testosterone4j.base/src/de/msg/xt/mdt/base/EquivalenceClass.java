package de.msg.xt.mdt.base;

import java.util.Set;

public interface EquivalenceClass {

	Object getValue();

	Set<? extends Tag> getClassTags();

	String getName();
}
