package org.testosterone4j.base;

import java.util.Set;

public interface EquivalenceClass {

	Object getValue();

	Set<? extends Tag> getClassTags();

	String getName();
}
