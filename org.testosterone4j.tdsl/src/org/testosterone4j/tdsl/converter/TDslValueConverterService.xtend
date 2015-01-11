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
package org.testosterone4j.tdsl.converter

import org.eclipse.xtext.xbase.conversion.XbaseValueConverterService

class TDslValueConverterService extends XbaseValueConverterService {
	
	override String toString(Object value, String lexerRule) {
		if (lexerRule.equals("ID")) {
			val index = value.toString.lastIndexOf(".")
			if (index > 0)
				return value.toString.substring(index + 1)
		} else if (lexerRule.equals("OFQN")) {
			val index = value.toString.lastIndexOf(".")
			if (index > 0) {
				val index2 = value.toString.substring(0, index).lastIndexOf(".")
				if (index2 > 0) {
					
					return "#" + value.toString.substring(index2 + 1, index) + "_#_" + value.toString.substring(index + 1)
				}
			}
		}
		return super.toString(value, lexerRule)
	}
	
}