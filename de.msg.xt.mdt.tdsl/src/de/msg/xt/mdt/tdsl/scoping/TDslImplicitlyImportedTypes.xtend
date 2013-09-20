package de.msg.xt.mdt.tdsl.scoping

import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedTypes

class TDslImplicitlyImportedTypes extends ImplicitlyImportedTypes {
	
	override protected getStaticImportClasses() {
		(super.getStaticImportClasses() + #[Math]).toList
	}
}