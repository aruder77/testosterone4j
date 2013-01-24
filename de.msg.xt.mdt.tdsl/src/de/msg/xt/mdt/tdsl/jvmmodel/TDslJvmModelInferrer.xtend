package de.msg.xt.mdt.tdsl.jvmmodel

import com.google.inject.Inject
import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.ConditionalNextActivity
import de.msg.xt.mdt.tdsl.tDsl.Control
import de.msg.xt.mdt.tdsl.tDsl.DataType
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.Operation
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import de.msg.xt.mdt.tdsl.tDsl.Parameter
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import de.msg.xt.mdt.tdsl.tDsl.TagsDeclaration 
import de.msg.xt.mdt.tdsl.tDsl.UseCase
import java.util.Collection
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import org.eclipse.emf.ecore.EObject
import org.eclipse.jdt.annotation.Nullable
import org.eclipse.xtext.common.types.JvmAnnotationReference
import org.eclipse.xtext.common.types.JvmAnnotationType
import org.eclipse.xtext.common.types.JvmConstructor
import org.eclipse.xtext.common.types.JvmEnumerationType
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.common.types.JvmTypeAnnotationValue
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.common.types.TypesFactory
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.lib.Procedures$Procedure1
import de.msg.xt.mdt.tdsl.tDsl.Test
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation

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
	@Inject extension ActivityExtensions
	
	@Inject extension DataTypeNaming
	
	@Inject extension UseCaseNaming
	
	@Inject extension PackageDeclarationNaming
	
	@Inject extension ControlNaming
	
	@Inject
	XbaseCompiler xbaseCompiler
	
	@Inject
	TypesFactory typesFactory;
	
	def dispatch void infer(PackageDeclaration pack, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		
		for (element : pack.elements) {
			element.infer(acceptor, isPreIndexingPhase)
		}

		acceptor.accept(pack.toInterface(pack.activityAdapter_FQN, [])).initializeLater [
			
			members += pack.toMethod("beforeTest", pack.newTypeRef(typeof(Object))) [
				it.setAbstract(true)
			]
			
			members += pack.toMethod("findContext", pack.newTypeRef(typeof(Object))) [
				it.setAbstract(true)
				it.parameters += pack.toParameter("id", pack.newTypeRef(typeof(String)))
				it.parameters += pack.toParameter("type", pack.newTypeRef(typeof(String)))
			]
			
			members += pack.toMethod("performOperation", pack.newTypeRef(typeof(Object))) [
				it.setAbstract(true)
				it.parameters += pack.toParameter("activityId", pack.newTypeRef(typeof(String)))
				it.parameters += pack.toParameter("activityType", pack.newTypeRef(typeof(String)))
				it.parameters += pack.toParameter("contextObject", pack.newTypeRef(typeof(Object)))
				it.parameters += pack.toParameter("operationName", pack.newTypeRef(typeof(String)))
			]
			
			for (Control control : pack.elements.filter(typeof(Control))) {
				members += control.toMethod("get" + control.name.toFirstUpper, control.newTypeRef(control.class_FQN.toString)) [
					it.setAbstract(true)
					it.parameters += control.toParameter("contextObject", control.newTypeRef(typeof(Object)))
					it.parameters += control.toParameter("controlName", control.newTypeRef(typeof(String)))
				]
			}
		]
	}
	

   	def dispatch void infer(Activity activity, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		val activityClass = activity.toClass(activity.class_FQN)
   		acceptor.accept(activityClass).initializeLater([
   			
   			superTypes += activity.newTypeRef("de.msg.xt.mdt.base.AbstractActivity")
   			
   			members += activity.toField("ID", activity.newTypeRef(typeof(String))) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer[
   						it.append('''"«activity.identifier»"''')
   				]
   			]
   			   				
   			members += activity.toField("ACTIVITY_TYPE", activity.newTypeRef(typeof(String))) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer[
   						it.append('''"«activity.type.name»"''')
   				]
   			]
   			
   			members += activity.toField("INJECTOR", activity.newTypeRef("com.google.inject.Injector")) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''com.google.inject.Guice.createInjector(new de.msg.xt.mdt.base.TDslModule())''')
   				]
   			]
   			
   			members += activity.toField("adapter", activity.newTypeRef((activity.eContainer as PackageDeclaration).activityAdapter_FQN)) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''INJECTOR.getInstance(ActivityAdapter.class)''')
   				]
   			]
   			   				
   			members += activity.toField("contextObject", activity.newTypeRef(typeof(Object)))
   			
   			members += activity.toMethod("find", newTypeRef(activityClass)) [
   				it.setStatic(true)
   				
   				setBody [
   					it.append(
   					'''
   					return new «activity.class_FQN.toString»(adapter.findContext(ID, ACTIVITY_TYPE));
   					''')
   				]
   			]
   			
   			members += activity.toConstructor [
   			]
   			
   			members += activity.toConstructor [
   				parameters += activity.toParameter("context", activity.newTypeRef(typeof(Object)))
   				it.setBody [
   					it.append('''
   					    this();
   					    this.contextObject = context;
   					''')
   				]
   			]
   			
			for (field : activity.fields) {
				members += field.toMethod(field.fieldGetterName, field.newTypeRef(field.control.fullyQualifiedName.toString)) [
					it.setBody [
						it.append('''
						     return «activity.class_SimpleName».adapter.get«field.control.name»(this.contextObject, "«field.identifier»");
						''')
					]
				]
			}
			
			for (field : activity.fields) {
				for (operation : field.control.operations) {
					members += operation.toActivityDelegationMethod(field)
				}
			}
			
			for (operation : activity.operations) {
				members += operation.toActivityOperation()
			}
   		])
   	} // dies ist ein Test
   	
   	def JvmOperation toActivityOperation(ActivityOperation operation) {
   		var JvmTypeReference returnTypeRef
   		var Boolean voidReturnType = true
   		var String nextActivityClassVar 
   		if (operation.nextActivities.empty) {
   			returnTypeRef = operation.newTypeRef(typeof(void) as Class<?>)
   		} else {
   			val Activity nextActivity = operation.nextActivities.get(0).next
   			nextActivityClassVar = nextActivity?.class_FQN?.toString
   			if (nextActivityClassVar != null) {
   				returnTypeRef = operation.newTypeRef(nextActivityClassVar)
   				voidReturnType = false
   			} else {
   				returnTypeRef = operation.newTypeRef(typeof(void) as Class<?>)
   			}
   		}
   		val voidReturn = voidReturnType
   		val nextActivityClass = nextActivityClassVar
   		operation.toMethod(operation.name, returnTypeRef) [
   			it.setBody [
   				it.append('''
   				   «IF !voidReturn»return new «nextActivityClass»(«ENDIF»adapter.performOperation(ID, ACTIVITY_TYPE, this.contextObject, "«operation.name»")«IF !voidReturn»)«ENDIF»;
   				''')
   			]
   		]
   	}
   	
   	def JvmOperation toActivityDelegationMethod(Operation operation, Field field) {
   		operation.toMethod(field.activityControlDelegationMethodName(operation), field.returnTypeFieldOperation(operation)) [
			val opMapping = field.findOperationMappingForOperation(operation)
			if (opMapping != null) {
				for (dataTypeMapping : opMapping.dataTypeMappings) {
					if (dataTypeMapping.controlOperationParameter != null && dataTypeMapping.datatype != null) {
						it.parameters += dataTypeMapping.toParameter(dataTypeMapping.controlOperationParameter.name, dataTypeMapping.newTypeRef(dataTypeMapping.datatype.class_FQN.toString))
					}
				}
			} else {
				if (operation.params.size > 0 || operation.returnType != null) {
					//throw new IllegalArgumentException("An operation with parameters or return value must have an OperationMapping!")
				}
			}
   			setBody [
   				if (operation.returnType == null) {
	   				it.append(
   						'''
   						«field.fieldGetterName»().«operation.name»(«mapParameters(field, operation)»);
   						return «IF opMapping.nextActivities.empty»this«ELSE»«(opMapping.nextActivities.get(0) as ConditionalNextActivity).next.class_SimpleName».find()«ENDIF»;
   						'''
   					)
   				} else {
   					it.append(
   						'''
   						«operation.returnType.name» value = «field.fieldGetterName»().«operation.name»(«mapParameters(field, operation)»); 
   						return new «opMapping.dataType.class_FQN»(value, «opMapping.dataType.equivalenceClass_name».getByValue(value));
   						'''
   					)
   				}
   			]
   		]
   	}
   	
   	def String mapParameters(Field field, Operation operation) {
		val opMapping = field.findOperationMappingForOperation(operation)
		'''«FOR dataTypeMapping : opMapping.dataTypeMappings SEPARATOR ','»«dataTypeMapping?.controlOperationParameter?.name».getValue()«ENDFOR»'''
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
   					field.newTypeRef(opMapping.nextActivities.get(0).next.class_FQN.toString)
   				}
   			}   			
   		} else {
   			if (opMapping == null || opMapping.dataType == null) {
   				System::out.println("For operations with return type a mapping with dataType must be specified!")
   				//throw new IllegalArgumentException("For operations with return type a mapping with dataType must be specified!")
   			} else {
   				opMapping.newTypeRef(opMapping.dataType.class_FQN.toString)
   			}
   		}
   	}
   	
   	
   	/**
   	 * This is a workaround to the uncomplete implementation of JvmTypesBuilder.toAnnotation. The original
   	 * does not support annotation values other than String. This is a copy of the original but
   	 * adapted to this particular Use Case (produce @RunWith(@Parameterized))
   	 * 
	 * Creates and returns an annotation reference of the given annotation type's name and the given value.
	 * 
	 * @param sourceElement
	 *            the source element to associate the created element with.
	 *            
	 * @return a result representing an annotation reference to the given annotation type, <code>null<code> if 
	 * 		sourceElement or annotationType are <code>null</code>.  
	 */
	@Nullable 
	def JvmAnnotationReference toRunWithAnnotation(@Nullable EObject sourceElement) {
		var JvmAnnotationReference result = typesFactory.createJvmAnnotationReference();
		val annotationTypeName = "org.junit.runner.RunWith"
		val value = sourceElement.newTypeRef("de.msg.xt.mdt.base.TDslParameterized")
		val JvmType jvmType = references.findDeclaredType(annotationTypeName, sourceElement);
		if (jvmType == null) {
			throw new IllegalArgumentException("The type "+annotationTypeName +" is not on the classpath.");
		}
		if (!(jvmType instanceof JvmAnnotationType)) {
			throw new IllegalArgumentException("The given class " + annotationTypeName + " is not an annotation type.");
		}
		result.setAnnotation(jvmType as JvmAnnotationType);

		val JvmTypeAnnotationValue annotationValue = typesFactory.createJvmTypeAnnotationValue();
		annotationValue.getValues().add(value);
		result.getValues().add(annotationValue);

		return result;
	}
   	
   	
   	def dispatch void infer(Test test, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		acceptor.accept(test.toClass(test.fullyQualifiedName)).initializeLater([
   			
   			annotations += test.toRunWithAnnotation
   			
   			members += test.toField("TEST_CASES_XML", test.newTypeRef(typeof(String))) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer[
   					it.append('''"./«test.name.toUpperCase»_«System::currentTimeMillis»"''')
   				]
   			]
   			
   			members += test.toField("INJECTOR", test.newTypeRef("com.google.inject.Injector")) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''com.google.inject.Guice.createInjector(new de.msg.xt.mdt.base.TDslModule());''')
   				]
   			]
   			
   			members += test.toField("ADAPTER", test.newTypeRef((test.eContainer as PackageDeclaration).activityAdapter_FQN)) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''INJECTOR.getInstance(ActivityAdapter.class)''')
   				]
   			]
   			
   			
   			members += test.toField("useCase", test.newTypeRef(test.useCase.class_FQN.toString)) [
   				it.setFinal(true)
   			]
   			
   			members += test.toMethod("config", test.newTypeRef(typeof(Collection), test.newTypeRef(typeof(Object)).createArrayType)) [
   				it.setStatic(true)
   				annotations += test.toAnnotation("de.msg.xt.mdt.base.Parameters")
   				it.setBody [
   					it.append('''
                        de.msg.xt.mdt.base.GenerationHelper testHelper = INJECTOR.getInstance(de.msg.xt.mdt.base.GenerationHelper.class);
                        de.msg.xt.mdt.base.Generator generator = INJECTOR.getInstance(de.msg.xt.mdt.base.Generator.class);
                        return testHelper.readOrGenerateTestCases(TEST_CASES_XML, generator, «test.useCase.class_FQN.toString».class); 
   					''')
   				]
   			]
   			
   			members += test.toConstructor [
   				parameters += test.toParameter("useCase", test.newTypeRef(test.useCase.class_FQN.toString))
   				body = [
   					it.append('''
                       this.useCase = useCase;
                       INJECTOR.injectMembers(this);
                       ''')
   				]
   			]
   			
   			members += test.toMethod("setup", "void".getTypeForName(test)) [
   				annotations += test.toAnnotation(typeof(org.junit.Before))
   				
   				body = [
   					it.append('''ADAPTER.beforeTest();''')
   				]
   			]
   			
   			members += test.toMethod("test", "void".getTypeForName(test)) [
   				annotations += test.toAnnotation(typeof(org.junit.Test))
   				
   				body = [
   					it.append('''this.useCase.run();''')
   				]
   			]
   		])
   	}


   	def dispatch void infer(Control control, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		acceptor.accept(control.toInterface(control.fullyQualifiedName.toString) [
   			
   			for (operation : control.operations) {
   				val returnType = 
   					if (operation.returnType != null)
   						operation.returnType.mappedBy
   					else
   						references.getTypeForName(typeof(void) as Class<?>, operation)
   				val JvmOperation op = operation.toMethod(operation.name, returnType) [
   					for (param : operation.params) {
   						parameters += param.toParameter(param.name, param.type.mappedBy)
   					}   					
   				]
   				op.setAbstract(true)
   				members += op
   			}
   		])
   	}

   		
   	def dispatch void infer(DataType dataType, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		val JvmEnumerationType equivalenceClass = dataType.toEnumerationType(dataType.equivalenceClass_name)[]
   		acceptor.accept(equivalenceClass).initializeLater [
   			
   			superTypes += dataType.newTypeRef("de.msg.xt.mdt.base.EquivalenceClass")
   			
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
				        if (value != null) {
				            switch (value) {
    					    «FOR clazz : dataType.classes»                           
				                case "«clazz.value»":
				                    clazz = «clazz.name»;
				                    break;
				            «ENDFOR»
				            }
				        }
				        return clazz;
   					''')
   				]
   			]
   		]

   		
   		acceptor.accept(dataType.toClass(dataType.fullyQualifiedName)).initializeLater([
   			superTypes += dataType.newTypeRef("de.msg.xt.mdt.base.DataType", dataType.newTypeRef(typeof(String)), newTypeRef(equivalenceClass))
   			
   			annotations += dataType.toAnnotation(typeof(XmlRootElement))
   			
			members += dataType.toField("_value", dataType.type.mappedBy) [
				annotations += dataType.toAnnotation(typeof(XmlAttribute))
			]
			members += dataType.toField("_equivalenceClass", newTypeRef(equivalenceClass)) [
				annotations += dataType.toAnnotation(typeof(XmlAttribute))
			]
			
			members += dataType.toConstructor [
				setVisibility(JvmVisibility::PUBLIC)
			]
			
			members += dataType.toConstructor [
				setVisibility(JvmVisibility::PUBLIC)
				parameters += dataType.toParameter("value", dataType.type.mappedBy)
				parameters += dataType.toParameter("equivalenceClass", newTypeRef(equivalenceClass))
				
				it.body = [it.append(
					'''
						this._value = value;
						this._equivalenceClass = equivalenceClass;
					''')]
			]
			
			members += dataType.toMethod("getValue", dataType.type.mappedBy) [
				it.body = [
					it.append(
						'''
						return this._value;
						'''
					)
				]
			]
			members += dataType.toMethod("setValue", null) [
				it.parameters += dataType.toParameter("value", dataType.type.mappedBy)
				it.body = [
					it.append(
						'''
						this._value = value;
						'''
					)
				]
			]

			members += dataType.toMethod("getEquivalenceClass", newTypeRef(equivalenceClass)) [
				it.body = [
					it.append(
						'''
						return this._equivalenceClass;
						'''
					)
				]
			]
			members += dataType.toMethod("setEquivalenceClass", null) [
				it.parameters += dataType.toParameter("equivalenceClass", newTypeRef(equivalenceClass))
				it.body = [
					it.append(
						'''
						this._equivalenceClass = equivalenceClass;
						'''
					)
				]
			]
			
			members += dataType.toMethod("getEquivalenceClassEnum", dataType.newTypeRef(typeof(Class), newTypeRef(equivalenceClass))) [
				it.body = [
					it.append(
						'''
						return «dataType.equivalenceClass_name».class;
						'''
					)
				]
			]
   		])
   	}
   	
   	def dispatch void infer(UseCase useCase, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		val useCaseClass = useCase.toClass(useCase.class_FQN)
   		acceptor.accept(useCaseClass).initializeLater [
   			it.superTypes += useCase.newTypeRef("java.lang.Runnable")

   			it.annotations += useCase.toAnnotation("javax.xml.bind.annotation.XmlRootElement")
   			
   			for (inputParam : useCase.inputParameter) {
   				members += inputParam.toField(inputParam.name, inputParam.newTypeRef(inputParam.dataType.class_FQN.toString)) [
   					it.annotations += inputParam.toAnnotation("javax.xml.bind.annotation.XmlElement")
   				]
   			}
   			
   			for (subUseCaseCall : useCase.block.expressions.filter(typeof(SubUseCaseCall))) {
   				if (subUseCaseCall.useCase != null) {
   					members += subUseCaseCall.toField(subUseCaseCall.useCase.name, subUseCaseCall.newTypeRef(subUseCaseCall.useCase.class_FQN.toString)) [
   						it.annotations += subUseCaseCall.toAnnotation("javax.xml.bind.annotation.XmlElement")
	   				]
   				}
   			}
   			
   			it.members += useCase.toField("generator", useCase.newTypeRef("de.msg.xt.mdt.base.Generator")) [
   				it.annotations += useCase.toAnnotation("javax.xml.bind.annotation.XmlTransient")
   			]
   			
   			it.members += useCase.toConstructor() []
   			it.members += useCase.toConstructor() [
   				it.parameters += useCase.toParameter("generator", useCase.newTypeRef("de.msg.xt.mdt.base.Generator"))
   				body = [
   					it.append('''this.generator = generator;''')
   				]
   			]
   			
   			if (!useCase.inputParameter.empty) {
   				it.members += useCase.toConstructor() [
   					for (inputParam : useCase.inputParameter) {
   						it.parameters += useCase.toParameter(inputParam.name, useCase.newTypeRef(inputParam.dataType.class_FQN.toString))	
   					}
	   				for (subUseCaseCall : useCase.block.expressions.filter(typeof(SubUseCaseCall))) {
   						if (subUseCaseCall.useCase != null) {
   							it.parameters += useCase.toParameter(subUseCaseCall.useCase.name, newTypeRef(useCaseClass))
   						}
   					}
	   				body = [
		   				for (inputParam : useCase.inputParameter) {
   							it.append(
   								'''
   								this.«inputParam.name» = «inputParam.name»;
   								''')
	   					}
		   				for (subUseCaseCall : useCase.block.expressions.filter(typeof(SubUseCaseCall))) {
	   						it.append(
	   							'''
	   							this.«subUseCaseCall.useCase.name» = «subUseCaseCall.useCase.name»;
	   							''')
	   					}				
   					]
   				]
   			}
   			
   			
   			it.members += useCase.toMethod("run", null) [
   				it.annotations += useCase.toAnnotation("java.lang.Override")
   				
   				body = [
   					it.append(
   						'''
   						execute(«useCase.initialActivity.class_SimpleName».find());
   						'''
   					)
   				]
   			]
   			
   			it.members += useCase.toMethod("execute", null) [
   				it.parameters += useCase.toParameter("initialActivity", useCase.newTypeRef(useCase.initialActivity.class_FQN.toString))
   				
   				body = [
   					it.append('''
   						de.msg.xt.mdt.base.AbstractActivity activity = initialActivity;
   						''')
   					for (statement : useCase.block.expressions) {
   						xbaseCompiler.compile(statement, it, "void".getTypeForName(statement), null)
   					}
   				]
   			]
   			
   			for (inputParam : useCase.inputParameter) {
   				it.members += useCase.toMethod("get" + inputParam.name.toFirstUpper, inputParam.newTypeRef(inputParam.dataType.class_FQN.toString)) [
   					body = [
   						it.append(
                            '''
                            if (this.«inputParam.name» == null && this.generator != null) {
                                this.«inputParam.name» = this.generator.generateDataTypeValue(«inputParam.dataType.class_FQN.toString».class, "«useCase.inputParamID(inputParam)»");
                            }
                            return this.«inputParam.name»;'''
   						)
   					]
   				]  
   				it.members += useCase.toSetter(inputParam.name, inputParam.newTypeRef(inputParam.dataType.class_FQN.toString))				
   			}
   			
   			for (subUseCaseCall : useCase.block.expressions.filter(typeof(SubUseCaseCall))) {
   				if (subUseCaseCall.useCase != null) {
   					members += subUseCaseCall.toMethod(subUseCaseCall.useCase.subUseCaseGetter, subUseCaseCall.newTypeRef(subUseCaseCall.useCase.class_FQN.toString)) [
   						body = [
   							it.append('''
                                if (this.«subUseCaseCall.useCase.name» == null && this.generator != null) {
                                    this.«subUseCaseCall.useCase.name» = new «subUseCaseCall.useCase.class_SimpleName»(this.generator);
                                }
                                return this.«subUseCaseCall.useCase.name»;
   							''')
   						]
	   				]
   				}
   			}
   			
   		]
   	}
   	
   	def dispatch Activity inferStatement(XExpression statement, Activity currentActivity, int activityIndex, ITreeAppendable app) {
   		currentActivity
   	}
   	
	def dispatch Activity inferStatement(OperationCall opCall, Activity currentActivity, int activityIndex, ITreeAppendable app) {
		val field = opCall.operation.eContainer as Field
		app.append(
			'''
			«currentActivity.localVariable_name(activityIndex)».«field.activityControlDelegationMethodName(opCall.operation.operation)»();
			'''
		)
		if (!opCall.operation.nextActivities.empty) {
			opCall.operation.nextActivities.get(0).next
		} else {
			currentActivity
		}
	}

   	
   	def inputParamID(UseCase useCase, Parameter inputParam) {
   		useCase.name + "_" + inputParam.name
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

