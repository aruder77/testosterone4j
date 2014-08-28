package org.testosterone4j.base;

import java.util.Collection;

import org.osgi.framework.Bundle;

public interface IActivityBundlesProvider {
	
	Collection<Bundle> getActivityBundles();

}
