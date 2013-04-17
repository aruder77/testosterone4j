package de.msg.xt.mdt.tdsl.scoping;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.util.IAcceptor;

import de.msg.xt.mdt.tdsl.tDsl.DataType;

public class TDslDefaultResourceDescriptionStrategy extends
		DefaultResourceDescriptionStrategy implements
		IDefaultResourceDescriptionStrategy {

	@Override
	public boolean createEObjectDescriptions(EObject eObject,
			IAcceptor<IEObjectDescription> acceptor) {
		if (eObject instanceof DataType) {
			DataType dt = (DataType) eObject;
			Map<String, String> userData = new HashMap<String, String>();
			if (dt.getType() != null
					&& getQualifiedNameProvider().getFullyQualifiedName(
							dt.getType()) != null) {
				userData.put("type", getQualifiedNameProvider()
						.getFullyQualifiedName(dt.getType()).toString());
			}
			userData.put("isDefault", dt.isDefault() ? "true" : "false");
			QualifiedName qualifiedName = getQualifiedNameProvider()
					.getFullyQualifiedName(dt);
			IEObjectDescription description = EObjectDescription.create(
					qualifiedName, dt, userData);
			acceptor.accept(description);
		}
		return super.createEObjectDescriptions(eObject, acceptor);
	}

}
