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
		EcoreUtil2::getContainerOfType(expr, typeof(XBlockExpression))
	}
	
	
	// OperationMapping
	
	def field(OperationMapping mapping) {
		mapping.eContainer as Field
	}

}