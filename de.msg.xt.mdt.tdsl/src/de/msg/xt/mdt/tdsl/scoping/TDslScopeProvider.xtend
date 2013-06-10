package de.msg.xt.mdt.tdsl.scoping

import de.msg.xt.mdt.tdsl.jvmmodel.MetaModelExtensions
import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.GeneratedValueExpression
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage
import de.msg.xt.mdt.tdsl.tDsl.UseCase
import java.util.ArrayList
import java.util.Collections
import java.util.List
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XIfExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.scoping.LocalVariableScopeContext
import org.eclipse.xtext.xbase.scoping.XbaseScopeProvider
import org.eclipse.xtext.resource.EObjectDescription
import org.eclipse.xtext.scoping.IGlobalScopeProvider
import org.eclipse.emf.ecore.resource.Resource
import java.util.StringTokenizer
import de.msg.xt.mdt.tdsl.tDsl.TDslFactory
import org.eclipse.emf.ecore.InternalEObject
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.scoping.impl.SimpleScope
import com.google.common.collect.Iterables
import java.util.Collection
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.emf.ecore.EClass
import org.eclipse.xtext.util.CancelIndicator
import de.msg.xt.mdt.tdsl.tDsl.ConditionalNextActivity
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import org.eclipse.emf.ecore.util.EcoreUtil
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall

class TDslScopeProvider extends XbaseScopeProvider {
	
	
    @Inject extension MetaModelExtensions
    
