package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.Control
import de.msg.xt.mdt.tdsl.tDsl.DataType
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.Operation
import de.msg.xt.mdt.tdsl.tDsl.Toolkit
import de.msg.xt.mdt.tdsl.tDsl.UseCase
import javax.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import de.msg.xt.mdt.tdsl.tDsl.TagsDeclaration
import de.msg.xt.mdt.tdsl.tDsl.Tag
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import de.msg.xt.mdt.base.AbstractActivity
import org.eclipse.emf.ecore.EObject
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import de.msg.xt.mdt.base.ActivityAdapter
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import de.msg.xt.mdt.tdsl.tDsl.Predicate

class NamingExtensions {
	
	@Inject extension IQualifiedNameProvider
	
	@Inject extension MetaModelExtensions
	
	@Inject extension UtilExtensions
	
	@Inject extension JvmTypesBuilder
	
	
	// Other
	def String tdslInjector() {
		"de.msg.xt.mdt.base.TDslInjector"
	}
	
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
			activity?.toolkit?.activityAdapter_FQN ?: "de.msg.xt.mdt.base.ActivityAdapter"
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
	
	def String generatedValueLocalVariableName(DataTypeMapping dataTypeMapping) {
		val fieldName = dataTypeMapping?.operationMapping?.field?.fullyQualifiedName?.toString
		val operationName = dataTypeMapping?.operationMapping?.name?.name
		val paramName = dataTypeMapping?.name?.name
		val variableName = (fieldName + '.' + operationName + '.' + paramName).toFieldName
		variableName
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
		predicate?.fullyQualifiedName?.toString
	}


	// SubUseCaseCall
	
	def variableName(SubUseCaseCall call) {
		(call?.useCase?.name + "_" + call?.useCasePath)?.toFieldName
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