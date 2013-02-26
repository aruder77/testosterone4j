package de.msg.xt.mdt.tdsl.sampleProject.template.test.activity;

import com.google.inject.Injector;

import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityAdapter;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.control.TextControl;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype.StringDT;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype.StringDTEquivalenceClass;

public class SampleActivity extends AbstractActivity {

    private static final String ID = "SampleActivity";

    private static final String TYPE = "EditorAcitivity";

    private final Injector injector = TDslInjector.getInjector();

    private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);

    Object contextObject; // like SWTBot, used to find local widgets

    static ActivityAdapter adapter = TDslInjector.getInjector().getInstance(ActivityAdapter.class);

    public static SampleActivity find() {
        return new SampleActivity(adapter.findContext(ID, TYPE));
    }

    public SampleActivity() {
    }

    public SampleActivity(Object context) {
        this();
        this.contextObject = context;
    }

    public TextControl getDescriptionTextControl() {
        return SampleActivity.adapter.getTextControl(this.contextObject, "description");
    }

    public SampleActivity description_setText(StringDT description) {
        this.protocol.appendControlOperationCall(this.getClass().getName(), "description", TextControl.class.getName(),
                "setText", null, description.getValue());
        getDescriptionTextControl().setText(description.getValue());
        return this;
    }

    public StringDT description_getText() {
        String currentDescription = getDescriptionTextControl().getText();
        this.protocol.appendControlOperationCall(this.getClass().getName(), "description", "TextControl", "getText",
                currentDescription);
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
