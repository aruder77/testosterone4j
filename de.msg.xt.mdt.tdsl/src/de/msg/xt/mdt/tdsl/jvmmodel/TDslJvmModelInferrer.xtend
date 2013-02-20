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
import org.eclipse.xtext.common.types.JvmEnumerationLiteral
import de.msg.xt.mdt.tdsl.tDsl.SUT
import org.eclipse.xtext.xbase.compiler.ImportManager

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
	
	@Inject extension NamingExtensions 
	@Inject extension MetaModelExtensions
	
	@Inject
	XbaseCompiler xbaseCompiler
	
	@Inject
	TypesFactory typesFactory;
	
	def dispatch void infer(PackageDeclaration pack, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		
		for (element : pack.elements) {
			element.infer(acceptor, isPreIndexingPhase)
		}
	}

	def dispatch void infer(SUT sut, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(sut.toInterface(sut.activityAdapter_FQN, [])).initializeLater [
			
			members += sut.toMethod("beforeTest", sut.newTypeRef(typeof(Object))) [
				it.setAbstract(true)
			]
			
			members += sut.toMethod("findContext", sut.newTypeRef(typeof(Object))) [
				it.setAbstract(true)
				it.parameters += sut.toParameter("id", sut.newTypeRef(typeof(String)))
				it.parameters += sut.toParameter("type", sut.newTypeRef(typeof(String)))
			]
			
			members += sut.toMethod("performOperation", sut.newTypeRef(typeof(Object))) [
				it.setAbstract(true)
				it.parameters += sut.toParameter("activityId", sut.newTypeRef(typeof(String)))
				it.parameters += sut.toParameter("activityType", sut.newTypeRef(typeof(String)))
				it.parameters += sut.toParameter("contextObject", sut.newTypeRef(typeof(Object)))
				it.parameters += sut.toParameter("operationName", sut.newTypeRef(typeof(String)))
				it.parameters += sut.toParameter("parameters", sut.newTypeRef(typeof(Object)).createArrayType)
			]
			
			for (Control control : sut.controls) {
				if (control?.name != null && control?.class_FQN?.toString != null) {
					members += control.toMethod("get" + control.name.toFirstUpper, control.newTypeRef(control.class_FQN.toString)) [
						it.setAbstract(true)
						it.parameters += control.toParameter("contextObject", control.newTypeRef(typeof(Object)))
						it.parameters += control.toParameter("controlName", control.newTypeRef(typeof(String)))
					]
				}
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
   			
   			val packageDecl = activity.eContainer as PackageDeclaration
   			val activityAdapterType = activity.newTypeRef(activity.sut.activityAdapter_FQN)
   			members += activity.toField("adapter", activityAdapterType) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''INJECTOR.getInstance(«activity.sut.activityAdapter_FQN».class)''')
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
				if (field?.control?.fullyQualifiedName?.toString != null) {
					members += field.toMethod(field.fieldGetterName, field.newTypeRef(field.control.fullyQualifiedName.toString)) [
						it.setBody [
							it.append('''
						    	 return «activity.class_SimpleName».adapter.get«field.control.name»(this.contextObject, "«field.identifier»");
							''')
						]
					]
				}
			}
			
			for (field : activity.fields) {
				if (field?.control != null) {
					for (operation : field.control.operations) {
						members += operation.toActivityDelegationMethod(field)
					}
				}
			}
			
			for (operation : activity.operations) {
				members += operation.toActivityOperation()
			}
   		])
   	}
   	
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
  			for (param : operation.params) {
				if (param?.dataType?.class_FQN?.toString != null) {
   					it.parameters += param.toParameter(param.name, param.newTypeRef(param.dataType.class_FQN.toString))
   				}
   			}
   			it.setBody [
   				it.append('''
   				   «IF !voidReturn»return new «nextActivityClass»(«ENDIF»adapter.performOperation(ID, ACTIVITY_TYPE, this.contextObject, "«operation.name»", new Object[] {«FOR param : operation.params SEPARATOR ', '»«param.name»«ENDFOR»})«IF !voidReturn»)«ENDIF»;
   				''')
   			]
   		]
   	}
   	
   	def JvmOperation toActivityDelegationMethod(Operation operation, Field field) {
   		operation.toMethod(field.activityControlDelegationMethodName(operation), field.returnTypeFieldOperation(operation)) [
			val opMapping = field.findOperationMappingForOperation(operation)
			if (opMapping != null) {
				for (dataTypeMapping : opMapping.dataTypeMappings) {
					if (dataTypeMapping.name != null && dataTypeMapping.datatype != null) {
						it.parameters += dataTypeMapping.toParameter(dataTypeMapping.name.name, dataTypeMapping.newTypeRef(dataTypeMapping.datatype.class_FQN.toString))
					}
				}
			} else {
				if (operation.params.size > 0 || operation.returnType != null) {
					//throw new IllegalArgumentException("An operation with parameters or return value must have an OperationMapping!")
				}
			}
   			setBody [
   				if (operation.returnType == null) {
   					val nextActivities = opMapping?.nextActivities
   					var Activity nextActivity = null
   					if (nextActivities != null && !nextActivities.empty) {
   						val condNextAct = nextActivities.get(0)
   						if (condNextAct != null) {
   							nextActivity = (condNextAct as ConditionalNextActivity).next
   						}
   					}
	   				it.append(
   						'''
   						«field.fieldGetterName»().«operation.name»(«mapParameters(field, operation)»);
   						return «IF nextActivity == null»this«ELSE»«nextActivity.class_SimpleName».find()«ENDIF»;
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
		if (opMapping != null)
			'''«FOR dataTypeMapping : opMapping.dataTypeMappings SEPARATOR ','»«dataTypeMapping?.name?.name».getValue()«ENDFOR»'''
		else
			""
   	}
   	
   	def JvmTypeReference returnTypeFieldOperation(Field field, Operation operation) {
 		val opMapping = field.findOperationMappingForOperation(operation)
 		val currentActivityType = field.newTypeRef(field.parentActivity.class_FQN.toString) 
   		if (operation.returnType == null) {
   			if (opMapping == null) {
   					currentActivityType
   			} else {
   				if (opMapping?.nextActivities?.empty || opMapping?.nextActivities?.get(0)?.next?.class_FQN?.toString == null) {
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
   				opMapping.newTypeRef(opMapping?.dataType?.class_FQN?.toString)
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
   			
   			members += test.toField("ADAPTER", test.newTypeRef(test.sut.activityAdapter_FQN)) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''INJECTOR.getInstance(«test.sut.activityAdapter_FQN».class)''')
   				]
   			]
   			
   			
   			if (test?.useCase?.class_FQN?.toString != null) {
   				members += test.toField("useCase", test.newTypeRef(test.useCase.class_FQN.toString)) [
   					it.setFinal(true)
   				]
   			}
   			
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
	   			if (test?.useCase?.class_FQN?.toString != null) {
   					parameters += test.toParameter("useCase", test.newTypeRef(test.useCase.class_FQN.toString))
   				}
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
   		
   		val JvmEnumerationType equivalenceClass = dataType.toEnumerationType(dataType.equivalenceClass_name)[
   			superTypes += dataType.newTypeRef("de.msg.xt.mdt.base.EquivalenceClass")
   			
   			for (clazz : dataType.classes) {
   				val lit = clazz.toEnumerationLiteral(clazz.name);
   				lit.setStatic(true)
   				members += lit
   			}
   		]
   		acceptor.accept(equivalenceClass).initializeLater [
   			
   			members += dataType.toMethod("getValue", dataType.type?.mappedBy) [
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
   			
			members += dataType.toField("_value", dataType.type?.mappedBy) [
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
				parameters += dataType.toParameter("value", dataType.type?.mappedBy)
				parameters += dataType.toParameter("equivalenceClass", newTypeRef(equivalenceClass))
				
				it.body = [it.append(
					'''
						this._value = value;
						this._equivalenceClass = equivalenceClass;
					''')]
			]
			
			members += dataType.toMethod("getValue", dataType.type?.mappedBy) [
				it.body = [
					it.append(
						'''
						return this._value;
						'''
					)
				]
			]
			members += dataType.toMethod("setValue", null) [
				it.parameters += dataType.toParameter("value", dataType.type?.mappedBy)
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
   			it.superTypes += useCase.newTypeRef("de.msg.xt.mdt.base.BaseUseCase")
   			it.superTypes += useCase.newTypeRef("java.lang.Runnable")

   			it.annotations += useCase.toAnnotation("javax.xml.bind.annotation.XmlRootElement")
   			
   			for (inputParam : useCase.inputParameter) {
   				if (inputParam.name != null && inputParam?.dataType?.class_FQN?.toString != null) {
   					members += inputParam.toField(inputParam.name, inputParam.newTypeRef(inputParam.dataType.class_FQN.toString)) [
   					it.annotations += inputParam.toAnnotation("javax.xml.bind.annotation.XmlElement")
   					]
   				}
   			}
   			
   			it.members += useCase.toConstructor() []
   			it.members += useCase.toConstructor() [
   				it.parameters += useCase.toParameter("generator", useCase.newTypeRef("de.msg.xt.mdt.base.Generator"))
   				body = [
   					it.append('''
   					    this();
   					    this.generator = generator;''')
   					for (inputParam : useCase.inputParameter) {
   						newLine
   						it.append(
   							'''
   							this.«inputParam.name» = this.getOrGenerateValue(«inputParam.dataType.class_FQN.toString».class, "«inputParam.name»");
   							''')
	   				}
   				]
   			]
   			
   			if (!useCase.inputParameter.empty) {
   				it.members += useCase.toConstructor() [
   					for (inputParam : useCase.inputParameter) {
   						if (inputParam?.dataType?.class_FQN?.toString != null) {
   							it.parameters += useCase.toParameter(inputParam.name, useCase.newTypeRef(inputParam.dataType.class_FQN.toString))
   						}	
   					}
	   				body = [
	   					it.append('''this();''')
		   				for (inputParam : useCase.inputParameter) {
		   					newLine
   							it.append(
   								'''
   								this.«inputParam.name» = «inputParam.name»;
   								''')
	   					}
   					]
   				]
   			}
   			
   			for (inputParam : useCase.inputParameter) {
   				if (inputParam?.dataType?.class_FQN?.toString != null) {
   					members += inputParam.toSetter(inputParam.name, inputParam.newTypeRef(inputParam.dataType.class_FQN.toString))	
   				} 
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
   				if (useCase?.initialActivity?.class_FQN != null) {
   					it.parameters += useCase.toParameter("initialActivity", useCase.newTypeRef(useCase.initialActivity.class_FQN.toString))
 	  			}
 	
   				body = [
   					it.append('''
   						de.msg.xt.mdt.base.AbstractActivity activity = initialActivity;
   						''')
   					for (statement : useCase.block.expressions) {
   						xbaseCompiler.compile(statement, it, "void".getTypeForName(statement), null)
   					}
   				]
   			]   				
   		]
   	}
   	
   	def dispatch Activity inferStatement(XExpression statement, Activity currentActivity, int activityIndex, ITreeAppendable app) {
   		currentActivity
   	}
   	
	def dispatch Activity inferStatement(OperationCall opCall, Activity currentActivity, int activityIndex, ITreeAppendable app) {
		val field = opCall.operation.eContainer as Field
		app.append(
			'''
			«currentActivity.localVariable_name(activityIndex)».«field.activityControlDelegationMethodName(opCall.operation.name)»();
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
   			superTypes += tags.newTypeRef("de.msg.xt.mdt.base.Tag")
   			for (tag : tags.tags) {
   				val enumLit = toEnumerationLiteral(tag.name)
   				enumLit.setStatic(true) 
   				members += enumLit
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

