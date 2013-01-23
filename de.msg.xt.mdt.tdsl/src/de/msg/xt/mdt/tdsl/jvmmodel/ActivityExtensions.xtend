package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import org.eclipse.xtext.naming.IQualifiedNameProvider
import javax.inject.Inject

class ActivityExtensions {
	
	@Inject extension IQualifiedNameProvider
	
	def identifier(Activity activity) { 
		if (activity.uniqueId == null) activity.fullyQualifiedName else activity.uniqueId
	}	
	
}