/* _____________________________________________________________________________
 *
 * Project: ACM
 * File:    SWTBotRadioButton.java
 * Version: $Revision$
 * _____________________________________________________________________________
 *
 * Created by:        reitznej
 * Creation date:     14.12.2011
 * Modified by:       $Author$
 * Modification date: $Date$
 * Description:       See class comment
 * _____________________________________________________________________________
 *
 * Copyright: (C) DAIMLER 2011, all rights reserved
 * _____________________________________________________________________________
 */
package de.msg.xt.mdt.base;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRadio;

/**
 * Handles some stimulations of ACM specific RadioButtonGroup correctly.
 * 
 * @version $Revision$
 * @author reitznej
 * @since 14.12.2011
 */
public class SWTBotRadioButton extends SWTBotRadio {

    /**
     * Wraps a SWTRadioButton.
     * 
     * @param w
     *            the swt radio button.
     * @throws WidgetNotFoundException
     */
    public SWTBotRadioButton(Button w) {
        super(w);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SWTBotRadioButton click() {
        notify(SWT.MouseEnter);
        notify(SWT.MouseMove);
        notify(SWT.Activate);
        notify(SWT.FocusIn);
        notify(SWT.MouseDown);
        notify(SWT.MouseUp);
        notify(SWT.Selection);
        notify(SWT.MouseHover);
        notify(SWT.MouseMove);
        notify(SWT.MouseExit);
        notify(SWT.Deactivate);
        notify(SWT.FocusOut);
        this.log.debug(MessageFormat.format("Clicked on {0}", this)); //$NON-NLS-1$
        return this;
    }

}
