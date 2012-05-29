package test.activity;

import de.msg.xt.mdt.StringDT;
import de.msg.xt.mdt.StringDTEquivalenceClass;
import de.msg.xt.mdt.TextControl;

public class SampleActivity {
	
	TextControl descriptionTextControl = new TextControl("description");
	
	public static SampleActivity find() {
		return new SampleActivity();
	}

	public TextControl getDescriptionTextControl() {
		return descriptionTextControl;
	}
	
	public SampleActivity setDescription(StringDT description) {
		getDescriptionTextControl().setText(description);
		return this;
	}

	public StringDT getDescription() {
		return getDescriptionTextControl().getText();
	}

	public OtherSampleActivity descriptionInvokeAction_1() {
		getDescriptionTextControl().invokeAction();
		return OtherSampleActivity.find();
	}
	
	public boolean isEnabledDescriptionInvokeAction_1() {
		return descriptionTextControl.getText().getEquivalenceClass() == StringDTEquivalenceClass.shortString;
	}

	public AnotherSampleActivity descriptionInvokeAction_2() {
		getDescriptionTextControl().invokeAction();
		return AnotherSampleActivity.find();
	}

	public boolean isEnabledDescriptionInvokeAction_2() {
		return !(descriptionTextControl.getText().getEquivalenceClass() == StringDTEquivalenceClass.shortString);
	}

}
