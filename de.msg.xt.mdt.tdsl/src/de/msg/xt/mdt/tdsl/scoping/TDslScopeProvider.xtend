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
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.xbase.scoping.LocalVariableScopeContext
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.xbase.scoping.featurecalls.LocalVarDescription
import org.eclipse.xtext.xbase.scoping.featurecalls.JvmFeatureScope

class TDslScopeProvider extends XbaseScopeProvider {
	
	
    @Inject extension IJvmModelAssociations associations
    
    @Inject extension MetaModelExtensions
    
	override protected createLocalVarScope(IScope parentScope, LocalVariableScopeContext scopeContext) {
		if (scopeContext != null && scopeContext.context != null) {
			val context = scopeContext.context
			if (context instanceof StatementLine) {
				val useCaseBlock = EcoreUtil2::getContainerOfType(context, typeof(XBlockExpression))
				var IScope pScope = parentScope
				if (scopeContext.canSpawnForContainer())
					pScope = createLocalVarScope(parentScope, scopeContext.spawnForContainer());
				
				val localVars = new ArrayList<XExpression>()
				val statementLine = EcoreUtil2::getContainerOfType(context, typeof(StatementLine))
				val indexInBlock = statementLine.indexInParentBlock
				for (expr : useCaseBlock.expressions.subList(0, indexInBlock)) {
					val line = expr as StatementLine
					if (line?.statement instanceof XVariableDeclaration) {
						localVars.add(line.statement)
					}
				}
				return Scopes::scopeFor(localVars, pScope)
			} else if (context instanceof UseCase) {
				
			}
		}
		
		return super.createLocalVarScope(parentScope, scopeContext)
	}
    
//    override IScope createLocalVarScope(Object context,
//            IScope parentScope) {
//        val pScope = super.createLocalVarScopeForJvmOperation(context,
//                parentScope);
// 
//        // retrieve the AST element associated to the method
//        // created by our model inferrer
//        val sourceElement = associations.getPrimarySourceElement(context);
//        if (sourceElement instanceof UseCase) {
//            val operation = sourceElement as UseCase;
//            return createLocalScopeForX(operation.getOutput(),
//                    pScope);
//        }
// 
//        return pScope;
//    }
//    
//    
//    def JvmType getJvmType(UseCase useCase) {
//        useCase.jvmElements.filter(typeof(JvmType)).head
//    }
    
	override getScope(EObject context, EReference reference) {
		if (reference == TDslPackage::eINSTANCE.operationParameterAssignment_Name) {
			var List<DataTypeMapping> mappings
			switch (context) {
				OperationCall: {
					val maps = context.operation.dataTypeMappings
					val scope = Scopes::scopeFor(maps, [
						QualifiedName::create(it?.name?.name)
					], IScope::NULLSCOPE)
					return scope
				}
				OperationParameterAssignment:
					return Scopes::scopeFor((context.eContainer as OperationCall).operation.dataTypeMappings, [
						QualifiedName::create(it?.name?.name)
					], IScope::NULLSCOPE)
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
			val activityOperation = EcoreUtil2::getContainerOfType(context, typeof(ActivityOperation))
			if (activityOperation != null) {
				val activity = activityOperation.activity
				if (reference == TDslPackage::eINSTANCE.operationCall_Operation) {				
					val operations = new ArrayList<OperationMapping>
					operations.addAll(activity.fieldOperations)
					operations.calculatesScopes
				} else {
					val operations = new ArrayList<ActivityOperation>
					if (activity?.allOperations != null) {
						operations.addAll(activity.allOperations)
					}
					Scopes::scopeFor(operations, [QualifiedName::create('#' + it.name)], IScope::NULLSCOPE)
					//Scopes::scopeFor(operations)
				}		
			} else {
				val XExpression opCall = context as XExpression
				var XExpression lastExpression = null
				if (opCall instanceof ActivityOperationCall || opCall instanceof OperationCall) {
					lastExpression = opCall.lastExpressionWithNextActivity
				} else {
					if (!(opCall instanceof XBlockExpression)) 
						lastExpression = if (opCall.containsActivitySwitchingOperation) opCall.activitySwitchingOperation else opCall.lastExpressionWithNextActivity
				}
				if (lastExpression == null) {
					val initialActivity = EcoreUtil2::getContainerOfType(opCall, typeof(UseCase)).initialActivity
					if (reference == TDslPackage::eINSTANCE.operationCall_Operation) {
						initialActivity.fieldOperations.calculatesScopes					
					} else {
						val IScope scope = Scopes::scopeFor(initialActivity.allOperations, [QualifiedName::create('#' + it.name)], IScope::NULLSCOPE)
						scope
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
						val nextActivities = lastExpression.determineNextActivities
						for (activity : nextActivities) {
							if (activity?.allOperations != null) {
								operations.addAll(activity.allOperations)
							}
						}
						Scopes::scopeFor(operations, [QualifiedName::create('#' + it.name)], IScope::NULLSCOPE)
						//Scopes::scopeFor(operations)
					}
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
		} else if (reference == TDslPackage::eINSTANCE.dataTypeMapping_Name) {
			var OperationMapping opMap
			if (context instanceof OperationMapping) {
				opMap = context as OperationMapping
			} else if (context instanceof DataTypeMapping) {
				val dataTypeMap = context as DataTypeMapping
				opMap = dataTypeMap?.operationMapping
			}
			val params = opMap?.name?.params
			if (params != null)
				Scopes::scopeFor(params)
			else
				IScope::NULLSCOPE
		} else if (reference == TDslPackage::eINSTANCE.generatedValueExpression_Param) {
			if (context instanceof GeneratedValueExpression) {
				val lastExpression = (context as XExpression).lastExpressionWithNextActivity
				if (lastExpression instanceof OperationCall) {
					val opCall = lastExpression as OperationCall
					val dataTypeMappings = opCall?.operation?.dataTypeMappings
					if (dataTypeMappings != null) 
						Scopes::scopeFor(dataTypeMappings, [
							val DataTypeMapping dtMap = it as DataTypeMapping
							QualifiedName::create(dtMap.name.name)
						], IScope::NULLSCOPE)
					else 
						IScope::NULLSCOPE
				}
			}
		} else {
			super.getScope(context, reference)
		}
	}
	
	def calculatesScopes(List<OperationMapping> operationMappings) {
		Scopes::scopeFor(operationMappings, [
					val OperationMapping opMap = it as OperationMapping
					if (opMap?.name?.name != null) {
						QualifiedName::create('#' + opMap.field.name, opMap.name.name)
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
			XBlockExpression:
				if(expr.expressions.last.containsActivitySwitchingOperation) 
					expr.expressions.last.activitySwitchingOperation.determineNextActivities
				else 
					expr.expressions.last.lastExpressionWithNextActivity.determineNextActivities
			default:
				expr.activitySwitchingOperation.determineNextActivities
		}
	}
}