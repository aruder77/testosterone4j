package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotExpandBar;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.ExpandBar;

public class SWTBotExpandBarControl implements ExpandBar {

    SWTBotExpandBar swtBotExpandBar;
    String id;

    public static SWTBotExpandBarControl findControl(ActivityContext context, String id) {
        System.out.println("Looking for Text control " + id + " in context " + context.getContext());
        return new SWTBotExpandBarControl(id, context.getBot().expandBarWithId(id));
    }

    public SWTBotExpandBarControl(String id, SWTBotExpandBar swtBotCheckBox) {
        this.id = id;
        this.swtBotExpandBar = swtBotCheckBox;
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotExpandBar.isEnabled();
    }

    @Override
    public void expand() {
    }

    @Override
    public void collapse() {
    }

    @Override
    public Boolean isExpanded() {
        return null;
    }

}
