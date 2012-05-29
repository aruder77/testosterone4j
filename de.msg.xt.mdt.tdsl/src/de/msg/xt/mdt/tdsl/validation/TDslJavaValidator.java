package de.msg.xt.mdt.tdsl.validation;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.xbase.typing.XbaseTypeConformanceComputer;

import de.msg.xt.mdt.tdsl.tDsl.Operation;
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping;
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage;
import de.msg.xt.mdt.tdsl.typeprovider.TDslTypeProvider;
 

public class TDslJavaValidator extends AbstractTDslJavaValidator {

	   @Inject
	    private XbaseTypeConformanceComputer typeConformanceComputer;
	    
	    @Inject
	    private TDslTypeProvider typeProvider;
	    
	    @Check
	    public void checkTypeConformanceOfOperation(OperationMapping op){
	        JvmTypeReference expectedType = typeProvider
	            .getExpectedType(op.getGuard());
	        JvmTypeReference commonReturnType = typeProvider
	            .getCommonReturnType(op.getGuard(), true);
	        if(!typeConformanceComputer.isConformant(expectedType, 
	            commonReturnType))
	            error("Type does not conform to expected type! " + "Should be: " + expectedType + " but is: " + commonReturnType,
	                TDslPackage.Literals.OPERATION_MAPPING__GUARD);
	    }
}
