package org.testosterone4j.tdsl.typeprovider

import java.util.ArrayList
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedFeatures
import org.junit.Assert
import org.testosterone4j.base.Predicates
import org.testosterone4j.base.RandomExtensions

class TDslImplicitlyImportedTypes extends ImplicitlyImportedFeatures {
	
	override protected getStaticImportClasses() {
		val list = new ArrayList<Class<?>>()
		list.addAll(super.staticImportClasses)
		list.add(typeof(Assert))
		list
	}		
	
	override protected getExtensionClasses() {
		val list = super.extensionClasses
		list.add(Predicates)
		list.add(RandomExtensions)
		list
	}

}