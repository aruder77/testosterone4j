package mp3manager.activitiyadapter;

import mp3manager.activities.OpenViewDialogAdapter;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

import de.msg.xt.mdt.tdsl.swtbot.activityadapter.SWTBotShellActivityAdapter;

public class SWTBotOpenViewDialogAdapter extends SWTBotShellActivityAdapter implements OpenViewDialogAdapter {

    @Override
    public Object selectLogicalView(String viewId) {
        SWTBotTree tree = this.contextObject.getBot().tree();
        tree.expandNode("MP3 Manager (Virtual)", false).getNode(viewId).select();
        return null;
    }
}
