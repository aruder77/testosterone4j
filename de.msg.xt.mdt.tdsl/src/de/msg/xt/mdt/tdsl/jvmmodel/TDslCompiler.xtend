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

class TDslCompiler extends XbaseCompiler {
	
	@Inject extension FieldNaming
	
	@Inject extension UseCaseNaming
	
	@Inject extension DataTypeNaming
	
	@Inject extension OperationMappingExtension
	
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
    		ActivityOperationCall: {
    			newLine
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
    
    def appendParameter(ActivityOperationCall call, ITreeAppendable appendable) {
    	for (parameter : call.operation.params) {
			var first = true
			if (!first) {
				appendable.append(",")
				first = false
			}
			val assignment = findAssignment(call, parameter)
			if (assignment == null) {
				appendable.append('''getOrGenerateValue(«parameter.dataType.class_FQN.toString».class, "«parameter.fullyQualifiedName»")''')
			} else {
				val expectedType = typeRefs.getTypeForName(parameter.dataType.class_FQN.toString, call)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append('''new «parameter.dataType.class_FQN.toString»(''')
				}
				compileAsJavaExpression(assignment.value, appendable, expectedType)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append(''', null)''')
				}
			}    		
    	} 
    }
    
	def appendParameter(OperationCall call, ITreeAppendable appendable) { 
		for (mapping : call.operation.dataTypeMappings) {
			var first = true
			if (!first) {
				appendable.append(",")
				first = false
			}
			val assignment = findAssignment(call, mapping.controlOperationParameter)
			if (assignment == null) {
				appendable.append('''getOrGenerateValue(«mapping.datatype.class_FQN.toString».class, "«mapping.fullyQualifiedName»")''')
			} else {
				val expectedType = typeRefs.getTypeForName(assignment.name.datatype.class_FQN.toString, call)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append('''new «assignment.name.datatype.class_FQN.toString»(''')
				}
				compileAsJavaExpression(assignment.value, appendable, expectedType)
				if (!assignment.value.type.type.equals(expectedType.type)) {
					appendable.append(''', null)''')
				}
			}
		}
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
			default:
				super.internalToConvertedExpression(expr, it)
        }                          	
	}
}