package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameter
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.Assert
import de.msg.xt.mdt.tdsl.tDsl.ControlOperationParameter
import de.msg.xt.mdt.tdsl.tDsl.DataType
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.GeneratedValueExpression
import de.msg.xt.mdt.tdsl.tDsl.GenerationSelektor
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import javax.inject.Inject
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.typing.ITypeProvider
import de.msg.xt.mdt.base.Tag
import de.msg.xt.mdt.tdsl.tDsl.ParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.Parameter

class TDslCompiler extends XbaseCompiler {
	
	@Inject extension NamingExtensions
	
	@Inject extension MetaModelExtensions
	
	@Inject extension UtilExtensions
	
	@Inject extension IQualifiedNameProvider
	
	@Inject extension ITypeProvider
	
	@Inject extension JvmTypesBuilder
	
	@Inject extension TypeReferences typeRefs
	
	
	override protected doInternalToJavaStatement(XExpression expr, 
                                               ITreeAppendable it, 
                                               boolean isReferenced) {
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
							append("stack.push(activity);").newLine
							append("activity = ")
						} else if (nextActivity.returnToLastActivity) {
							returnToPreviousActivity = true
						}
					}	    			
					append("((")
					expr.newTypeRef((field.eContainer as Activity).class_FQN.toString).serialize(expr, it)					
    				append(''')activity).«field.activityControlDelegationMethodName(expr.operation.name)»(''')
					appendParameter(expr, it)
					append(");")
					if (returnToPreviousActivity) {
						newLine
						append("activity = stack.pop();")
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
						append("stack.push(activity);").newLine
						append("activity = ")
					} else if (nextActivity.returnToLastActivity) {
						returnToPreviousActivity = true
					}
				}	    			
				append("((")
				expr.newTypeRef((expr.operation.eContainer as Activity).class_FQN.toString).serialize(expr, it)
    			append(''')activity).«expr.operation.name»(''')	
				appendParameter(expr, it)
				append(");")
				if (returnToPreviousActivity) {
					newLine
					append("activity = stack.pop();")
				}
    		}
    		SubUseCaseCall: {
    			for (param : expr.paramAssignment) {
    				newLine
    				append('''«expr.useCase.subUseCaseGetter».set«param.name.name.toFirstUpper»(''')
    				appendParameterValue(it, param.name.dataType, param.value)
    				append(");")
    			}
    			newLine
    			expr.newTypeRef(expr.useCase.class_fqn).serialize(expr, it)
    			append(''' «expr.variableName» = «expr.useCase.subUseCaseGetter»;''')
    			newLine
    			append('''«expr.variableName».execute((''')
    			expr.newTypeRef(expr.useCase.initialActivity.class_FQN.toString).serialize(expr, it)
    			append(''')activity);''')
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
    		default:
    			super.doInternalToJavaStatement(expr, it, isReferenced)
    	}   	
    }

    override protected internalToConvertedExpression(XExpression expr, 
                                                 ITreeAppendable it) {
 
 		switch (expr) {
 			OperationCall: {
     			val field = expr.operation.eContainer as Field
     			append("((")
     			expr.newTypeRef((field.eContainer as Activity).class_FQN.toString).serialize(expr, it)
    			append(''')activity).«field.activityControlDelegationMethodName(expr.operation.name)»(''')
    			expr.generateEmbeddedParameters(it)
    			append(")") 		
			}
			SubUseCaseCall: {
				append("should not be possible!")
			}
			GenerationSelektor: {
				val container = expr.eContainer
				var DataType dataType
				var String varName
				switch (container) {
					OperationParameterAssignment : {
						dataType = container.name.datatype
						varName = container.name.fullyQualifiedName.toString
					}
					ActivityOperationParameterAssignment : {
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
					append(''' {«FOR tag: expr.tags SEPARATOR ","»Tags.«tag.name»«ENDFOR»}''')
				}
				if (expr.expression != null) {
					append(", ")
					expr.expression.compileAsJavaExpression(it, typeRefs.createArrayType(typeRefs.getTypeForName("Tags", expr)))
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
			
			default:
				super.internalToConvertedExpression(expr, it)
        }                          	
	}
	
	def dispatch appendGeneratedValueExpression(XExpression lastExpression, GeneratedValueExpression generatedValueExpr, ITreeAppendable appendable) {
	}
    
	def dispatch appendGeneratedValueExpression(OperationCall lastExpression, GeneratedValueExpression generatedValueExpr, ITreeAppendable appendable) {
		val variableName = lastExpression.getVariableNameForOperationCallParameter(generatedValueExpr.param as DataTypeMapping)
		appendable.append(variableName) 
	}

	def dispatch appendGeneratedValueExpression(ActivityOperationCall lastExpression, GeneratedValueExpression generatedValueExpr, ITreeAppendable appendable) {
		val variableName = generatedValueExpr.param.fullyQualifiedName.toString.toFieldName
		appendable.append(variableName) 
	}

	def dispatch appendGeneratedValueExpression(SubUseCaseCall lastExpression, GeneratedValueExpression generatedValueExpr, ITreeAppendable appendable) {
		val variableName = lastExpression.variableName
		val getterName = (generatedValueExpr.param as Parameter).name.getterName
		appendable.append('''«variableName».«getterName»()''') 
	}

    def getVariableNameForOperationCallParameter(OperationCall call, DataTypeMapping mapping) {
    	val name = call?.fullyQualifiedName?.toString
    	mapping?.name?.name + name?.substring(name?.lastIndexOf("@") + 1)?.toFieldName
    }
    
	def generateParameters(OperationCall call, ITreeAppendable appendable) { 
		for (mapping : call.operation.dataTypeMappings) {
			val assignment = findAssignment(call, mapping.name)
			val dataTypeName = mapping.datatype.class_fqn
			appendable.newLine
			mapping.newTypeRef(dataTypeName).serialize(mapping, appendable)
			appendable.append(''' «call.getVariableNameForOperationCallParameter(mapping)» = ''')
			if (assignment == null) {
				appendable.append("getOrGenerateValue(")
				mapping.newTypeRef(dataTypeName).serialize(mapping, appendable)
				appendable.append('''.class, "«call.getVariableNameForOperationCallParameter(mapping)»")''')
			} else {
				appendParameterValue(appendable, mapping.datatype, assignment.value)
			}
			appendable.append(";")
		}		
	}
	
	def generateEmbeddedParameters(OperationCall call, ITreeAppendable appendable) { 
		var i = 1
		for (mapping : call.operation.dataTypeMappings) {
			val assignment = findAssignment(call, mapping.name)
			val dataTypeName = mapping.datatype.class_fqn
			if (assignment == null) {
				appendable.append("getOrGenerateValue(")
				mapping.newTypeRef(dataTypeName).serialize(mapping, appendable)
				appendable.append('''.class, "«call.getVariableNameForOperationCallParameter(mapping)»")''')
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
		val typeMatch = expectedType.type.equals(datatype.newTypeRef(datatype.class_fqn).type)
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
			val assignment = findAssignment(call, parameter)
			val dataTypeName = parameter.dataType.class_fqn
			appendable.newLine
			parameter.newTypeRef(dataTypeName).serialize(parameter, appendable)
			appendable.append(''' «parameter.fullyQualifiedName.toString.toFieldName» = ''')
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
	
    
	def generateParameters(SubUseCaseCall call, ITreeAppendable appendable) { 
    	for (parameter : call.useCase.inputParameter) {
			val assignment = findAssignment(call, parameter)
			val dataTypeName = parameter.dataType.class_fqn
			appendable.newLine
			parameter.newTypeRef(dataTypeName).serialize(parameter, appendable)
			appendable.append(''' «parameter.fullyQualifiedName.toString.toFieldName» = ''')
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
    	appendable.append('''«FOR parameter : call.operation.params SEPARATOR ', '»«parameter.fullyQualifiedName.toString.toFieldName»«ENDFOR»''')
    }
    
	def appendParameter(OperationCall call, ITreeAppendable appendable) {
		val params = '''«FOR parameter : call.operation.dataTypeMappings SEPARATOR ', '»«call.getVariableNameForOperationCallParameter(parameter)»«ENDFOR»''' 
    	appendable.append(params)
	}
	        
	def findAssignment(ActivityOperationCall call, ActivityOperationParameter param) {
		for (assignment : call.paramAssignment) {
			if (assignment.name.equals(param)) {
				return assignment
			}
		}
		return null 
	}

	def findAssignment(OperationCall call, ControlOperationParameter param) {
		for (assignment : call.paramAssignment) {
			if (assignment.name.name.equals(param)) {
				return assignment
			}
		}
		return null
	}    

	def findAssignment(SubUseCaseCall call, Parameter param) {
		for (assignment : call.paramAssignment) {
			if (assignment.name.equals(param)) {
				return assignment
			}
		}
		return null
	}    
}
