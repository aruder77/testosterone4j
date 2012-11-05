package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SachnummerEquivalenceClassAdapter extends XmlAdapter<String, SachnummerEquivalenceClass> {

    @Override
    public String marshal(SachnummerEquivalenceClass arg0) throws Exception {
        return String.valueOf(arg0.getId());
    }

    @Override
    public SachnummerEquivalenceClass unmarshal(String arg0) throws Exception {
        return SachnummerEquivalenceClass.INSTANCE.getById(Short.parseShort(arg0));
    }

}
