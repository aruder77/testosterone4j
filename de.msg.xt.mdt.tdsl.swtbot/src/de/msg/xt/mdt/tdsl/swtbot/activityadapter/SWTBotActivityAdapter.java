package de.msg.xt.mdt.tdsl.swtbot.activityadapter;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.Label;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;
import de.msg.xt.mdt.tdsl.swtbot.TreeControl;
import de.msg.xt.mdt.tdsl.swtbot.control.SWTBotLabelControl;
import de.msg.xt.mdt.tdsl.swtbot.control.SWTBotTextControl;
import de.msg.xt.mdt.tdsl.swtbot.control.SWTBotTreeControl;

public class SWTBotActivityAdapter {

    protected ActivityContext contextObject;

    public void setContext(Object context) {
        this.contextObject = (ActivityContext) context;
    }

    public TextControl getTextControl(String controlName) {
        return SWTBotTextControl.findControl(this.contextObject, controlName);
    }

    public TreeControl getTreeControl(String controlName) {
        return SWTBotTreeControl.findControl(this.contextObject, controlName);
    }

    public Label getLabel(String controlName) {
        return SWTBotLabelControl.findControl(this.contextObject, controlName);
    }

}
