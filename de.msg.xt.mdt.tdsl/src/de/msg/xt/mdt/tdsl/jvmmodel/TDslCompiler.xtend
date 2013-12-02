package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.base.AbstractActivity
import de.msg.xt.mdt.base.Tag
import de.msg.xt.mdt.base.util.TDslHelper
import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationBlock
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameter
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.Assert
import de.msg.xt.mdt.tdsl.tDsl.ControlOperationParameter
import de.msg.xt.mdt.tdsl.tDsl.CurrentActivityExpression
import de.msg.xt.mdt.tdsl.tDsl.DataType
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.GeneratedValueExpression
import de.msg.xt.mdt.tdsl.tDsl.GenerationSelektor
import de.msg.xt.mdt.tdsl.tDsl.InnerBlock
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.Parameter
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import de.msg.xt.mdt.tdsl.tDsl.UseCaseBlock
import java.util.Set
import java.util.Stack
import javax.inject.Inject
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.lib.Pair
import org.eclipse.xtext.xbase.typing.ITypeProvider
import de.msg.xt.mdt.tdsl.tDsl.ActivityExpectation
import de.msg.xt.mdt.tdsl.tDsl.ParameterAssignment

class TDslCompiler extends XbaseCompiler {

	@Inject extension NamingExtensions

	@Inject extension MetaModelExtensions

	@Inject extension UtilExtensions

	@Inject extension IQualifiedNameProvider

	@Inject extension ITypeProvider

	@Inject extension JvmTypesBuilder

	@Inject extension TypeReferences typeRefs

	override protected doInternalToJavaStatement(XExpression expr,
		ITreeAppendable it, boolean isReferenced) {
		switch (expr) {
			OperationCall: {
				if (!isReferenced) {
					val field = expr.operation.eContainer as Field
					expr.generateParameters(it)
					newLine
					var returnToPreviousActivity = false
					if (!expr.operation.nextActivities.empty) {
						val nextActivity = expr.operation.nextActivities.get(0)
						if (nextActivity.next != null) {
							append("stack.push(currentActivity);").newLine
							append("currentActivity = ")
						} else if (nextActivity.returnToLastActivity) {
							returnToPreviousActivity = true
						}
					}
					append("((")
					expr.newTypeRef((field.eContainer as Activity).class_FQN.toString).serialize(expr, it)
					append(''')currentActivity).«field.activityControlDelegationMethodName(expr.operation.name)»(''')
					appendParameter(expr, it)
					append(");")
					if (returnToPreviousActivity) {
						newLine
						append("currentActivity = stack.pop();")
					}
				}
			}
			GeneratedValueExpression: {
				if (!isReferenced) {
					newLine
					append(expr.param.fullyQualifiedName.toString.toFieldName)
				}
			}
			ActivityOperationCall: {
				expr.generateParameters(it)
				newLine
				var returnToPreviousActivity = false
				if (!expr.operation.nextActivities.empty) {
					val nextActivity = expr.operation.nextActivities.get(0)
					if (nextActivity.next != null) {
						append("stack.push(currentActivity);").newLine
						append("currentActivity = ")
					} else if (nextActivity.returnToLastActivity) {
						returnToPreviousActivity = true
					}
				}
				append("((")
				expr.newTypeRef((expr.operation.eContainer as Activity).class_FQN.toString).serialize(expr, it)
				append(''')currentActivity).«expr.operation.name»(''')
				appendParameter(expr, it)
				append(");")
				if (returnToPreviousActivity) {
					newLine
					append("currentActivity = stack.pop();")
				}
			}
			SubUseCaseCall: {
				newLine
				expr.newTypeRef(expr.useCase.class_fqn).serialize(expr, it)
				val useCaseVariable = declareVariable(expr, expr.variableName)
				append(''' «useCaseVariable» = getOrGenerateSubUseCase(''')
				expr.newTypeRef(expr.useCase.class_fqn).serialize(expr, it)
				append(".class, \"" + expr.readableUniqueKey + "\");")
				for (param : expr.paramAssignment) {
					val paramAssignment = param as ParameterAssignment
					newLine
					append('''«useCaseVariable».set«paramAssignment.name.name.toFirstUpper»(''')
					appendParameterValue(it, paramAssignment.name.dataType, paramAssignment.value)
					append(");")
				}
				newLine
				append('''«useCaseVariable».execute((''')
				expr.newTypeRef(expr.useCase.initialActivity.class_FQN.toString).serialize(expr, it)
				append(''')currentActivity);''')
			}
			GenerationSelektor: {
			}
			StatementLine: {
				expr.statement.doInternalToJavaStatement(it, isReferenced)
			}
			Assert: {
				newLine
				append("if (this.generator == null) {").increaseIndentation
				expr.expression.doInternalToJavaStatement(it, false)
				decreaseIndentation.newLine.append("}")
			}
			ActivityOperationBlock: {
				compileBlock(expr, it, expr.activityOperation.returnedActivity)
			}
			UseCaseBlock: {
				compileBlock(expr, it, expr.useCase.returnedActivity)
			}
			InnerBlock: {
				compileInnerBlock(expr, it)
			}
			CurrentActivityExpression: {
			}
			ActivityExpectation: {
				val valName = it.declareVariable(expr.guard, "expectation")
				append('''boolean «valName» = ''')
				expr.guard.internalToJavaStatement(it, true)
				append(';').newLine
			}
			/*XAbstractFeatureCall: {
    			if (isReferenced && isVariableDeclarationRequired(expr, it)) {
    				val type = getTypeForVariableDeclaration(expr);
    				if (type != null) {
    					super.doInternalToJavaStatement(expr, it, isReferenced)	
    				}
    			}    			
    		}*/
			default:
				super.doInternalToJavaStatement(expr, it, isReferenced)
		}
	}
	
