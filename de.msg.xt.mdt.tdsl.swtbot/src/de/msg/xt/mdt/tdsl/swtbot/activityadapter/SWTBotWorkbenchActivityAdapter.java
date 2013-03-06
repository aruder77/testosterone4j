package de.msg.xt.mdt.tdsl.swtbot.activityadapter;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.MainMenu;

public class SWTBotWorkbenchActivityAdapter extends SWTBotActivityAdapter {

    public Object openView() {
        MainMenu.window().click().menu("Open View").click().menu("Other...").click();
        SWTBot bot = new SWTBot();
        SWTBotShell shell = bot.shell("Show View");
        return new ActivityContext(shell, shell.getId(), shell.bot());
    }

}
