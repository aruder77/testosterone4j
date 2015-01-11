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
package org.testosterone4j.tdsl.scoping

import org.eclipse.xtext.xbase.jvmmodel.JvmGlobalScopeProvider
import com.google.common.base.Predicate
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.common.types.xtext.TypesAwareDefaultGlobalScopeProvider

class TDslGlobalScopeProvider extends TypesAwareDefaultGlobalScopeProvider {
	
	def getScope(Resource context, EClass type, Predicate<IEObjectDescription> filter) {
		super.getScope(context, false, type, filter)
	}
	
}