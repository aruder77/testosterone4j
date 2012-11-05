package de.msg.xt.mdt.tdsl.sampleProject.template.test.activity;

public class AnotherSampleActivity {
	
	public static AnotherSampleActivity find() {
		return new AnotherSampleActivity();
	}

	public void setAdress(String address) {
		System.out.println("AnotherSampleActivity.setAdress: " + address);
	}
}
