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

import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.IScopeProvider
import org.eclipse.xtext.xbase.annotations.typesystem.XbaseWithAnnotationsBatchScopeProvider

class TDslBatchScopeProvider extends XbaseWithAnnotationsBatchScopeProvider {
	
	
    @Inject
    IScopeProvider scopeProvider
    
       
	override getScope(EObject context, EReference reference) {
		val tdslScopeProvider = scopeProvider as TDslScopeProvider
		var scope = tdslScopeProvider.tdslGetScope(context, reference)
		if (scope == null)
			scope = if (context != null)
						super.getScope(context, reference)
					else
						IScope::NULLSCOPE
		scope
	}		
}
