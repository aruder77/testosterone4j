/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.msg.xt.mdt.tdsl.scoping

import org.eclipse.xtext.common.types.JvmIdentifiableElement
import org.eclipse.xtext.xbase.featurecalls.IdentifiableSimpleNameProvider

class TDslIdentifiableSimpleNameProvider extends IdentifiableSimpleNameProvider {
	
	override getSimpleName(JvmIdentifiableElement element) {
			super.getSimpleName(element)
	}
	
}