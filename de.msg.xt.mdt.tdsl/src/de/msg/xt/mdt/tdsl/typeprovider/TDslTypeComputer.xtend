package de.msg.xt.mdt.tdsl.typeprovider

import com.google.inject.Singleton
import de.msg.xt.mdt.tdsl.jvmmodel.MetaModelExtensions
import de.msg.xt.mdt.tdsl.jvmmodel.NamingExtensions
import de.msg.xt.mdt.tdsl.jvmmodel.UtilExtensions
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationBlock
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationCall
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameter
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.Assert
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping
import de.msg.xt.mdt.tdsl.tDsl.GeneratedValueExpression
import de.msg.xt.mdt.tdsl.tDsl.GenerationSelektor
import de.msg.xt.mdt.tdsl.tDsl.OperationCall
import de.msg.xt.mdt.tdsl.tDsl.OperationParameterAssignment
import de.msg.xt.mdt.tdsl.tDsl.Parameter
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.SubUseCaseCall
import javax.inject.Inject
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.annotations.typesystem.XbaseWithAnnotationsTypeComputer
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.xbase.typesystem.computation.ITypeComputationState
import org.eclipse.xtext.xbase.typesystem.computation.ITypeExpectation
import org.eclipse.xtext.xbase.typesystem.conformance.ConformanceHint
import org.eclipse.xtext.xbase.typesystem.references.AnyTypeReference
import org.eclipse.xtext.xbase.XBlockExpression
import de.msg.xt.mdt.tdsl.tDsl.UseCaseBlock
import de.msg.xt.mdt.tdsl.tDsl.UseCase

@Singleton
class TDslTypeComputer extends XbaseWithAnnotationsTypeComputer {
	
	@Inject TypeReferences typeReferences
	
	@Inject extension NamingExtensions
	@Inject extension MetaModelExtensions
	@Inject extension UtilExtensions
	
	@Inject extension IJvmModelAssociations
	

	override computeTypes(XExpression expression, ITypeComputationState state) {
		if(expression instanceof Assert) {
			_computeTypes(expression as Assert, state);
		} else if(expression instanceof OperationCall) {
			_computeTypes(expression as OperationCall, state);
		} else if(expression instanceof StatementLine) {
			_computeTypes(expression as StatementLine, state);
		} else if(expression instanceof GeneratedValueExpression) {
			_computeTypes(expression as GeneratedValueExpression, state);
		} else if(expression instanceof ActivityOperationCall) {
			_computeTypes(expression as ActivityOperationCall, state);
		} else if(expression instanceof SubUseCaseCall) {
			_computeTypes(expression as SubUseCaseCall, state);
		} else if(expression instanceof GenerationSelektor) {
			_computeTypes(expression as GenerationSelektor, state);
		} else if(expression instanceof ActivityOperationBlock) {
			_computeTypes(expression as ActivityOperationBlock, state);
		} else if(expression instanceof UseCaseBlock) {
			_computeTypes(expression as UseCaseBlock, state);
		} else {
			super.computeTypes(expression, state)
		}
	}

	protected def _computeTypes(Assert assert, ITypeComputationState state) {
		if (assert?.expression == null)
			state.acceptActualType(getTypeForName(Void::TYPE, state))
		else 
			computeTypes(assert?.expression, state)
	}
	
	protected def _computeTypes(OperationCall opCall, 
                    ITypeComputationState state) {
		for(expr: opCall.paramAssignment.map [it.value]) {
			computeTypes(expr, state)
		}                    	
                    	
   		val typeName = opCall.operation?.dataType?.class_FQN?.toString
   		state.acceptActualType(state.converter.toLightweightReference(typeReferences.getTypeForName(typeName, opCall)))   		
  	}
  	
	protected def _computeTypes(StatementLine expr, 
                    ITypeComputationState state) {
		if (expr?.statement == null)
			state.acceptActualType(getTypeForName(Void::TYPE, state))
  		computeTypes(expr.statement, state)
  	}
  	
