package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTabItem;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.TabItem;

public class SWTBotTabItemControl implements TabItem {

	SWTBotTabItem swtBotTabItem;
	String id;

	public static SWTBotTabItemControl findControl(final ActivityContext context, final String id) {
		System.out.println("Looking for Text control " + id + " in context " + context.getContext());
		return new SWTBotTabItemControl(id, context.getBot().tabItemWithId(id));
	}

	public SWTBotTabItemControl(final String id, final SWTBotTabItem swtBotTabItem) {
		this.id = id;
		this.swtBotTabItem = swtBotTabItem;
	}

	@Override
	public void push() {
		swtBotTabItem.activate().setFocus();
	}

	@Override
	public Boolean isEnabled() {
		return swtBotTabItem.isEnabled();
	}

	@Override
	public Boolean isSelected() {
		return swtBotTabItem.isActive();
	}

}
