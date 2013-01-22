package de.msg.xt.mdt.base;

import mp3manager.TextControl;
import mp3manager.TreeControl;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

public class SWTBotMP3ActivityAdapter implements mp3manager.ActivityAdapter {

    @Override
    public Object findContext(String id, String type) {
        if ("mp3manager.OpenViewDialog".equals(id)) {
            SWTBot bot = new SWTBot();
            SWTBotShell shell = bot.shell("Show View");
            return shell;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TreeControl getTreeControl(Object contextObject, String controlName) {
        // TODO Auto-generated method stub
        return null;
    }

}
