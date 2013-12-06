package de.msg.xt.mdt.tdsl.tests.headless

import de.msg.xt.mdt.tdsl.TDslInjectorProvider
import de.msg.xt.mdt.tdsl.tDsl.TestModel
import javax.inject.Inject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper
import org.eclipse.xtext.xbase.lib.util.ReflectExtensions
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert

@RunWith(XtextRunner)
@InjectWith(TDslInjectorProvider)
class ActivityCompileTest {
	
	@Inject extension ParseHelper<TestModel>
	@Inject extension CompilationTestHelper
	@Inject extension ReflectExtensions
	
	def wrapEnvelope(CharSequence body) {
		BasicTestSetupTest.packageEnvelope('''
			«BasicTestSetupTest.STRING_TYPE»
			«BasicTestSetupTest.STRINGDT_DATATYPE»
			«BasicTestSetupTest.STDTOOLKIT_TOOLKIT»
			«BasicTestSetupTest.TEXTCONTROL_CONTROL»
			
			«body»			
		''')
	}
	
	@Test
	def void testActivityMethodReturnType() {
		'''
			activity TestActivity {
				op void voidActivityMethod
			}
		'''.wrapEnvelope.compile[
			val clazz = getCompiledClass("tdsl.testpackage.StdtoolkitActivityAdapter")
			Assert.assertTrue(clazz.methods.map[name].contains("getTextControl"))
		]
	}
	
}