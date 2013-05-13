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
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import java.util.List
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameter
import org.eclipse.xtext.common.types.JvmGenericType
import de.msg.xt.mdt.tdsl.tDsl.Toolkit
import org.eclipse.xtext.xbase.lib.Functions$Function1
import org.junit.After
import org.eclipse.xtext.xbase.compiler.TypeReferenceSerializer
import com.google.inject.Injector
import de.msg.xt.mdt.base.ActivityLocator
import de.msg.xt.mdt.base.ITestProtocol
import de.msg.xt.mdt.base.AbstractActivity
import de.msg.xt.mdt.base.TDslParameterized
import org.junit.runner.RunWith
import de.msg.xt.mdt.base.TestDescriptor
import de.msg.xt.mdt.base.EquivalenceClass
import de.msg.xt.mdt.base.Tag
import de.msg.xt.mdt.base.BaseUseCase
import de.msg.xt.mdt.base.Generator
import de.msg.xt.mdt.base.ActivityAdapter
import de.msg.xt.mdt.base.Parameters
import de.msg.xt.mdt.base.GenerationHelper
import de.msg.xt.mdt.base.util.TDslHelper
import java.util.Iterator
import javax.xml.bind.annotation.XmlElement
import java.util.Stack
import de.msg.xt.mdt.tdsl.tDsl.Predicate
import de.msg.xt.mdt.tdsl.tDsl.Element
import de.msg.xt.mdt.tdsl.tDsl.Predicate

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
	
	@Inject extension TypeReferenceSerializer
	
	@Inject extension NamingExtensions fqn
	@Inject extension MetaModelExtensions
	
	
	@Inject
	XbaseCompiler xbaseCompiler
	
	@Inject
	TypesFactory typesFactory;
	
	def dispatch void infer(PackageDeclaration pack, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		
		for (element : pack.elements.filter([!(it instanceof Predicate)])) {
			element.infer(acceptor, isPreIndexingPhase)
		}
		
		val predicates = pack.elements.filter(typeof(Predicate))
		if (!predicates.empty) { 
			acceptor.accept(pack.toClass(pack.predicateClass_fqn)).initializeLater [
				
				for (predicate : predicates) {
					members += predicate.toMethod(predicate.name, predicate.newTypeRef(typeof(Boolean))) [
						parameters += predicate.toParameter("fieldTags", predicate.newTypeRef(typeof(Tag)).createArrayType)
						parameters += predicate.toParameter("equivClassTags", predicate.newTypeRef(typeof(Tag)).createArrayType)
						
						it.body = [
							it.append('''
								return false;
							''')
						]
					]
				}
			]
		}
	}

	def dispatch void infer(Toolkit toolkit, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		if (toolkit.activityAdapter_FQN != null) {
			acceptor.accept(toolkit.toInterface(toolkit.activityAdapter_FQN, [])).initializeLater [
			
				superTypes += toolkit.newTypeRef(typeof(ActivityAdapter))
			
				for (Control control : toolkit.controls) {
					if (control?.name != null && control?.class_fqn != null) {
						members += control.toMethod(control.toolkitGetter, control.newTypeRef(control.class_fqn)) [
							it.setAbstract(true)
							it.parameters += control.toParameter("controlName", control.newTypeRef(typeof(String)))
						]
					}
				}
			]
		}
	}
	
	
	def activityAdapterParentClass(Activity activity) {
		activity?.parent?.adapterInterface_fqn ?: activity?.toolkit?.activityAdapter_FQN
	}
	
   	def dispatch void infer(Activity activity, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		
   		var JvmGenericType activityAdapterClassVar = null
   		if (activity.adapterInterface_fqn != null) {
	   		activityAdapterClassVar = activity.toInterface(activity.adapterInterface_fqn) []
   			acceptor.accept(activityAdapterClassVar).initializeLater [
   				if (activity.activityAdapterParentClass != null)
   					superTypes += activity.newTypeRef(activity.activityAdapterParentClass)

	   			for (activityMethod : activity.operations) {
	   				if (activityMethod.name != null && activityMethod.body == null) {
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
   			
   			val superClass = activity.superClass_ref
   			if (superClass != null)
   				superTypes += superClass
   			
   			members += activity.toField("ID", activity.newTypeRef(typeof(String))) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer[
   					it.append('''"«activity.identifier»"''')
   				]
   			]
   			   				
   			members += activity.toField("injector", activity.newTypeRef(typeof(Injector))) [
   				it.setFinal(true)
   				it.setInitializer [
   					activity.newTypeRef(fqn.tdslInjector).serialize(activity, it)
   					it.append(".getInjector()")
   				]
   			]
   			
   			val activityLocatorType = activity.newTypeRef(typeof(ActivityLocator))
   			members += activity.toField("activityLocator", activityLocatorType) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					activity.newTypeRef(fqn.tdslInjector).serialize(activity, it)   					
   					it.append(".getInjector().getInstance(")
   					activity.newTypeRef(typeof(ActivityLocator)).serialize(activity, it)
   					it.append(".class)")
   				]
   			]
   			   			
   			members += activity.toField("protocol", activity.newTypeRef(typeof(ITestProtocol))) [
   				it.setFinal(true)
   				it.setInitializer [
   					it.append("this.injector.getInstance(")
   					activity.newTypeRef(typeof(ITestProtocol)).serialize(activity, it)
   					it.append(".class)");
   				]
   			]
   			   				
   			members += activity.toField("contextAdapter", newTypeRef(activityAdapterClass))
   			
   			members += activity.toMethod("find", newTypeRef(activityClass)) [
   				it.setStatic(true)
   				
   				setBody [
   					it.append("return new ")
   					activity.newTypeRef(activity.class_fqn).serialize(activity, it)
   					it.append("(activityLocator.find(ID, ")
   					activity.newTypeRef(activity.adapterInterface_fqn).serialize(activity, it)
   					it.append(".class));")
   				]
   			]
   			
   			members += activity.toConstructor [
   				it.setBody [
   					it.append('''super();''')
   				]
   			]
   			
   			members += activity.toConstructor [
   				parameters += activity.toParameter("contextAdapter", newTypeRef(activityAdapterClass))
   				it.setBody [
   					it.append('''
   					    «IF activity.parent != null»super(contextAdapter)«ELSE»this()«ENDIF»;
   					    this.contextAdapter = contextAdapter;''')
   				]
   			]
   			
			for (field : activity.fields) {
				if (field?.control?.fqn != null) {
					members += field.toMethod(field.fieldGetterName, field.newTypeRef(field.control.fqn)) [
						it.setBody [
							it.append('''
						    	 return this.contextAdapter.«field.control.activityAdapterGetter»("«field.identifier»");''')
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
   		var Boolean voidReturnType = false
   		var String nextActivityClassVar 
   		if (operation.nextActivities.empty) {
   			nextActivityClassVar = operation.activity.class_fqn
   			returnTypeRef = operation.newTypeRef(nextActivityClassVar)
   		} else {
   			val condNext = operation.nextActivities.get(0)
   			val Activity nextActivity = condNext.next
   			nextActivityClassVar = nextActivity?.class_fqn
   			if (nextActivityClassVar != null) {
   				returnTypeRef = operation.newTypeRef(nextActivityClassVar)
   			} else {
   				returnTypeRef = operation.newTypeRef(Void::TYPE)
   				voidReturnType = true
   			}
   		}
   		val voidReturn = voidReturnType
   		val nextActivityClass = nextActivityClassVar
   		operation.toMethod(operation.name, returnTypeRef) [
  			for (param : operation.params) {
				if (param?.dataType?.class_fqn != null) {
   					it.parameters += param.toParameter(param.name, param.newTypeRef(param.dataType.class_fqn))
   				}
   			}
   			
   			it.setBody [
   				it.append('''
   				    this.protocol.appendActivityOperationCall(this.getClass().getName(), "«operation.name»", null«appendActivityParameter(operation.params)»);
				''')
				if (operation.body != null) {
   					operation.newTypeRef(typeof(Stack), operation.newTypeRef(typeof(AbstractActivity))).serialize(operation, it);
   					it.append(" stack = new Stack<AbstractActivity>();").newLine
					operation.newTypeRef(typeof(AbstractActivity)).serialize(operation, it)
					it.append(" activity = this;")
					var expectedReturnType = operation.newTypeRef(Void::TYPE)
					if (!voidReturn) {
						expectedReturnType = operation.newTypeRef(typeof(Object))
					}
					xbaseCompiler.compile(operation.body, it, expectedReturnType)

					if (!voidReturn) {					
						it.newLine.append("return (")
						operation.newTypeRef(nextActivityClass).serialize(operation, it)
						it.append(")activity;")
					}
				} else {
					operation.newTypeRef(typeof(Object)).serialize(operation, it)
					it.append(''' o = contextAdapter.«operation.name»(«FOR param : operation.params SEPARATOR ', '»«param.name».getValue()«ENDFOR»);''').newLine
					if (!voidReturn) {
						operation.newTypeRef(nextActivityClass).serialize(operation, it)
						it.append('''
							 nextActivity = null;
							if (o instanceof ''')
						operation.newTypeRef(nextActivityClass).serialize(operation, it)
						it.append(") {").increaseIndentation.newLine
						it.append("nextActivity = (")
						operation.newTypeRef(nextActivityClass).serialize(operation, it)
						it.append(")o;").decreaseIndentation.newLine
						it.append("} else {").increaseIndentation.newLine
						operation.newTypeRef(nextActivityClass + "Adapter").serialize(operation, it)
						it.append(" adapter = injector.getInstance(")
						operation.newTypeRef(nextActivityClass + "Adapter").serialize(operation, it)
						it.append('''
							.class);
							adapter.setContext(o);
							nextActivity = new ''')
						operation.newTypeRef(nextActivityClass).serialize(operation, it)
						it.append("(adapter);").decreaseIndentation.newLine
						it.append('''
							}
							return nextActivity;''')					
					}
				}
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
						if (dataTypeMapping?.name?.name != null && dataTypeMapping?.datatype?.class_fqn != null) {
							it.parameters += dataTypeMapping.toParameter(dataTypeMapping.name.name, dataTypeMapping.newTypeRef(dataTypeMapping.datatype.class_fqn))
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
   					
   						if (opMapping != null) {
		   					it.append('''
		   						this.protocol.appendControlOperationCall(this.getClass().getName(), "«field.name»", «field.control?.name».class.getName(), "«operation.name»", null«appendParameter(opMapping.dataTypeMappings)»);
		   						«field.fieldGetterName»().«operation.name»(«mapParameters(field, operation)»);
		   						return «IF nextActivity == null»this«ELSE»«nextActivity.class_SimpleName».find()«ENDIF»;''')
   						}
   					} else {
   						if (opMapping != null) {
   							it.append('''
   								«operation.returnType?.name» value = «field.fieldGetterName»().«operation.name»(«mapParameters(field, operation)»); 
   								this.protocol.appendControlOperationCall(this.getClass().getName(), "«field.name»", «field.control?.name».class.getName(), "«operation.name»", value != null ? value.toString() : "null" «appendParameter(opMapping.dataTypeMappings)»);
   								return new «opMapping.dataType?.class_FQN»(value, «opMapping.dataType?.equivalenceClass_name».getByValue(value));''')
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
 		if (field.parentActivity?.class_fqn != null)
 			currentActivityType = field.newTypeRef(field.parentActivity.class_fqn) 
   		if (operation.returnType == null) {
   			if (opMapping == null) {
   					currentActivityType
   			} else {
   				if (opMapping?.nextActivities?.empty || opMapping?.nextActivities?.get(0)?.next?.class_fqn == null) {
   					currentActivityType
   				} else {
   					field.newTypeRef(opMapping.nextActivities.get(0).next.class_fqn)
   				}
   			}   			
   		} else {
   			if (opMapping == null || opMapping.dataType == null) {
   				//System::out.println("For operations with return type a mapping with dataType must be specified! " + field.fullyQualifiedName.toString + ": " + operation.name)
   				//throw new IllegalArgumentException("For operations with return type a mapping with dataType must be specified!")
   			} else {
   				if (opMapping?.dataType?.class_fqn != null) {
   					opMapping.newTypeRef(opMapping?.dataType?.class_fqn)
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
		val value = sourceElement.newTypeRef(typeof(TDslParameterized))
		val JvmType jvmType = references.findDeclaredType(typeof(RunWith), sourceElement);
		if (jvmType == null) {
			throw new IllegalArgumentException("The type org.junit.RunWith is not on the classpath.");
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
   			
   			members += test.toField("INJECTOR", test.newTypeRef(typeof(Injector))) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					test.newTypeRef(fqn.tdslInjector).serialize(test, it)
   					it.append(".createInjector(TEST_CASES_SERIALIZATION)")
   				]
   			]
   			
   			val activityLocator = test.newTypeRef(typeof(ActivityLocator))
   			members += test.toField("LOCATOR", activityLocator) [
   				it.setStatic(true)
   				it.setFinal(true)
   				it.setInitializer [
   					it.append("INJECTOR.getInstance(")
   					activityLocator.serialize(test, it)
   					it.append(".class)")
   				]
   			]
   			
   			val iTestProtocol = test.newTypeRef(typeof(ITestProtocol))
   			members += test.toField("protocol", iTestProtocol) [
   				it.setFinal(true)
   				it.setInitializer [
   					it.append("INJECTOR.getInstance(")
   					iTestProtocol.serialize(test, it)
   					it.append(".class)")
   				]
   			]
   			
   			members += test.toField("testNumber", test.newTypeRef(typeof(int))) [
   			]
   			
   			
   			if (test?.useCase?.class_fqn != null) {
   				members += test.toField("useCase", test.newTypeRef(test.useCase.class_fqn)) [
   					it.setFinal(true)
   				]
   			}
   			
   			members += test.toMethod("config", test.newTypeRef(typeof(Collection), test.newTypeRef(typeof(Object)).createArrayType)) [
   				it.setStatic(true)
   				annotations += test.toAnnotation(typeof(Parameters))
   				it.setBody [
   					test.newTypeRef(typeof(GenerationHelper)).serialize(test, it)
                   	it.append(" testHelper = INJECTOR.getInstance(GenerationHelper.class);").newLine
   					test.newTypeRef(typeof(Generator)).serialize(test, it)
                   	it.append(" generator = INJECTOR.getInstance(Generator.class);").newLine
                   	if (!test.tags.empty) {
                   		it.append("generator.setTags(new ")
                   		test.newTypeRef(typeof(Tag)).createArrayType.serialize(test, it)
                   		it.append(''' {«FOR tag : test.tags SEPARATOR ","»«tag.enumLiteral_FQN»«ENDFOR»});''').newLine
                   	}
                   	if (!test.excludeTags.empty) {
                        it.append("generator.setExcludeTags(new ")
                   		test.newTypeRef(typeof(Tag)).createArrayType.serialize(test, it)
                        it.append(''' {«FOR tag : test.excludeTags SEPARATOR ","»«tag.enumLiteral_FQN»«ENDFOR»});''').newLine
                    }
                    if (test.useCase?.class_fqn != null) {
                    	it.append('''
                        	LOCATOR.beforeTest();
                        	return testHelper.readOrGenerateTestCases(TEST_CASES_SERIALIZATION, generator, ''')
                    	test.newTypeRef(test.useCase.class_fqn).serialize(test, it)
                    	it.append(".class);")                   
                    }
   				]
   			]
   			
   			members += test.toConstructor [
	   			if (test?.useCase?.class_fqn != null) {
	   				val useCaseClass = test.newTypeRef(test.useCase.class_fqn)
	   				if (useCaseClass != null) {
   						parameters += test.toParameter("testDescriptor", test.newTypeRef(typeof(TestDescriptor), useCaseClass))
   					}
   				}
   				body = [
   					it.append('''
   						this.testNumber = testDescriptor.getTestNumber();
   						this.useCase = testDescriptor.getTestCase();
   						INJECTOR.injectMembers(this);''')
   				]
   			]
   			
   			members += test.toMethod("setup", test.newTypeRef(Void::TYPE)) [
   				annotations += test.toAnnotation(typeof(org.junit.Before))
   				
   				body = [
   					it.append("LOCATOR.beforeTest();")
   				]
   			]
   			
   			members += test.toMethod("cleanup", test.newTypeRef(Void::TYPE)) [
   				annotations += test.toAnnotation(typeof(After))
   				
   				body = [
   					it.append("LOCATOR.afterTest();")
   				]
   			]
   			
   			members += test.toMethod("test", test.newTypeRef(Void::TYPE)) [
   				annotations += test.toAnnotation(typeof(org.junit.Test))
   				
   				body = [
   					it.append('''
                        this.protocol.newTest(String.valueOf(this.testNumber));
                        try {
                        	this.useCase.run();
                        	this.protocol.append("Test OK");
                        } catch (java.lang.RuntimeException ex) {
                        	throw ex;
                        } finally {
                        	this.protocol.appendSummary();
                        }''')
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
   						references.getTypeForName(Void::TYPE, operation)
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
   			superTypes += dataType.newTypeRef(typeof(EquivalenceClass))
   			
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
        					switch (this) {''')
        				it.increaseIndentation
        				
        				for (clazz : dataType.classes) {
        					it.newLine
    	    				it.append('''
	        					case «clazz.name»:''').increaseIndentation.newLine
        					if (clazz.value != null) {
        						it.append("value = ")	
        						val expectedType = dataType.type.mappedBy
        						xbaseCompiler.compileAsJavaExpression(clazz.value, it, expectedType)
        					} else if (clazz.values != null) {
        						dataType.newTypeRef(typeof(Iterable), dataType.type.mappedBy).serialize(dataType, it)
        						it.append(''' «clazz.name.toFirstLower»Iterable = ''')
        						val expectedType = dataType.newTypeRef(typeof(Iterable), dataType.type.mappedBy)
        						xbaseCompiler.compileAsJavaExpression(clazz.values, it, expectedType)
        						it.append(";").newLine
        						it.append("value = ")
        						dataType.newTypeRef(typeof(TDslHelper)).serialize(dataType, it)
        						it.append('''.selectRandom(«clazz.name.toFirstLower»Iterable.iterator());''')
        					} else if (clazz.valueGenerator != null) {
        						it.append("value = ")	
        						val expectedType = dataType.type.mappedBy
        						xbaseCompiler.compileAsJavaExpression(clazz.valueGenerator, it, expectedType)        						
        					}
        					
        					it.append('''
        						;
        						break;''').decreaseIndentation
	        			}
            			
            			it.decreaseIndentation.newLine.append('''
        					}
        					return value;''')
					]	
	   			]
   			}
   			
   			val tagArrayRef = dataType.newTypeRef(typeof(Tag)).createArrayType
   			members += dataType.toMethod("getTags", tagArrayRef) [
   				setBody [
   					tagArrayRef.serialize(dataType, it)
					it.append("tags = null;").newLine
					it.append('''
						switch (this) {
							«FOR clazz : dataType.classes»
								case «clazz.name»:
									tags = new Tag[] { «FOR tag : clazz.tags SEPARATOR ', '»«tag.enumLiteral_FQN»«ENDFOR» };
									break;
							«ENDFOR»
						}
						return tags;''')
				]	
   			]
   			
   			members += dataType.toMethod("getByValue", dataType.newTypeRef(it.fullyQualifiedName.toString)) [
   				it.setStatic(true)
   				if (dataType.type?.mappedBy != null) {
	   				it.parameters += it.toParameter("value", dataType.type.mappedBy)
   					it.setBody [
   						it.append('''
					        «dataType.equivalenceClass_name» clazz = null;
					        if (value != null) {''').increaseIndentation.newLine
					    for (clazz : dataType.classes) {
					    	if (clazz.value != null) {
					    		it.append("if (value.equals(")
						    	xbaseCompiler.compileAsJavaExpression(clazz.value, it, dataType.type.mappedBy)
					    	    it.append('''
					        		)) {
					        			return «clazz.name»;
					        		}
					        	''')
					        } else if (clazz.values != null) {
        						val expectedType = dataType.newTypeRef(typeof(Iterable), dataType.type.mappedBy)
					        	expectedType.serialize(dataType, it)
        						it.append(''' «clazz.name.toFirstLower»Iterable = ''')
        						xbaseCompiler.compileAsJavaExpression(clazz.values, it, expectedType)
        						it.append(";").newLine
        						dataType.newTypeRef(typeof(Iterator), dataType.type.mappedBy).serialize(dataType, it)
        						it.append(''' «clazz.name.toFirstLower»Iterator = «clazz.name.toFirstLower»Iterable.iterator();''')
        						it.append('''
        							while(«clazz.name.toFirstLower»Iterator.hasNext()) {
        								if (value.equals(«clazz.name.toFirstLower»Iterator.next())) {
        									return «clazz.name»;
        								}
        							}''')        						
					        } else if (clazz.valueGenerator != null) {
					        	it.append("if (")
					        	val expectedType = dataType.newTypeRef(typeof(Functions$Function1), dataType.type.mappedBy, dataType.newTypeRef(typeof(Boolean)))
        						xbaseCompiler.compileAsJavaExpression(clazz.classPredicate, it, expectedType)
					        	it.append('''
					        		.apply(value)) {
					        			return «clazz.name»;
					        		}''')
					        }
					    }
					    it.decreaseIndentation.newLine.append('''
					    	}
					    	return null;''')
   					]   				
   				}
   			]
   		]

   		
   		acceptor.accept(dataType.toClass(dataType.fullyQualifiedName)).initializeLater([
   			superTypes += dataType.newTypeRef(typeof(de.msg.xt.mdt.base.DataType), dataType.type.mappedBy, newTypeRef(equivalenceClass))
   			
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
				
				it.body = [
					it.append('''
						this._value = value;
						this._equivalenceClass = equivalenceClass;''')
				]
			]
			
			members += dataType.toMethod("getValue", dataType.type?.mappedBy) [
				it.body = [
					it.append("return this._value;")
				]
			]
			members += dataType.toMethod("setValue", dataType.newTypeRef(Void::TYPE)) [
				it.parameters += dataType.toParameter("value", dataType.type?.mappedBy)
				it.body = [
					it.append('''
						this._value = value;''')
				]
			]

			members += dataType.toMethod("getEquivalenceClass", newTypeRef(equivalenceClass)) [
				it.body = [
					it.append('''
						return this._equivalenceClass;''')
				]
			]
			members += dataType.toMethod("setEquivalenceClass", dataType.newTypeRef(Void::TYPE)) [
				it.parameters += dataType.toParameter("equivalenceClass", newTypeRef(equivalenceClass))
				it.body = [
					it.append('''
						this._equivalenceClass = equivalenceClass;''')
				]
			]
			
			members += dataType.toMethod("getTags", dataType.newTypeRef(typeof(Tag)).createArrayType) [
				it.body = [
					it.append('''
						return getEquivalenceClass().getTags();''')
				]
			]
			
			members += dataType.toMethod("getEquivalenceClassEnum", dataType.newTypeRef(typeof(Class), newTypeRef(equivalenceClass))) [
				it.body = [
					it.append('''
						return «dataType.equivalenceClass_name».class;''')
				]
			]
   		])
   	}
   	
   	
   	def dispatch void infer(Predicate predicate, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		if (predicate?.name != null) {
   			
   		}
   	}
   	
   	def dispatch void infer(UseCase useCase, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   		if (useCase.class_FQN == null)
   			return
   		val useCaseClass = useCase.toClass(useCase.class_FQN)
   		acceptor.accept(useCaseClass).initializeLater [
   			it.superTypes += useCase.newTypeRef(typeof(BaseUseCase))
   			it.superTypes += useCase.newTypeRef(typeof(Runnable))

   			it.annotations += useCase.toAnnotation(typeof(XmlRootElement))
   			   			
   			for (inputParam : useCase.inputParameter) {
   				if (inputParam.name != null && inputParam?.dataType?.class_fqn != null) {
   					members += inputParam.toField(inputParam.name, inputParam.newTypeRef(inputParam.dataType.class_fqn)) [
   					it.annotations += inputParam.toAnnotation(typeof(XmlElement))
   					]
   				}
   			}
   			
   			it.members += useCase.toConstructor() []
   			it.members += useCase.toConstructor() [
   				it.parameters += useCase.toParameter("generator", useCase.newTypeRef(typeof(Generator)))
   				body = [
   					it.append('''
   					    this();
   					    this.generator = generator;''')
   					for (inputParam : useCase.inputParameter) {
   						newLine
   						if (inputParam?.dataType?.class_fqn != null) {
	   						it.append('''
   								this.«inputParam.name» = this.getOrGenerateValue(«inputParam.dataType.class_fqn».class, "«inputParam.fullyQualifiedName.toString»");''')
   						}
	   				}
   				]
   			]
   			
   			
   			if (!useCase.inputParameter.empty) {
   				it.members += useCase.toConstructor() [
   					for (inputParam : useCase.inputParameter) {
   						if (inputParam?.dataType?.class_fqn != null) {
   							it.parameters += useCase.toParameter(inputParam.name, useCase.newTypeRef(inputParam.dataType.class_fqn))
   						}	
   					}
	   				body = [
	   					it.append("this();")
		   				for (inputParam : useCase.inputParameter) {
		   					newLine
   							it.append('''
   								this.«inputParam.name» = «inputParam.name»;''')
	   					}
   					]
   				]
   			}
   			
   			for (inputParam : useCase.inputParameter) {
   				if (inputParam?.dataType?.class_fqn != null) {
   					members += inputParam.toSetter(inputParam.name, inputParam.newTypeRef(inputParam.dataType.class_fqn))	
   				} 
   			}
   			
   			it.members += useCase.toMethod("run", useCase.newTypeRef(Void::TYPE)) [
   				it.annotations += useCase.toAnnotation(typeof(Override))
   				
   				body = [
   					it.append('''
   						execute(«useCase.initialActivity?.class_SimpleName».find());''')
   				]
   			]
   			
   			val returnType = useCase.nextActivity?.next?.class_fqn
   			it.members += useCase.toMethod("execute", if (returnType != null) useCase.newTypeRef(returnType) else useCase.newTypeRef(Void::TYPE)) [
   				if (useCase.initialActivity?.class_FQN != null) {
   					it.parameters += useCase.toParameter("initialActivity", useCase.newTypeRef(useCase.initialActivity.class_fqn))
 	  			}
 	
   				body = [
   					useCase.newTypeRef(typeof(Stack), useCase.newTypeRef(typeof(AbstractActivity))).serialize(useCase, it);
   					it.append(" stack = new Stack<AbstractActivity>();").newLine
   					useCase.newTypeRef(typeof(AbstractActivity)).serialize(useCase, it)
   					it.append(" activity = initialActivity;")
   					for (statement : useCase.block.expressions) {
   						xbaseCompiler.compile(statement, it, statement.newTypeRef(Void::TYPE), null)
   					}
   					if (returnType != null) {
   						it.newLine
   						it.append('''return («returnType»)activity;''')
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
			app.append('''
				«currentActivity?.localVariable_name(activityIndex)».«field.activityControlDelegationMethodName(opCall.operation.name)»();
			''')
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
   		if (tags?.enumClass_FQN != null) {
   			acceptor.accept(tags.toEnumerationType(tags.enumClass_FQN) [
   				superTypes += tags.newTypeRef(typeof(Tag))
   				for (tag : tags.tags) {
   					if (tag.name != null) {
   						val enumLit = toEnumerationLiteral(tag.enumLiteral_SimpleName)
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

