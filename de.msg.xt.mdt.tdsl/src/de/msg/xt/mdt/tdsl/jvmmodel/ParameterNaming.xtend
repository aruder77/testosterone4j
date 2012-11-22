package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import javax.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName

class ParameterNaming {
	
	@Inject extension IQualifiedNameProvider
	
	
	def QualifiedName classFullyQualifiedName(Activity activity) {
		activity.fullyQualifiedName
	}

	def String classSimpleName(Activity activity) {
		activity.name.toFirstUpper
	}	
}