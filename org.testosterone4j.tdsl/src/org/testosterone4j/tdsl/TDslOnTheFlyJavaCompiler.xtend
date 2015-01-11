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
package org.testosterone4j.tdsl

import org.eclipse.xtext.xbase.compiler.OnTheFlyJavaCompiler

class TDslOnTheFlyJavaCompiler extends OnTheFlyJavaCompiler {
	
	override protected getComplianceLevelArg() {
		return "-1.6";
	}
	
}