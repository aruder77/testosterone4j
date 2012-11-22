package de.msg.xt.mdt.tdsl.jvmmodel

import com.google.inject.Inject
import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.Control
import de.msg.xt.mdt.tdsl.tDsl.DataType
import de.msg.xt.mdt.tdsl.tDsl.TagsDeclaration
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmConstructor
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.common.types.TypesFactory
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.lib.Procedures$Procedure1
import com.google.inject.internal.Initializer$InjectableReference
import org.eclipse.xtext.common.types.JvmOperation
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.Operation
import org.eclipse.xtext.common.types.JvmTypeReference
import de.msg.xt.mdt.tdsl.jvmmodel.FieldExtensions
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping

/**
 * <p>Infers a JVM model from the source model.</p> 
 *
 * <p>The JVM model should contain all elements that would appear in the Java code 
 * which is generated from the source model. Other models link against the JVM model rather than the source model.</p>     
 */
class TDslJvmModelInferrer extends AbstractModelInferrer {

    /**
     * convenience API to build and initialize JVM types and their members.
     */
	@Inject extension JvmTypesBuilder
	
	@Inject extension IQualifiedNameProvider
	
	@Inject extension TypeReferences references
	
	@Inject extension FieldNaming
	@Inject extension FieldExtensions
	
	@Inject extension ActivityNaming
	
	@Inject extension DataTypeNaming

   	def dispatch void infer(Activity activity, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		acceptor.accept(activity.toClass(activity.class_FQN)).initializeLater([
   			
  			for (param : activity.inputParameter) {
   				members += param.toField(param.name, param.newTypeRef(param.dataType.fullyQualifiedName.toString))
   			}
   				
   			for (field : activity.fields) {
   				members += field.toField(field.name, field.newTypeRef(field.control.fullyQualifiedName.toString))
   			}

   			members += activity.toConstructor [
   				for (param : activity.inputParameter) {
   					parameters += param.toParameter(param.name, param.newTypeRef(param.dataType.fullyQualifiedName.toString))
   				}
   				
   				it.setBody [
   					for (param : activity.inputParameter) {
   						it.append("this." + param.name + " = " + param.name + ";")
   					}
   				]
   			]
   			
			for (field : activity.fields) {
				members += field.toGetter(field.name, field.newTypeRef(field.control.fullyQualifiedName.toString))
			}
			
			for (field : activity.fields) {
				for (operation : field.control.operations) {
					members += operation.toActivityDelegationMethod(field)
				}
			}
   		])
   	}
   	
   	def JvmOperation toActivityDelegationMethod(Operation operation, Field field) {
   		operation.toMethod(field.activityControlDelegationMethodName(operation), field.returnTypeFieldOperation(operation)) [
			val opMapping = field.findOperationMappingForOperation(operation)
			for (dataTypeMapping : opMapping.dataTypeMappings) {
				it.parameters += dataTypeMapping.toParameter(dataTypeMapping.name.name, dataTypeMapping.newTypeRef(dataTypeMapping.datatype.class_FQN.toString))
			}
   			setBody [
   				if (operation.returnType == null) {
	   				it.append(
   						'''
   						«field.getterName»().«operation.name»(«mapParameters(field, operation)»);
        				return this;
   						'''
   					)
   				} else {
   					
   				}
   			]
   		]
   	}
   	
   	def String mapParameters(Field field, Operation operation) {
		val opMapping = field.findOperationMappingForOperation(operation)
		'''«FOR dataTypeMapping : opMapping.dataTypeMappings SEPARATOR ','»«dataTypeMapping.name.name»«ENDFOR»'''
   	}
   	
   	def JvmTypeReference returnTypeFieldOperation(Field field, Operation operation) {
 		val opMapping = field.findOperationMappingForOperation(operation)
 		val currentActivityType = field.newTypeRef(field.parentActivity.class_FQN.toString) 
   		if (operation.returnType == null) {
   			if (opMapping == null) {
   					currentActivityType
   			} else {
   				if (opMapping.nextActivities.empty) {
   					currentActivityType
   				} else {
   					field.newTypeRef((opMapping.nextActivities.get(0) as Activity).class_FQN.toString)
   				}
   			}   			
   		} else {
   			opMapping.newTypeRef(opMapping.dataType.class_FQN.toString)
   		}
   	}


