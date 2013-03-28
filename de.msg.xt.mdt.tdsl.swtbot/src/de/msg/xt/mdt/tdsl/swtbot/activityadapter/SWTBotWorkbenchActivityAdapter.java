package de.msg.xt.mdt.tdsl.swtbot.activityadapter;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotPerspective;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;

public class SWTBotWorkbenchActivityAdapter extends SWTBotActivityAdapter {

	protected SWTWorkbenchBot workbenchBot = new SWTWorkbenchBot();

	public Object openView() {
		workbenchBot.menu("Window").click().menu("Show View").click().menu("Other...").click();
		final SWTBotShell shell = workbenchBot.shell("Show View");
		return new ActivityContext(shell, shell.getId(), shell.bot());
	}

	public Object resetPerspective() {
		workbenchBot.resetActivePerspective();
		return null;
	}

	public Object openPerspective(final String id) {
		final SWTBotPerspective perspective = workbenchBot.perspectiveById(id);
		perspective.activate();
		return null;
	}

	public Object openPerspectiveByName(final String name) {
		final SWTBotPerspective perspective = workbenchBot.perspectiveByLabel(name);
		perspective.activate();
		return null;
	}
}
