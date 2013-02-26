package de.msg.xt.mdt.tdsl.sampleProject.template.test.usecase;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import de.msg.xt.mdt.base.BaseUseCase;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.activity.OtherSampleActivity;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype.StringDT;

@XmlRootElement
public class SampleSubUseCase extends BaseUseCase implements Serializable {

    public SampleSubUseCase() {
    }

    public SampleSubUseCase(StringDT inputParam1) {
        this();
        this.generatedData.put("SampleSubUseCase_inputParam1", inputParam1);
    }

    public SampleSubUseCase(Generator generator) {
        this();
        this.generator = generator;
    }

    public void execute(OtherSampleActivity activity) {
        activity.setItem(this.getOrGenerateValue(StringDT.class, "SampleSubUseCase_inputParam1").getValue());
    }
}
