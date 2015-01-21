package org.testosterone4j.tdsl.jvmmodel

import java.util.List
import java.util.Set
import java.util.Stack
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.lib.Pair
import org.testosterone4j.base.AbstractActivity
import org.testosterone4j.base.GenerationHelper
import org.testosterone4j.base.Tag
import org.testosterone4j.base.util.TDslHelper
import org.testosterone4j.tdsl.tDsl.Activity
import org.testosterone4j.tdsl.tDsl.ActivityExpectation
import org.testosterone4j.tdsl.tDsl.ActivityOperationBlock
import org.testosterone4j.tdsl.tDsl.ActivityOperationCall
import org.testosterone4j.tdsl.tDsl.ActivityOperationParameter
import org.testosterone4j.tdsl.tDsl.ActivityOperationParameterAssignment
import org.testosterone4j.tdsl.tDsl.Assert
import org.testosterone4j.tdsl.tDsl.ControlOperationParameter
import org.testosterone4j.tdsl.tDsl.DataType
import org.testosterone4j.tdsl.tDsl.DataTypeMapping
import org.testosterone4j.tdsl.tDsl.Field
import org.testosterone4j.tdsl.tDsl.GeneratedValueExpression
import org.testosterone4j.tdsl.tDsl.GenerationSelektor
import org.testosterone4j.tdsl.tDsl.InnerBlock
import org.testosterone4j.tdsl.tDsl.OperationCall
import org.testosterone4j.tdsl.tDsl.OperationParameterAssignment
import org.testosterone4j.tdsl.tDsl.Parameter
import org.testosterone4j.tdsl.tDsl.ParameterAssignment
import org.testosterone4j.tdsl.tDsl.Selector
import org.testosterone4j.tdsl.tDsl.StatementLine
import org.testosterone4j.tdsl.tDsl.SubUseCaseCall
import org.testosterone4j.tdsl.tDsl.UseCaseBlock

class TDslCompiler extends XbaseCompiler {

	@Inject extension NamingExtensions

	@Inject extension MetaModelExtensions

	@Inject extension UtilExtensions

	@Inject extension IQualifiedNameProvider

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
				/*
				 * - generate parameter
				 * [stack.push(currentActivity);]  		// when explicit next activtiy
				 * if (<expectBed1>) {					// when expect statement after current
				 * 		[currentActivity = //when explicit next activtiy] ((MainWindow)currentActivity).openEthernetNavigator(params..., <expected Activity>, <expected Adapter>);
				 * } else {								
				 * 		[currentActivity = //when explicit next activtiy] ((MainWindow)currentActivity).openEthernetNavigator(params...); 
				 * 		[currentActivity = stack.pop();]	// when returnToLastActivity
				 * }									// when expect statement
				 */
				expr.generateParameters(it)
				newLine
				if (expr.isSwitchingToNewActivity) {
					append("stack.push(currentActivity);").newLine
				}
				val expect = expr.expectation
				if (expect != null) {
					expect.guard.doInternalToJavaStatement(it, true)
					append("if (")
					expect.guard.internalToConvertedExpression(it, expect.guard.newTypeRef(Boolean).toLightweight(expect))
					append(") {").increaseIndentation.newLine
					appendActivityOperationCall(expr, it, expect.activity)
					expect.block.doInternalToJavaStatement(it, false)
					
					decreaseIndentation.newLine
					append("} else {").increaseIndentation.newLine			
				}
				appendActivityOperationCall(expr, it, null)
				if (expr.returningToPreviousActivity) {
					newLine
					append("currentActivity = stack.pop();")
				}
				
