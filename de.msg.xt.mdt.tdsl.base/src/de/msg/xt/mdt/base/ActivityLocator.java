package de.msg.xt.mdt.base;

public interface ActivityLocator {

	Object beforeTest();

	Object afterTest();

	<T extends ActivityAdapter> T find(String id, Class<T> adapterClass);

	<E extends AbstractActivity, T extends ActivityAdapter> T find(
			Class<E> activityClass, Class<T> adapterClass);
}
