package mp3manager.activitiyadapter;

import mp3manager.activities.SongEditorAdapter;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

public class SWTBotSongEditorAdapter extends SWTBotBaseAdapter implements SongEditorAdapter {

    @Override
    public String getType() {
        return "Editor";
    }

    @Override
    public Object saveAndClose() {
        SWTBotEditor editor = (SWTBotEditor) this.contextObject.getContext();
        editor.saveAndClose();
        return null;
    }

}
