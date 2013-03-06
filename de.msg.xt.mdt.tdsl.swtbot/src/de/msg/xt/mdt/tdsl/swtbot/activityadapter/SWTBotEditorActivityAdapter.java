package de.msg.xt.mdt.tdsl.swtbot.activityadapter;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

public class SWTBotEditorActivityAdapter extends SWTBotActivityAdapter {

    public Object saveAndClose() {
        SWTBotEditor editor = (SWTBotEditor) this.contextObject.getContext();
        editor.saveAndClose();
        return null;
    }

    public void save() {

    }

    public void close() {

    }
}
