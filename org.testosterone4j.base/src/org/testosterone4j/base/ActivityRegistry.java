package org.testosterone4j.base;

public interface ActivityRegistry {

	
	public Class<? extends AbstractActivity> resolveActivity(String id);
}
