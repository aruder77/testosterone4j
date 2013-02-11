package de.msg.xt.mdt.tdsl.jvmmodel

import org.eclipse.xtext.xbase.scoping.XbaseQualifiedNameProvider
import org.eclipse.emf.ecore.EObject
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import javax.inject.Inject
import org.eclipse.xtext.naming.QualifiedName
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import java.util.ArrayList
import de.msg.xt.mdt.tdsl.tDsl.Operation

class TDslQualifiedNameProvider extends XbaseQualifiedNameProvider {
	
	@Inject extension MetaModelExtensions
	
//	override getFullyQualifiedName(EObject obj) {
//		val qName = super.getFullyQualifiedName(obj)
//		System::out.println("Qualified name [" + obj + "]: "+ qName)
//		qName
//	}
	
	override getFullyQualifiedName(EObject obj) {
		if (obj instanceof DataTypeMapping) {
			val dt = obj as DataTypeMapping
			val opMap = dt.operationMapping
			val opMapName = opMap?.fullyQualifiedName?.toString
			val field = opMap.field
			val fieldName = field?.fullyQualifiedName?.toString
			val activity = field.parentActivity
			val activityName = activity?.fullyQualifiedName?.toString
			val packageDecl = activity.packageDeclaration
			val packageName = packageDecl?.fullyQualifiedName?.toString
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