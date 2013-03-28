package de.msg.xt.mdt.tdsl.swtbot.activityadapter;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

public class SWTBotEditorActivityAdapter extends SWTBotActivityAdapter {

	protected SWTBotEditor getEditorContext() {
		return (SWTBotEditor) contextObject.getContext();
	}

	public Object saveAndClose() {
		getEditorContext().saveAndClose();
		return null;
	}

	public Object save() {
		getEditorContext().save();
		return null;
	}

	public Object close() {
		getEditorContext().close();
		return null;
	}

	public Boolean isDirty() {
		return getEditorContext().isDirty();
	}

	public Object returnToMain() {
		// nothing to do here
		return null;
	}
}
