package de.msg.xt.mdt.tdsl.swtbot.activityadapter;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.Button;
import de.msg.xt.mdt.tdsl.swtbot.Label;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;
import de.msg.xt.mdt.tdsl.swtbot.TreeControl;
import de.msg.xt.mdt.tdsl.swtbot.control.SWTBotButtonControl;
import de.msg.xt.mdt.tdsl.swtbot.control.SWTBotLabelControl;
import de.msg.xt.mdt.tdsl.swtbot.control.SWTBotTextControl;
import de.msg.xt.mdt.tdsl.swtbot.control.SWTBotTreeControl;

public class SWTBotActivityAdapter {

	protected transient ActivityContext contextObject;

	public void setContext(final Object context) {
		contextObject = (ActivityContext) context;
	}

	public TextControl getTextControl(final String controlName) {
		return SWTBotTextControl.findControl(contextObject, controlName);
	}

	public TreeControl getTreeControl(final String controlName) {
		return SWTBotTreeControl.findControl(contextObject, controlName);
	}

	public Label getLabel(final String controlName) {
		return SWTBotLabelControl.findControl(contextObject, controlName);
	}

	public Button getButton(final String buttonName) {
		return SWTBotButtonControl.findControl(contextObject, buttonName);
	}
}
