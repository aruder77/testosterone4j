package de.msg.xt.mdt.tdsl.swtbot.control;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.ComboBox;

public class SWTBotComboBoxControl implements ComboBox {

	SWTBotCombo swtBotCombo;
	String id;

	public static SWTBotComboBoxControl findControl(final ActivityContext context, final String id) {
		return new SWTBotComboBoxControl(id, context.getBot().comboBoxWithId(id));
	}

	public SWTBotComboBoxControl(final String id, final SWTBotCombo swtBotText) {
		this.id = id;
		swtBotCombo = swtBotText;
	}

	@Override
	public void setText(final String str) {
		swtBotCombo.setSelection(str);
	}

	@Override
	public String getText() {
		return swtBotCombo.selection();
	}

	@Override
	public Boolean isEnabled() {
		return swtBotCombo.isEnabled();
	}
}