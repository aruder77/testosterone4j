package de.msg.xt.mdt.tdsl.jvmmodel

import javax.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import de.msg.xt.mdt.tdsl.tDsl.DataType

class DataTypeNaming {
	
	@Inject extension IQualifiedNameProvider
	
	def QualifiedName class_FQN(DataType dataType) {
		dataType?.fullyQualifiedName
	}
	
	def String equivalenceClass_name(DataType dataType) {
		dataType?.class_FQN?.toString + "EquivalenceClass"
	}
}