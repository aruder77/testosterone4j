package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.Operation
import javax.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider

class FieldNaming {
	@Inject extension IQualifiedNameProvider
	
	def String activityControlDelegationMethodName(Field field, Operation operation) {
		field.name + "_" + operation.name
	}
	
	def String getterName(Field field) {
		"get" + field.name.toFirstUpper
	}
	
}