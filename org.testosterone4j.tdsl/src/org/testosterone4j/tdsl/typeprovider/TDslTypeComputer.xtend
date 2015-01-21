package org.testosterone4j.tdsl.typeprovider

import com.google.inject.Singleton
import org.testosterone4j.base.AbstractActivity
import org.testosterone4j.tdsl.jvmmodel.MetaModelExtensions
import org.testosterone4j.tdsl.jvmmodel.NamingExtensions
import org.testosterone4j.tdsl.jvmmodel.UtilExtensions
import org.testosterone4j.tdsl.tDsl.ActivityExpectation
import org.testosterone4j.tdsl.tDsl.ActivityOperationBlock
import org.testosterone4j.tdsl.tDsl.ActivityOperationCall
import org.testosterone4j.tdsl.tDsl.ActivityOperationParameter
import org.testosterone4j.tdsl.tDsl.ActivityOperationParameterAssignment
import org.testosterone4j.tdsl.tDsl.Assert
import org.testosterone4j.tdsl.tDsl.DataTypeMapping
import org.testosterone4j.tdsl.tDsl.GeneratedValueExpression
import org.testosterone4j.tdsl.tDsl.GenerationSelektor
import org.testosterone4j.tdsl.tDsl.InnerBlock
import org.testosterone4j.tdsl.tDsl.OperationCall
import org.testosterone4j.tdsl.tDsl.OperationParameterAssignment
import org.testosterone4j.tdsl.tDsl.Parameter
import org.testosterone4j.tdsl.tDsl.ParameterAssignment
import org.testosterone4j.tdsl.tDsl.StatementLine
import org.testosterone4j.tdsl.tDsl.SubUseCaseCall
import org.testosterone4j.tdsl.tDsl.UseCase
import org.testosterone4j.tdsl.tDsl.UseCaseBlock
import javax.inject.Inject
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.annotations.typesystem.XbaseWithAnnotationsTypeComputer
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.xbase.typesystem.computation.ITypeComputationState
import org.eclipse.xtext.xbase.typesystem.conformance.ConformanceHint
import org.testosterone4j.tdsl.tDsl.Selector
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReferenceFactory
import org.eclipse.xtext.xbase.typesystem.legacy.StandardTypeReferenceOwner
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices

@Singleton
class TDslTypeComputer extends XbaseWithAnnotationsTypeComputer {

	@Inject TypeReferences typeReferences

	@Inject
	private CommonTypeComputationServices services;

	@Inject extension NamingExtensions
	@Inject extension MetaModelExtensions
	@Inject extension UtilExtensions

	@Inject extension IJvmModelAssociations

	override computeTypes(XExpression expression, ITypeComputationState state) {
		if (expression instanceof Assert) {
			_computeTypes(expression as Assert, state);
		} else if (expression instanceof OperationCall) {
			_computeTypes(expression as OperationCall, state);
		} else if (expression instanceof StatementLine) {
			_computeTypes(expression as StatementLine, state);
		} else if (expression instanceof GeneratedValueExpression) {
			_computeTypes(expression as GeneratedValueExpression, state);
		} else if (expression instanceof ActivityOperationCall) {
			_computeTypes(expression as ActivityOperationCall, state);
		} else if (expression instanceof SubUseCaseCall) {
			_computeTypes(expression as SubUseCaseCall, state);
		} else if (expression instanceof GenerationSelektor) {
			_computeTypes(expression as GenerationSelektor, state);
		} else if (expression instanceof ActivityOperationBlock) {
			_computeTypes(expression as ActivityOperationBlock, state);
		} else if (expression instanceof UseCaseBlock) {
			_computeTypes(expression as UseCaseBlock, state);
		} else if (expression instanceof InnerBlock) {
			_computeTypes(expression as InnerBlock, state);
		} else if (expression instanceof ActivityExpectation) {
			_computeTypes(expression as ActivityExpectation, state);
		} else if (expression instanceof Selector) {
			_computeTypes(expression as Selector, state);
		} else {
			if(expression != null) super.computeTypes(expression, state)
		}
	}

