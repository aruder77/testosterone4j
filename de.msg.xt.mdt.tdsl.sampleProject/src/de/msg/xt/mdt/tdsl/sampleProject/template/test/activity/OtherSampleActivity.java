package de.msg.xt.mdt.tdsl.sampleProject.template.test.activity;

public class OtherSampleActivity {
	
	public static OtherSampleActivity find() {
		return new OtherSampleActivity();
	}

	public OtherSampleActivity setItem(String item) {
		System.out.println("Setting item: " + item);
		return this;
	}
}
