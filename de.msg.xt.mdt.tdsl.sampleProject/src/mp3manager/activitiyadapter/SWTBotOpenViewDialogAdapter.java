package mp3manager.activitiyadapter;

import mp3manager.activities.OpenViewDialogAdapter;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

public class SWTBotOpenViewDialogAdapter extends SWTBotBaseAdapter implements OpenViewDialogAdapter {

    @Override
    public Object selectLogicalView(String viewId) {
        SWTBotTree tree = this.contextObject.getBot().tree();
        tree.expandNode("MP3 Manager (Virtual)", false).getNode(viewId).select();
        return null;
    }

    @Override
    public Object ok() {
        this.contextObject.getBot().button("OK").click();
        return null;
    }

    @Override
    public Object cancel() {
        this.contextObject.getBot().button("Cancel").click();
        return null;
    }

}
