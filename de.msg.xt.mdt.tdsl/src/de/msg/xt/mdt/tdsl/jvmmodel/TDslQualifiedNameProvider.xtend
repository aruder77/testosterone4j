package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import java.util.ArrayList
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.scoping.XbaseQualifiedNameProvider

class TDslQualifiedNameProvider extends XbaseQualifiedNameProvider {
	
	@Inject extension MetaModelExtensions
	
	override getFullyQualifiedName(EObject obj) {
		if (obj instanceof DataTypeMapping) {
			val dt = obj as DataTypeMapping
			QualifiedName::create(dt.toString)
		} else if (obj instanceof OperationMapping) {
			val opMap = obj as OperationMapping
			val names = new ArrayList<String>
			names.addAll(opMap?.field?.fullyQualifiedName?.segments)
			val opName = opMap.toString
			names.add(opName)
			QualifiedName::create(names)
		} else {
			super.getFullyQualifiedName(obj)
		}
	}
}