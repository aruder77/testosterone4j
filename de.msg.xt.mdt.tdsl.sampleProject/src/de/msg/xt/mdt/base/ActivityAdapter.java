package de.msg.xt.mdt.base;

import de.msg.xt.mdt.tdsl.sampleProject.template.test.control.TextControl;

public interface ActivityAdapter {

    String getType();

    void setContext(Object context);

    TextControl getTextControl(String string);
}
