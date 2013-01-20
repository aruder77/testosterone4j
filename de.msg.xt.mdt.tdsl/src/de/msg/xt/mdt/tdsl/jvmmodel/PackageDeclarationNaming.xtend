package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import javax.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider

class PackageDeclarationNaming {
	
	@Inject extension IQualifiedNameProvider
	
	
	def String activityAdapter_FQN(PackageDeclaration pack) {
		pack.fullyQualifiedName.toString + ".ActivityAdapter"
	}
}