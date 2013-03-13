package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.Button;

public class SWTBotButtonControl implements Button {

    SWTBotButton swtBotButton;
    String id;

    public static SWTBotButtonControl findControl(ActivityContext context, String id) {
        System.out.println("Looking for Text control " + id + " in context " + context.getContext());
        return new SWTBotButtonControl(id, context.getBot().buttonWithId(id));
    }

    public SWTBotButtonControl(String id, SWTBotButton swtBotButton) {
        this.id = id;
        this.swtBotButton = swtBotButton;
    }

    @Override
    public void push() {
        this.swtBotButton.click();
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotButton.isEnabled();
    }

}
