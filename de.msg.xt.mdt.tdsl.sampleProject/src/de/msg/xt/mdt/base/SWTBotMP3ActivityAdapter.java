package de.msg.xt.mdt.base;

import mp3manager.Label;
import mp3manager.TextControl;
import mp3manager.TreeControl;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

public class SWTBotMP3ActivityAdapter implements mp3manager.ActivityAdapter {

    private static final class SWTBotLabelControl implements Label {

        SWTBotLabel swtBotLabel;

        public static SWTBotLabelControl findControl(Object context, String id) {
            System.out.println("Looking for Label control " + id + " in context " + context);
            return new SWTBotLabelControl(((SWTBot) context).labelWithId(id));
        }

        public SWTBotLabelControl(SWTBotLabel swtBotLabel) {
            this.swtBotLabel = swtBotLabel;
        }

        @Override
        public String getText() {
            return this.swtBotLabel.getText();
        }

    }

    private static final class SWTBotTextControl implements TextControl {

        SWTBotText swtBotText;

        public static SWTBotTextControl findControl(Object context, String id) {
            System.out.println("Looking for Text control " + id + " in context " + context);
            return new SWTBotTextControl(((SWTBot) context).textWithId(id));
        }

        public SWTBotTextControl(SWTBotText swtBotText) {
            this.swtBotText = swtBotText;
        }

        @Override
        public void setText(String str) {
            this.swtBotText.setText(str);
        }

        @Override
        public String getText() {
            return this.swtBotText.getText();
        }
    }

    public static class SWTBotTreeControl implements TreeControl {

        SWTBotTree swtBotTree;

        public static SWTBotTreeControl findControl(Object context, String id) {
            return new SWTBotTreeControl(((SWTBot) context).tree());
        }

        public SWTBotTreeControl(SWTBotTree tree) {
            this.swtBotTree = tree;
        }

        @Override
        public void doubleClickItem() {
            String nodeText = "Stir It Up";
            SWTBotTreeItem marleyItem = this.swtBotTree.expandNode("Bob Marley", true);
            SWTBotTreeItem legendItem = marleyItem.getNode("Legend");
            legendItem.getNode(nodeText).doubleClick();
        }
    }

    @Override
    public Object findContext(String id, String type) {
        if ("mp3manager.OpenViewDialog".equals(id)) {
            SWTBot bot = new SWTBot();
            SWTBotShell shell = bot.shell("Show View");
            return shell;
        } else if ("Editor".equals(type)) {
            SWTWorkbenchBot bot = new SWTWorkbenchBot();
            SWTBotEditor editor = bot.editorById(id);
            SWTBot editorBot = editor.bot();
            return editorBot;
        }
        return new SWTBot();
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
    public Object performOperation(String id, String type, Object contextObject, String operationName) {
        Object returnContext = null;
        if ("mp3manager.MainWindow".equals(id)) {
            if ("openView".equals(operationName)) {
                MainMenu.window().click().menu("Open View").click().menu("Other...").click();
                SWTBot bot = new SWTBot();
                SWTBotShell shell = bot.shell("Show View");
                returnContext = shell;
            } else if ("findLogicalView".equals(operationName)) {
                SWTWorkbenchBot workbenchbot = new SWTWorkbenchBot();
                SWTBotView viewBot = workbenchbot.viewByTitle("Logical View");
                returnContext = viewBot.bot();
            }
        } else if ("mp3manager.OpenViewDialog".equals(id)) {
            if ("selectLogicalView".equals(operationName)) {
                SWTBotTree tree = ((SWTBotShell) contextObject).bot().tree();
                tree.expandNode("MP3 Manager (Virtual)", false).getNode("Logical View").select();
            } else if ("ok".equals(operationName)) {
                ((SWTBotShell) contextObject).bot().button("OK").click();
            }
        }
        return returnContext;
    }

    @Override
    public Object beforeTest() {
        TestUtil.resetWorkbench();
        return null;
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
