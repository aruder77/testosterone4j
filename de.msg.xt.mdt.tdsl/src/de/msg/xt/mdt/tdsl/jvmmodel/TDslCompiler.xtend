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

class TDslCompiler extends XbaseCompiler {
	
	@Inject extension FieldNaming
	
	@Inject extension UseCaseNaming
	
	@Inject extension DataTypeNaming
	
	@Inject extension MetaModelExtensions
	
	@Inject extension IQualifiedNameProvider
	
	@Inject extension ITypeProvider
	
	
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
						((«(field.eContainer as Activity).name»)activity).«field.activityControlDelegationMethodName(expr.operation.operation)»(''')
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
    			append('''((«(expr.operation.eContainer as Activity).name»)activity).«expr.operation.name»(''')	
				appendParameter(expr, it)
				append(");")
    		}
    		SubUseCaseCall: {
    			newLine
    			for (param : expr.paramAssignment) {
    				append('''«expr.useCase.subUseCaseGetter»().set«param.name.name.toFirstUpper»(''')
    				compileAsJavaExpression(param.value, it, typeRefs.getTypeForName(param.name.dataType.class_FQN.toString, param))
    				append(");")    
    				newLine				
    			}
    			append('''«expr.useCase.subUseCaseGetter»().execute((«expr.useCase.initialActivity.name»)activity);''')
    		}
    		GenerationSelektor: {
    		}
    		default:
    			super.doInternalToJavaStatement(expr, it, isReferenced)
    	}   	
    }
    
	def generateParameters(OperationCall call, ITreeAppendable appendable) { 
		for (mapping : call.operation.dataTypeMappings) {
			val assignment = findAssignment(call, mapping.controlOperationParameter)
			val dataTypeName = mapping.datatype.class_FQN.toString
			appendable.append('''«dataTypeName» «mapping.fullyQualifiedName.toString.toFieldName» = ''')
			if (assignment == null) {
				appendable.append('''getOrGenerateValue(«dataTypeName».class, "«mapping.fullyQualifiedName»")''')
			} else {
				val expectedType = typeRefs.getTypeForName(dataTypeName, call)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append('''new «dataTypeName»(''')
				}
				compileAsJavaExpression(assignment.value, appendable, expectedType)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append(''', null)''')
				}
			}
			appendable.append(";")
			appendable.newLine
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
				compileAsJavaExpression(assignment.value, appendable, expectedType)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append(''', null)''')
				}
			}    		
			appendable.append(";")
			appendable.newLine
    	} 
	}
	
	def toFieldName(String string) { 
		string.replace('.', '_')	
	}


    
    def appendParameter(ActivityOperationCall call, ITreeAppendable appendable) {
    	appendable.append('''«FOR parameter : call.operation.params SEPARATOR ', '»«parameter.fullyQualifiedName.toString.toFieldName»«ENDFOR»''')
    }
    
	def appendParameter(OperationCall call, ITreeAppendable appendable) { 
    	appendable.append('''«FOR parameter : call.operation.dataTypeMappings SEPARATOR ', '»«parameter.fullyQualifiedName.toString.toFieldName»«ENDFOR»''')
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
			if (assignment.name.controlOperationParameter.equals(param)) {
				return assignment
			}
		}
		return null
	}
    
    override protected internalToConvertedExpression(XExpression expr, 
                                                 ITreeAppendable it) {
 
 		switch (expr) {
 			OperationCall: {
     			val field = expr.operation.eContainer as Field
    			append('''((«(field.eContainer as Activity).name»)activity).«field.activityControlDelegationMethodName(expr.operation.operation)»()''') 		
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
				append(expr.param.fullyQualifiedName.toString.toFieldName)
			}
			default:
				super.internalToConvertedExpression(expr, it)
        }                          	
	}
}