  	protected def _computeTypes(UseCaseBlock block, ITypeComputationState state) {
  		var nextActivityClass = (block.eContainer as UseCase).returnedActivity?.class_fqn
  		val returnType = if (nextActivityClass != null) typeReferences.getTypeForName(nextActivityClass, block) else typeReferences.getTypeForName(Void::TYPE, block)

		computeBlockExpressionTypes(state, block)

  		val assignedType = state.converter.toLightweightReference(returnType)
  		state.acceptActualType(assignedType)
  	}
  	
  	protected def _computeTypes(ActivityOperationBlock block, ITypeComputationState state) {
  		var nextActivityClass = block.activityOperation.returnedActivity?.class_fqn
  		val returnType = if (nextActivityClass != null) typeReferences.getTypeForName(nextActivityClass, block) else typeReferences.getTypeForName(Void::TYPE, block)

		computeBlockExpressionTypes(state, block)

  		val assignedType = state.converter.toLightweightReference(returnType)
  		state.acceptActualType(assignedType)
  	}
			
			protected def computeBlockExpressionTypes(ITypeComputationState state, XBlockExpression block) {
				for (ITypeExpectation expectation: state.getExpectations()) {
					val expectedType = expectation.getExpectedType();
					val expressions = block.getExpressions();
					if (!expressions.isEmpty()) {
						for(XExpression expression: expressions) {
							val expressionState = state.withoutExpectation();
							expressionState.computeTypes(expression);
							if (expression instanceof XVariableDeclaration) {
								addLocalToCurrentScope(expression as XVariableDeclaration, state);
							} else if (expression instanceof StatementLine) {
								val stmtLine = expression as StatementLine
								if (stmtLine.statement instanceof XVariableDeclaration) {
									addLocalToCurrentScope(stmtLine.statement as XVariableDeclaration, state)
								}
							}
						}
					}
					expectation.acceptActualType(expectedType, ConformanceHint.CHECKED, ConformanceHint.SUCCESS);
				}
			}
  	
	protected def _computeTypes(GeneratedValueExpression expr, 
                    ITypeComputationState state) {
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
  			
  		state.acceptActualType(state.converter.toLightweightReference(type))
  	}
  	
	protected def _computeTypes(ActivityOperationCall opCall, 
                    ITypeComputationState state) {
                    	
		for(expr: opCall.paramAssignment.map [it.value]) {
			computeTypes(expr, state)
		}                    	
                    	
   		val typeName = opCall.operation?.returnType?.class_FQN?.toString
   		
   		var JvmTypeReference type
   		if (typeName == null)
   			type = typeReferences.getTypeForName(Void::TYPE, opCall)
   		else 
   			type = typeReferences.getTypeForName(typeName, opCall)
   		
   		state.acceptActualType(state.converter.toLightweightReference(type))   		
  	}
  	
	protected def _computeTypes(SubUseCaseCall subUseCaseCall, 
                    ITypeComputationState state) {
		for (expr: subUseCaseCall.paramAssignment.map [it.value] ) {
			computeTypes(expr, state)
		}                    	
                    	
  		state.acceptActualType(state.converter.toLightweightReference(typeReferences.getTypeForName(Void::TYPE, subUseCaseCall)))
  	}
  	
	protected def _computeTypes(GenerationSelektor generationSelektor, 
                    ITypeComputationState state) {
  		val container = generationSelektor.eContainer
  		var JvmTypeReference type
  		switch container {
  			OperationParameterAssignment : 
  				type = typeReferences.getTypeForName(container?.name?.datatype?.class_FQN?.toString, generationSelektor)
  			ActivityOperationParameterAssignment :
  				type = typeReferences.getTypeForName(container?.name?.dataType?.class_FQN?.toString, generationSelektor)	
  		}
  		state.acceptActualType(state.converter.toLightweightReference(type))
  	}
}