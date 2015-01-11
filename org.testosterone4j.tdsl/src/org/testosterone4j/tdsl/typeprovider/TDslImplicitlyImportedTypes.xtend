/*
 * #%L
 * org.testosterone4j.tdsl
 * %%
 * Copyright (C) 2015 Axel Ruder
 * %%
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * #L%
 */
package org.testosterone4j.tdsl.typeprovider

import org.testosterone4j.base.Tag
import java.util.ArrayList
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedTypes
import org.junit.Assert
import org.testosterone4j.base.Predicates
import org.testosterone4j.base.RandomExtensions

class TDslImplicitlyImportedTypes extends ImplicitlyImportedTypes {
	
	override protected getStaticImportClasses() {
		val list = new ArrayList<Class<?>>()
		list.addAll(super.staticImportClasses)
		list.add(typeof(Assert))
		list
	}		
	
	override protected getExtensionClasses() {
		val list = super.extensionClasses
		list.add(Predicates)
		list.add(RandomExtensions)
		list
	}

}