	protected def _computeTypes(Assert assert, ITypeComputationState state) {
		if (assert?.expression == null)
			state.acceptActualType(getTypeForName(Void::TYPE, state))
		else
			computeTypes(assert.expression, state)
	}

	protected def _computeTypes(OperationCall opCall, ITypeComputationState state) {
		val typeFactory = new LightweightTypeReferenceFactory(new StandardTypeReferenceOwner(services, opCall))
		
		val typeName = opCall.operation?.dataType?.class_FQN?.toString
		for (expr : opCall.paramAssignment.map[(it as OperationParameterAssignment).value]) {
			state.withNonVoidExpectation.computeTypes(expr)
		}

		if (typeName != null) {
			state.acceptActualType(typeFactory.toLightweightReference(typeReferences.getTypeForName(typeName, opCall)))
		} else
			state.acceptActualType(getTypeForName(Void::TYPE, state))
	}

	protected def _computeTypes(StatementLine expr, ITypeComputationState state) {
		if (expr?.statement == null)
			state.acceptActualType(getTypeForName(Void::TYPE, state))
		computeTypes(expr.statement, state)
	}

	protected def _computeTypes(UseCaseBlock block, ITypeComputationState state) {
		val typeFactory = new LightweightTypeReferenceFactory(new StandardTypeReferenceOwner(services, block))
		
		var String nextActivityClass = (block.eContainer as UseCase).returnedActivity?.class_fqn
		val returnType = if (nextActivityClass != null)
				typeReferences.getTypeForName(nextActivityClass, block)
			else
				typeReferences.getTypeForName(Void::TYPE, block)

		computeBlockExpressionTypes(state, block)

		state.acceptActualType(typeFactory.toLightweightReference(returnType))
	}

	protected def _computeTypes(InnerBlock block, ITypeComputationState state) {
		val returnType = getTypeForName(Void::TYPE, state)

		computeBlockExpressionTypes(state, block)

		state.acceptActualType(returnType)
	}

	protected def _computeTypes(ActivityExpectation actExp, ITypeComputationState state) {
		val returnType = getTypeForName(Void::TYPE, state)

		state.withNonVoidExpectation.computeTypes(actExp.guard)

		//state.withRootExpectation(state.converter.toLightweightReference(typeReferences.getTypeForName(Boolean, actExp))))
		computeBlockExpressionTypes(state, actExp.block)

		state.acceptActualType(returnType)
	}

	protected def _computeTypes(ActivityOperationBlock block, ITypeComputationState state) {
		val expectedType = state.expectations.filter[expectedType != null].head.expectedType
		var returnType = getTypeForName(AbstractActivity, state)

		//		var nextActivityClass = block.activityOperation.returnedActivity?.class_fqn
		//		val returnType = if (nextActivityClass != null)
		//				typeReferences.getTypeForName(nextActivityClass, block)
		//			else
		//				typeReferences.getTypeForName(Void::TYPE, block)
		computeBlockExpressionTypes(state, block)

		if (expectedType != null)
			state.acceptActualType(expectedType)
		state.acceptActualType(returnType)
	}

	protected def computeBlockExpressionTypes(ITypeComputationState state, XBlockExpression block) {
		if (block != null) {
			val expressions = block.getExpressions();
			if (!expressions.isEmpty()) {
				for (XExpression expression : expressions) {
					val expressionState = state.withoutExpectation();
					if (expression instanceof StatementLine) {
						val stmtLine = expression as StatementLine
						expressionState.computeTypes(stmtLine.statement);
						if (stmtLine.statement instanceof XVariableDeclaration) {
							addLocalToCurrentScope(stmtLine.statement as XVariableDeclaration, state)
						}
					}
				}
			}
		}
	}