	protected def compileInnerBlock(XBlockExpression expr, ITreeAppendable it) {
		super.doInternalToJavaStatement(expr, it, false)
	}

	protected def compileBlock(XBlockExpression expr, ITreeAppendable it, Activity returnedActivity) {
		val nextActivityClass = returnedActivity?.class_fqn
		val nextActivityAdapterClass = returnedActivity?.adapterInterface_fqn

		expr.newTypeRef(typeof(Stack), expr.newTypeRef(typeof(AbstractActivity))).serialize(expr, it);
		it.append(" stack = new Stack<AbstractActivity>();").newLine
		expr.newTypeRef(typeof(AbstractActivity)).serialize(expr, it)
		it.append(" currentActivity = this; 	// the current activity")
		it.declareVariable("activity", "activity")
		var expectedReturnType = expr.newTypeRef(Void::TYPE)
		if (nextActivityAdapterClass != null) {
			expectedReturnType = expr.newTypeRef(typeof(Object))
		}

		super.doInternalToJavaStatement(expr, it, false)

		it.declareVariable(expr, "nextActivity")

		if (nextActivityAdapterClass != null) {
			expr.newTypeRef(nextActivityClass).serialize(expr, it)
			it.append(" ")
			it.append(it.getName(expr)).append(" = ")
			expr.newTypeRef(typeof(TDslHelper)).serialize(expr, it)
			it.append(".castActivity(injector, currentActivity, ")
			expr.newTypeRef(nextActivityClass).serialize(expr, it)
			it.append(".class, ")
			expr.newTypeRef(nextActivityAdapterClass).serialize(expr, it)
			it.append(".class);")
		}
	}

	override protected internalToConvertedExpression(XExpression expr,
		ITreeAppendable it) {

		switch (expr) {
			OperationCall: {
				val field = expr.operation.eContainer as Field
				append("((")
				expr.newTypeRef((field.eContainer as Activity).class_FQN.toString).serialize(expr, it)
				append(''')currentActivity).«field.activityControlDelegationMethodName(expr.operation.name)»(''')
				expr.generateEmbeddedParameters(it)
				append(")")
			}
			ActivityOperationCall: {
			}
			SubUseCaseCall: {
			}
			GenerationSelektor: {
				val container = expr.eContainer
				var DataType dataType
				var String varName
				switch (container) {
					OperationParameterAssignment: {
						dataType = container.name.datatype
						varName = container.name.fullyQualifiedName.toString
					}
					ActivityOperationParameterAssignment: {
						dataType = container.name.dataType
						varName = container.name.fullyQualifiedName.toString
					}
				}
				append("getOrGenerateValue(")
				expr.newTypeRef(dataType.class_FQN.toString).serialize(expr, it)
				append('''.class, "«varName»"''')
				if (!expr.tags.empty) {
					append(", ")
					expr.newTypeRef(typeof(Tag)).createArrayType.serialize(expr, it)
					append(''' {«FOR tag : expr.tags SEPARATOR ","»Tags.«tag.name»«ENDFOR»}''')
				}
				if (expr.expression != null) {
					append(", ")
					expr.expression.compileAsJavaExpression(it,
						expr.newTypeRef(typeof(Set), expr.newTypeRef(typeof(Tag))))
				}
				append(")")
			}
			GeneratedValueExpression: {
				val lastExpression = expr.lastExpressionWithNextActivity
				appendGeneratedValueExpression(lastExpression, expr, it)
			}
			StatementLine: {
				expr.statement.internalToJavaExpression(it)
			}
			Assert: {
				expr.expression.internalToJavaExpression(it)
			}
			ActivityOperationBlock: {
				append("nextActivity")
			}
			UseCaseBlock: {
				append("nextActivity")
			}
			InnerBlock: {
			}
			CurrentActivityExpression: {
				append("currentActivity")
			}
			ActivityExpectation: {
				append(expr.guard.getVarName(it))
			}
			default:
				super.internalToConvertedExpression(expr, it)
		}
	}

