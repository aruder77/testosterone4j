package de.msg.xt.mdt.tdsl.tests.headless

import de.msg.xt.mdt.tdsl.TDslInjectorProvider
import de.msg.xt.mdt.tdsl.tDsl.TestModel
import javax.inject.Inject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(XtextRunner)
@InjectWith(TDslInjectorProvider)
class BasicTestSetupTest {
	
	static def packageEnvelope(CharSequence body) 
	'''
		«TEST_PACKAGE» {
			«body»
		}
	'''
	
	public static val TEST_PACKAGE = '''
		package tdsl.testpackage
	'''
	
	public static val STRING_TYPE = '''
		type String mappedBy String
	'''
	
	public static val STRINGDT_DATATYPE = '''
		datatype StringDT type String {
			class shortString classValue "shortString"
		}	
	'''
	
	public static val INVALID_EMPTY_TAGS = '''
		tags {
			Empty, Invalid
		}				
	'''
	
	public static val TEXTCONTROL_CONTROL = '''
		control TextControl {
			op void setText(String str)
			op String getText
			op void search
		}
	'''

	public static val BUTTON_CONTROL = '''
		control Button {
			op void push
		}
	'''
	
	public static val STDTOOLKIT_TOOLKIT = '''
		toolkit Stdtoolkit using controls {
			TextControl
		}				
	'''
	
	public static val SIMPLE_EDITOR_ACTIVITY = '''
		activity EditorActivity {
			field shortName control TextControl {
				op void setText(StringDT str)
				op StringDT getText
				op void search
			}
			
			op saveAndClose
		}
	'''
	
	public static val EDITOR_ACTIVITY = '''
		activity EditorActivity {
			field shortName control TextControl {
				op void setText(StringDT str)
				op StringDT getText
				op void search
			}
			
			op openDialog => DialogActivity
			op saveAndClose => usually ViewActivity
		}
	'''
	
	public static val SIMPLE_VIEW_ACTIVITY = '''
		activity ViewActivity {
			field searchField control TextControl {
				op void setText(StringDT str)
				op StringDT getText
				op void search
			}
			op refresh
			op openEditor => EditorActivity 
		}
	'''

	public static val VIEW_ACTIVITY = '''
		activity ViewActivity {
			field searchField control TextControl {
				op void setText(StringDT str)
				op StringDT getText
				op void search => SearchResult
			}
			op refresh
			op operation1
			op append(StringDT appendStr)
			op openEditor => EditorActivity 
		}
	'''
	
	public static val SEARCH_RESULT_ACTIVITY = '''
		activity SearchResult {
			op refreshSearch
		}	
	'''
	
	public static val SIMPLE_DIALOG_ACTIVITY = '''
		activity DialogActivity {
			field dialogTextField control TextControl {
				op void setText(StringDT str)
				op StringDT getText
				op void search
			}
			op cancel => returnToLastActivity
		}
	'''
	
	public static val DIALOG_ACTIVITY = '''
		activity DialogActivity {
			field dialogTextField control TextControl {
				op void setText(StringDT str)
				op StringDT getText
				op void search => SearchResult
			}
			op ok => ViewActivity
			op cancel => returnToLastActivity
		}
	'''
	
	public static val SAMPLE_TEST_USECASE = '''
		useCase SampleTestUseCase initial EditorActivity {
			#shortName.setText
			#saveAndClose
		}
	'''
	
	public static val SAMPLE_TEST_USECASE_WITH_ACTIVITY_SWITCH = '''
		useCase SampleTestUseCaseWithActivitySwitch initial ViewActivity {
			#openEditor
		} => EditorActivity
	'''
	
	public static val SAMPLE_TEST_USECASE_WITH_PARAMETER = '''
		useCase SampleTestUseCaseWithParameter(StringDT param) initial ViewActivity {
			#refresh
		}
	'''
	
	public static val SAMPLE_TEST_USECASE_TEST = '''
		test SampleTestUseCaseTest generator de.msg.xt.mdt.base.SampleTestGenerator useCase SampleTestUseCase
	'''
	
	
	public static val BASIC_TEST_PREAMBLE = '''
		«TEST_PACKAGE» {
			
			«STRING_TYPE»
		
			«STRINGDT_DATATYPE»
		
			«TEXTCONTROL_CONTROL»
		
			«BUTTON_CONTROL»
		
			«STDTOOLKIT_TOOLKIT»
		
			«EDITOR_ACTIVITY»

			«VIEW_ACTIVITY»
		
			«SEARCH_RESULT_ACTIVITY»
		
			«DIALOG_ACTIVITY»
		
			«SAMPLE_TEST_USECASE»
		
			«SAMPLE_TEST_USECASE_WITH_ACTIVITY_SWITCH»
		
			«SAMPLE_TEST_USECASE_WITH_PARAMETER»
		
			«SAMPLE_TEST_USECASE_TEST»
	'''
	