    @Inject
    IGlobalScopeProvider globalScopeProvider
    
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
		try {
		if (reference == TDslPackage::eINSTANCE.operationParameterAssignment_Name) {
			switch (context) {
				OperationCall: {
					System::out.println("getScope " + context.description + " OperationParameterAssignment_Name")
					val maps = context.operation.dataTypeMappings
					var IScope scope 
					scope = Scopes::scopeFor(maps, [
						val controlOperationParam = it?.name
						val name = controlOperationParam.name
						if (controlOperationParam.eIsProxy) {
							throw new ScopingException("ControlOperationParameter could not be resolved: " + context.description + "OperationParameterAssignment_Name")
						}
						QualifiedName::create(name)
					], IScope::NULLSCOPE)
					return scope
				}
				OperationParameterAssignment: {
					System::out.println("getScope " + (context.eContainer as OperationCall).description + "/Param" + " OperationParameterAssignment_Name")
					val operation = (context.eContainer as OperationCall).operation
					val dataTypeMappings = operation.dataTypeMappings
					if (operation.eIsProxy) {
						throw new ScopingException("Operation could not be resolved: " + (context.eContainer as OperationCall).operation + " OperationParameterAssignment_Name")
					}
					return Scopes::scopeFor(dataTypeMappings, [
						val controlOperationParameter = it?.name
						val name = controlOperationParameter?.name
						if (controlOperationParameter?.eIsProxy) {
							throw new ScopingException("ControlOperationParameter could not be resolved: " + operation + " OperationParameterAssignment_Name")
						}			
						QualifiedName::create(name)				
					], IScope::NULLSCOPE)
				}
			}
		} else if (reference == TDslPackage::eINSTANCE.activityOperationParameterAssignment_Name) {
			switch (context) {
				ActivityOperationCall: {
					System::out.println("getScope " + context.useCasePath + " activityOperationParameterAssignment_Name")
					val operation = context.operation
					val operationParameters = operation.params
					if (operation.eIsProxy) {
						throw new ScopingException("OperationParameters could not be resolved: " + context.useCasePath + " activtiyOperationParameterAssignment_Name")
					}					
					return Scopes::scopeFor(operationParameters)
				}
				ActivityOperationParameterAssignment: {
					System::out.println("getScope " + (context.eContainer as ActivityOperationCall).useCasePath + "/Param" + " activityOperationParameterAssignment_Name")
					val operation = (context.eContainer as ActivityOperationCall).operation
					val params = operation.params 					
					if (operation.eIsProxy) {
						throw new ScopingException("OperationParameters could not be resolved: " + (context.eContainer as ActivityOperationCall).useCasePath + " activtiyOperationParameterAssignment_Name")
					}					
					return Scopes::scopeFor(params)
				}
				default:
					IScope::NULLSCOPE
			}
		} else if (reference == TDslPackage::eINSTANCE.operationCall_Operation || reference == TDslPackage::eINSTANCE.activityOperationCall_Operation) {
			val XExpression opCall = context as XExpression
			val nextActivities = opCall.currentActivities
			if (reference == TDslPackage::eINSTANCE.operationCall_Operation) {	
				val millis = System::currentTimeMillis			
				System::out.println(millis + " getScope " + opCall.useCasePath + " operationCall_Operation")
				val operations = new ArrayList<OperationMapping>
				for (activity : nextActivities) {
					operations.addAll(activity.fieldOperations)
				}
				System::out.println(millis + " getScope finished " + opCall.useCasePath + " operationCall_Operation")
				calculatesScopes(operations)
			} else {
				System::out.println("getScope " + opCall.useCasePath + " activityOperationCall_Operation")
				val activityOperations = new ArrayList<ActivityOperation>()
				for (activity : nextActivities) {
					activityOperations.addAll(activity.allOperations)
				}
				Scopes::scopeFor(activityOperations, [
					val actOp = it as ActivityOperation
					val name = actOp.name
					if (actOp.eIsProxy) {
						throw new ScopingException("ActivityOperation could not be resolved: " + opCall.useCasePath + " activtiyOperationCall_Operation")
					}
					QualifiedName::create("#" + name)
				], IScope::NULLSCOPE)
			}
		} else if (reference == TDslPackage::eINSTANCE.operationMapping_Name) {
			var Field field 
			if (context instanceof OperationMapping) {
				field = (context as OperationMapping).field
			} else {
				field = context as Field
			}
			if (field.control != null) {
				val control = field.control
				val operations = control.operations
				if (control.eIsProxy) {
					throw new ScopingException("Could not resolve control for field " + field.name)
				}
				Scopes::scopeFor(operations)
			} else 
				IScope::NULLSCOPE
		} else if (reference == TDslPackage::eINSTANCE.dataTypeMapping_Name) {
			var OperationMapping opMap
			if (context instanceof OperationMapping) {
				opMap = context as OperationMapping
			} else if (context instanceof DataTypeMapping) {
				val dataTypeMap = context as DataTypeMapping
				opMap = dataTypeMap?.operationMapping
			}
			val operation = opMap?.name
			val params = operation.params
			if (operation.eIsProxy) {
				throw new ScopingException("Could not resolve operation!")
			}
			if (params != null)
				Scopes::scopeFor(params)
			else
				IScope::NULLSCOPE
		} else if (reference == TDslPackage::eINSTANCE.generatedValueExpression_Param) {
			if (context instanceof GeneratedValueExpression) {
				val lastExpression = (context as XExpression).lastExpressionWithNextActivity
				if (lastExpression instanceof OperationCall) {
					val opCall = lastExpression as OperationCall
					val operation = opCall?.operation
					val dataTypeMappings = operation?.dataTypeMappings
					if (operation?.eIsProxy) {
						throw new ScopingException("Could not resolve operation: " + opCall.useCasePath + " generatedValueExpression_Param")
					}
					if (dataTypeMappings != null) 
						Scopes::scopeFor(dataTypeMappings, [
							val DataTypeMapping dtMap = it as DataTypeMapping
							val controlOpParam = dtMap.name
							val name = controlOpParam?.name
							if (controlOpParam.eIsProxy) {
								throw new ScopingException("Could not resolve ControlOperationParameter: " + opCall.useCasePath + " generatedValueExpression_Param")
							}
							QualifiedName::create(dtMap.name.name)
						], IScope::NULLSCOPE)
					else 
						IScope::NULLSCOPE
				} else if (lastExpression instanceof ActivityOperationCall) {
					val opCall = lastExpression as ActivityOperationCall
					val operation = opCall?.operation
					if (operation?.eIsProxy) {
						throw new ScopingException("Could not resolve operation: " + opCall.useCasePath + " generatedValueExpression_Param")
					}
					val operationParams = operation?.params
					if (operationParams != null) {
						Scopes::scopeFor(operationParams)
					}
				} else if (lastExpression instanceof SubUseCaseCall) {
					val call = lastExpression as SubUseCaseCall
					val useCase = call?.useCase
					if (useCase?.eIsProxy) {
						throw new ScopingException("Could not resolve operation: " + call.useCasePath + " generatedValueExpression_Param")
					}
					val params = call?.useCase?.inputParameter
					if (params != null) {
						Scopes::scopeFor(params)
					}
				}
			}
		} else {
			if (context != null)
				super.getScope(context, reference)
			else 
				IScope::NULLSCOPE
		}
		} catch(ScopingException ex) {
			System::out.println(ex.message)
			return IScope::NULLSCOPE
		}
	}
	
	def Activity determineInitialActivity(XExpression expr) {
		var Activity initialActivity = null
		val activityOperation = EcoreUtil2::getContainerOfType(expr, typeof(ActivityOperation))
		if (activityOperation != null) {
			initialActivity = activityOperation.activity
		} else {
			initialActivity = EcoreUtil2::getContainerOfType(expr, typeof(UseCase)).initialActivity
		}
		initialActivity
	}
	
	def calculatesScopes(List<OperationMapping> operationMappings) {
		Scopes::scopeFor(operationMappings, [
					val OperationMapping opMap = it as OperationMapping
					if (opMap?.name?.name != null) {
						QualifiedName::create('#' + opMap.field.name, opMap.name.name)
					} else {
						throw new ScopingException("Could not resolve operation in caculatesScopes!")
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
	
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(OperationCall call) throws ScopingException {
		val operation = call?.operation
		val nextActivities = operation.nextActivities
		if (operation.eIsProxy) {
			throw new ScopingException("Call operation could not get resolved!")
		} 
		if (nextActivities == null || nextActivities.empty) {
			Collections::singletonList(call.operation.field.parentActivity)
		}
		nextActivities
	}			
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(ActivityOperationCall call) throws ScopingException {
		val operation = call?.operation
		val nextActivities = operation?.nextActivities
		if (operation.eIsProxy) {
			throw new ScopingException("Call operation could not get resolved!")
		}		
		if (nextActivities == null) {
			Collections::singletonList(call.operation.activity)
		}
		nextActivities
	}			
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(SubUseCaseCall call) throws ScopingException {
		val useCase = call?.useCase
		var nextActivity = useCase?.nextActivity
		if (useCase.eIsProxy) {
			throw new ScopingException("Call operation could not get resolved!")			
		}
		if (nextActivity == null) {
			val condNextAct = TDslFactory::eINSTANCE.createConditionalNextActivity
			condNextAct.next = useCase?.initialActivity
			nextActivity = condNextAct
		} 
		Collections::singletonList(nextActivity)
	}			
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(XIfExpression ifExpr) {
		ifExpr.then.determineExplicitNextActivities
	}			
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(XVariableDeclaration varDecl) {
		varDecl.right.determineExplicitNextActivities
	}			
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(XBlockExpression blockExpr) {
		if (blockExpr.expressions.empty)
			return Collections::emptyList
		var index = blockExpr.expressions.size - 1
		var currentExpression = blockExpr.expressions.last
		while (index >= 0 && currentExpression.determineExplicitNextActivities.empty) {
			currentExpression = blockExpr.expressions.get(index)
			index = index - 1
		}
		if (index >= 0) {
			return currentExpression.determineExplicitNextActivities
		} else {
			return Collections::emptyList 
		}
	}			
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(XExpression expr) {
		/*if (expr.containsActivitySwitchingOperation)
			expr.activitySwitchingOperation.determineExplicitNextActivities
		else */ 
			Collections::emptyList
	}
	
	def lastActivitySwitchingExpression(XExpression expr, boolean startWithCurrent) {
		var XExpression currentExpression = 
			if (startWithCurrent) 
				expr.activitySwitchingOperation
			else 
				expr.precedingExpression 
		System::out.println("LastActivitySwitchingExpression: currentExpression: " + currentExpression.useCasePath)
		if (currentExpression != null) {
			var nextActivities = currentExpression?.determineExplicitNextActivities
			while (currentExpression != null && nextActivities != null && nextActivities.empty) {
				currentExpression = currentExpression.precedingExpression
				System::out.println("LastActivitySwitchingExpression: currentExpression: " + currentExpression.useCasePath)
				nextActivities = currentExpression?.determineExplicitNextActivities
			} 
		}
		currentExpression
	}	
	
	
	def List<Activity> currentActivities(XExpression expr) {
		val currentTime = System::currentTimeMillis
		System::out.println(currentTime + " CurrentActivities for " + expr.useCasePath)
		val lastExpression = expr?.lastActivitySwitchingExpression(expr instanceof StatementLine)
		if (lastExpression == null) {
			val act = expr.determineInitialActivity
			act.name			
			System::out.println(currentTime + " Finished CurrentActivities for " + expr.useCasePath + ": " + act.name)
			return Collections::singletonList(act)
		}

		val nextActivities = lastExpression.determineExplicitNextActivities	
		val returnList = new ArrayList<Activity> 
		if (nextActivities != null) {
			for (nextActivity : nextActivities) {
				if (nextActivity.returnToLastActivity) {
					val previousExpression = lastExpression.lastActivitySwitchingExpression(false)
					System::out.println("CurrentActivities: returnToLastActivity: previousExpression: " + previousExpression.useCasePath)
					if (previousExpression != null) 
						returnList.addAll(previousExpression.currentActivities)
				} else {
					returnList.add(nextActivity.next)
				}
				if (nextActivity.eIsProxy) {
					throw new ScopingException("Could not resolve nextActivity: " + lastExpression.useCasePath)
				}
			}
		}
		System::out.println(currentTime + " Finished CurrentActivities for " + expr.useCasePath + ": " + returnList.head?.name)
		returnList
	}
}
