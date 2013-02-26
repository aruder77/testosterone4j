package de.msg.xt.mdt.tdsl.sampleProject.template.test.usecase;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.BaseUseCase;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.activity.AnotherSampleActivity;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.activity.OtherSampleActivity;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.activity.SampleActivity;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype.StringDT;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SampleUseCase extends BaseUseCase implements Runnable, Serializable {

    public SampleUseCase() {
        super();
    }

    public SampleUseCase(Generator generator) {
        this();
        this.generator = generator;
    }

    public SampleUseCase(StringDT inputParam1, StringDT inputParam2) {
        this();
        this.generatedData.put("SampleUseCase_inputParam1", inputParam1);
        this.generatedData.put("SampleUseCase_inputParam2", inputParam2);
    }

    @Override
    public void run() {
        execute(SampleActivity.find());
    }

    public void execute(SampleActivity initialActivity) {
        AbstractActivity activity = initialActivity;
        activity = ((SampleActivity) activity).description_setText(this.getOrGenerateValue(StringDT.class,
                "SampleUseCase_inputParam1"));
        if (((SampleActivity) activity).isEnabled_description_invokeAction_1()) {
            OtherSampleActivity activity2 = ((SampleActivity) activity).description_invokeAction_1();
            getOrGenerateSubUseCase(SampleSubUseCase.class, "SampleUseCase_subUseCase1").execute(activity2);
        } else if (((SampleActivity) activity).isEnabled_description_invokeAction_2()) {
            AnotherSampleActivity activity3 = ((SampleActivity) activity).description_invokeAction_2();
            activity3.setAdress("Gunta-Stölzl-Str. 5");
        }
    }

}
