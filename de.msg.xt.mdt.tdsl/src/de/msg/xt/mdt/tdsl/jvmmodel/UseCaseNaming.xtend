package de.msg.xt.mdt.tdsl.jvmmodel

import javax.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import de.msg.xt.mdt.tdsl.tDsl.UseCase
import org.eclipse.xtext.naming.QualifiedName

class UseCaseNaming {
	@Inject extension IQualifiedNameProvider
	
	def QualifiedName class_FQN(UseCase useCase) {
		useCase.fullyQualifiedName
	}
	
}