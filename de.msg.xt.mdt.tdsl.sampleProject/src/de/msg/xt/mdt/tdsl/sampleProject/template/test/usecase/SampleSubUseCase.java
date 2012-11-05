package de.msg.xt.mdt.tdsl.sampleProject.template.test.usecase;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.msg.xt.mdt.tdsl.sampleProject.template.test.activity.OtherSampleActivity;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype.StringDT;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.generator.Generator;

@XmlRootElement
public class SampleSubUseCase {

    @XmlElement
    private StringDT inputParam1;

    @XmlTransient
    private Generator generator;

    public SampleSubUseCase() {
    }

    public SampleSubUseCase(StringDT inputParam1) {
        this.inputParam1 = inputParam1;
    }

    public SampleSubUseCase(Generator generator) {
        this.generator = generator;
    }

    public void execute(OtherSampleActivity activity) {
        activity.setItem(getInputParam1().getValue());
    }

    public StringDT getInputParam1() {
        if (this.inputParam1 == null && this.generator != null) {
            this.inputParam1 = this.generator.generateStringDT("SampleSubUseCase_inputParam1");
        }
        return this.inputParam1;
    }

}
