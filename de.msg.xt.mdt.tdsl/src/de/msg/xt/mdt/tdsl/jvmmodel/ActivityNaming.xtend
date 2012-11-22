package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import javax.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName

class ActivityNaming {
	
	@Inject extension IQualifiedNameProvider
	
	
	def QualifiedName class_FQN(Activity activity) {
		activity.fullyQualifiedName
	}

	def String class_SimpleName(Activity activity) {
		activity.name.toFirstUpper
	}	
}