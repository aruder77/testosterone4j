package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import org.eclipse.xtext.naming.IQualifiedNameProvider
import javax.inject.Inject
import de.msg.xt.mdt.tdsl.tDsl.Field
import org.eclipse.emf.ecore.EObject
import de.msg.xt.mdt.tdsl.tDsl.Operation
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.EcoreUtil2
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.SUT
import de.msg.xt.mdt.tdsl.tDsl.Test
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration

/**
 * Convenience meta-model extensions. Please order by Metamodel-Class and alphabetically!
 */
class MetaModelExtensions {
	
	@Inject extension IQualifiedNameProvider
	
	
	// Activity 
	
	def identifier(Activity activity) { 
		if (activity.uniqueId == null) activity.fullyQualifiedName else activity.uniqueId
	}	
	
	def getPackageDeclaration(Activity activity) {
		activity.eContainer as PackageDeclaration
	}
	
	def SUT getSut(Activity activity) {
		activity?.packageDeclaration?.sut
	}
	
	
	// ActivityOperation
	
	def getActivity(ActivityOperation operation) {
		operation.eContainer as Activity
	}
	
	
	// DataTypeMapping
	def getOperationMapping(DataTypeMapping dataTypeMapping) {
		dataTypeMapping.eContainer as OperationMapping
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
		for (op : field.operations) {
			if (op.name == operation) {
				return op
			}
		}
		return null
	}
	
	def identifier(Field field) {
		if (field.uniqueId == null) field.fullyQualifiedName else field.uniqueId
	}
	
	
	// OperationCall
	
	def methodName(OperationCall opCall) {
		opCall
	}
	
	def block(XExpression expr) {
		if (expr != null)
			EcoreUtil2::getContainerOfType(expr, typeof(XBlockExpression))
		else 
			null
	}
	
	
	// OperationMapping
	
	def field(OperationMapping mapping) {
		mapping.eContainer as Field
	}


	// OperationParameterAssignment
	
	def OperationCall operation(OperationParameterAssignment opParamAssignment) {
		opParamAssignment.eContainer as OperationCall
	}
	
	// PackageDeclaration
	
	def SUT getSut(PackageDeclaration pack) {
		val sut = pack?.elements?.filter(typeof(SUT))?.last
		if (sut == null)
			pack.sutRef
		else 
			sut
	}
	
	// Test
	
	def PackageDeclaration getPackageDeclaration(Test test) {
		test.eContainer as PackageDeclaration
	}
	
	def SUT getSut(Test test) {
		test?.packageDeclaration?.sut
	}
	
	// XExpression
	
	def XExpression lastExpressionWithNextActivity(XExpression expr) {
		if (expr == null)
			return null
		var XExpression lastStatement
		val exprBlock = expr.block
		val index = expr.indexInParentBlock
		if (index > 0) {
			lastStatement = (exprBlock.expressions.get(index - 1) as StatementLine).statement
			if (!lastStatement.containsActivitySwitchingOperation) {
				return lastStatement.lastExpressionWithNextActivity
			}
		} else {
			val parentIndex = exprBlock.indexInParentBlock
			if (parentIndex == null) {
				return null
			}
			val parentBlock = EcoreUtil2::getContainerOfType(exprBlock.eContainer, typeof(XBlockExpression))
			return parentBlock?.expressions?.get(parentIndex)?.lastExpressionWithNextActivity
		}
		return lastStatement	
	}
		
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
	
	def containsActivitySwitchingOperation(XExpression expr) {
		if (expr == null)
			return false
		if (expr instanceof OperationCall || expr instanceof ActivityOperationCall) {
			true
		} else {
			!(EcoreUtil2::getAllContentsOfType(expr, typeof(OperationCall)).empty && EcoreUtil2::getAllContentsOfType(expr, typeof(ActivityOperationCall)).empty)
		} 
	}
	
	def activitySwitchingOperation(XExpression expr) {
		var XExpression operation = null
		val opCalls = EcoreUtil2::getAllContentsOfType(expr, typeof(OperationCall))
		if (!opCalls.empty) {
			operation = opCalls.get(0)
		}
		if (operation == null) {
			val aOpCalls = EcoreUtil2::getAllContentsOfType(expr, typeof(ActivityOperationCall))
			if (!aOpCalls.empty)
				operation = aOpCalls.get(0)
		}
		operation
	}
}