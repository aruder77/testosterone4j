package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;

public class SWTBotTextControl implements TextControl {

    SWTBotText swtBotText;
    String id;

    public static SWTBotTextControl findControl(ActivityContext context, String id) {
        System.out.println("Looking for Text control " + id + " in context " + context.getContext());
        return new SWTBotTextControl(id, context.getBot().textWithId(id));
    }

    public SWTBotTextControl(String id, SWTBotText swtBotText) {
        this.id = id;
        this.swtBotText = swtBotText;
    }

    @Override
    public void setText(String str) {
        System.out.println("TextControl[" + this.id + "].setText(\"" + str + "\")");
        this.swtBotText.setText(str);
    }

    @Override
    public String getText() {
        System.out.println("TextControl[" + this.id + "].getText()");
        return this.swtBotText.getText();
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotText.isEnabled();
    }
}