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

class TDslCompiler extends XbaseCompiler {
	
	@Inject extension FieldNaming
	
	@Inject extension UseCaseNaming
	
	@Inject extension DataTypeNaming
	
	@Inject extension OperationMappingExtension
	
	@Inject extension IQualifiedNameProvider
	
	
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
    		default:
    			super.doInternalToJavaStatement(expr, it, isReferenced)
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
				appendable.append('''this.generator.generateDataTypeValue(«mapping.datatype.class_FQN.toString».class, "«mapping.fullyQualifiedName»")''')
			} else {
				compileAsJavaExpression(assignment.value, appendable, typeRefs.getTypeForName(assignment.name.datatype.class_FQN.toString, call))
			}
		}
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
			default:
				super.internalToConvertedExpression(expr, it)
        }                          	
	}
}