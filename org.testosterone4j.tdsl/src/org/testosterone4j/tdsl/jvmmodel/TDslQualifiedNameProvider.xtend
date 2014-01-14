package org.testosterone4j.tdsl.jvmmodel

import org.testosterone4j.tdsl.tDsl.ActivityOperationParameterAssignment
import org.testosterone4j.tdsl.tDsl.DataTypeMapping
import org.testosterone4j.tdsl.tDsl.OperationCall
import org.testosterone4j.tdsl.tDsl.OperationMapping
import org.testosterone4j.tdsl.tDsl.OperationParameterAssignment
import org.testosterone4j.tdsl.tDsl.ParameterAssignment
import java.util.ArrayList
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.scoping.XbaseQualifiedNameProvider
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.emf.ecore.impl.EObjectImpl
import org.testosterone4j.tdsl.tDsl.impl.OperationMappingImpl

class TDslQualifiedNameProvider extends XbaseQualifiedNameProvider {
	
	@Inject extension MetaModelExtensions
	
	override getFullyQualifiedName(EObject obj) {		
		if (obj instanceof DataTypeMapping) {
			val dt = obj as DataTypeMapping
			QualifiedName::create(dt.toString)
		} else if (obj instanceof ParameterAssignment) {
			val pa = obj as ParameterAssignment
			QualifiedName::create(pa.toString)
		} else if (obj instanceof OperationMapping) {
			val opMap = obj as OperationMapping
			val names = new ArrayList<String>
			names.addAll(opMap?.field?.fullyQualifiedName?.segments)
			val operation = (opMap as OperationMappingImpl).basicGetName
			val opName = if (operation == null || operation.eIsProxy) opMap.toString else operation.name
			names.add(opName)
			QualifiedName::create(names)
		} else if (obj instanceof OperationCall) {
			val call = obj as OperationCall
			QualifiedName::create(call.toString)			 
		} else if (obj instanceof OperationParameterAssignment) { 
			val opParamAssignment = obj as OperationParameterAssignment
			val names = new ArrayList<String>
//			if (opParamAssignment?.operation?.fullyQualifiedName?.segments != null)
//				names.addAll(opParamAssignment.operation.fullyQualifiedName.segments)
//			var opName = opParamAssignment?.name?.name?.name
//			if (opName == null)
			val opName = opParamAssignment.toString
			names.add(opName)
			QualifiedName::create(names)
		} else if (obj instanceof ActivityOperationParameterAssignment) {
			val assignment = obj as ActivityOperationParameterAssignment
			QualifiedName::create(assignment.toString)
		} else {
			super.getFullyQualifiedName(obj)
		}
	}
}