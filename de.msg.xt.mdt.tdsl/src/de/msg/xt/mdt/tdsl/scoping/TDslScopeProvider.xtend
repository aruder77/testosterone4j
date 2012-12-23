package de.msg.xt.mdt.tdsl.scoping

import de.msg.xt.mdt.tdsl.tDsl.UseCase
import javax.inject.Inject
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.xbase.scoping.XbaseScopeProvider
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.common.types.JvmOperation

class TDslScopeProvider extends XbaseScopeProvider {
	
	
    @Inject extension IJvmModelAssociations associations
    
/*    override IScope createLocalVarScopeForJvmOperation(JvmOperation context,
            IScope parentScope) {
        val pScope = super.createLocalVarScopeForJvmOperation(context,
                parentScope);
 
        // retrieve the AST element associated to the method
        // created by our model inferrer
        val sourceElement = associations.getPrimarySourceElement(context);
        if (sourceElement instanceof UseCase) {
            val operation = sourceElement as UseCase;
            return createLocalScopeForX(operation.getOutput(),
                    pScope);
        }
 
        return pScope;
    }
    
    
    def JvmType getJvmType(UseCase useCase) {
        useCase.jvmElements.filter(typeof(JvmType)).head
    } */    
}