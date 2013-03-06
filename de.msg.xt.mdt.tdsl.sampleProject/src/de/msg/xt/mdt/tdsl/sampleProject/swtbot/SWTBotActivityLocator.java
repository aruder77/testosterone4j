package de.msg.xt.mdt.tdsl.sampleProject.swtbot;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

import de.msg.xt.mdt.base.ActivityAdapter;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.TestUtil;
import de.msg.xt.mdt.tdsl.swtbot.activityadapter.SWTBotEditorActivityAdapter;

public class SWTBotActivityLocator implements ActivityLocator {

    @Override
    public Object beforeTest() {
        TestUtil.resetWorkbench();
        return null;
    }

    @Override
    public <T extends ActivityAdapter> T find(String id, Class<T> class1) {
        T activityAdapter = TDslInjector.getInjector().getInstance(class1);
        if ("mp3manager.activities.OpenViewDialog".equals(id)) {
            SWTBot bot = new SWTBot();
            SWTBotShell shell = bot.shell("Show View");
            activityAdapter.setContext(new ActivityContext(shell, id, shell.bot()));
            return activityAdapter;
        } else if (activityAdapter instanceof SWTBotEditorActivityAdapter) {
            SWTWorkbenchBot bot = new SWTWorkbenchBot();
            SWTBotEditor editor = bot.editorById(id);
            activityAdapter.setContext(new ActivityContext(editor, id, editor.bot()));
        } else {
            activityAdapter.setContext(new ActivityContext(new SWTWorkbenchBot(), id, new SWTBot()));
        }
        return activityAdapter;
    }

}
