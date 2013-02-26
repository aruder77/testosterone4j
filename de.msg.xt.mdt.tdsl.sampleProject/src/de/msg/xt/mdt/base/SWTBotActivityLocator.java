package de.msg.xt.mdt.base;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

public class SWTBotActivityLocator implements ActivityLocator {

    @Override
    public Object beforeTest() {
        TestUtil.resetWorkbench();
        return null;
    }

    @Override
    public <T> T find(String id, Class<T> class1) {
        T activityAdapter = TDslInjector.getInjector().getInstance(class1);
        String type = activityAdapter.getType();
        if ("mp3manager.activities.OpenViewDialog".equals(id)) {
            SWTBot bot = new SWTBot();
            SWTBotShell shell = bot.shell("Show View");
            activityAdapter.setContext(new ActivityContext(shell, id, shell.bot()));
            return activityAdapter;
        } else if ("Editor".equals(type)) {
            SWTWorkbenchBot bot = new SWTWorkbenchBot();
            SWTBotEditor editor = bot.editorById(id);
            activityAdapter.setContext(new ActivityContext(editor, id, editor.bot()));
        } else {
            activityAdapter.setContext(new ActivityContext(new SWTWorkbenchBot(), id, new SWTBot()));
        }
        return activityAdapter;
    }

}
