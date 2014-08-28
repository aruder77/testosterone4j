package org.testosterone4j.tdsl.tests.ui.contentassist

import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.testosterone4j.tdsl.TDslUiInjectorProvider
import org.testosterone4j.tdsl.tests.headless.BasicTestSetupTest
import org.junit.Ignore

@RunWith(XtextRunner)
@InjectWith(TDslUiInjectorProvider)
class DebugContentAssistTest extends MyAbstractContentAssistTest {
	
	@Test @Ignore
	def void inActivityOperation() {
		val builder = newBuilder.append(BasicTestSetupTest.BASIC_TEST_PREAMBLE).append('''
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
		''')
		
		builder.append("val localVar = ").assertProposal("param")
	}	
	
	@Test
	def void inActivityOperation2() {
		val builder = newBuilder.append(BasicTestSetupTest.BASIC_TEST_PREAMBLE).append('''
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
		''')
		
		builder.append("if (").assertProposal("param")
	}	
	
}
	