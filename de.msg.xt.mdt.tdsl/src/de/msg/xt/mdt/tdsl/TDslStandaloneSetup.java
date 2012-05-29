
package de.msg.xt.mdt.tdsl;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class TDslStandaloneSetup extends TDslStandaloneSetupGenerated{

	public static void doSetup() {
		new TDslStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