	protected def _computeTypes(GeneratedValueExpression expr, ITypeComputationState state) {
		val typeFactory = new LightweightTypeReferenceFactory(new StandardTypeReferenceOwner(services, expr))
		val param = expr?.param
		val dataTypeName = switch param {
			Parameter:
				param?.dataType?.class_FQN?.toString
			DataTypeMapping:
				param?.datatype?.class_FQN?.toString
			ActivityOperationParameter:
				param?.dataType?.class_FQN?.toString
		}
		var JvmTypeReference type
		if (dataTypeName == null)
			type = typeReferences.getTypeForName(Void::TYPE, expr)
		else
			type = typeReferences.getTypeForName(dataTypeName, expr)

		state.acceptActualType(typeFactory.toLightweightReference(type))
	}

	protected def _computeTypes(ActivityOperationCall opCall, ITypeComputationState state) {

		for (expectation : state.expectations) {
			val expectedType = expectation.expectedType
			for (expr : opCall.paramAssignment.map[(it as ActivityOperationParameterAssignment).value]) {
				state.withNonVoidExpectation.computeTypes(expr)
			}
			if (expectedType != null)
				expectation.acceptActualType(expectedType, ConformanceHint.CHECKED, ConformanceHint.SUCCESS);
		}

		val typeName = opCall.operation?.returnType?.class_FQN?.toString

		var JvmTypeReference type
		if (typeName == null)
			type = typeReferences.getTypeForName(Void::TYPE, opCall)
		else
			type = typeReferences.getTypeForName(typeName, opCall)

		val typeFactory = new LightweightTypeReferenceFactory(new StandardTypeReferenceOwner(services, opCall))
		state.acceptActualType(typeFactory.toLightweightReference(type), ConformanceHint.SUCCESS)
	}

	protected def _computeTypes(SubUseCaseCall subUseCaseCall, ITypeComputationState state) {
		val typeFactory = new LightweightTypeReferenceFactory(new StandardTypeReferenceOwner(services, subUseCaseCall))
		for (paramAssignment : subUseCaseCall.paramAssignment) {
			val ITypeComputationState subState = state.withNonVoidExpectation
			subState.withinScope(subUseCaseCall)
			subState.computeTypes((paramAssignment as ParameterAssignment).value)
			subState.acceptActualType(
				typeFactory.toLightweightReference(typeReferences.getTypeForName(Void::TYPE, paramAssignment)))
		}

		state.acceptActualType(
			typeFactory.toLightweightReference(typeReferences.getTypeForName(Void::TYPE, subUseCaseCall)))
	}

	protected def _computeTypes(GenerationSelektor generationSelektor, ITypeComputationState state) {
		val typeFactory = new LightweightTypeReferenceFactory(new StandardTypeReferenceOwner(services, generationSelektor))
		
		computeTypes(generationSelektor.expression, state.withNonVoidExpectation)

		val container = generationSelektor.eContainer
		var JvmTypeReference type
		switch container {
			OperationParameterAssignment: {
				val dataType = container?.name?.datatype?.class_FQN?.toString
				if (dataType != null)
					type = typeReferences.getTypeForName(dataType, generationSelektor)
			}
			ActivityOperationParameterAssignment: {
				val dataType = container?.name?.dataType?.class_FQN?.toString
				if (dataType != null)
					type = typeReferences.getTypeForName(dataType, generationSelektor)
			}
		}
		state.acceptActualType(typeFactory.toLightweightReference(type))
	}

	protected def _computeTypes(Selector selector, ITypeComputationState state) {
		val typeFactory = new LightweightTypeReferenceFactory(new StandardTypeReferenceOwner(services, selector))
		
		computeTypes(selector.expression, state.withNonVoidExpectation)

		var JvmTypeReference type
		val dataType = selector.dataType?.class_FQN?.toString
		if (dataType != null)
			type = typeReferences.getTypeForName(dataType, selector)
		state.acceptActualType(typeFactory.toLightweightReference(type))
	}
}
