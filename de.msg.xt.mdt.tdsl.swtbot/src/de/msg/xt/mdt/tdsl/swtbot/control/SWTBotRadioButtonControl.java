package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotRadio;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.RadioButton;

public class SWTBotRadioButtonControl implements RadioButton {

    SWTBotRadio swtBotRadio;
    String id;

    public static SWTBotRadioButtonControl findControl(ActivityContext context, String id) {
        System.out.println("Looking for Text control " + id + " in context " + context.getContext());
        return new SWTBotRadioButtonControl(id, context.getBot().radioWithId(id));
    }

    public SWTBotRadioButtonControl(String id, SWTBotRadio swtBotRadio) {
        this.id = id;
        this.swtBotRadio = swtBotRadio;
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotRadio.isEnabled();
    }

    @Override
    public void click() {
        this.swtBotRadio.click();
    }

    @Override
    public Boolean isSelected() {
        return this.swtBotRadio.isSelected();
    }

}
