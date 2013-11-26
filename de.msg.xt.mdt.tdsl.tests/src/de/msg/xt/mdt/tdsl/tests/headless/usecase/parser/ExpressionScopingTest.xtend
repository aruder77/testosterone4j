package de.msg.xt.mdt.tdsl.tests.headless.usecase.parser

import de.msg.xt.mdt.tdsl.TDslInjectorProvider
import de.msg.xt.mdt.tdsl.tDsl.TestModel
import de.msg.xt.mdt.tdsl.tests.headless.BasicTestSetupTest
import javax.inject.Inject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(XtextRunner)
@InjectWith(TDslInjectorProvider)
class ExpressionScopingTest {
	
	@Inject extension ParseHelper<TestModel>
	@Inject extension ValidationTestHelper
	
	@Test
	def void testInitialActivity() {
		'''
		#refresh
		'''.parseTest
	}
	
	@Test
	def void testActivityOperationActivitySwitch() {
		'''
		#openEditor
		#saveAndClose
		'''.parseTest
	}
	
	@Test
	def void testControlOperationActivitySwitch() {
		'''
		#searchField.search
		#refreshSearch
		'''.parseTest
	}
	
	@Test
	def void testInitialActivityFallback() {
		'''
		#refresh
		#operation1
		'''.parseTest
	}
	
	@Test
	def void testBasicVariableExpression() {
		'''
		val testVal = #searchField.getText
		'''.parseTest
	}
	
	@Test
	def void testAfterVariableExpression() {
		'''
		val testVal = #searchField.getText
		#refresh
		'''.parseTest
	}
	
	@Test
	def void testIfExpression() {
		'''
		if(true) {
			#operation1
		} else {
			#operation1
		}
		#refresh
		'''.parseTest
	}
	
	@Test
	def void testActivityOperationSwitchInIfExpression() {
		'''
		if(true) {
			#openEditor
			#saveAndClose
		} else {
			#openEditor
			#saveAndClose
		}
		#refresh
		'''.parseTest
	}
	
	@Test
	def void testControlOperationSwitchInIfExpression() {
		'''
		if(true) {
			#searchField.search
			#refreshSearch
		} else {
			#searchField.search
			#refreshSearch
		}			
		#refresh
		'''.parseTest
	}
	
	@Test
	def void testInitialActivityInExpect() {
		'''
		#openEditor
		#saveAndClose
		expect DialogActivity if (true) {
			#ok
		}
		'''.parseTest
	}
	
	@Test
	def void testAfterExpect() {
		'''
		#openEditor
		#saveAndClose
		expect DialogActivity if (true) {
		}
		#refresh
		'''.parseTest
	}
	
	@Test
	def void testActivityOperationActivitySwitchInExpect() {
		'''
		#openEditor
		#saveAndClose
		expect DialogActivity if (true) {
			#ok
			#refresh
		}
		'''.parseTest
	}
	
	@Test
	def void testControlOperationActivitySwitchInExpect() {
		'''
		#openEditor
		#saveAndClose
		expect DialogActivity if (true) {
			#dialogTextField.search
			#refreshSearch
		}
		'''.parseTest
	}
	
	@Test
	def void testAfterXExpression() {
		'''
		[ println(":-)") ]
		#refresh
		'''.parseTest
	}
	
	@Test
	def void testAfterAssert() {
		'''
		assert [ println(":-)") ]
		#refresh
		'''.parseTest
	}
	
	@Test
	def void testReturnToLastActivity() {
		'''
		#openEditor
		#openDialog
		#cancel
		#saveAndClose
		'''.parseTest
	}
	
	def void parseTest(CharSequence fragment) {
		'''
		«BasicTestSetupTest.BASIC_TEST_PREAMBLE»
			
			useCase SampleUseCase initial ViewActivity {
				«fragment»
			}
		}
		'''.parse.assertNoErrors
		
		'''
		«BasicTestSetupTest.BASIC_TEST_PREAMBLE»
			
			activity SampleTestActivity {
				
				field searchField control TextControl {
					op void setText(StringDT str)
					op StringDT getText
					op void search => SearchResult
				}
				op refresh
				op operation1
				op openEditor => EditorActivity 
				
				op activityOperation {
					«fragment»
				}
			}
		}
		'''.parse.assertNoErrors
	}
}