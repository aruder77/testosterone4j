package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation
import javax.inject.Inject

class UtilExtensions {
	
	@Inject extension MetaModelExtensions
	
	
	// ActivityOperation
	
	def Activity returnedActivity(ActivityOperation operation) {
   		if (operation.nextActivities.empty)
   			operation.activity
   		else 
   			operation.nextActivities.get(0)?.next
   	}
	
	
	
	// String
	
	def toFieldName(String string) { 
		string?.replace('.', '_')?.replace('@', '_')?.replace(':', '_')?.toFirstLower
	}

	def getterName(String string) {
		'get' + string?.toFirstUpper
	}
	
}