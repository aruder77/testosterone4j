package mp3manager.activitiyadapter;

import mp3manager.activities.MainWindowAdapter;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.activityadapter.SWTBotWorkbenchActivityAdapter;

public class SWTBotMainWindowAdapter extends SWTBotWorkbenchActivityAdapter implements MainWindowAdapter {

    @Override
    public Object findLogicalView() {
        SWTWorkbenchBot workbenchbot = new SWTWorkbenchBot();
        SWTBotView viewBot = workbenchbot.viewByTitle("Logical View");
        return new ActivityContext(viewBot, viewBot.getViewReference().getId(), viewBot.bot());
    }

    @Override
    public Object openView() {
        this.workbenchBot.menu("Window").click().menu("Open View").click().menu("Other...").click();
        final SWTBotShell shell = this.workbenchBot.shell("Show View");
        return new ActivityContext(shell, shell.getId(), shell.bot());
    }

}
