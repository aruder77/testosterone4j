package de.msg.xt.mdt.tdsl.typeprovider

import com.google.inject.Singleton
import de.msg.xt.mdt.tdsl.jvmmodel.NamingExtensions
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.Assert
import de.msg.xt.mdt.tdsl.tDsl.GeneratedValueExpression
import de.msg.xt.mdt.tdsl.tDsl.GenerationSelektor
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import javax.inject.Inject
import org.eclipse.xtext.common.types.JvmEnumerationType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.xbase.XFeatureCall
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.typing.XbaseTypeProvider
import org.eclipse.xtext.xbase.XBlockExpression

@Singleton
class TDslTypeProvider extends XbaseTypeProvider {
	
	@Inject TypeReferences typeReferences
	
	@Inject extension NamingExtensions
	
	@Inject JvmTypesBuilder typesBuilder

	override dispatch type(XFeatureCall featureCall, 
                    JvmTypeReference typeRef, 
                    boolean isRawTypes) {
        if(featureCall.declaringType instanceof JvmEnumerationType) {
        	val enumType = featureCall.declaringType as JvmEnumerationType
        	return enumType.getTypeForIdentifiable(isRawTypes)
        }
   		super._type(featureCall, typeRef, isRawTypes)            	
    }
	
	def dispatch type(Assert assert, JvmTypeReference typeRef, boolean isRawTypes) {
		val type = assert?.expression?.type(typeRef, isRawTypes)
		if (type == null) {
			typeReferences.getTypeForName(Void::TYPE, assert)
		} else
			type
	}
	
	def dispatch type(OperationCall opCall, 
                    JvmTypeReference typeRef, 
                    boolean isRawTypes) {
   		val typeName = opCall.operation?.dataType?.class_FQN?.toString
   		
   		if (typeName == null)
   			typeReferences.getTypeForName(Void::TYPE, opCall)
   		else 
   			typeReferences.getTypeForName(typeName, opCall)   		
  	}
  	
  	def dispatch type(StatementLine expr, JvmTypeReference typeRef, boolean isRawType) {
  		val type = expr?.statement?.type(typeRef, isRawType)
  		if (type == null) {
   			typeReferences.getTypeForName(Void::TYPE, expr)  			
  		} else {
  			type
  		}
  	}
  	
  	def dispatch type(GeneratedValueExpression expr, JvmTypeReference typeRef, boolean isRawType) {
  		val dataTypeName = expr?.param?.datatype?.class_FQN?.toString
  		if (dataTypeName == null)
  			typeReferences.getTypeForName(Void::TYPE, expr)
  		else 
  			typeReferences.getTypeForName(dataTypeName, expr)
  	}
  	
	def dispatch type(ActivityOperationCall opCall, 
                    JvmTypeReference typeRef, 
                    boolean isRawTypes) {
   		val typeName = opCall.operation?.returnType?.class_FQN?.toString
   		
   		if (typeName == null)
   			typeReferences.getTypeForName(Void::TYPE, opCall)
   		else 
   			typeReferences.getTypeForName(typeName, opCall)   		
  	}
  	
  	def dispatch type(SubUseCaseCall subUseCaseCall, JvmTypeReference typeRef, boolean isRawTypes) {
  		typeReferences.getTypeForName(Void::TYPE, subUseCaseCall)
  	}
  	
  	def dispatch type(GenerationSelektor generationSelektor, JvmTypeReference typeRef, boolean isRawTypes) {
  		val container = generationSelektor.eContainer
  		switch container {
  			OperationParameterAssignment : 
  				typeReferences.getTypeForName(container?.name?.datatype?.class_FQN?.toString, generationSelektor)
  			ActivityOperationParameterAssignment :
  				typeReferences.getTypeForName(container?.name?.dataType?.class_FQN?.toString, generationSelektor)	
  		}
  	}
}