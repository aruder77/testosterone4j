package mp3manager.activitiyadapter;

import mp3manager.activities.MainWindowAdapter;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.MainMenu;

public class SWTBotMainWindowAdapter extends SWTBotBaseAdapter implements MainWindowAdapter {

    @Override
    public Object openView() {
        MainMenu.window().click().menu("Open View").click().menu("Other...").click();
        SWTBot bot = new SWTBot();
        SWTBotShell shell = bot.shell("Show View");
        return new ActivityContext(shell, shell.getId(), shell.bot());
    }

    @Override
    public Object findLogicalView() {
        SWTWorkbenchBot workbenchbot = new SWTWorkbenchBot();
        SWTBotView viewBot = workbenchbot.viewByTitle("Logical View");
        return new ActivityContext(viewBot, viewBot.getViewReference().getId(), viewBot.bot());
    }

}