	def dispatch appendGeneratedValueExpression(XExpression lastExpression, GeneratedValueExpression generatedValueExpr,
		ITreeAppendable appendable) {
	}

	def dispatch appendGeneratedValueExpression(OperationCall lastExpression,
		GeneratedValueExpression generatedValueExpr, ITreeAppendable appendable) {
		val dataTypeMapping = generatedValueExpr.param as DataTypeMapping
		val variableName = new Pair<OperationCall, DataTypeMapping>(lastExpression, dataTypeMapping).getVarName(appendable)
		appendable.append(variableName)
	}

	def dispatch appendGeneratedValueExpression(ActivityOperationCall lastExpression,
		GeneratedValueExpression generatedValueExpr, ITreeAppendable appendable) {
		val activityOperationParam = generatedValueExpr.param as ActivityOperationParameter
		val variableName = new Pair<ActivityOperationCall, ActivityOperationParameter>(lastExpression, activityOperationParam).getVarName(appendable)
		appendable.append(variableName)
	}

	def dispatch appendGeneratedValueExpression(SubUseCaseCall lastExpression,
		GeneratedValueExpression generatedValueExpr, ITreeAppendable appendable) {
		val variableName = lastExpression.getVarName(appendable)
		val getterName = (generatedValueExpr.param as Parameter).name.getterName
		appendable.append('''«variableName».«getterName»()''')
	}

	def getVariableNameForOperationCallParameter(OperationCall call, DataTypeMapping mapping) {
		val name = call?.fullyQualifiedName?.toString
		mapping?.name?.name + name?.substring(name?.lastIndexOf("@") + 1)?.toFieldName
	}

	def generateParameters(OperationCall call, ITreeAppendable appendable) {
		for (mapping : call.operation.dataTypeMappings) {
			val assignment = findAssignment(call, mapping.name) as OperationParameterAssignment
			val dataTypeName = mapping.datatype.class_fqn
			appendable.newLine
			mapping.newTypeRef(dataTypeName).serialize(mapping, appendable)
			val varName = appendable.declareVariable(new Pair<OperationCall, DataTypeMapping>(call, mapping), mapping.preferredVariableName)
			appendable.append(''' «varName» = ''')
			if (assignment == null) {
				appendable.append("getOrGenerateValue(")
				mapping.newTypeRef(dataTypeName).serialize(mapping, appendable)
				appendable.append('''.class, "«call.readableUniqueKey(mapping)»")''')
			} else {
				appendParameterValue(appendable, mapping.datatype, assignment.value)
			}
			appendable.append(";")
		}
	}

	def generateEmbeddedParameters(OperationCall call, ITreeAppendable appendable) {
		var i = 1
		for (mapping : call.operation.dataTypeMappings) {
			val assignment = findAssignment(call, mapping.name) as OperationParameterAssignment
			val dataTypeName = mapping.datatype.class_fqn
			if (assignment == null) {
				appendable.append("getOrGenerateValue(")
				mapping.newTypeRef(dataTypeName).serialize(mapping, appendable)
				appendable.append('''.class, "«new Pair<OperationCall, DataTypeMapping>(call, mapping).getVarName(appendable)»")''')
			} else {
				appendParameterValue(appendable, mapping.datatype, assignment.value)
			}
			if (i < call.operation.dataTypeMappings.size)
				appendable.append(", ")
			i = i + 1
		}
	}

	def appendParameterValue(ITreeAppendable appendable, DataType datatype, XExpression expr) {
		val dataTypeName = datatype.class_fqn
		val expectedType = expr.type
		val typeMatch = (expectedType?.type != null) &&
			expectedType.type.equals(datatype.newTypeRef(datatype.class_fqn).type)
		if (!typeMatch) {
			appendable.append("new ")
			datatype.newTypeRef(dataTypeName).serialize(datatype, appendable)
			appendable.append("(")
		}
		compileAsJavaExpression(expr, appendable, expectedType)
		if (!typeMatch) {
			appendable.append(", null)")
		}
	}

