package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.CheckBox;

public class SWTBotCheckBoxControl implements CheckBox {

    SWTBotCheckBox swtBotCheckBox;
    String id;

    public static SWTBotCheckBoxControl findControl(ActivityContext context, String id) {
        System.out.println("Looking for Text control " + id + " in context " + context.getContext());
        return new SWTBotCheckBoxControl(id, context.getBot().checkBoxWithId(id));
    }

    public SWTBotCheckBoxControl(String id, SWTBotCheckBox swtBotCheckBox) {
        this.id = id;
        this.swtBotCheckBox = swtBotCheckBox;
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotCheckBox.isEnabled();
    }

    @Override
    public void toggle() {
        this.swtBotCheckBox.click();
    }

    @Override
    public void setSelected(Boolean selected) {
        if (selected) {
            this.swtBotCheckBox.select();
        } else {
            this.swtBotCheckBox.deselect();
        }
    }

    @Override
    public Boolean isSelected() {
        return this.swtBotCheckBox.isChecked();
    }

}
