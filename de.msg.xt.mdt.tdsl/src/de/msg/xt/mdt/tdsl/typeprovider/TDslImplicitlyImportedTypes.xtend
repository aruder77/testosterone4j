package de.msg.xt.mdt.tdsl.typeprovider

import de.msg.xt.mdt.base.Tag
import java.util.ArrayList
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedTypes
import org.junit.Assert
import de.msg.xt.mdt.base.Predicates

class TDslImplicitlyImportedTypes extends ImplicitlyImportedTypes {
	
	override protected getStaticImportClasses() {
		val list = new ArrayList<Class<?>>()
		list.addAll(super.staticImportClasses)
		list.add(typeof(Assert))
		list.add(typeof(Predicates))
		list
	}		
}