	def generateParameters(ActivityOperationCall call, ITreeAppendable appendable) {
		for (parameter : call.operation.params) {
			val assignment = findAssignment(call, parameter) as ActivityOperationParameterAssignment
			val dataTypeName = parameter.dataType.class_fqn
			appendable.newLine
			val proposedName = call.operation.name + parameter.name?.toFirstUpper
			val variableName = appendable.declareVariable(new Pair<ActivityOperationCall, ActivityOperationParameter>(call, parameter), proposedName)
			parameter.newTypeRef(dataTypeName).serialize(parameter, appendable)
			appendable.append(''' «variableName» = ''')
			if (assignment == null) {
				appendable.append("getOrGenerateValue(")
				parameter.newTypeRef(dataTypeName).serialize(parameter, appendable)
				appendable.append('''.class, "«parameter.fullyQualifiedName»")''')
			} else {
				val expectedType = typeRefs.getTypeForName(dataTypeName, call)
				val actualType = assignment.value?.type?.type
				if (actualType != null && expectedType != null) {
					if (!assignment.value.type.type.equals(expectedType.type)) {
						appendable.append("new ")
						parameter.newTypeRef(dataTypeName).serialize(parameter, appendable)
						appendable.append("(")
					}
					compileAsJavaExpression(assignment.value, appendable, assignment.value.type)
					if (!actualType.equals(expectedType.type)) {
						appendable.append(", null)")
					}
				}
			}
			appendable.append(";")
		}
	}

	def generateParameters(SubUseCaseCall call, ITreeAppendable appendable) {
		for (parameter : call.useCase.inputParameter) {
			val assignment = findAssignment(call, parameter) as ParameterAssignment
			val dataTypeName = parameter.dataType.class_fqn
			appendable.newLine
			val proposedName = call.useCase.name?.toFirstLower + parameter.name?.toFirstUpper
			val variableName = appendable.declareVariable(new Pair<SubUseCaseCall, Parameter>(call, parameter), proposedName)
			parameter.newTypeRef(dataTypeName).serialize(parameter, appendable)
			appendable.append(''' «proposedName» = ''')
			if (assignment == null) {
				appendable.append("getOrGenerateValue(")
				parameter.newTypeRef(dataTypeName).serialize(parameter, appendable)
				appendable.append('''.class, "«parameter.fullyQualifiedName»")''')
			} else {
				val expectedType = typeRefs.getTypeForName(dataTypeName, call)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append("new ")
					parameter.newTypeRef(dataTypeName).serialize(parameter, appendable)
					appendable.append("(")
				}
				compileAsJavaExpression(assignment.value, appendable, assignment.value.type)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append(", null)")
				}
			}
			appendable.append(";")
		}
	}

	def appendParameter(ActivityOperationCall call, ITreeAppendable appendable) {
		appendable.append(
			'''«FOR parameter : call.operation.params SEPARATOR ', '»«new Pair<ActivityOperationCall, ActivityOperationParameter>(call, parameter).getVarName(appendable)»«ENDFOR»''')
	}

	def appendParameter(OperationCall call, ITreeAppendable appendable) {
		val params = '''«FOR parameter : call.operation.dataTypeMappings SEPARATOR ', '»«new Pair<OperationCall, DataTypeMapping>(call, parameter).getVarName(appendable)»«ENDFOR»'''
		appendable.append(params)
	}

	def findAssignment(ActivityOperationCall call, ActivityOperationParameter param) {
		for (assignment : call.paramAssignment) {
			val paramAssignment = assignment as ActivityOperationParameterAssignment
			if (paramAssignment.name.equals(param)) {
				return assignment
			}
		}
		return null
	}

	def findAssignment(OperationCall call, ControlOperationParameter param) {
		for (assignment : call.paramAssignment) {
			val paramAssignment = assignment as OperationParameterAssignment
			if (paramAssignment.name.name.equals(param)) {
				return assignment
			}
		}
		return null
	}

	def findAssignment(SubUseCaseCall call, Parameter param) {
		for (assignment : call.paramAssignment) {
			val paramAssignment = assignment as ParameterAssignment
			if (paramAssignment.name.equals(param)) {
				return assignment
			}
		}
		return null
	}
}
