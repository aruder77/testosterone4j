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
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import org.eclipse.xtext.scoping.IScope
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameterAssignment
import org.eclipse.xtext.xbase.XBlockExpression
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import org.eclipse.internal.xtend.expression.ast.IfExpression
import org.eclipse.xtext.xbase.XIfExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.XExpression
import java.util.Collections
import de.msg.xt.mdt.tdsl.tDsl.Activity
import org.eclipse.xtext.EcoreUtil2
import de.msg.xt.mdt.tdsl.tDsl.UseCase
import de.msg.xt.mdt.tdsl.jvmmodel.MetaModelExtensions
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import java.util.ArrayList
import org.eclipse.xtext.naming.QualifiedName
import com.google.common.base.Function
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation
import de.msg.xt.mdt.tdsl.tDsl.GeneratedValueExpression
import de.msg.xt.mdt.tdsl.tDsl.Field

class TDslScopeProvider extends XbaseScopeProvider {
	
	
    @Inject extension IJvmModelAssociations associations
    
    @Inject extension MetaModelExtensions
    
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
		if (reference == TDslPackage::eINSTANCE.operationParameterAssignment_Name) {
			var List<DataTypeMapping> mappings
			switch (context) {
				OperationCall:
					return Scopes::scopeFor(context.operation.dataTypeMappings)
				OperationParameterAssignment:
					return Scopes::scopeFor((context.eContainer as OperationCall).operation.dataTypeMappings)
			}
		} else if (reference == TDslPackage::eINSTANCE.activityOperationParameterAssignment_Name) {
			switch (context) {
				ActivityOperationCall:
					return Scopes::scopeFor(context.operation.params)
				ActivityOperationParameterAssignment:
					return Scopes::scopeFor((context.eContainer as ActivityOperationCall).operation.params)
				default:
					IScope::NULLSCOPE
			}
		} else if (reference == TDslPackage::eINSTANCE.operationCall_Operation || reference == TDslPackage::eINSTANCE.activityOperationCall_Operation) {
			val XExpression opCall = context as XExpression
			val lastExpression = opCall.lastExpressionWithNextActivity
			if (lastExpression == null) {
				val initialActivity = EcoreUtil2::getContainerOfType(opCall, typeof(UseCase)).initialActivity
				if (reference == TDslPackage::eINSTANCE.operationCall_Operation) {
					initialActivity.fieldOperations.calculatesScopes					
				} else {
					Scopes::scopeFor(initialActivity.operations)
				}
			} else {
				if (reference == TDslPackage::eINSTANCE.operationCall_Operation) {				
					val operations = new ArrayList<OperationMapping>
					for (activity : lastExpression.determineNextActivities) {
						operations.addAll(activity.fieldOperations)
					}
					operations.calculatesScopes
				} else {
					val operations = new ArrayList<ActivityOperation>
					for (activity : lastExpression.determineNextActivities) {
						operations.addAll(activity.operations)
					}
					Scopes::scopeFor(operations)
					
				}
			}
		} else if (reference == TDslPackage::eINSTANCE.operationMapping_Name) {
			var Field field 
			if (context instanceof OperationMapping) {
				field = (context as OperationMapping).field
			} else {
				field = context as Field
			}
			Scopes::scopeFor(field.control.operations)
		} else {
			super.getScope(context, reference)
		}
	}
	
	def calculatesScopes(List<OperationMapping> operationMappings) {
		Scopes::scopeFor(operationMappings, [
					val OperationMapping opMap = it as OperationMapping
					if (opMap?.name?.name != null) {
						QualifiedName::create(opMap.field.name, opMap.name.name)
					} else {
						QualifiedName::EMPTY
					}
				], IScope::NULLSCOPE) 
	}
	
	def List<OperationMapping> getFieldOperations(Activity activity) {
		val List<OperationMapping> operations = new ArrayList<OperationMapping>
		if (activity != null) {
			for (field : activity.fields) {
				operations.addAll(field.operations)
			}		
		}
		operations
	}
	
	def XExpression lastExpressionWithNextActivity(XExpression expr) {
		var XExpression lastStatement
		val exprBlock = expr.block
		val index = expr.indexInParentBlock
		if (index > 0) {
			lastStatement = exprBlock.expressions.get(index - 1)
			if (!lastStatement.containsActivitySwitchingOperation) {
				return lastStatement.lastExpressionWithNextActivity
			}
		} else {
			val parentIndex = exprBlock.indexInParentBlock
			if (parentIndex == null) {
				return null
			}
			val parentBlock = EcoreUtil2::getContainerOfType(exprBlock.eContainer, typeof(XBlockExpression))
			return parentBlock?.expressions.get(parentIndex)?.lastExpressionWithNextActivity
		}
		return lastStatement	
	}
	
	
	def Integer getIndexInParentBlock(XExpression expr) {
		var EObject currentExpr = expr
		var parent = expr.eContainer
		while (!(parent instanceof XBlockExpression) && parent != null) {
			currentExpr = parent
			parent = parent.eContainer
		}
		if (parent == null) {
			return null
		}
		val index = (parent as XBlockExpression).expressions.indexOf(currentExpr)
		return index
	}
	
	def containsActivitySwitchingOperation(XExpression expr) {
		if (expr instanceof OperationCall || expr instanceof ActivityOperationCall) {
			true
		} else {
			!(EcoreUtil2::getAllContentsOfType(expr, typeof(OperationCall)).empty && EcoreUtil2::getAllContentsOfType(expr, typeof(ActivityOperationCall)).empty)
		} 
	}
	
	
	def List<Activity> determineNextActivities(XExpression expr) {
		switch (expr) {
			OperationCall:
				if (expr.operation.nextActivities.empty) {
					Collections::singletonList(expr.operation.field.parentActivity)
				} else { 
					expr.operation.nextActivities.map[e | e.next]	
				}
			ActivityOperationCall:
				if (expr.operation?.nextActivities.empty) {
					Collections::singletonList(expr.operation.activity)
				} else { 
					expr.operation.nextActivities.map[e | e.next]
				}
			SubUseCaseCall:
				Collections::singletonList(expr.useCase.initialActivity)
			XIfExpression:
				expr.then.determineNextActivities
			XVariableDeclaration:
				expr.right.determineNextActivities
		}
	}
}