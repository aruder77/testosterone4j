package mp3manager.activitiyadapter;

import java.util.StringTokenizer;

import mp3manager.MP3MangerAppActivityAdapter;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import swtcontrols.Label;
import swtcontrols.TextControl;
import swtcontrols.TreeControl;
import de.msg.xt.mdt.base.ActivityContext;

public abstract class SWTBotBaseAdapter implements MP3MangerAppActivityAdapter {

    protected ActivityContext contextObject;

    @Override
    public void setContext(Object context) {
        this.contextObject = (ActivityContext) context;
    }

    @Override
    public TextControl getTextControl(String controlName) {
        return SWTBotTextControl.findControl(this.contextObject, controlName);
    }

    @Override
    public TreeControl getTreeControl(String controlName) {
        return SWTBotTreeControl.findControl(this.contextObject, controlName);
    }

    @Override
    public Label getLabel(String controlName) {
        return SWTBotLabelControl.findControl(this.contextObject, controlName);
    }

    private static final class SWTBotLabelControl implements swtcontrols.Label {

        SWTBotLabel swtBotLabel;

        public static SWTBotLabelControl findControl(ActivityContext context, String id) {
            System.out.print("Looking for Label control " + id + " in context " + context.getContext() + "...");
            SWTBotLabel label = context.getBot().labelWithId(id);
            System.out.println("found.");
            return new SWTBotLabelControl(label);
        }

        public SWTBotLabelControl(SWTBotLabel swtBotLabel) {
            this.swtBotLabel = swtBotLabel;
        }

        @Override
        public String getText() {
            return this.swtBotLabel.getText();
        }

    }

    private static final class SWTBotTextControl implements swtcontrols.TextControl {

        SWTBotText swtBotText;
        String id;

        public static SWTBotTextControl findControl(ActivityContext context, String id) {
            System.out.println("Looking for Text control " + id + " in context " + context.getContext());
            return new SWTBotTextControl(id, context.getBot().textWithId(id));
        }

        public SWTBotTextControl(String id, SWTBotText swtBotText) {
            this.id = id;
            this.swtBotText = swtBotText;
        }

        @Override
        public void setText(String str) {
            System.out.println("TextControl[" + this.id + "].setText(\"" + str + "\")");
            this.swtBotText.setText(str);
        }

        @Override
        public String getText() {
            System.out.println("TextControl[" + this.id + "].getText()");
            return this.swtBotText.getText();
        }
    }

    public static class SWTBotTreeControl implements swtcontrols.TreeControl {

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
    }

}
