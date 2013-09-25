package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.Operation
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import de.msg.xt.mdt.tdsl.tDsl.Test
import de.msg.xt.mdt.tdsl.tDsl.Toolkit
import java.util.ArrayList
import java.util.Collections
import java.util.List
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import de.msg.xt.mdt.tdsl.tDsl.Type
import org.eclipse.xtext.scoping.IGlobalScopeProvider
import org.eclipse.emf.ecore.util.EcoreUtil
import de.msg.xt.mdt.tdsl.tDsl.DataType
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage
import org.eclipse.xtext.scoping.IScopeProvider
import de.msg.xt.mdt.tdsl.tDsl.UseCase
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import de.msg.xt.mdt.tdsl.tDsl.Predicate
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import org.eclipse.emf.ecore.InternalEObject
import de.msg.xt.mdt.tdsl.tDsl.TagsDeclaration
import de.msg.xt.mdt.tdsl.tDsl.Tag

/**
 * Convenience meta-model extensions. Please order by Metamodel-Class and alphabetically!
 */
class MetaModelExtensions {
	
	@Inject extension IQualifiedNameProvider
	
	@Inject 
	IScopeProvider scopeProvider;
	
	Logger logger = LoggerFactory::getLogger(typeof(MetaModelExtensions))
	
	// Activity 
	
	def identifier(Activity activity) { 
		if (activity?.uniqueId == null) activity?.fullyQualifiedName else activity?.uniqueId
	}	
	
	def getPackageDeclaration(Activity activity) {
		activity?.eContainer as PackageDeclaration
	}
	
	def Toolkit getToolkit(Activity activity) {
		activity?.packageDeclaration?.toolkit
	}
	
	def List<ActivityOperation> getAllOperations(Activity activity) {
		val ops = new ArrayList<ActivityOperation>()
		if (activity != null) {
			ops.addAll(activity.operations)
			if (activity.parent != null) {
				ops.addAll(activity.parent.allOperations)
			}
		}
		ops
	}	
	
	def List<OperationMapping> getFieldOperations(Activity activity) {
		val fieldOperations = new ArrayList<OperationMapping>()
		if (activity != null) {
			for (field : activity.fields) {
				fieldOperations.addAll(field.operations)
			}
		}
		fieldOperations
	}
	
	def boolean needsOwnActivityAdapter(Activity activity) {
		if (activity == null)
			return false
		!activity.operations.filter[it.body == null].empty
//		true
	}
	
	def Activity adapterProvidingActivity(Activity activity) {
		if (activity == null)
			return null
		if (activity.needsOwnActivityAdapter)
			activity
		else 
			activity.parent?.adapterProvidingActivity
	}
	
	// ActivityOperation
	
	def getActivity(ActivityOperation operation) {
		operation?.eContainer as Activity
	}
	
	// DataTypeMapping
	def getOperationMapping(DataTypeMapping dataTypeMapping) {
		dataTypeMapping?.eContainer as OperationMapping
	}
	
	
	// Field 
	
	def parentActivity(Field field) {
		var EObject current = field
		while (current != null && !(current instanceof Activity)) {
			current = current.eContainer
		}
		return current as Activity
	}
	
	def findOperationMappingForOperation(Field field, Operation operation) {
		if (field?.operations != null) {
			for (op : field.operations) {
				if (op?.name == operation) {
					return op
				}
			}
		}
		return null
	}
	
	def identifier(Field field) {
		if (field?.uniqueId == null) field?.fullyQualifiedName else field.uniqueId
	}
	
	
	// OperationCall
	
	def methodName(OperationCall opCall) {
		opCall
	}
	
	def block(XExpression expr) {
		if (expr != null)
			EcoreUtil2::getContainerOfType(expr.eContainer, typeof(XBlockExpression))
		else 
			null
	}
	
	def description(OperationCall opCall) {
		opCall.useCasePath
	}
	
	
	// OperationMapping
	
	def field(OperationMapping mapping) {
		mapping?.eContainer as Field
	}


	// OperationParameterAssignment
	
	def OperationCall operation(OperationParameterAssignment opParamAssignment) {
		opParamAssignment?.eContainer as OperationCall
	}
	
	// PackageDeclaration
	
	def Toolkit getToolkit(PackageDeclaration pack) {
		val sut = pack?.elements?.filter(typeof(Toolkit))?.last
		if (sut == null)
			pack?.sutRef
		else 
			sut
	}
	
	// Predicate

	def PackageDeclaration getPackageDeclaration(Predicate predicate) {
		predicate?.eContainer as PackageDeclaration
	}
	
	def TagsDeclaration getTagsDeclaration(Tag tag) {
		tag?.eContainer as TagsDeclaration
	}
	
	
	// Test
	
	def PackageDeclaration getPackageDeclaration(Test test) {
		test?.eContainer as PackageDeclaration
	}
	
	def Toolkit getSut(Test test) {
		test?.packageDeclaration?.toolkit
	}
	
	// Type
	
	def DataType defaultDataType(Field field, Type type) {
		if (type != null) {
			val scope = scopeProvider.getScope(field, TDslPackage$Literals::OPERATION_MAPPING__DATA_TYPE)
			val objectDescriptions = scope.allElements.filter [ "true".equals(it.getUserData("isDefault")) ]
			for (element : objectDescriptions) {
				val dataTypeProxy = element?.EObjectOrProxy as DataType
				val dataType = EcoreUtil::resolve(dataTypeProxy, field) as DataType

				if (dataType.type == type) {
					return dataType
				}
			}
			if (!objectDescriptions.empty)
				return (objectDescriptions.last.EObjectOrProxy as DataType)
			return null
		}
	}
	
