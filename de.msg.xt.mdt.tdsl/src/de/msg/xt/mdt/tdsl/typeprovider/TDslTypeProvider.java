package de.msg.xt.mdt.tdsl.typeprovider;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.typing.XbaseTypeProvider;

import com.google.inject.Singleton;

import de.msg.xt.mdt.tdsl.tDsl.TDslPackage;

@Singleton
public class TDslTypeProvider extends XbaseTypeProvider {
	
	@Inject
	JvmTypesBuilder builder;
	
	public TDslTypeProvider() {
		super();
	}
	
	protected JvmTypeReference _expectedType(EObject container,
			EReference reference, int index, boolean rawType) {
		if (reference == TDslPackage.Literals.OPERATION_MAPPING__GUARD) {
			return builder.newTypeRef(container, Boolean.class);
		}
		return null;
	}

}
