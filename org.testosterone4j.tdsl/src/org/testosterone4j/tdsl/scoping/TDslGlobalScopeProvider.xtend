package org.testosterone4j.tdsl.scoping

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