package de.msg.xt.mdt.tdsl.swtbot.control;

import java.util.StringTokenizer;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.TreeControl;

public class SWTBotTreeControl implements TreeControl {

    SWTBotTree swtBotTree;

    public static SWTBotTreeControl findControl(ActivityContext context, String id) {
        return new SWTBotTreeControl(context.getBot().tree());
    }

    public SWTBotTreeControl(SWTBotTree tree) {
        this.swtBotTree = tree;
    }

    @Override
    public void selectNode(String nodePath) {
        getItem(nodePath).select();
    }

    private SWTBotTreeItem getItem(String nodePath) {
        StringTokenizer st = new StringTokenizer(nodePath, "/");
        SWTBotTreeItem item = this.swtBotTree.expandNode(st.nextToken(), true);
        while (st.hasMoreTokens()) {
            String nodeText = st.nextToken();
            item = item.getNode(nodeText);
        }
        return item;
    }

    @Override
    public void doubleClickNode(String nodePath) {
        getItem(nodePath).doubleClick();
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotTree.isEnabled();
    }
}