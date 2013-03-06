package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.Label;

public class SWTBotLabelControl implements Label {

    SWTBotLabel swtBotLabel;

    public static SWTBotLabelControl findControl(ActivityContext context, String id) {
        System.out.print("Looking for Label control " + id + " in context " + context.getContext() + "...");
        SWTBotLabel label = context.getBot().labelWithId(id);
        System.out.println("found.");
        return new SWTBotLabelControl(label);
    }

    public SWTBotLabelControl(SWTBotLabel swtBotLabel) {
        this.swtBotLabel = swtBotLabel;
    }

    @Override
    public String getText() {
        return this.swtBotLabel.getText();
    }

}