   	def dispatch void infer(Control control, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		acceptor.accept(control.toClass(control.fullyQualifiedName)).initializeLater([
   			
   			members += control.toField("id", control.newTypeRef(typeof(String)))
   			
   			for (operation : control.operations) {
   				for (opParam : operation.params) {
   					members += opParam.toField(opParam.name, opParam.type.mappedBy)
   				}
   			}
   			   			
   			val	Procedure1<ITreeAppendable> body = [it.append(
					'''
						this.id = id;
					''')]
			members += control.toConstructor("id", body) [
   				parameters += control.toParameter("id", control.newTypeRef(typeof(String)))
   				setVisibility(JvmVisibility::PUBLIC)
   			]
   			
   			for (operation : control.operations) {
   				val returnType = 
   					if (operation.returnType != null)
   						operation.returnType.mappedBy
   					else
   						references.getTypeForName(typeof(void) as Class<?>, operation)
   				members += operation.toMethod(operation.name, returnType) [
   					for (param : operation.params) {
   						parameters += param.toParameter(param.name, param.type.mappedBy)
   					}
   					
   					setBody [
   						it.append(
   						'''
   						System.out.println("«control.name»[" + id + "].«operation.name»("
   						«FOR param : operation.params SEPARATOR ' + ", " '»
   							+ "«param.name» = " + «param.name»  
   						«ENDFOR»
   						+ ") called.");
   						''')
   						if (operation.returnType != null) {
   							it.append("return null;")
   						}
   					]
   				]
   			}
   		])
   	}

   		
   	def dispatch void infer(DataType dataType, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		acceptor.accept(dataType.toEnumerationType(dataType.fullyQualifiedName.toString + "EquivalenceClass")[]).initializeLater [
   			for (clazz : dataType.classes) {
   				members += clazz.toEnumerationLiteral(clazz.name);
   			}
   			
   			members += dataType.toMethod("getValue", dataType.type.mappedBy) [
   				setBody [
					it.append('''
        				«dataType.type.mappedBy.simpleName» value = null;
        				switch (this) {
        				«FOR clazz : dataType.classes»
        				case «clazz.name»:
        				value = "«clazz.value»";
        				break;
            			«ENDFOR»
        				}
        				return value;
					''')
				]	
   			]
   			
   			members += dataType.toMethod("getTags", dataType.newTypeRef(dataType.eContainer.fullyQualifiedName.toString + ".Tags").createArrayType) [
   				setBody [
					it.append('''
					    Tags[] tags = null;                                   
					    switch (this) {            
    					«FOR clazz : dataType.classes»                           
    					case «clazz.name»:                                           
    						tags = new Tags[] { «FOR tag : clazz.tags SEPARATOR ', '»Tags.«tag.name»«ENDFOR» };
    						break;                       
        				«ENDFOR»                     
					    }                                                     
					    
					    return tags;                                          
					''')
				]	
   			]
   			
   			members += dataType.toMethod("getByValue", dataType.newTypeRef(it.fullyQualifiedName.toString)) [
   				it.setStatic(true)
   				it.parameters += it.toParameter("value", dataType.type.mappedBy)
   				it.setBody [
   					it.append('''
				        «dataType.name.toFirstUpper + "EquivalenceClass"» clazz = null;
				        switch (value) {
    					«FOR clazz : dataType.classes»                           
				        case "«clazz.value»":
				        clazz = «clazz.name»;
				        break;
				        «ENDFOR»
				        }
				        return clazz;
   					''')
   				]
   			]
   		]

   		
   		acceptor.accept(dataType.toClass(dataType.fullyQualifiedName)).initializeLater([
   			annotations += dataType.toAnnotation(typeof(XmlRootElement))
   			
			members += dataType.toField("value", dataType.type.mappedBy) [
				annotations += dataType.toAnnotation(typeof(XmlAttribute))
			]
			members += dataType.toField("equivalenceClass", dataType.newTypeRef(dataType.fullyQualifiedName.toString + "EquivalenceClass")) [
				annotations += dataType.toAnnotation(typeof(XmlAttribute))
			]
			
			val	Procedure1<ITreeAppendable> body = [it.append(
					'''
						this.value = value;
						this.equivalenceClass = equivalenceClass;
					''')]
			members += dataType.toConstructor("value", body) [
				setVisibility(JvmVisibility::PUBLIC)
				parameters += dataType.toParameter("value", dataType.type.mappedBy)
				parameters += dataType.toParameter("equivalenceClass", dataType.newTypeRef(dataType.fullyQualifiedName.toString + "EquivalenceClass"))
			]
			
			members += dataType.toGetter("value", dataType.type.mappedBy)
			members += dataType.toGetter("equivalenceClass", dataType.newTypeRef(dataType.fullyQualifiedName.toString + "EquivalenceClass"))
   		])
   	}
   	
   	def dispatch void infer(TagsDeclaration tags, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		acceptor.accept(tags.toEnumerationType(tags.eContainer.fullyQualifiedName.toString + ".Tags") [
   			for (tag : tags.tags) {
   				members += toEnumerationLiteral(tag.name)
   			}
   		])
   	}   		


	def toConstructor(EObject sourceElement, String simpleName, Procedure1<ITreeAppendable> body, Procedure1<JvmConstructor> init) {
		val constructor = TypesFactory::eINSTANCE.createJvmConstructor
		constructor.simpleName = simpleName
		setBody(constructor, body)
		if (init != null && simpleName != null)
			init.apply(constructor)
		associate(sourceElement, constructor)
	}
}

