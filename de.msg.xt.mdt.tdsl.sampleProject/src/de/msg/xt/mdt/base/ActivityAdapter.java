package de.msg.xt.mdt.base;

import de.msg.xt.mdt.tdsl.sampleProject.template.test.control.TextControl;

public interface ActivityAdapter {

    TextControl getTextControl(Object contextObject, String string);

    Object findContext(String id, String type);

    Object performOperation(Object contextObject, String operationName);

}
