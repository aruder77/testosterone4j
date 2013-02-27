package de.msg.xt.mdt.tdsl.sampleProject.template.test.activity;

import de.msg.xt.mdt.base.ActivityAdapter;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.control.TextControl;

public interface SampleActivityAdapter extends ActivityAdapter {

    TextControl getTextControl(String string);

}
