package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCCombo;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.CComboBox;

public class SWTBotComboBoxControl implements CComboBox {

    SWTBotCCombo swtBotCombo;
    String id;

    public static SWTBotComboBoxControl findControl(ActivityContext context, String id) {
        return new SWTBotComboBoxControl(id, context.getBot().ccomboBoxWithId(id));
    }

    public SWTBotComboBoxControl(String id, SWTBotCCombo swtBotText) {
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