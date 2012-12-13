package de.msg.xt.mdt.tdsl.typeprovider

import de.msg.xt.mdt.tdsl.jvmmodel.DataTypeNaming
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage$Literals
import javax.inject.Inject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.typing.XbaseTypeProvider
import com.google.inject.Singleton
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall

@Singleton
class TDslTypeProvider extends XbaseTypeProvider {
	
	@Inject TypeReferences typeReferences
	
	@Inject extension DataTypeNaming
	
	@Inject JvmTypesBuilder typesBuilder

	def dispatch expectedType(OperationMapping container,
			EReference reference, int index, boolean rawType) {
		if (reference == TDslPackage$Literals::OPERATION_MAPPING__GUARD) {
			typesBuilder.newTypeRef(container, typeof(Boolean));
		}
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
  	
  	def dispatch type(SubUseCaseCall subUseCaseCall, JvmTypeReference typeRef, boolean isRawTypes) {
  		typeReferences.getTypeForName(Void::TYPE, subUseCaseCall)
  	}
}