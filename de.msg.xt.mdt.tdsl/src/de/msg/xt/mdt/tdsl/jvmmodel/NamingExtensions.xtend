package de.msg.xt.mdt.tdsl.jvmmodel

import javax.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.Control
import de.msg.xt.mdt.tdsl.tDsl.DataType
import de.msg.xt.mdt.tdsl.tDsl.Operation
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import de.msg.xt.mdt.tdsl.tDsl.UseCase
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.SUT

class NamingExtensions {
	
	@Inject extension IQualifiedNameProvider
	
	@Inject extension MetaModelExtensions
	
	@Inject extension UtilExtensions
	
	// Activity 
	
	def QualifiedName class_FQN(Activity activity) {
		activity?.fullyQualifiedName
	}

	def String class_SimpleName(Activity activity) {
		activity?.name?.toFirstUpper
	}	
	
	def String localVariable_name(Activity activity, int index) {
		activity?.name?.toFirstLower + index
	}
	
	def String adapterInterface_FQN(Activity activity) {
		activity?.fullyQualifiedName?.toString + "Adapter"
	}
	
	
	// Control
	
	def QualifiedName class_FQN(Control control) {
		control.fullyQualifiedName
	}
	
	
	// DataType
	
	def QualifiedName class_FQN(DataType dataType) {
		dataType?.fullyQualifiedName
	}
	
	def String equivalenceClass_name(DataType dataType) {
		dataType?.class_FQN?.toString + "EquivalenceClass"
	}
	
	
	// DataTypeMapping
	
	def String generatedValueLocalVariableName(DataTypeMapping dataTypeMapping) {
		val fieldName = dataTypeMapping.operationMapping.field.fullyQualifiedName.toString
		val operationName = dataTypeMapping.operationMapping.name.name
		val paramName = dataTypeMapping.name.name
		val variableName = (fieldName + '.' + operationName + '.' + paramName).toFieldName
		variableName
	}

	// Field
		
	def String activityControlDelegationMethodName(Field field, Operation operation) {
		field.name + "_" + operation.name
	}
	
	def String getterName(Field field) {
		"get" + field.name.toFirstUpper
	}
	
	def String getFieldGetterName(Field field) {
		"get" + field?.name?.toFirstUpper + field?.control?.name?.toFirstUpper
	}
	

	// PackageDeclaration
	
	// SUT
	
	def String activityAdapter_FQN(SUT sut) {
		sut?.fullyQualifiedName?.toString + "ActivityAdapter"
	}

	// UseCase
	
	def QualifiedName class_FQN(UseCase useCase) {
		useCase.fullyQualifiedName
	}
	
	def String class_SimpleName(UseCase useCase) {
		useCase.name.toFirstUpper
	}
	
	def String subUseCaseGetter(UseCase useCase) {
		"getOrGenerateSubUseCase(" + useCase.fullyQualifiedName.toString + ".class, \"" + useCase.name + "\")"  
	}
	
}