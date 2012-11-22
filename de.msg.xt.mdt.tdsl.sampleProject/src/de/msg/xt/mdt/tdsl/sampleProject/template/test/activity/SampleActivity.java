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

    public SampleActivity description_setText(StringDT description) {
        getDescriptionTextControl().setText(description.getValue());
        return this;
    }

    public StringDT description_getText() {
        String currentDescription = this.descriptionTextControl.getText();
        StringDT descriptionDataType = new StringDT(currentDescription, StringDTEquivalenceClass.getByValue(currentDescription));
        return descriptionDataType;
    }

    public OtherSampleActivity description_invokeAction_1() {
        getDescriptionTextControl().invokeAction();
        return OtherSampleActivity.find();
    }

    public boolean isEnabled_description_invokeAction_1() {
        return description_getText().getEquivalenceClass() == StringDTEquivalenceClass.SHORT;
    }

    public AnotherSampleActivity description_invokeAction_2() {
        getDescriptionTextControl().invokeAction();
        return AnotherSampleActivity.find();
    }

    public boolean isEnabled_description_invokeAction_2() {
        return !(description_getText().getEquivalenceClass() == StringDTEquivalenceClass.SHORT);
    }

}
