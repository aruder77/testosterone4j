
package org.testosterone4j.tdsl;

import de.msg.xt.mdt.tdsl.TDslStandaloneSetupGenerated;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class TDslStandaloneSetup extends TDslStandaloneSetupGenerated{

	public static void doSetup() {
		new TDslStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