	// UseCase
	def Activity returnedActivity(UseCase useCase) {
		if (useCase?.nextActivity?.next != null) 
			useCase.nextActivity.next
		else
			useCase?.initialActivity
	}
	
	
	// XExpression
	
	def Integer getIndexInParentBlock(XExpression expr) {
		if (expr == null) {
			return -1
		}
		var EObject currentExpr = expr
		var parent = expr.eContainer
		while (!(parent instanceof XBlockExpression) && parent != null) {
			currentExpr = parent
			parent = parent.eContainer
		}
		if (parent == null) {
			return -1
		}
		val index = (parent as XBlockExpression).expressions.indexOf(currentExpr)
		return index
	}
	
	def XExpression precedingExpression(XExpression expr) {
		logger.debug("Determining precedingExpression for " + expr + "[" + expr.useCasePath + "]")
		if (expr == null)
			return null
		var XExpression lastStatement
		val exprBlock = expr.block
		val index = expr.indexInParentBlock
		if (index > 0) {
			val expressions = exprBlock?.expressions
			val offset = index - 1
			val statement = (expressions?.get(offset)) as StatementLine
			lastStatement = statement?.statement
		} else {
			val parentIndex = exprBlock?.indexInParentBlock
			if (parentIndex == null || parentIndex == -1) {
				return null
			}
			val parentBlock = EcoreUtil2::getContainerOfType(exprBlock?.eContainer, typeof(XBlockExpression))
			return parentBlock?.expressions?.get(parentIndex)?.precedingExpression
		}
		return lastStatement			
	}
	
	def XExpression lastExpressionWithNextActivity(XExpression expr) {
		if (expr == null)
			return null
		var XExpression lastStatement
		val exprBlock = expr.block
		val index = expr.indexInParentBlock
		if (index > 0) {
			val offset = index - 1
			lastStatement = (exprBlock?.expressions?.get(offset) as StatementLine)?.statement
			if (!lastStatement?.containsActivitySwitchingOperation) {
				return lastStatement?.lastExpressionWithNextActivity
			}
		} else {
			val parentIndex = exprBlock?.indexInParentBlock
			if (parentIndex == null || parentIndex == -1) {
				return null
			}
			val parentBlock = EcoreUtil2::getContainerOfType(exprBlock?.eContainer, typeof(XBlockExpression))
			return parentBlock?.expressions?.get(parentIndex)?.lastExpressionWithNextActivity
		}
		return lastStatement	
	}
		
	def dispatch boolean containsActivitySwitchingOperation(OperationCall call) {
		true
	}
	
	def dispatch boolean containsActivitySwitchingOperation(ActivityOperationCall call) {
		true
	}
	
	def dispatch boolean containsActivitySwitchingOperation(SubUseCaseCall call) {
		true
	}
	
	def dispatch boolean containsActivitySwitchingOperation(XExpression expr) {
		if (expr == null)
			return false
		!(EcoreUtil2::getAllContentsOfType(expr, typeof(OperationCall)).empty && EcoreUtil2::getAllContentsOfType(expr, typeof(ActivityOperationCall)).empty && EcoreUtil2::getAllContentsOfType(expr, typeof(SubUseCaseCall)).empty )
	}
	
	def dispatch List<Activity> explicitNextActivities(OperationCall call) {
		val nextActivities = call?.operation?.nextActivities.map([e | e.next])
		if (nextActivities == null || nextActivities.empty)
			null
		else 
			nextActivities
	}
	
	def dispatch List<Activity> explicitNextActivities(ActivityOperationCall call) {
		val nextActivities = call?.operation?.nextActivities.map([e | e.next])
		if (nextActivities == null || nextActivities.empty)
			null
		else 
			nextActivities
	}
	
	def dispatch List<Activity> explicitNextActivities(SubUseCaseCall call) {
		val nextActivity = call?.useCase?.nextActivity?.next
		if (nextActivity != null)
			Collections::singletonList(nextActivity)
		null
	}

	def dispatch List<Activity> explicitNextActivities(XExpression call) {
		null
	}
	
	def XExpression activitySwitchingOperation(XExpression expr) {
		var XExpression operation = null
		if (expr instanceof OperationCall || expr instanceof ActivityOperationCall || expr instanceof SubUseCaseCall)
			return expr
		val opCalls = EcoreUtil2::getAllContentsOfType(expr, typeof(OperationCall))
		if (!opCalls.empty) {
			operation = opCalls.get(0)
		}
		if (operation == null) {
			val aOpCalls = EcoreUtil2::getAllContentsOfType(expr, typeof(ActivityOperationCall))
			if (!aOpCalls.empty)
				operation = aOpCalls.get(0)
		}
		if (operation == null) {
			val subUseCaseCalls = EcoreUtil2::getAllContentsOfType(expr, typeof(SubUseCaseCall))	
			if (!subUseCaseCalls.empty)
				operation = subUseCaseCalls.get(0)		
		}
		operation
	}
	
	def getUseCase(XExpression expr) {
		EcoreUtil2::getContainerOfType(expr, typeof(UseCase))
	}
	
	def getActivityOperation(XExpression expr) {
		EcoreUtil2::getContainerOfType(expr, typeof(ActivityOperation))
	}
	
	def String getUseCasePath(XExpression expr) {
		if (expr == null)
			return null
		val index = expr?.indexInParentBlock
		if (index >= 0) {
			expr?.block?.useCasePath + "::" + index			
		} else {
			val useCase = expr.useCase
			if (useCase != null)
				expr?.useCase?.name 
			else 
				expr?.activityOperation?.name
		}
	}
}