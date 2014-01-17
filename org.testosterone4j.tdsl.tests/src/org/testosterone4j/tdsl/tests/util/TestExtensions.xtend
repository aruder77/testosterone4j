package org.testosterone4j.tdsl.tests.util

import org.eclipse.xtext.xbase.compiler.CompilationTestHelper

class TestExtensions {

	def int compileAllClasses(CompilationTestHelper$Result result) {
		for (key: result.generatedCode.keySet)
			result.getCompiledClass(key)
		result.generatedCode.size
	}	
}