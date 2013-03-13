package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTabItem;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.TabItem;

public class SWTBotTabItemControl implements TabItem {

    SWTBotTabItem swtBotTabItem;
    String id;

    public static SWTBotTabItemControl findControl(ActivityContext context, String id) {
        System.out.println("Looking for Text control " + id + " in context " + context.getContext());
        return new SWTBotTabItemControl(id, context.getBot().tabItemWithId(id));
    }

    public SWTBotTabItemControl(String id, SWTBotTabItem swtBotTabItem) {
        this.id = id;
        this.swtBotTabItem = swtBotTabItem;
    }

    @Override
    public void push() {
        this.swtBotTabItem.activate();
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotTabItem.isEnabled();
    }

    @Override
    public Boolean isSelected() {
        return this.swtBotTabItem.isActive();
    }

}
