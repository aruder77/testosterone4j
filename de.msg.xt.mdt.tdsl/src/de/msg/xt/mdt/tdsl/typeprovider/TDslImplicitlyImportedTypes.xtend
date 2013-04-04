package de.msg.xt.mdt.tdsl.typeprovider

import java.util.List
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedTypes
import java.util.ArrayList
import org.junit.Assert

class TDslImplicitlyImportedTypes extends ImplicitlyImportedTypes {
	
	override protected List<Class<?>> getLiteralClasses() {
		val list = new ArrayList<Class<?>>()
		list.addAll(super.literalClasses)
		list.add(typeof(Assert))
		list
	}
}