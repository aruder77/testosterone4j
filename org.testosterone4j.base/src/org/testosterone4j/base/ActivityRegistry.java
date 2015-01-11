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


import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.testosterone4j.base.util.TDslHelper;

public class ActivityRegistry {
	
	private static ActivityRegistry instance = null;
	
	private Reflections reflections;
	
	private Map<String, Class<? extends AbstractActivity>> activityMap = new HashMap<String, Class<? extends AbstractActivity>>();

	public static ActivityRegistry getInstance() {
		if (instance == null) {
			instance = new ActivityRegistry();
		}
		return instance;
	}
	
	private ActivityRegistry() {
		IActivityBundlesProvider activityBundlesProvider = TDslInjector.getInjector().getInstance(IActivityBundlesProvider.class);
		scanForActivities(activityBundlesProvider.getActivityBundles());
	}

	protected void scanForActivities(Collection<Bundle> bundles) {
		List<URL> entries = new ArrayList<URL>();
		for (Bundle bundle: bundles) {
			entries.addAll(ClasspathHelper.forManifest(bundle.getEntry("/")));
		}
		reflections = new Reflections(new ConfigurationBuilder().addUrls(entries));

		Set<Class<?>> activityClasses = reflections.getTypesAnnotatedWith(Activity.class);
		for (Class<?> clazz: activityClasses) {
			Class<? extends AbstractActivity> activityClass = (Class<? extends AbstractActivity>)clazz;
			String id = TDslHelper.extractId(activityClass);
			activityMap.put(id, activityClass);
		}	
	}

	
	public Class<? extends AbstractActivity> resolveActivity(String id) {
		return activityMap.get(id);
	}
}
