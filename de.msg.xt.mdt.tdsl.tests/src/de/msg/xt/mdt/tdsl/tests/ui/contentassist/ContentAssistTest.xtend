package de.msg.xt.mdt.tdsl.tests.ui.contentassist

import de.msg.xt.mdt.tdsl.TDslUiInjectorProvider
import de.msg.xt.mdt.tdsl.tests.headless.BasicTestSetupTest
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWithimport org.eclipse.xtext.junit4.ui.util.JavaProjectSetupUtil
import org.eclipse.jdt.core.JavaCore
import org.eclipse.core.runtime.Path
import org.eclipse.xtext.xbase.junit.ui.AbstractContentAssistTest

@RunWith(XtextRunner)
@InjectWith(TDslUiInjectorProvider)
class ContentAssistTest extends MyAbstractContentAssistTest {
	
	@Test
	def void testEmptyFile() {
		newBuilder.assertProposal("package")	
	}	
	
	/**
	 * does not work :(. org.junit needs to be on the classpath for the test project...
	 */
	@Test
	def void testExpectProposalAfterActivitySwitch() {
		newBuilder.append(BasicTestSetupTest.BASIC_TEST_PREAMBLE).append('''
			useCase SampleUseCase initial ViewActivity {
				#openEditor
				#saveAndClose
				expect DialogActivity if (true) {
					#ok
		''').assertProposal("#refresh")
	}
}
	