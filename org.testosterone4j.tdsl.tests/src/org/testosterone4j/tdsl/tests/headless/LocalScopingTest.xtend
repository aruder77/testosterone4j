package org.testosterone4j.tdsl.tests.headless

import org.testosterone4j.tdsl.TDslInjectorProvider
import org.testosterone4j.tdsl.tDsl.TestModel
import org.testosterone4j.tdsl.tests.XtextParameterized
import org.testosterone4j.tdsl.tests.XtextParameterized.Parameter
import org.testosterone4j.tdsl.tests.XtextParameterized.Parameters
import org.testosterone4j.tdsl.tests.headless.BasicTestSetupTest
import java.util.ArrayList
import javax.inject.Inject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(XtextParameterized)
@InjectWith(TDslInjectorProvider)
class LocalScopingTest {
	
	@Inject extension ParseHelper<TestModel>
	@Inject extension ValidationTestHelper
	
	@Parameter(0)
	public String name
	
	@Parameter(1)
	public CharSequence preamble
	
	@Parameter(2)
	public String expression
	
	@Parameter(3)
	public CharSequence suffix
	
	@Parameters(name="{0}")
	def public static Iterable<Object[]> parameters() {
		new ArrayList<Object[]> => [
			add(#[	"testParamAvailableInVariableDeclaration", 
					'''
					val localVar = 
					''', 
					"param",
					''''''
			])
			add(#[	"testParamAvailableInIf", 
					'''
					if ( 
					''', 
					"param",
					'''
					.value == "abc") {
					}'''
			])
			add(#[	"testParamAvailableInIfBlock", 
					'''
					if (true) {
						val blockVar =  
					''', 
					"param",
					'''
					.value
					}'''
			])
			add(#[	"testParamAvailableInAssert", 
					'''
					assert [ println( 
					''', 
					"param",
					'''
					.value) ]'''
			])
			add(#[	"testParamAvailableInXExpression", 
					'''
					[ println( 
					''', 
					"param",
					'''
					.value) ]'''
			])
			add(#[	"testParamAvailableInControlOperationParameterAssignment", 
					'''
					#searchField.setText(str= 
					''', 
					"param",
					'''
					)'''
			])
			add(#[	"testParamAvailableInActivityOperationParameterAssignment", 
					'''
					#append(appendStr= 
					''', 
					"param",
					'''
					)'''
			])
			add(#[	"testParamAvailableInSubUseCaseCall", 
					'''
					val localVar = "Hello, world"
					call SampleTestUseCaseWithParameter(param= 
					''', 
					"param",
					'''
					)'''
			])
			add(#[	"testParamAvailableInActivityExpectationGuard", 
					'''
					expect DialogActivity if ( 
					''', 
					"param",
					'''
					.value == "abc") {}'''
			])
			add(#[	"testLocalVariableAvailableInVariableDeclaration", 
					'''
					val localVar = "Hello, world"
					val var2 =  
					''', 
					"localVar",
					''''''
			])
			add(#[	"testLocalVariableAvailableInIf", 
					'''
					val localVar = "Hello, world"
					if ( 
					''', 
					"localVar",
					'''
					 == "abc") {
					}'''
			])
			add(#[	"testLocalVariableAvailableInIfBlock", 
					'''
					val localVar = "Hello, world"
					if (true) {
						val blockVar =  
					''', 
					"localVar",
					'''
					}'''
			])
			add(#[	"testLocalVariableAvailableInAssert", 
					'''
					val localVar = "Hello, world"
					assert [ println( 
					''', 
					"localVar",
					'''
					) ]'''
			])
			add(#[	"testLocalVariableAvailableInXExpression", 
					'''
					val localVar = "Hello, world"
					[ println( 
					''', 
					"localVar",
					'''
					) ]'''
			])
			add(#[	"testLocalVariableAvailableInControlOperationParameterAssignment", 
					'''
					val localVar = "Hello, world"
					#searchField.setText(str= 
					''', 
					"localVar",
					'''
					)'''
			])
			add(#[	"testLocalVariableAvailableInActivityOperationParameterAssignment", 
					'''
					val localVar = "Hello, world"
					#append(appendStr= 
					''', 
					"localVar",
					'''
					)'''
			])
			add(#[	"testLocalVariableAvailableInSubUseCaseCall", 
					'''
					val localVar = "Hello, world"
					call SampleTestUseCaseWithParameter(param= 
					''', 
					"localVar",
					'''
					)'''
			])
			add(#[	"testLocalVariableAvailableInActivityExpectationGuard", 
					'''
					val localVar = "Hello, world"
					expect DialogActivity if ( 
					''', 
					"localVar",
					'''
					 == "abc") {}'''
			])
			add(#[	"testCurrentActivityInVariableDeclaration", 
					'''
					val localVar = currentActivity.''', 
					"adapter",
					''''''
			])
		]
	}
	
	@Test
	def void inUseCase() {
		'''
		«BasicTestSetupTest.BASIC_TEST_PREAMBLE»
			
			useCase SampleUseCase(StringDT param) initial ViewActivity {
				«preamble + expression + suffix»
			}
		}
		'''.parse.assertNoErrors
	}
	
	@Test
	def void inActivityOperation() {
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
				op append(StringDT appendStr)
				op openEditor => EditorActivity 
				
				op activityOperation(StringDT param) {
					«preamble + expression + suffix»
				}
			}
		}
		'''.parse.assertNoErrors
	}
	
	@Test
	def void inInnerBlock() {
		'''
		«BasicTestSetupTest.BASIC_TEST_PREAMBLE»
			
			useCase SampleUseCase(StringDT param) initial ViewActivity {
				if (true) {
					«preamble + expression + suffix»
				}
			}
		}
		'''.parse.assertNoErrors
	}
	
		
}