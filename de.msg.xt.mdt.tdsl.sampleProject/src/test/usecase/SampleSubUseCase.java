package test.usecase;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import test.activity.OtherSampleActivity;
import test.generator.Generator;
import de.msg.xt.mdt.StringDT;

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
		if (inputParam1 == null && generator != null) {
			inputParam1 = generator.generateStringDT("SampleSubUseCase_inputParam1");
		}
		return inputParam1;
	}

}
