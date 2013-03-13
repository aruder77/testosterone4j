package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotLink;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.Link;

public class SWTBotLinkControl implements Link {

    SWTBotLink swtBotLink;
    String id;

    public static SWTBotLinkControl findControl(ActivityContext context, String id) {
        System.out.println("Looking for Text control " + id + " in context " + context.getContext());
        return new SWTBotLinkControl(id, context.getBot().linkWithId(id));
    }

    public SWTBotLinkControl(String id, SWTBotLink swtBotButton) {
        this.id = id;
        this.swtBotLink = swtBotButton;
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotLink.isEnabled();
    }

    @Override
    public void click() {
        this.swtBotLink.click();
    }

}