	@Inject extension ParseHelper<TestModel>
	@Inject extension ValidationTestHelper
	
	@Test
	def void test_100_PackageDefinition() {
		'''
			«TEST_PACKAGE» {
			}
		'''.parse.assertNoErrors
	}
	
	@Test
	def void test_200_TypeDefinition() {
		packageEnvelope('''
			«STRING_TYPE»
		''').parse.assertNoErrors
	}
	
	@Test
	def void test_300_DataTypeDefinition() {
		packageEnvelope('''
			«STRING_TYPE»
			
			«STRINGDT_DATATYPE»
		''').parse.assertNoErrors		
	}
	
	@Test
	def void test_400_TagDefinition() {
		packageEnvelope('''
			«INVALID_EMPTY_TAGS»
		''').parse.assertNoErrors				
	}
	
	@Test
	def void test_500_ControlDefinition() {
		packageEnvelope('''
			«STRING_TYPE»
			
			«TEXTCONTROL_CONTROL»
		''').parse.assertNoErrors				
	}
	
	@Test
	def void test_600_ToolkitDefinition() {
		packageEnvelope('''
			«STRING_TYPE»
			
			«TEXTCONTROL_CONTROL»
			
			«STDTOOLKIT_TOOLKIT»
		''').parse.assertNoErrors				
	}
	
	@Test
	def void test_700_ActivityDefinition() {
		packageEnvelope('''
			«STRING_TYPE»
			
			«STRINGDT_DATATYPE»
			
			«TEXTCONTROL_CONTROL»
			
			«STDTOOLKIT_TOOLKIT»
			
			«SIMPLE_EDITOR_ACTIVITY»
		''').parse.assertNoErrors				
	}
	
	@Test
	def void test_701_ActivityDefinitionWithNavigation() {
		packageEnvelope('''
			«STRING_TYPE»
			
			«STRINGDT_DATATYPE»
			
			«TEXTCONTROL_CONTROL»
			
			«STDTOOLKIT_TOOLKIT»
			
			«EDITOR_ACTIVITY»
			
			«SIMPLE_DIALOG_ACTIVITY»
			
			«SIMPLE_VIEW_ACTIVITY»
		''').parse.assertNoErrors
	}
	
	@Test
	def void test_701_ActivityDefinitionWithReturnToLastActivity() {
		packageEnvelope('''
			«STRING_TYPE»
			
			«STRINGDT_DATATYPE»
			
			«TEXTCONTROL_CONTROL»
			
			«STDTOOLKIT_TOOLKIT»
			
			«SIMPLE_DIALOG_ACTIVITY»
		''').parse.assertNoErrors
	}
	
	@Test
	def void test_800_UseCaseDefinition() {
		packageEnvelope('''
			«STRING_TYPE»
			
			«STRINGDT_DATATYPE»
			
			«TEXTCONTROL_CONTROL»
			
			«STDTOOLKIT_TOOLKIT»
			
			«SIMPLE_EDITOR_ACTIVITY»
			
			«SAMPLE_TEST_USECASE»
		''').parse.assertNoErrors
	}

	@Test
	def void test_801_UseCaseDefinitionWithActivitySwitch() {
		packageEnvelope('''
			«STRING_TYPE»
			
			«STRINGDT_DATATYPE»
			
			«TEXTCONTROL_CONTROL»
			
			«STDTOOLKIT_TOOLKIT»
			
			«SIMPLE_EDITOR_ACTIVITY»
			
			«SIMPLE_VIEW_ACTIVITY»
			
			«SAMPLE_TEST_USECASE_WITH_ACTIVITY_SWITCH»
		''').parse.assertNoErrors
	}

	@Test
	def void test_900_TestDefinition() {
		packageEnvelope('''
			«STRING_TYPE»
			
			«STRINGDT_DATATYPE»
			
			«TEXTCONTROL_CONTROL»
			
			«STDTOOLKIT_TOOLKIT»
			
			«SIMPLE_EDITOR_ACTIVITY»
			
			«SAMPLE_TEST_USECASE»
			
			«SAMPLE_TEST_USECASE_TEST»
		''').parse.assertNoErrors
	}
	
	@Test
	def void test_1000_testBasicTestPramble() {
		'''
		«BASIC_TEST_PREAMBLE»
		}
		'''.parse.assertNoErrors
	}
	
}