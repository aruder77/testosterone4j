package de.msg.xt.mdt.base;

import java.util.StringTokenizer;

import mp3manager.MP3MangerAppActivityAdapter;
import mp3manager.datatypes.StringDT;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import swtcontrols.Label;
import swtcontrols.TextControl;
import swtcontrols.TreeControl;

public class SWTBotMP3ActivityAdapter implements MP3MangerAppActivityAdapter {

    private static final class SWTBotLabelControl implements swtcontrols.Label {

        SWTBotLabel swtBotLabel;

        public static SWTBotLabelControl findControl(Object context, String id) {
            ActivityContext actContext = (ActivityContext) context;
            System.out.print("Looking for Label control " + id + " in context " + actContext.getContext() + "...");
            SWTBotLabel label = actContext.getBot().labelWithId(id);
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

        public static SWTBotTextControl findControl(Object context, String id) {
            ActivityContext actContext = (ActivityContext) context;
            System.out.println("Looking for Text control " + id + " in context " + actContext.getContext());
            return new SWTBotTextControl(id, actContext.getBot().textWithId(id));
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

        public static SWTBotTreeControl findControl(Object context, String id) {
            ActivityContext actContext = (ActivityContext) context;
            return new SWTBotTreeControl(actContext.getBot().tree());
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

    // @Override
    // public TextControl getTextControl(final Object contextObject, final
    // String controlName) {
    // return new TextControl() {
    // @Override
    // public void setText(final String str) {
    // System.out.println(contextObject + " TextControl[" + controlName +
    // "].setText(" + "str = " + str + ") called.");
    //
    // }
    //
    // @Override
    // public String getText() {
    // System.out.println(contextObject + " TextControl[" + controlName +
    // "].getText(" + ") called.");
    // return null;
    // }
    //
    // @Override
    // public void invokeAction() {
    // System.out.println(contextObject + " TextControl[" + controlName +
    // "].invokeAction(" + ") called.");
    //
    // }
    // };
    // }

    @Override
    public Object performOperation(String id, String type, Object contextObject, String operationName, final Object[] parameters) {
        Object returnContext = null;
        ActivityContext actContext = (ActivityContext) contextObject;
        System.out.println("Performing operation[id='" + id + "', type='" + type + "', operationName='" + operationName + "']");
        if ("mp3manager.activities.MainWindow".equals(id)) {
            if ("openView".equals(operationName)) {
                MainMenu.window().click().menu("Open View").click().menu("Other...").click();
                SWTBot bot = new SWTBot();
                SWTBotShell shell = bot.shell("Show View");
                returnContext = new ActivityContext(shell, shell.getId(), shell.bot());
            } else if ("findLogicalView".equals(operationName)) {
                SWTWorkbenchBot workbenchbot = new SWTWorkbenchBot();
                SWTBotView viewBot = workbenchbot.viewByTitle("Logical View");
                returnContext = new ActivityContext(viewBot, viewBot.getViewReference().getId(), viewBot.bot());
            }
        } else if ("mp3manager.activities.OpenViewDialog".equals(id)) {
            if ("selectLogicalView".equals(operationName)) {
                SWTBotTree tree = (actContext.getBot().tree());
                StringDT viewIdParam = (StringDT) parameters[0];
                tree.expandNode("MP3 Manager (Virtual)", false).getNode(viewIdParam.getValue()).select();
            } else if ("ok".equals(operationName)) {
                actContext.getBot().button("OK").click();
            }
        } else if ("com.siemens.ct.mp3m.ui.editors.ids.Id3DataBindingEditor".equals(id)) {
            if ("saveAndClose".equals(operationName)) {
                SWTBotEditor editor = (SWTBotEditor) actContext.getContext();
                editor.saveAndClose();
            }
        }
        return returnContext;
    }

    @Override
    public TextControl getTextControl(Object contextObject, String controlName) {
        return SWTBotTextControl.findControl(contextObject, controlName);
    }

    @Override
    public TreeControl getTreeControl(Object contextObject, String controlName) {
        return SWTBotTreeControl.findControl(contextObject, controlName);
    }

    @Override
    public Label getLabel(Object contextObject, String controlName) {
        return SWTBotLabelControl.findControl(contextObject, controlName);
    }

}
