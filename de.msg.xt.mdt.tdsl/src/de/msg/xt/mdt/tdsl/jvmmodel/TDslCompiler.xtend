package de.msg.xt.mdt.tdsl.jvmmodel

import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import de.msg.xt.mdt.tdsl.tDsl.Activity
import javax.inject.Inject
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.ControlOperationParameter
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import de.msg.xt.mdt.tdsl.tDsl.GenerationSelektor
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.DataType
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameterAssignment
import org.eclipse.xtext.xbase.typing.ITypeProvider
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameter
import de.msg.xt.mdt.tdsl.tDsl.GeneratedValueExpression
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.Assert
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder

class TDslCompiler extends XbaseCompiler {
	
	@Inject extension NamingExtensions
	
	@Inject extension MetaModelExtensions
	
	@Inject extension UtilExtensions
	
	@Inject extension IQualifiedNameProvider
	
	@Inject extension ITypeProvider
	
	@Inject extension JvmTypesBuilder
	
	
	@Inject
	TypeReferences typeRefs
	
	
	override protected doInternalToJavaStatement(XExpression expr, 
                                               ITreeAppendable it, 
                                               boolean isReferenced) {
                                           
    	switch (expr) {
    		OperationCall: {
    			if (!isReferenced) {
	    			val field = expr.operation.eContainer as Field
	    			newLine
	    			expr.generateParameters(it)
					if (!expr.operation.nextActivities.empty) {
						append('''activity = ''')
					}	    			
    				append(
						'''
						((«(field.eContainer as Activity).class_FQN.toString»)activity).«field.activityControlDelegationMethodName(expr.operation.name)»(''')
					appendParameter(expr, it)
					append(");")
				}
    		}
    		GeneratedValueExpression: {
    			if (!isReferenced) {
					append(expr.param.fullyQualifiedName.toString.toFieldName)    				
    			}
    		}
    		ActivityOperationCall: {
    			newLine
    			expr.generateParameters(it)
				if (!expr.operation.nextActivities.empty) {
					append('''activity = ''')
				}	    			
    			append('''((«(expr.operation.eContainer as Activity).class_FQN.toString»)activity).«expr.operation.name»(''')	
				appendParameter(expr, it)
				append(");")
    		}
    		SubUseCaseCall: {
    			newLine
    			for (param : expr.paramAssignment) {
    				append('''«expr.useCase.subUseCaseGetter».set«param.name.name.toFirstUpper»(''')
    				appendParameterValue(it, param.name.dataType, param.value)
//    				compileAsJavaExpression(param.value, it, typeRefs.getTypeForName(param.name.dataType.class_FQN.toString, param))
    				append(");")    
    				newLine				
    			}
    			append('''«expr.useCase.subUseCaseGetter».execute((«expr.useCase.initialActivity.class_FQN.toString»)activity);''')
    		}
    		GenerationSelektor: {
    		}
    		StatementLine: {
    			expr.statement.doInternalToJavaStatement(it, isReferenced)
    		}
    		Assert: {
    			newLine
    			append('''if (this.generator == null) {''')
    			expr.expression.doInternalToJavaStatement(it, isReferenced)
    			newLine
    			append("}")
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
    			append('''((«(field.eContainer as Activity).class_FQN.toString»)activity).«field.activityControlDelegationMethodName(expr.operation.name)»(''')
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
				append('''getOrGenerateValue(«dataType.class_FQN.toString».class, "«varName»"''')
				if (!expr.tags.empty) {
					append(''', new de.msg.xt.mdt.base.Tag[] {«FOR tag: expr.tags SEPARATOR ","»Tags.«tag.name»«ENDFOR»}''')
				}
				if (expr.expression != null) {
					append(", ")
					expr.expression.compileAsJavaExpression(it, typeRefs.createArrayType(typeRefs.getTypeForName("Tags", expr)))
				}
				append(''')''')
			}
			GeneratedValueExpression: {
				val lastExpression = expr.lastExpressionWithNextActivity
				val opCall = lastExpression as OperationCall
				append(opCall.getVariableNameForOperationCallParameter(expr.param))
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
    
    def getVariableNameForOperationCallParameter(OperationCall call, DataTypeMapping mapping) {
    	call?.fullyQualifiedName?.toString?.toFieldName + "_" + mapping?.fullyQualifiedName?.toString?.toFieldName
    }
    
	def generateParameters(OperationCall call, ITreeAppendable appendable) { 
		for (mapping : call.operation.dataTypeMappings) {
			val assignment = findAssignment(call, mapping.name)
			val dataTypeName = mapping.datatype.class_FQN.toString
			appendable.append('''«dataTypeName» «call.getVariableNameForOperationCallParameter(mapping)» = ''')
			if (assignment == null) {
				appendable.append('''getOrGenerateValue(«dataTypeName».class, "«call.getVariableNameForOperationCallParameter(mapping)»")''')
			} else {
				appendParameterValue(appendable, mapping.datatype, assignment.value)
			}
			appendable.append(";")
			appendable.newLine
		}		
	}
	
	def generateEmbeddedParameters(OperationCall call, ITreeAppendable appendable) { 
		var i = 1
		for (mapping : call.operation.dataTypeMappings) {
			val assignment = findAssignment(call, mapping.name)
			val dataTypeName = mapping.datatype.class_FQN.toString
			if (assignment == null) {
				appendable.append('''getOrGenerateValue(«dataTypeName».class, "«call.getVariableNameForOperationCallParameter(mapping)»")''')
			} else {
				appendParameterValue(appendable, mapping.datatype, assignment.value)
			}
			if (i < call.operation.dataTypeMappings.size)
				appendable.append(", ").newLine
			i = i + 1
		}		
	}
	
	def appendParameterValue(ITreeAppendable appendable, DataType datatype, XExpression expr) {
		val dataTypeName = datatype.class_FQN.toString
		val expectedType = expr.type
		val typeMatch = expectedType.type.equals(datatype.newTypeRef(datatype.class_FQN.toString).type)
		if (!typeMatch) {
			appendable.append('''new «dataTypeName»(''')
		}
		compileAsJavaExpression(expr, appendable, expectedType)
		if (!typeMatch) {
			appendable.append(''', null)''')
		}		
	}

    
	def generateParameters(ActivityOperationCall call, ITreeAppendable appendable) { 
    	for (parameter : call.operation.params) {
			val assignment = findAssignment(call, parameter)
			val dataTypeName = parameter.dataType.class_FQN.toString
			appendable.append('''«dataTypeName» «parameter.fullyQualifiedName.toString.toFieldName» = ''')
			if (assignment == null) {
				appendable.append('''getOrGenerateValue(«dataTypeName».class, "«parameter.fullyQualifiedName»")''')
			} else {
				val expectedType = typeRefs.getTypeForName(dataTypeName, call)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append('''new «dataTypeName»(''')
				}
				compileAsJavaExpression(assignment.value, appendable, assignment.value.type)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append(''', null)''')
				}
			}    		
			appendable.append(";")
			appendable.newLine
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
}
