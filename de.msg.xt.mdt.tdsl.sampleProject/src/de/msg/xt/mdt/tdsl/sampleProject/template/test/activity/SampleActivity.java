package de.msg.xt.mdt.tdsl.sampleProject.template.test.activity;

import de.msg.xt.mdt.tdsl.sampleProject.template.test.control.TextControl;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype.StringDT;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype.StringDTEquivalenceClass;

public class SampleActivity {

    TextControl descriptionTextControl = new TextControl("description");

    public static SampleActivity find() {
        return new SampleActivity();
    }

    public TextControl getDescriptionTextControl() {
        return this.descriptionTextControl;
    }

    public SampleActivity setDescription(StringDT description) {
        getDescriptionTextControl().setText(description.getValue());
        return this;
    }

    public StringDT getDescription() {
        String currentDescription = this.descriptionTextControl.getText();
        StringDT descriptionDataType = new StringDT(currentDescription, StringDTEquivalenceClass.getByValue(currentDescription));
        return descriptionDataType;
    }

    public OtherSampleActivity descriptionInvokeAction_1() {
        getDescriptionTextControl().invokeAction();
        return OtherSampleActivity.find();
    }

    public boolean isEnabledDescriptionInvokeAction_1() {
        return getDescription().getEquivalenceClass() == StringDTEquivalenceClass.SHORT;
    }

    public AnotherSampleActivity descriptionInvokeAction_2() {
        getDescriptionTextControl().invokeAction();
        return AnotherSampleActivity.find();
    }

    public boolean isEnabledDescriptionInvokeAction_2() {
        return !(getDescription().getEquivalenceClass() == StringDTEquivalenceClass.SHORT);
    }

}
