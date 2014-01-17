package org.testosterone4j.tdsl.jvmmodel

import org.testosterone4j.base.AbstractActivity
import org.testosterone4j.tdsl.tDsl.Activity
import org.testosterone4j.tdsl.tDsl.Control
import org.testosterone4j.tdsl.tDsl.DataType
import org.testosterone4j.tdsl.tDsl.DataTypeMapping
import org.testosterone4j.tdsl.tDsl.EquivalenceClass
import org.testosterone4j.tdsl.tDsl.Field
import org.testosterone4j.tdsl.tDsl.Operation
import org.testosterone4j.tdsl.tDsl.OperationCall
import org.testosterone4j.tdsl.tDsl.PackageDeclaration
import org.testosterone4j.tdsl.tDsl.Predicate
import org.testosterone4j.tdsl.tDsl.SubUseCaseCall
import org.testosterone4j.tdsl.tDsl.Tag
import org.testosterone4j.tdsl.tDsl.TagsDeclaration
import org.testosterone4j.tdsl.tDsl.Toolkit
import org.testosterone4j.tdsl.tDsl.UseCase
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.testosterone4j.tdsl.tDsl.GeneratedValueExpression

class NamingExtensions {
	
	@Inject extension IQualifiedNameProvider
	
	@Inject extension MetaModelExtensions
	
	@Inject extension UtilExtensions
	
	@Inject extension JvmTypesBuilder
	
	
	// Other
	def String fqn(EObject o) {
		o?.fullyQualifiedName?.toString
	}
	
	// Activity 
	
	def QualifiedName class_FQN(Activity activity) {
		activity?.fullyQualifiedName
	}
	
	def String class_fqn(Activity activity) {
		activity.class_FQN?.toString
	}

	def String class_SimpleName(Activity activity) {
		activity?.name?.toFirstUpper
	}	
	
	def String localVariable_name(Activity activity, int index) {
		activity?.name?.toFirstLower + index
	}
	
	def String adapterInterface_fqn(Activity activity) {
		val providingActivity = activity.adapterProvidingActivity
		if (providingActivity == null) {
			activity?.toolkit?.activityAdapter_FQN ?: "org.testosterone4j.base.ActivityAdapter"
		} else 
			providingActivity.fullyQualifiedName?.toString + "Adapter"
	}
	
	def JvmTypeReference superClass_ref(Activity activity) {
		val parentClassName = activity?.parent?.class_fqn
		if (parentClassName != null) 
			activity.newTypeRef(parentClassName)
		else 
			activity.newTypeRef(typeof(AbstractActivity))
	}
	
	
	// Control
	
	def QualifiedName class_FQN(Control control) {
		control?.fullyQualifiedName
	}
	
	def String class_fqn(Control control) {
		control.class_FQN?.toString
	}
	
	def String activityAdapterGetter(Control control) {
		"get" + control?.name?.toFirstUpper
	}
	
	def String toolkitGetter(Control control) {
		"get" + control?.name?.toFirstUpper
	}
	
	// DataType
	
	def QualifiedName class_FQN(DataType dataType) {
		dataType?.fullyQualifiedName
	}
	
	def String class_fqn(DataType dataType) {
		dataType.class_FQN?.toString
	}
	
	def String equivalenceClass_name(DataType dataType) {
		dataType?.class_FQN?.toString + "EquivalenceClass"
	}
	
	
	// DataTypeMapping
	
	def String readableUniqueKey(OperationCall call, DataTypeMapping dataTypeMapping) {
		val callIndex = call.useCasePath
		val fieldName = dataTypeMapping?.operationMapping?.field?.name
		val operationName = dataTypeMapping?.operationMapping?.name?.name
		val paramName = dataTypeMapping?.name?.name
		val variableName = callIndex + ":" + fieldName + '.' + operationName + '.' + paramName
		variableName
	}
	
	def String preferredVariableName(DataTypeMapping dataTypeMapping) {
		val fieldName = dataTypeMapping?.operationMapping?.field?.name
		val paramName = dataTypeMapping?.name?.name
		val variableName = (fieldName + '.' + paramName).toFieldName
		variableName
	}
	
	// EquivalenceClass
	
	def String valueMethod_name(EquivalenceClass clazz) {
		"get" + clazz.name.toFirstUpper + "Value"
	}
	
	def String classPredicate_name(EquivalenceClass clazz) {
		"get" + clazz.name.toFirstUpper + "Predicate"
	}

	// Field
		
	def String activityControlDelegationMethodName(Field field, Operation operation) {
		field?.name + "_" + operation?.name
	}
	
	def String getterName(Field field) {
		"get" + field?.name?.toFirstUpper
	}
	
	def String getFieldGetterName(Field field) {
		"get" + field?.name?.toFirstUpper + field?.control?.name?.toFirstUpper
	}
	
	def String controlFieldName(Field field) {
		field?.name + "Field"
	}
	
	

	// PackageDeclaration
	
	def String predicateClass_fqn(PackageDeclaration pack) {
		pack.fqn + ".Predicates"
	}
	
	
	// Predicate
	
	def String class_fqn(Predicate predicate) {
		predicate?.packageDeclaration?.fqn + "." + predicate?.name?.toFirstUpper
	}


	// SubUseCaseCall
	
	def variableName(SubUseCaseCall call) {
		call?.useCase?.name?.toFirstLower
	}
	
	def readableUniqueKey(SubUseCaseCall call) {
		call.useCasePath + "." + call.useCase.name
	}
	
	// SUT
	
	def String activityAdapter_FQN(Toolkit toolkit) {
		
		toolkit?.fullyQualifiedName?.toString + "ActivityAdapter"
	}
	
	// Tag
	
	def String enumLiteral_SimpleName(Tag tag) {
		tag.name
	}
	
	def String enumLiteral_FQN(Tag tag) {
		(tag?.eContainer as TagsDeclaration)?.enumClass_FQN + "." + tag.enumLiteral_SimpleName
	}
	
	// TagDeclaration
	
	def String enumClass_FQN(TagsDeclaration tags) {
		val enumClass = tags?.eContainer?.fullyQualifiedName?.toString
		if (enumClass != null) 
			enumClass + ".Tags"
		else 
			null
	}

	// UseCase
	
	def QualifiedName class_FQN(UseCase useCase) {
		useCase?.fullyQualifiedName
	}
	
	def String class_fqn(UseCase useCase) {
		useCase.class_FQN?.toString
	}
	
	def String class_SimpleName(UseCase useCase) {
		useCase?.name?.toFirstUpper
	}
	
	def String subUseCaseGetter(UseCase useCase) {
		"getOrGenerateSubUseCase(" + useCase?.fullyQualifiedName?.toString + ".class, \"" + useCase?.name + "\")"  
	}
	
}