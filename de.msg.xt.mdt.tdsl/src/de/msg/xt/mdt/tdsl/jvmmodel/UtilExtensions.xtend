package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation
import javax.inject.Inject

class UtilExtensions {
	
	@Inject extension MetaModelExtensions
	
	
	// ActivityOperation
	
	/**
	 * returns the following activity for the given ActivityOperation.
	 * This is either the currentActivity, if no next activity is specified,
	 * or the explicitly specified next activity, if concretely named, 
	 * or null, if 'returnToLastActivity' or 'usually' is used.
	 */
	def Activity returnedActivity(ActivityOperation operation) {
   		if (operation.nextActivities.empty)
   			operation.activity
   		else { 
   			val condNextAct = operation.nextActivities.get(0)
   			if (!condNextAct.usually && condNextAct.next != null)
   				operation.nextActivities.get(0)?.next
   			else 
   				null	
   		}
   	}
	
	
	
	// String
	
	def toFieldName(String string) { 
		string?.replace('.', '_')?.replace('@', '_')?.replace(':', '_')?.toFirstLower
	}

	def getterName(String string) {
		'get' + string?.toFirstUpper
	}
	
}