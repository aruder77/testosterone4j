package de.msg.xt.mdt;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringDTEquivalenceClassAdapter extends
		XmlAdapter<String, StringDTEquivalenceClass> {

	@Override
	public String marshal(StringDTEquivalenceClass arg0) throws Exception {
		return String.valueOf(arg0.getId());
	}

	@Override
	public StringDTEquivalenceClass unmarshal(String arg0) throws Exception {
		return StringDTEquivalenceClass.INSTANCE.getById(Short.parseShort(arg0));
	}

}
