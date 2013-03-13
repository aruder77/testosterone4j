package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.ComboBox;

public class SWTBotCComboBoxControl implements ComboBox {

    SWTBotCombo swtBotCombo;
    String id;

    public static SWTBotCComboBoxControl findControl(ActivityContext context, String id) {
        return new SWTBotCComboBoxControl(id, context.getBot().comboBoxWithId(id));
    }

    public SWTBotCComboBoxControl(String id, SWTBotCombo swtBotText) {
        this.id = id;
        this.swtBotCombo = swtBotText;
    }

    @Override
    public void setText(String str) {
        this.swtBotCombo.setText(str);
    }

    @Override
    public String getText() {
        return this.swtBotCombo.getText();
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotCombo.isEnabled();
    }
}