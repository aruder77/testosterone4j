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
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.IAcceptor;

import de.msg.xt.mdt.tdsl.tDsl.Activity;
import de.msg.xt.mdt.tdsl.tDsl.ConditionalNextActivity;
import de.msg.xt.mdt.tdsl.tDsl.DataType;

public class TDslDefaultResourceDescriptionStrategy extends
		DefaultResourceDescriptionStrategy implements
		IDefaultResourceDescriptionStrategy {

	@Override
	public boolean createEObjectDescriptions(final EObject eObject,
			final IAcceptor<IEObjectDescription> acceptor) {
		if (eObject instanceof DataType) {
			final DataType dt = (DataType) eObject;
			final Map<String, String> userData = new HashMap<String, String>();
			if (dt.getType() != null) {
				resolveObject(dt.getType());
				final URI typeUri = EcoreUtil2.getURI(dt.getType());
				userData.put("type", typeUri.toString());
			}
			userData.put("isDefault", dt.isDefault() ? "true" : "false");
			createDescription(acceptor, dt, userData);
			return true;
		} else {
			return super.createEObjectDescriptions(eObject, acceptor);
		}
	}

	private void addConditionalNextActivityToUserData(
			final Map<String, String> userData,
			final ConditionalNextActivity condNextAct) {
		final Activity nextActivity = condNextAct.getNext();
		if (nextActivity != null) {
			resolveObject(nextActivity);
			final URI nextActivityUri = EcoreUtil2.getURI(nextActivity);
			if (nextActivityUri.toString().contains("xtextLink")) {
				System.out.println("Putting URI " + nextActivityUri
						+ " into Description");
			}
			userData.put("nextActivityUris", nextActivityUri.toString());
		} else {
			userData.put("returnToPreviousActivity", "true");
		}
	}

	private void resolveObject(final EObject eObject) {
		EcoreUtil2.resolveAll(eObject, new CancelIndicator() {
			@Override
			public boolean isCanceled() {
				return false;
			}
		});
	}

	private String getUriStrings(final List<? extends EObject> list) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			resolveObject(list.get(i));
			sb.append(EcoreUtil2.getURI(list.get(i)));
			if (i < (list.size() - 1)) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	private void createDescription(
			final IAcceptor<IEObjectDescription> acceptor,
			final EObject object, final Map<String, String> userData) {
		final QualifiedName qualifiedName = getQualifiedNameProvider()
				.getFullyQualifiedName(object);
		final IEObjectDescription description = EObjectDescription.create(
				qualifiedName, object, userData);
		acceptor.accept(description);
	}

}
