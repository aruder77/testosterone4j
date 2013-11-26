package de.msg.xt.mdt.tdsl.scoping

import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.scoping.IGlobalScopeProvider
import org.eclipse.xtext.scoping.IScopeProvider
import org.eclipse.xtext.xbase.annotations.typesystem.XbaseWithAnnotationsBatchScopeProvider

class TDslBatchScopeProvider extends XbaseWithAnnotationsBatchScopeProvider {
	
	
    @Inject
    IGlobalScopeProvider globalScopeProvider
    
    @Inject
    IScopeProvider scopeProvider
    
    
    
	override getScope(EObject context, EReference reference) {
		scopeProvider.getScope(context, reference)
	}	
}