				if (expect != null) {
					decreaseIndentation.newLine
					append("}")
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
			Selector: {
			}
			StatementLine: {
				expr.statement.doInternalToJavaStatement(it, isReferenced)
			}
			Assert: {
				newLine
				append("if (!")
				expr.newTypeRef(GenerationHelper).serialize(expr, it)
				append(".activeGeneration) {").increaseIndentation
				expr.expression.doInternalToJavaStatement(it, false)
				decreaseIndentation.newLine.append("}")
			}
			ActivityOperationBlock: {
				compileBlock(expr, it, expr.activityOperation.returnedActivity, true)
			}
			UseCaseBlock: {
				compileBlock(expr, it, expr.useCase.returnedActivity, false)
			}
			InnerBlock: {
				compileInnerBlock(expr, it)
			}
			ActivityExpectation: {
//				val valName = it.declareVariable(expr.guard, "expectation")
//				append('''boolean «valName» = ''')
//				expr.guard.internalToJavaStatement(it, true)
//				append(';').newLine
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
	
	protected def appendActivityOperationCall(ActivityOperationCall expr, ITreeAppendable it, Activity expectedActivity) {
		if (expr.isSwitchingToNewActivity) {
			append("currentActivity = ")
		} 
		append("((")
		expr.newTypeRef((expr.operation.eContainer as Activity).class_FQN.toString).serialize(expr, it)
		append(''')currentActivity).«expr.operation.name»(''')
		appendParameter(expr, it)
		if (expectedActivity != null) {
			if (!expr.operation.params.empty)
				append(", ")
			expr.newTypeRef(expectedActivity.class_FQN.toString).serialize(expr, it)
			append(".class, ")
			expr.newTypeRef(expectedActivity.adapterInterface_fqn.toString).serialize(expr, it)
			append(".class, true")
		}
		append(");")
	}
	
	protected def compileInnerBlock(XBlockExpression expr, ITreeAppendable it) {
		super.doInternalToJavaStatement(expr, it, false)
	}

	protected def compileBlock(XBlockExpression expr, ITreeAppendable it, Activity returnedActivity, boolean useGenerics) {
		val nextActivityClass = returnedActivity?.class_fqn
		val nextActivityAdapterClass = returnedActivity?.adapterInterface_fqn

		expr.newTypeRef(typeof(Stack), expr.newTypeRef(typeof(AbstractActivity))).serialize(expr, it);
		it.append(" stack = new Stack<AbstractActivity>();").newLine
		it.append("currentActivity = this; 	// the current activity")
		it.declareVariable("activity", "activity")
		var expectedReturnType = expr.newTypeRef(Void::TYPE)
		if (nextActivityAdapterClass != null) {
			expectedReturnType = expr.newTypeRef(typeof(Object))
		}

		super.doInternalToJavaStatement(expr, it, false)

		it.declareVariable(expr, "nextActivity")

		if (nextActivityAdapterClass != null) {
			if (useGenerics)
				it.append("T")
			else
				expr.newTypeRef(nextActivityClass).serialize(expr, it)
			it.append(" ")
			it.append(it.getName(expr)).append(" = ")
			expr.newTypeRef(typeof(TDslHelper)).serialize(expr, it)
			it.append(".castActivity(injector, currentActivity, ")
			if (useGenerics) {
				it.append("activityClass, adapterClass);")
			} else {
				expr.newTypeRef(nextActivityClass).serialize(expr, it)
				it.append(".class, ")
				expr.newTypeRef(nextActivityAdapterClass).serialize(expr, it)
				it.append(".class);")
			}
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
						val call = container.eContainer as OperationCall
						varName = call.readableUniqueKey(container.name)
					}
					ActivityOperationParameterAssignment: {
						dataType = container.name.dataType
						val call = container.eContainer as ActivityOperationCall
						varName = call.readableUniqueKey(container.name)
					}
				}
				appendGetOrGenerateValue(expr, dataType, varName, expr.tags, expr.expression)				
			}
			Selector: {
				var String varName = (expr.eContainer as XVariableDeclaration).name
				appendGetOrGenerateValue(it, expr, expr.dataType, varName, expr.tags, expr.expression)
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
			ActivityExpectation: {
				//append(expr.guard.getVarName(it))
			}
			default:
				super.internalToConvertedExpression(expr, it)
		}
	}
	
	protected def appendGetOrGenerateValue(ITreeAppendable it, EObject expr, DataType dataType, String varName, List<org.testosterone4j.tdsl.tDsl.Tag> tags, XExpression tagExpression)  {
		append("getOrGenerateValue(")
		expr.newTypeRef(dataType.class_FQN.toString).serialize(expr, it)
		append('''.class, "«varName»"''')
		if (!tags.empty) {
			append(", ")
			expr.newTypeRef(typeof(Tag)).createArrayType.serialize(expr, it)
			append(''' {«FOR tag : tags SEPARATOR ","»Tags.«tag.name»«ENDFOR»}''')
		}
		if (tagExpression != null) {
			append(", ")
			tagExpression.compileAsJavaExpression(it,
				expr.newTypeRef(typeof(Set), expr.newTypeRef(typeof(Tag))))
		}
		append(")")
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
