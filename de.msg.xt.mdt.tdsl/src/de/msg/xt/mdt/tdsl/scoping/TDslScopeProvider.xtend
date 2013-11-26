package de.msg.xt.mdt.tdsl.scoping

import de.msg.xt.mdt.tdsl.jvmmodel.MetaModelExtensions
import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.ConditionalNextActivity
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.GeneratedValueExpression
import de.msg.xt.mdt.tdsl.tDsl.InnerBlock
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.ParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import de.msg.xt.mdt.tdsl.tDsl.TDslFactory
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
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.annotations.scoping.XbaseWithAnnotationsScopeProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TDslScopeProvider extends XbaseWithAnnotationsScopeProvider {
	
	
    @Inject extension MetaModelExtensions
    
    Logger logger = LoggerFactory::getLogger(typeof(TDslScopeProvider))

	override getScope(EObject context, EReference reference) {
		try {
		if (reference == TDslPackage::eINSTANCE.operationParameterAssignment_Name) {
			switch (context) {
				OperationCall: {
					logger.debug("getScope " + context.description + " OperationParameterAssignment_Name")
					val maps = context.operation.dataTypeMappings
					var IScope scope 
					if (!maps.filter[it?.name?.name == null].empty) {
						logger.warn("ControlOperationParameter could not be resolved: {} OperationParameterAssignment_Name", context.description)
					}
					scope = Scopes::scopeFor(maps.filter [it?.name?.name != null], [
						QualifiedName::create(it.name.name)
					], IScope::NULLSCOPE)
					return scope
				}
				OperationParameterAssignment: {
					logger.debug("getScope " + (context.eContainer as OperationCall).description + "/Param" + " OperationParameterAssignment_Name")
					val operation = (context.eContainer as OperationCall).operation
					val dataTypeMappings = operation.dataTypeMappings
					if (operation.eIsProxy) {
						logger.warn("Operation could not be resolved: {} OperationParameterAssignment_Name", (context.eContainer as OperationCall).operation)
						throw new ScopingException("Operation could not be resolved: " + (context.eContainer as OperationCall).operation + " OperationParameterAssignment_Name")
					}
					if (!dataTypeMappings.filter [it?.name?.name == null].empty) {
						logger.warn("ControlOperationParameter could not be resolved: {} OperationParameterAssignment_Name", operation)
					}
					return Scopes::scopeFor(dataTypeMappings.filter [it?.name?.name != null], [
						QualifiedName::create(it.name.name)				
					], IScope::NULLSCOPE)
				}
			}
		} else if (reference == TDslPackage::eINSTANCE.activityOperationParameterAssignment_Name) {
			switch (context) {
				ActivityOperationCall: {
					logger.debug("getScope " + context.useCasePath + " activityOperationParameterAssignment_Name")
					val operation = context.operation
					val operationParameters = operation.params
					if (operation.eIsProxy) {
						logger.warn("OperationParameters could not be resolved: " + context.useCasePath + " activtiyOperationParameterAssignment_Name")
						throw new ScopingException("OperationParameters could not be resolved: " + context.useCasePath + " activtiyOperationParameterAssignment_Name")
					}					
					return Scopes::scopeFor(operationParameters)
				}
				ActivityOperationParameterAssignment: {
					logger.debug("getScope " + (context.eContainer as ActivityOperationCall).useCasePath + "/Param" + " activityOperationParameterAssignment_Name")
					val operation = (context.eContainer as ActivityOperationCall).operation
					val params = operation.params 					
					if (operation.eIsProxy) {
						logger.warn("OperationParameters could not be resolved: " + (context.eContainer as ActivityOperationCall).useCasePath + " activtiyOperationParameterAssignment_Name")
						throw new ScopingException("OperationParameters could not be resolved: " + (context.eContainer as ActivityOperationCall).useCasePath + " activtiyOperationParameterAssignment_Name")
					}					
					return Scopes::scopeFor(params)
				}
				default:
					IScope::NULLSCOPE
			}
		} else if (reference == TDslPackage::eINSTANCE.parameterAssignment_Name) {
			switch (context) {
				SubUseCaseCall: {
					logger.debug("getScope " + context.useCasePath + " parameterAssignment_Name")
					val useCase = context.useCase
					val useCaseParameter = useCase.inputParameter
					if (useCase.eIsProxy) {
						logger.warn("UseCase could not be resolved: " + context.useCasePath + " parameterAssignment_Name")
						throw new ScopingException("Parameters could not be resolved: " + context.useCasePath + " parameterAssignment_Name")
					}					
					return Scopes::scopeFor(useCaseParameter)
				}
				ParameterAssignment: {
					logger.debug("getScope " + (context.eContainer as SubUseCaseCall).useCasePath + "/Param" + " parameterAssignment_Name")
					val useCase = (context.eContainer as SubUseCaseCall).useCase
					val params = useCase.inputParameter 					
					if (useCase.eIsProxy) {
						logger.warn("UseCase could not be resolved: " + (context.eContainer as SubUseCaseCall).useCasePath + " parameterAssignment_Name")
						throw new ScopingException("UseCase could not be resolved: " + (context.eContainer as SubUseCaseCall).useCasePath + " parameterAssignment_Name")
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
				logger.debug(millis + " getScope " + opCall.useCasePath + " operationCall_Operation")
				val operations = new ArrayList<OperationMapping>
				for (activity : nextActivities) {
					operations.addAll(activity.fieldOperations)
				}
				logger.debug(millis + " getScope finished " + opCall.useCasePath + " operationCall_Operation")
				calculatesScopes(operations)
			} else {
				logger.debug("getScope " + opCall.useCasePath + " activityOperationCall_Operation")
				val activityOperations = new ArrayList<ActivityOperation>()
				for (activity : nextActivities) {
					activityOperations.addAll(activity.allOperations)
				}
				if (!activityOperations.filter[it.name == null].empty) {
					logger.warn("ActivityOperation could not be resolved: " + opCall.useCasePath + " activtiyOperationCall_Operation")
				}
				Scopes::scopeFor(activityOperations.filter[it.name != null], [
					QualifiedName::create("#" + it.name)
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
					logger.warn("Could not resolve control for field " + field.name)
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
				logger.warn("Could not resolve operation!")
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
						logger.warn("Could not resolve operation: " + opCall.useCasePath + " generatedValueExpression_Param")
						throw new ScopingException("Could not resolve operation: " + opCall.useCasePath + " generatedValueExpression_Param")
					}
					if (dataTypeMappings != null) 
						Scopes::scopeFor(dataTypeMappings.filter [ it?.name?.name != null ], [
							QualifiedName::create(it.name.name)
						], IScope::NULLSCOPE)
					else 
						IScope::NULLSCOPE
				} else if (lastExpression instanceof ActivityOperationCall) {
					val opCall = lastExpression as ActivityOperationCall
					val operation = opCall?.operation
					if (operation?.eIsProxy) {
						logger.warn("Could not resolve operation: " + opCall.useCasePath + " generatedValueExpression_Param")
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
						logger.warn("Could not resolve operation: " + call.useCasePath + " generatedValueExpression_Param")
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
			logger.debug(ex.message)
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
		Scopes::scopeFor(operationMappings.filter [ it?.name?.name != null ], [
					QualifiedName::create('#' + it.field.name, it.name.name)
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
			logger.warn("Call operation could not get resolved!")
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
			logger.warn("Call operation could not get resolved!")
			throw new ScopingException("Call operation could not get resolved!")
		}		
		if (nextActivities == null) {
			Collections::singletonList(call.operation.activity)
		}
		nextActivities
	}			
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(InnerBlock innerBlock) throws ScopingException {
		if (innerBlock.activityExpectationBlock) {
			val condNextAct = TDslFactory::eINSTANCE.createConditionalNextActivity
			condNextAct.next = innerBlock.expectedActivity
			Collections::singletonList(condNextAct)
		} else {
			(innerBlock.eContainer as XExpression).determineExplicitNextActivities
		}
	}
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(SubUseCaseCall call) throws ScopingException {
		val useCase = call?.useCase
		var nextActivity = useCase?.nextActivity
		if (useCase.eIsProxy) {
			logger.warn("Call operation could not get resolved!")
			throw new ScopingException("Call operation could not get resolved!")			
		}
		if (nextActivity == null) {
			val condNextAct = TDslFactory::eINSTANCE.createConditionalNextActivity
			condNextAct.next = useCase?.initialActivity
			nextActivity = condNextAct
		} 
		Collections::singletonList(nextActivity)
	}			
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(XVariableDeclaration varDecl) {
		varDecl.right?.determineExplicitNextActivities
	}			
	
	def dispatch List<ConditionalNextActivity> determineExplicitNextActivities(XExpression expr) {
		Collections::emptyList
	}
	
	def lastActivitySwitchingExpression(XExpression expr, boolean startWithCurrent) {
		var XExpression currentExpression = 
			if (startWithCurrent && (expr.activitySwitchingOperation != null)) 
				expr.activitySwitchingOperation
			else 
				expr.precedingExpression 
		logger.debug("LastActivitySwitchingExpression: currentExpression: " + currentExpression.useCasePath)
		if (currentExpression != null) {
			var nextActivities = currentExpression?.determineExplicitNextActivities
			while (currentExpression != null && nextActivities != null && nextActivities.empty) {
				currentExpression = currentExpression.precedingExpression
				logger.debug("LastActivitySwitchingExpression: currentExpression: " + currentExpression.useCasePath)
				nextActivities = currentExpression?.determineExplicitNextActivities
			} 
		}
		currentExpression
	}	
	
	
	def List<Activity> currentActivities(XExpression expr) {
		val currentTime = System::currentTimeMillis
		var nestedCounter = 0
		logger.info(currentTime + " CurrentActivities for " + expr.useCasePath)
		var lastExpression = expr?.lastActivitySwitchingExpression(expr instanceof StatementLine)

		val returnList = new ArrayList<Activity> 

		// handle ActivityExpectation blocks
		if (expr instanceof InnerBlock) {
			val innerBlock = expr as InnerBlock
			if (innerBlock.activityExpectationBlock) {
				returnList.add(innerBlock.expectedActivity)
				return returnList				
			}
		}

		while (lastExpression != null && returnList.empty) {
			val nextActivities = lastExpression.determineExplicitNextActivities	
			if (nextActivities != null) {
				for (nextActivity : nextActivities) {
					if (nextActivity.returnToLastActivity) {
						nestedCounter = nestedCounter + 1
						lastExpression = lastExpression.lastActivitySwitchingExpression(false)
					} else {
						if (nestedCounter == 0)
								returnList.add(nextActivity.next)
						else {
							nestedCounter = nestedCounter - 1
							lastExpression = lastExpression.lastActivitySwitchingExpression(false)
						}
					}
					if (nextActivity.eIsProxy) {
						logger.warn("Could not resolve nextActivity: " + lastExpression.useCasePath)
						throw new ScopingException("Could not resolve nextActivity: " + lastExpression.useCasePath)
					}
				}
			} else {
				throw new ScopingException("Hmm... weird! LastActivitySwitchingExpression return activitiy which does not contain explicit next activities!")
			}
		}

		if (lastExpression == null) {
			val act = expr.determineInitialActivity
			act.name			
			logger.info(currentTime + " Finished CurrentActivities for " + expr.useCasePath + ": " + act.name)
			return Collections::singletonList(act)
		}

		logger.info(currentTime + " Finished CurrentActivities for " + expr.useCasePath + ": " + returnList.head?.name)
		returnList
	}
}
