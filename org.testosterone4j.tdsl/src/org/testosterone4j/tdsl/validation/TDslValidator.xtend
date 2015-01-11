/*
 * #%L
 * org.testosterone4j.tdsl
 * %%
 * Copyright (C) 2015 Axel Ruder
 * %%
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * #L%
 */
package org.testosterone4j.tdsl.validation

import org.testosterone4j.tdsl.jvmmodel.MetaModelExtensions
import org.testosterone4j.tdsl.jvmmodel.NamingExtensions
import org.testosterone4j.tdsl.tDsl.Activity
import org.testosterone4j.tdsl.tDsl.ActivityOperationParameterAssignment
import org.testosterone4j.tdsl.tDsl.Field
import org.testosterone4j.tdsl.tDsl.OperationParameterAssignment
import org.testosterone4j.tdsl.tDsl.ParameterAssignment
import org.testosterone4j.tdsl.tDsl.TDslPackage
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.typing.ITypeProvider
import org.testosterone4j.tdsl.tDsl.ActivityExpectation

class TDslValidator extends AbstractTDslValidator {

	public static val UNSUFFICIENT_OPERATION_MAPPINGS = "xt.mdt.unsufficientOperationMappings"
	public static val CONTROL_NOT_IN_TOOLKIT = "xt.mdt.controlNotInToolkit"

	@Inject extension MetaModelExtensions
	@Inject extension NamingExtensions

	@Inject extension ITypeProvider
	@Inject extension JvmTypesBuilder


	@Check
	def checkFirstUpperCaseActivities(Activity act) {
		if (act.name != null && !act.name.toFirstUpper.equals(act.name)) {
			error("Activity names must start with an uppercase letter.", TDslPackage.Literals::ACTIVITY__NAME)
		}
	}

	@Check
	def checkOperationMapping(Field field) {
		val operationMappings = field.getOperations();
		val operations = field.getControl().getOperations();
		if (operationMappings.size != field.control.operations.size()) {
			val missingMappings = <String>newArrayList
			for (op : operations) {
				var found = false;
				for (opMapping : operationMappings) {
					if (opMapping.getName().equals(op)) {
						found = true;
					}
				}
				if (!found) {
					missingMappings.add(op.name)
				}
			}
			if (!missingMappings.empty) {
				error(
					'''An operation mapping must be defined for operations «FOR m : missingMappings SEPARATOR ", "»'«m»'«ENDFOR».''',
					TDslPackage.Literals::FIELD__OPERATIONS, UNSUFFICIENT_OPERATION_MAPPINGS);

			}
		}
	}

	@Check
	def checkControlsInToolkit(Field field) {
		if (field?.parentActivity?.toolkit != null && !field.parentActivity.toolkit.controls.contains(field.control) &&
			field.control?.name != null) {
			error("The control '" + field.control.name + "' is not included in the current toolkit!",
				TDslPackage.Literals::FIELD__CONTROL, CONTROL_NOT_IN_TOOLKIT, field.control.name)
		}
	}

	@Check
	def checkTypeComplianceOperationParameter(OperationParameterAssignment assignment) {
		if (assignment?.name?.datatype?.class_FQN?.toString != null &&
			assignment.name.datatype.type?.mappedBy?.type != null) {
			val JvmType expectedDataType = assignment.newTypeRef(assignment.name.datatype.class_FQN.toString).type
			val JvmType expectedType = assignment.name.datatype.type.mappedBy.type
			val assignmentValueType = assignment?.value.type
			val JvmType exprType = assignment?.value.type.type
			if (!exprType.equals(expectedDataType) && !exprType.equals(expectedType) &&
				!expectedType.isCompatible(exprType)) {
				error(
					"Value of parameter " + assignment.name.name.name + " must be either " + expectedDataType.simpleName +
						" or " + expectedType.simpleName + ", but was " + exprType.simpleName + "!",
					TDslPackage.Literals::OPERATION_PARAMETER_ASSIGNMENT__VALUE);
			}
		}
	}

	@Check
	def checkTypeComplianceActivityOperationParameter(ActivityOperationParameterAssignment assignment) {
		if (assignment?.name?.dataType?.class_FQN?.toString != null &&
			assignment.name.dataType.type?.mappedBy?.type != null) {
			val JvmType expectedDataType = assignment.newTypeRef(assignment.name.dataType.class_FQN.toString).type
			val JvmType expectedType = assignment.name.dataType.type.mappedBy.type
			val JvmType exprType = assignment?.value?.type?.type
			if (exprType != null && !exprType.equals(expectedDataType) && !exprType.equals(expectedType) &&
				!expectedType.isCompatible(exprType)) {
				error(
					"Value of parameter " + assignment.name.name + " must be either " + expectedDataType.simpleName +
						" or " + expectedType.simpleName + ", but was " + exprType.simpleName + "!",
					TDslPackage.Literals::ACTIVITY_OPERATION_PARAMETER_ASSIGNMENT__VALUE);
			}
		}
	}

	@Check
	def checkTypeComplianceSubUseCaseCallParameter(ParameterAssignment assignment) {
		if (assignment?.name?.dataType?.class_FQN?.toString != null &&
			assignment.name.dataType.type?.mappedBy?.type != null) {
			val JvmType expectedDataType = assignment.newTypeRef(assignment.name.dataType.class_FQN.toString).type
			val JvmType expectedType = assignment.name.dataType.type.mappedBy.type
			val JvmType exprType = assignment?.value?.type?.type
			if (exprType != null && !exprType.equals(expectedDataType) && !exprType.equals(expectedType) &&
				!expectedType.isCompatible(exprType)) {
				error(
					"Value of parameter " + assignment.name.name + " must be either " + expectedDataType.simpleName +
						" or " + expectedType.simpleName + ", but was " + exprType.simpleName + "!",
					TDslPackage.Literals::PARAMETER_ASSIGNMENT__VALUE);
			}
		}
	}

	def boolean isCompatible(JvmType expectedType, JvmType actualType) {
		if (expectedType.simpleName.equals("Integer") && actualType.simpleName.equals("int"))
			return true
		if (expectedType.simpleName.equals("Double") && actualType.simpleName.equals("double"))
			return true
		if (expectedType.simpleName.equals("Long") && actualType.simpleName.equals("long"))
			return true
		if (expectedType.simpleName.equals("Float") && actualType.simpleName.equals("float"))
			return true
		return false
	}
	
	override protected isValueExpectedRecursive(XExpression expr) {
		val feature = expr.eContainingFeature()
		val container = expr.eContainer()
		
		if (container instanceof ActivityExpectation && feature == TDslPackage.Literals.ACTIVITY_EXPECTATION__GUARD)
			true
		else if (container instanceof OperationParameterAssignment && feature == TDslPackage.Literals.OPERATION_PARAMETER_ASSIGNMENT__VALUE)
			true
		else if (container instanceof ActivityOperationParameterAssignment && feature == TDslPackage.Literals.ACTIVITY_OPERATION_PARAMETER_ASSIGNMENT__VALUE)
			true
		else if (container instanceof ParameterAssignment && feature == TDslPackage.Literals.PARAMETER_ASSIGNMENT__VALUE)
			true
		else 
			super.isValueExpectedRecursive(expr)
	}

}
