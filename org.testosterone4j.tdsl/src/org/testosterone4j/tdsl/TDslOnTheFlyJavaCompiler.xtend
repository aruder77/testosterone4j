package org.testosterone4j.tdsl

import org.eclipse.xtext.xbase.compiler.OnTheFlyJavaCompiler

class TDslOnTheFlyJavaCompiler extends OnTheFlyJavaCompiler {
	
	override protected getComplianceLevelArg() {
		return "-1.6";
	}
	
}