package org.testosterone4j.base;

import java.util.List;
import java.util.Set;

public interface Generator {

	<E extends Runnable> List<E> generate(Class<E> clazz);

	<T extends DataType> T generateDataTypeValue(Class<T> clazz, String id,
			Set<Tag> tags);

	void setTags(Tag[] tags);

	void setExcludeTags(Tag[] excludeTags);

	void setMinTestCases(int min);

	void setMaxTestCases(int max);
}
