/* _____________________________________________________________________________
 *
 * Project: BNE
 * File:    BaseDataType.java
 * Version: $Revision$
 * _____________________________________________________________________________
 *
 * Created by:        qxf0198
 * Creation date:     30.07.2013
 * Modified by:       $Author$
 * Modification date: $Date$
 * Description:       See class comment
 * _____________________________________________________________________________
 *
 * Copyright: (C) BMW 2013, all rights reserved
 * _____________________________________________________________________________
 */
package de.msg.xt.mdt.base;

import javax.xml.bind.annotation.XmlTransient;

/**
 * TODO Replace with class description.
 * 
 * @version $Revision$
 * @author qxf0198
 * @since 30.07.2013
 */
public abstract class BaseDataType {

	@XmlTransient
	protected transient Boolean valueInitialized = null;

	@XmlTransient
	protected transient Boolean valueDeterministic = null;

	public BaseDataType() {
		valueInitialized = Boolean.FALSE;
		valueDeterministic = Boolean.FALSE;

		final String deterministic = System.getProperty("deterministicValues");
		if ("true".equals(deterministic)) {
			valueDeterministic = Boolean.TRUE;
		}
	}

}
