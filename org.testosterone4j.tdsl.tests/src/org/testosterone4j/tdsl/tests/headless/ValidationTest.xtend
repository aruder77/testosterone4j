package de.msg.xt.mdt.tdsl.tests.headless

import de.msg.xt.mdt.tdsl.TDslInjectorProvider
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage
import de.msg.xt.mdt.tdsl.tDsl.TestModel
import javax.inject.Inject
import org.eclipse.emf.ecore.EClass
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith
import de.msg.xt.mdt.tdsl.validation.TDslValidator

@RunWith(XtextRunner)
@InjectWith(TDslInjectorProvider)
class ValidationTest {
	
	@Inject extension ParseHelper<TestModel>
	@Inject extension ValidationTestHelper
	
	@Test
	def void checkFirstUpperCaseActivitiesTest() {
		'''
			activity sampleTestActivity {
			}
		'''.assertTDslError(TDslPackage.eINSTANCE.activity, "Activity names must start with an uppercase letter.")
	}
	
	@Test
	def void checkOperationMappingOneMissingTest() {
		'''
			activity SampleTestActivity {
				field shortName control TextControl {
					op void setText(StringDT str)
					op StringDT getText
				}
			}
		'''.assertTDslError(TDslPackage.eINSTANCE.field, "An operation mapping must be defined for operations 'search'.", TDslValidator.UNSUFFICIENT_OPERATION_MAPPINGS)
	}
	
	@Test
	def void checkOperationMappingAllMissingTest() {
		'''
			activity SampleTestActivity {
				field shortName control TextControl {
				}
			}
		'''.assertTDslError(TDslPackage.eINSTANCE.field, "An operation mapping must be defined for operations 'setText', 'getText', 'search'.", TDslValidator.UNSUFFICIENT_OPERATION_MAPPINGS)
	}
	
	@Test
	def void checkControlsInToolkitTest() {
		'''
			activity SampleTestActivity {
				field okButton control Button {
					op void push
				}
			}
		'''.assertTDslError(TDslPackage.eINSTANCE.field, "The control 'Button' is not included in the current toolkit!", TDslValidator.CONTROL_NOT_IN_TOOLKIT)		
	}
	
	@Test
	def void checkTypeComplianceOperationParameterIntTest() {
		'''
			useCase TestUseCase initial ViewActivity {
				#searchField.setText(str=5)
			}
		'''.assertTDslError(TDslPackage.eINSTANCE.operationParameterAssignment, "Value of parameter str must be either StringDT or String, but was int!")
	}
	
	@Test
	def void checkTypeComplianceActivityOperationParameterIntTest() {
		'''
			useCase TestUseCase initial ViewActivity {
				#append(appendStr=5)
			}
		'''.assertTDslError(TDslPackage.eINSTANCE.activityOperationParameterAssignment, "Value of parameter appendStr must be either StringDT or String, but was int!")
	}
	
	/* hmm... not possible since JvmModelInferrer is not called 
	 * @Test
	def void checkTypeComplianceSubUseCaseCallParameterIntTest() {
		'''
			useCase TestUseCase1(StringDT str) initial ViewActivity {
			}
			
			useCase TestUseCase2 initial ViewActivity {
				call TestUseCase1(str=5)
			}
		'''.assertTDslError(TDslPackage.eINSTANCE.parameterAssignment, "Value of parameter appendStr must be either StringDT or String, but was int!")
	}*/
	
	
	private def void assertTDslError(CharSequence fragment, EClass eClass, String message) {
		assertTDslError(fragment, eClass, message, null)
	}

	private def void assertTDslError(CharSequence fragment, EClass eClass, String message, String code) {
		fragment.stage.assertError(eClass, code, message)
	}
	
	private def stage(CharSequence fragment) {
		'''
		«BasicTestSetupTest.BASIC_TEST_PREAMBLE»
			«fragment»
		}
		'''.parse
	}
}