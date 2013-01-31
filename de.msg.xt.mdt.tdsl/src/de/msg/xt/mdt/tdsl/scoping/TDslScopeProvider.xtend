package de.msg.xt.mdt.tdsl.scoping

import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.xbase.scoping.XbaseScopeProvider
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage
import java.util.List
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping

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
    
	override getScope(EObject context, EReference reference) {
		if (reference.equals(TDslPackage::eINSTANCE.operationParameterAssignment_Name)) {
			var List<DataTypeMapping> mappings
			switch (context) {
				OperationCall:
					return Scopes::scopeFor(context.operation.dataTypeMappings)
				OperationParameterAssignment:
					return Scopes::scopeFor((context.eContainer as OperationCall).operation.dataTypeMappings)
			}
		} else {
			super.getScope(context, reference)
		}
	}
	
}