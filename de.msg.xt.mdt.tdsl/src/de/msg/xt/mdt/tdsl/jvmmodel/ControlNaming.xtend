package de.msg.xt.mdt.tdsl.jvmmodel

import org.eclipse.xtext.naming.QualifiedName
import javax.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import de.msg.xt.mdt.tdsl.tDsl.Control

class ControlNaming {
	
	@Inject extension IQualifiedNameProvider
	
	
	def QualifiedName class_FQN(Control control) {
		control.fullyQualifiedName
	}
	
}