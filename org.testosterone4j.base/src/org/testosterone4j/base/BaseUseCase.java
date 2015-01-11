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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

public class BaseUseCase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected transient Generator generator;

	@XmlElement
	protected Map<String, DataType> generatedData = new HashMap<String, DataType>();

	@XmlElement
	protected Map<String, BaseUseCase> generatedSubUseCases = new HashMap<String, BaseUseCase>();

	public <T extends DataType> T getOrGenerateValue(final Class<T> clazz,
			final String key) {
		return getOrGenerateValue(clazz, key, (Set<Tag>) null);
	}

	public <T extends DataType> T getOrGenerateValue(final Class<T> clazz,
			final String key, final Tag[] tags) {
		return getOrGenerateValue(clazz, key,
				new HashSet<Tag>(Arrays.asList(tags)));
	}

	public <T extends DataType> T getOrGenerateValue(final Class<T> clazz,
			final String key, final Set<Tag> tags) {
		T value = null;
		if (generatedData.containsKey(key)) {
			value = (T) generatedData.get(key);
		} else {
			value = generator.generateDataTypeValue(clazz, key, tags);
			generatedData.put(key, value);
		}
		return value;
	}

	protected <E extends BaseUseCase> E getOrGenerateSubUseCase(
			final Class<E> clazz, final String key) {
		return this.getOrGenerateSubUseCase(clazz, key, null);
	}

	protected <E extends BaseUseCase> E getOrGenerateSubUseCase(
			final Class<E> clazz, final String key, final Tag[] tags) {
		E value = null;
		if (generatedSubUseCases.containsKey(key)) {
			value = (E) generatedSubUseCases.get(key);
		} else {
			try {
				value = clazz.getConstructor(Generator.class).newInstance(
						generator);
				generatedSubUseCases.put(key, value);
			} catch (final InstantiationException e) {
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
			} catch (final NoSuchMethodException e) {
				e.printStackTrace();
			} catch (final SecurityException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}
