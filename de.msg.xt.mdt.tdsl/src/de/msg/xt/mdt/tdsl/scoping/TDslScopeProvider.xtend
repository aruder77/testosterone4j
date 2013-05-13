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
		if (reference == TDslPackage::eINSTANCE.operationParameterAssignment_Name) {
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
			val XExpression opCall = context as XExpression
			val nextActivities = opCall.currentActivities
			if (reference == TDslPackage::eINSTANCE.operationCall_Operation) {				
				val operations = new ArrayList<OperationMapping>
				for (activity : nextActivities) {
					operations.addAll(activity.fieldOperations)
				}
				calculatesScopes(operations)
			} else {
				val activityOperations = new ArrayList<ActivityOperation>()
				for (activity : nextActivities) {
					activityOperations.addAll(activity.allOperations)
				}
				Scopes::scopeFor(activityOperations, [
					val actOp = it as ActivityOperation
					QualifiedName::create("#" + actOp.name)
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
				Scopes::scopeFor(field.control.operations)
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
			if (context != null)
				super.getScope(context, reference)
			else 
				IScope::NULLSCOPE
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
	
	
	def dispatch List<Activity> determineExplicitNextActivities(OperationCall call) {
		call?.operation?.nextActivities?.map[it.next]
	}			
	
	def dispatch List<Activity> determineExplicitNextActivities(ActivityOperationCall call) {
		call?.operation?.nextActivities?.map[it.next]
	}			
	
	def dispatch List<Activity> determineExplicitNextActivities(SubUseCaseCall call) {
		var nextActivity = call?.useCase?.nextActivity?.next
		if (nextActivity == null) 
			nextActivity = call?.useCase?.initialActivity 
		Collections::singletonList(nextActivity)
	}			
	
	def dispatch List<Activity> determineExplicitNextActivities(XIfExpression ifExpr) {
		ifExpr.then.determineExplicitNextActivities
	}			
	
	def dispatch List<Activity> determineExplicitNextActivities(XVariableDeclaration varDecl) {
		varDecl.right.determineExplicitNextActivities
	}			
	
	def dispatch List<Activity> determineExplicitNextActivities(XBlockExpression blockExpr) {
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
	
	def dispatch List<Activity> determineExplicitNextActivities(XExpression expr) {
		if (expr.containsActivitySwitchingOperation)
			expr.activitySwitchingOperation.determineExplicitNextActivities
		else 
			Collections::emptyList
	}
	
	def lastActivitySwitchingExpression(XExpression expr) {
		var currentExpression = expr.precedingExpression
		while (currentExpression != null && currentExpression.determineExplicitNextActivities.empty) {
			currentExpression = currentExpression.precedingExpression
		} 
		currentExpression
	}	
	
	
	def List<Activity> currentActivities(XExpression expr) {
		val lastExpression = expr?.lastActivitySwitchingExpression
		if (lastExpression == null) {
			val act = expr.determineInitialActivity
			act.name			
			return Collections::singletonList(act)
		}

		val nextActivities = lastExpression.determineExplicitNextActivities	
		val returnList = new ArrayList<Activity> 
		for (nextActivity : nextActivities) {
			val uriString = EcoreUtil2::getURI(nextActivity)
			if (uriString.equals("returnToPreviousActivity")) {
				val previousExpression = lastExpression.lastActivitySwitchingExpression
				returnList.addAll(previousExpression.currentActivities)
			} else {
				returnList.add(nextActivity)
			}
		}
		returnList
	}
}
