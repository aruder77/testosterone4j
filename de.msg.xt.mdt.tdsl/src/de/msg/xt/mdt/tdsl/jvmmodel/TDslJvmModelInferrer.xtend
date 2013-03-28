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
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import java.util.List
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameter
import org.eclipse.xtext.common.types.JvmGenericType

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
		if (sut.activityAdapter_FQN != null) {
			acceptor.accept(sut.toInterface(sut.activityAdapter_FQN, [])).initializeLater [
			
				superTypes += sut.newTypeRef("de.msg.xt.mdt.base.ActivityAdapter")
			
				for (Control control : sut.controls) {
					if (control?.name != null && control?.class_FQN?.toString != null) {
						members += control.toMethod("get" + control.name.toFirstUpper, control.newTypeRef(control.class_FQN.toString)) [
							it.setAbstract(true)
							it.parameters += control.toParameter("controlName", control.newTypeRef(typeof(String)))
						]
					}
				}
			]
		}
	}
	

   	def dispatch void infer(Activity activity, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		var JvmGenericType activityAdapterClassVar = null
   		if (activity.adapterInterface_FQN != null ) {
	   		activityAdapterClassVar = activity.toInterface(activity.adapterInterface_FQN) []
   			acceptor.accept(activityAdapterClassVar).initializeLater [
   				if (activity.parent?.adapterInterface_FQN != null) {
   					superTypes += activity.newTypeRef(activity.parent.adapterInterface_FQN)
	   			} else if (activity.sut?.activityAdapter_FQN != null) {
   					superTypes += activity.newTypeRef(activity.sut.activityAdapter_FQN)
   				}

	   			for (activityMethod : activity.operations) {
	   				if (activityMethod.name != null) {
   						members += activityMethod.toMethod(activityMethod.name, activityMethod.newTypeRef(typeof(Object))) [
							it.setAbstract(true)
   					
   							for (param : activityMethod.params) {
   								if (param?.name != null && param?.dataType?.type?.mappedBy != null) {
   									it.parameters += param.toParameter(param?.name, param?.dataType?.type?.mappedBy)
   								}
   							}
   					
   						]
   					}
   				}
   			]
   		}
   		val activityAdapterClass = activityAdapterClassVar
   		
   		
   		if (activity.class_FQN == null)
   			return
   			
   		val activityClass = activity.toClass(activity.class_FQN)
   		acceptor.accept(activityClass).initializeLater([
   			
   			if (activity?.parent?.class_FQN?.toString != null) {
   				superTypes += activity.newTypeRef(activity.parent.class_FQN.toString)
   			} else {
	   			superTypes += activity.newTypeRef("de.msg.xt.mdt.base.AbstractActivity")
   			}
   			
   			members += activity.toField("ID", activity.newTypeRef(typeof(String))) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer[
   						it.append('''"«activity.identifier»"''')
   				]
   			]
   			   				
   			members += activity.toField("injector", activity.newTypeRef("com.google.inject.Injector")) [
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''de.msg.xt.mdt.base.TDslInjector.getInjector()''')
   				]
   			]
   			
   			val packageDecl = activity.packageDeclaration
   			val activityLocatorType = activity.newTypeRef("de.msg.xt.mdt.base.ActivityLocator")
   			members += activity.toField("activityLocator", activityLocatorType) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''de.msg.xt.mdt.base.TDslInjector.getInjector().getInstance(de.msg.xt.mdt.base.ActivityLocator.class)''')
   				]
   			]
   			
   			    //private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
   			
   			members += activity.toField("protocol", activity.newTypeRef("de.msg.xt.mdt.base.ITestProtocol")) [
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''this.injector.getInstance(de.msg.xt.mdt.base.ITestProtocol.class)''');
   				]
   			]
   			   				
   			members += activity.toField("contextAdapter", newTypeRef(activityAdapterClass))
   			
   			members += activity.toMethod("find", newTypeRef(activityClass)) [
   				it.setStatic(true)
   				
   				setBody [
   					it.append(
   					'''
   					return new «activity.class_FQN.toString»(activityLocator.find(ID, «activity.adapterInterface_FQN».class));
   					''')
   				]
   			]
   			
   			members += activity.toConstructor [
   				it.setBody [
   					it.append('''
   						super();
   					''')
   				]
   			]
   			
   			members += activity.toConstructor [
   				parameters += activity.toParameter("contextAdapter", newTypeRef(activityAdapterClass))
   				it.setBody [
   					it.append('''
   					    «IF activity.parent != null»super(contextAdapter)«ELSE»this()«ENDIF»;
   					    this.contextAdapter = contextAdapter;
   					''')
   				]
   			]
   			
			for (field : activity.fields) {
				if (field?.control?.fullyQualifiedName?.toString != null) {
					members += field.toMethod(field.fieldGetterName, field.newTypeRef(field.control.fullyQualifiedName.toString)) [
						it.setBody [
							it.append('''
						    	 return this.contextAdapter.get«field.control.name»("«field.identifier»");
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
   		if (operation?.name == null) {
   			return null
   		}
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
   				    this.protocol.appendActivityOperationCall(this.getClass().getName(), "«operation.name»", null«appendActivityParameter(operation.params)»);
   				    Object o = contextAdapter.«operation.name»(«FOR param : operation.params SEPARATOR ', '»«param.name».getValue()«ENDFOR»);
   				    «IF !voidReturn»
   				    	«nextActivityClass»Adapter adapter = injector.getInstance(«nextActivityClass»Adapter.class);
   				    	adapter.setContext(o);
   				    	return new «nextActivityClass»(adapter);
   				    «ENDIF»
   				''')
   			]
   		]
   	}
   	
   	def JvmOperation toActivityDelegationMethod(Operation operation, Field field) {
   		if (field == null || operation == null)
   			return null
   		val activityMethodName = field.activityControlDelegationMethodName(operation)
   		val returnType = field.returnTypeFieldOperation(operation)
   		if (activityMethodName != null && returnType != null) { 
   			operation.toMethod(activityMethodName, returnType) [
				val opMapping = field.findOperationMappingForOperation(operation)
				if (opMapping != null) {
					for (dataTypeMapping : opMapping.dataTypeMappings) {
						if (dataTypeMapping?.name?.name != null && dataTypeMapping?.datatype?.class_FQN?.toString != null) {
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
   						//         this.protocol.appendControlOperationCall(this.getClass().getName(), "description", TextControl.class.getName(),
	            //    "setText", null, description.getValue());
   					
		   				it.append(
   							'''
   							this.protocol.appendControlOperationCall(this.getClass().getName(), "«field.name»", «field.control?.name».class.getName(), "«operation.name»", null«appendParameter(opMapping.dataTypeMappings)»);
   							«field.fieldGetterName»().«operation.name»(«mapParameters(field, operation)»);
   							return «IF nextActivity == null»this«ELSE»«nextActivity.class_SimpleName».find()«ENDIF»;
   							'''
   						)
   					} else {
   						if (opMapping != null) {
   							it.append(
   								'''
	   							«operation.returnType?.name» value = «field.fieldGetterName»().«operation.name»(«mapParameters(field, operation)»); 
   								this.protocol.appendControlOperationCall(this.getClass().getName(), "«field.name»", «field.control?.name».class.getName(), "«operation.name»", value.toString()«appendParameter(opMapping.dataTypeMappings)»);
   								return new «opMapping.dataType?.class_FQN»(value, «opMapping.dataType?.equivalenceClass_name».getByValue(value));
   								'''
   							)
   						}
   					}
   				]
	   		]
   		}
   	}
   	
   	def String appendParameter(List<DataTypeMapping> mappings) {
   		'''«FOR mapping : mappings», «mapping?.name?.name».getValue().toString()«ENDFOR»'''
   	}
   	
   	def String appendActivityParameter(List<ActivityOperationParameter> mappings) {
   		'''«FOR mapping : mappings», «mapping?.name».getValue().toString()«ENDFOR»'''
   	}

   	def String mapParameters(Field field, Operation operation) {
		val opMapping = field.findOperationMappingForOperation(operation)
		if (opMapping != null)
			'''«FOR dataTypeMapping : opMapping.dataTypeMappings SEPARATOR ", "»«dataTypeMapping?.name?.name».getValue()«ENDFOR»'''
		else
			""
   	}
   	
   	def JvmTypeReference returnTypeFieldOperation(Field field, Operation operation) {
   		if (field == null || operation == null)
   			return null
 		val opMapping = field.findOperationMappingForOperation(operation)
 		var JvmTypeReference currentActivityType = null
 		if (field.parentActivity?.class_FQN?.toString != null)
 			currentActivityType = field.newTypeRef(field.parentActivity.class_FQN.toString) 
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
   				if (opMapping?.dataType?.class_FQN?.toString != null) {
   					opMapping.newTypeRef(opMapping?.dataType?.class_FQN?.toString)
   				}
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
		annotationValue?.getValues().add(value);
		result.getValues().add(annotationValue);

		return result;
	}
   	
   	
   	def dispatch void infer(Test test, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		if (test.fullyQualifiedName == null)
   			return
   		
   		val testClass = test.toClass(test.fullyQualifiedName)
   		acceptor.accept(testClass).initializeLater([
   			
   			annotations += test.toRunWithAnnotation
   			
   			members += test.toField("TEST_CASES_SERIALIZATION", test.newTypeRef(typeof(String))) [
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
   					it.append('''de.msg.xt.mdt.base.TDslInjector.createInjector(TEST_CASES_SERIALIZATION)''')
   				]
   			]
   			
   			members += test.toField("LOCATOR", test.newTypeRef("de.msg.xt.mdt.base.ActivityLocator")) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''INJECTOR.getInstance(de.msg.xt.mdt.base.ActivityLocator.class)''')
   				]
   			]
   			
   			members += test.toField("protocol", test.newTypeRef("de.msg.xt.mdt.base.ITestProtocol")) [
   				it.setFinal(true)
   				it.setInitializer [
   					it.append('''INJECTOR.getInstance(de.msg.xt.mdt.base.ITestProtocol.class)''')
   				]
   			]
   			
   			members += test.toField("testNumber", test.newTypeRef(typeof(int))) [
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
                        LOCATOR.beforeTest();
                        return testHelper.readOrGenerateTestCases(TEST_CASES_SERIALIZATION, generator, «test.useCase?.class_FQN?.toString».class); 
   					''')
   				]
   			]
   			
   			members += test.toConstructor [
	   			if (test?.useCase?.class_FQN?.toString != null) {
   					parameters += test.toParameter("testDescriptor", test.newTypeRef("de.msg.xt.mdt.base.TestDescriptor", test.newTypeRef(test.useCase.class_FQN.toString)))
   				}
   				body = [
   					it.append('''
                       this.testNumber = testDescriptor.getTestNumber();
                       this.useCase = testDescriptor.getTestCase();
                       INJECTOR.injectMembers(this);
                       ''')
   				]
   			]
   			
   			members += test.toMethod("setup", "void".getTypeForName(test)) [
   				annotations += test.toAnnotation(typeof(org.junit.Before))
   				
   				body = [
   					it.append('''LOCATOR.beforeTest();''')
   				]
   			]
   			
   			members += test.toMethod("test", "void".getTypeForName(test)) [
   				annotations += test.toAnnotation(typeof(org.junit.Test))
   				
   				body = [
   					it.append('''
                        try {
                            this.protocol.openLog(this.testNumber);
                            this.protocol.newTest(String.valueOf(this.testNumber));
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                        try {
                        	this.useCase.run();
                        } finally {
                        	this.protocol.close();
                        }
   					''')
   				]
   			]
   		])
   	}


   	def dispatch void infer(Control control, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		if (control.fullyQualifiedName?.toString == null)
   			return;
   		
   		acceptor.accept(control.toInterface(control.fullyQualifiedName.toString) [
   			
   			for (operation : control.operations) {
   				val returnType = 
   					if (operation.returnType != null)
   						operation.returnType.mappedBy
   					else
   						references.getTypeForName(typeof(void) as Class<?>, operation)
   				if (operation.name != null) {
	   				val JvmOperation op = operation.toMethod(operation.name, returnType) [
   						for (param : operation.params) {
   							if (param.name != null && param.type?.mappedBy != null)
   								parameters += param.toParameter(param.name, param.type.mappedBy)
   						}   					
   					]
	   				op?.setAbstract(true)
   					if (op != null)
   						members += op
   				}
   			}
   		])
   	}

   		
   	def dispatch void infer(DataType dataType, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		if (dataType.name == null || dataType.equivalenceClass_name == null || dataType.type?.mappedBy == null)
   			return
   		
   		val JvmEnumerationType equivalenceClass = dataType.toEnumerationType(dataType.equivalenceClass_name)[
   			superTypes += dataType.newTypeRef("de.msg.xt.mdt.base.EquivalenceClass")
   			
   			for (clazz : dataType.classes) {
   				if (clazz.name != null) {
	   				val lit = clazz.toEnumerationLiteral(clazz.name);
   					lit.setStatic(true)
   					members += lit
   				}
   			}
   		]
   		acceptor.accept(equivalenceClass).initializeLater [
   			
   			if (dataType.type?.mappedBy != null) {
   				members += dataType.toMethod("getValue", dataType.type.mappedBy) [
   					setBody [
						it.append('''
        					«dataType.type.mappedBy.simpleName» value = null;
        					switch (this) {
        					''')
        				
        				for (clazz : dataType.classes) {
    	    				it.append('''
	        					case «clazz.name»:
        						value = ''')
        					val expectedType = dataType.type.mappedBy
        					xbaseCompiler.compileAsJavaExpression(clazz.value, it, expectedType)
        					it.append('''
        					;
        					break;
    	    				''')
	        			}
            			
            			it.append('''
        					}
        					return value;
						''')
					]	
	   			]
   			}
   			
   			members += dataType.toMethod("getTags", dataType.newTypeRef("de.msg.xt.mdt.base.Tag").createArrayType) [
   				setBody [
					it.append('''
					    de.msg.xt.mdt.base.Tag[] tags = null;                                   
					    switch (this) {            
    					«FOR clazz : dataType.classes»                           
    					case «clazz.name»:                                           
    						tags = new de.msg.xt.mdt.base.Tag[] { «FOR tag : clazz.tags SEPARATOR ', '»Tags.«tag.name»«ENDFOR» };
    						break;                       
        				«ENDFOR»                     
					    }                                                     
					    
					    return tags;                                          
					''')
				]	
   			]
   			
   			members += dataType.toMethod("getByValue", dataType.newTypeRef(it.fullyQualifiedName.toString)) [
   				it.setStatic(true)
   				if (dataType.type?.mappedBy != null) {
	   				it.parameters += it.toParameter("value", dataType.type.mappedBy)
   					it.setBody [
   						it.append('''
					        «dataType.name?.toFirstUpper + "EquivalenceClass"» clazz = null;
					        if (value != null) {
				    	''')
					    for (clazz : dataType.classes) {
					    	it.append('''
					    		if(value.equals(
					        ''')
					    	xbaseCompiler.compileAsJavaExpression(clazz.value, it, dataType.type.mappedBy)
				    	    it.append('''
				        		)) {
				        			clazz = «clazz.name»;
					        	}
					        	''')
					        }
					    it.append('''
					        }
				    	    return clazz;
	   					''')
   					]   				
   				}
   			]
   		]

   		
   		acceptor.accept(dataType.toClass(dataType.fullyQualifiedName)).initializeLater([
   			superTypes += dataType.newTypeRef("de.msg.xt.mdt.base.DataType", dataType.type.mappedBy, newTypeRef(equivalenceClass))
   			
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
   		if (useCase.class_FQN == null)
   			return
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
   						if (inputParam?.dataType?.class_FQN?.toString != null) {
	   						it.append(
   								'''
   								this.«inputParam.name» = this.getOrGenerateValue(«inputParam.dataType.class_FQN.toString».class, "«inputParam.name»");
   								''')
   						}
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
   						execute(«useCase.initialActivity?.class_SimpleName».find());
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
		val field = opCall?.operation?.eContainer as Field
		if (field != null) {
			app.append(
				'''
				«currentActivity?.localVariable_name(activityIndex)».«field.activityControlDelegationMethodName(opCall.operation.name)»();
				'''
			)
			if (!opCall.operation.nextActivities.empty) {
				opCall.operation.nextActivities.get(0).next
			} else {
				currentActivity
			}
		} else {
			currentActivity
		}
	}

   	
   	def inputParamID(UseCase useCase, Parameter inputParam) {
   		useCase?.name + "_" + inputParam?.name
   	}
   	
   	def dispatch void infer(TagsDeclaration tags, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		if (tags?.eContainer?.fullyQualifiedName?.toString != null) {
   			acceptor.accept(tags.toEnumerationType(tags.eContainer.fullyQualifiedName.toString + ".Tags") [
   				superTypes += tags.newTypeRef("de.msg.xt.mdt.base.Tag")
   				for (tag : tags.tags) {
   					if (tag.name != null) {
   						val enumLit = toEnumerationLiteral(tag.name)
   						enumLit.setStatic(true) 
	   					members += enumLit
   					}
   				}
	   		])
   		}
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

