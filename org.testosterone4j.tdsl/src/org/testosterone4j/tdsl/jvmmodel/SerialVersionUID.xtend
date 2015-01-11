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

import org.testosterone4j.tdsl.tDsl.UseCase
import org.testosterone4j.tdsl.tDsl.DataType

class SerialVersionUID {
	
	
	def long calcSerialVersionUID(UseCase useCase) {
		1l
	}
	
	def long calcSerialVersionUID(DataType dataType) {
		1l
	}
	
}