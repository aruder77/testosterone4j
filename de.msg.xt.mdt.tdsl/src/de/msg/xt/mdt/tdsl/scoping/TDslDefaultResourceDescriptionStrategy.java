package de.msg.xt.mdt.tdsl.scoping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.util.IAcceptor;

import de.msg.xt.mdt.tdsl.tDsl.Activity;
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation;
import de.msg.xt.mdt.tdsl.tDsl.ConditionalNextActivity;
import de.msg.xt.mdt.tdsl.tDsl.DataType;
import de.msg.xt.mdt.tdsl.tDsl.Field;
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping;

public class TDslDefaultResourceDescriptionStrategy extends DefaultResourceDescriptionStrategy implements
		IDefaultResourceDescriptionStrategy {

	@Override
	public boolean createEObjectDescriptions(final EObject eObject, final IAcceptor<IEObjectDescription> acceptor) {
		if (eObject instanceof DataType) {
			final DataType dt = (DataType) eObject;
			final Map<String, String> userData = new HashMap<String, String>();
			if (dt.getType() != null) {
				final URI typeUri = EcoreUtil2.getURI(dt.getType());
				userData.put("type", typeUri.toString());
			}
			userData.put("isDefault", dt.isDefault() ? "true" : "false");
			createDescription(acceptor, dt, userData);
		} else if (eObject instanceof ActivityOperation) {
			final ActivityOperation operation = (ActivityOperation) eObject;
			final Map<String, String> userData = new HashMap<String, String>();
			if (!operation.getNextActivities().isEmpty()) {
				final ConditionalNextActivity condNextAct = operation.getNextActivities().get(0);
				final Activity nextActivity = condNextAct.getNext();
				final URI nextActivityUri = EcoreUtil2.getURI(nextActivity);
				userData.put("nextActivityUris", nextActivityUri.toString());
			}
			createDescription(acceptor, operation, userData);
		} else if (eObject instanceof OperationMapping) {
			final OperationMapping operation = (OperationMapping) eObject;
			final Map<String, String> userData = new HashMap<String, String>();
			userData.put("operationName", operation.getName().getName());
			if (!operation.getNextActivities().isEmpty()) {
				final ConditionalNextActivity condNextAct = operation.getNextActivities().get(0);
				final Activity nextActivity = condNextAct.getNext();
				final URI nextActivityUri = EcoreUtil2.getURI(nextActivity);
				userData.put("nextActivityUris", nextActivityUri.toString());
			}
			createDescription(acceptor, operation, userData);
		} else if (eObject instanceof Activity) {
			final Activity activity = (Activity) eObject;
			final Map<String, String> userData = new HashMap<String, String>();
			final String fieldUris = getUriStrings(activity.getFields());
			final String operationUris = getUriStrings(activity.getOperations());
			userData.put("fieldUris", fieldUris);
			userData.put("operationUris", operationUris);
			createDescription(acceptor, activity, userData);
		} else if (eObject instanceof Field) {
			final Field field = (Field) eObject;
			final Map<String, String> userData = new HashMap<String, String>();
			final String operationMappingUris = getUriStrings(field.getOperations());
			userData.put("operationMappingUris", operationMappingUris);
			createDescription(acceptor, field, userData);
		}
		return super.createEObjectDescriptions(eObject, acceptor);
	}

	private String getUriStrings(final List<? extends EObject> list) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(EcoreUtil2.getURI(list.get(i)));
			if (i < (list.size() - 1)) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	private void createDescription(final IAcceptor<IEObjectDescription> acceptor, final EObject object,
			final Map<String, String> userData) {
		final QualifiedName qualifiedName = getQualifiedNameProvider().getFullyQualifiedName(object);
		final IEObjectDescription description = EObjectDescription.create(qualifiedName, object, userData);
		acceptor.accept(description);
	}

}
