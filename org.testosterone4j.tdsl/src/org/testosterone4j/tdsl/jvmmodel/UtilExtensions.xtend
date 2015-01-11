/*
 * #%L
 * org.testosterone4j.tdsl
 * %%
 * Copyright (C) 2015 Axel Ruder
 * %%
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * #L%
 */
package org.testosterone4j.tdsl.jvmmodel

import org.testosterone4j.tdsl.tDsl.Activity
import org.testosterone4j.tdsl.tDsl.ActivityOperation
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