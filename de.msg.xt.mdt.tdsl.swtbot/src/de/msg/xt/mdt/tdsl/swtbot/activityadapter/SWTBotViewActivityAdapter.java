package de.msg.xt.mdt.tdsl.swtbot.activityadapter;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;

public class SWTBotViewActivityAdapter extends SWTBotActivityAdapter {

	protected SWTBotView getViewContext() {
		return (SWTBotView) contextObject.getContext();
	}

	public Object close() {
		getViewContext().close();
		return null;
	}

	public Object returnToMain() {
		// nothing to do here...
		return null;
	}

}
