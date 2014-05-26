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
class CompileExpressionsTest {
	
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
			add(#[	"testSelectNoTagSpec", 
					'''''', 
					"val testVal = select(StringDT)",
					''''''
			])
			add(#[	"testSelectTagSpec",
				'''''',
				"val testVal = select(StringDT, tags Invalid, Empty)",
				''''''
			])
			add(#[	"testSelectDynamicTagSpec",
				'''''',
				"val testVal = select(StringDT, tags Invalid, Empty)",
				''''''
			])
		]
	}
	
	@Test
	def void inUseCase() {
		'''
		«BasicTestSetupTest.BASIC_TEST_PREAMBLE»
		
		«localPreamble»
		
			
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
			
			«localPreamble»
			
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
		
		«localPreamble»
		
				useCase SampleUseCase(StringDT param) initial ViewActivity {
				if (true) {
					«preamble + expression + suffix»
				}
			}
		}
		'''.parse.assertNoErrors
	}
	
	def String localPreamble() '''
		tags {
			Invalid, Empty, Mandatory
		}
			
		datatype TestDT type String {
			class shortString classValue "shortString"
			class emptyString classValue "" tags Empty
			class invalidString classValue "someInvalidValue" tags Invalid 
		}				
	'''
		
}