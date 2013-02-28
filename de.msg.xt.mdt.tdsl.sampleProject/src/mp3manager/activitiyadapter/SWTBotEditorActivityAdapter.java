package mp3manager.activitiyadapter;

import mp3manager.activities.EditorActivityAdapter;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

public class SWTBotEditorActivityAdapter extends SWTBotBaseAdapter implements EditorActivityAdapter {

    @Override
    public Object saveAndClose() {
        SWTBotEditor editor = (SWTBotEditor) this.contextObject.getContext();
        editor.saveAndClose();
        return null;
    }

}
