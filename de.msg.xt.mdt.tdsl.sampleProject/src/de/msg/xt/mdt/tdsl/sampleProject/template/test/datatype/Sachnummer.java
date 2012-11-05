package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class Sachnummer {
    @XmlAttribute
    private String value;

    @XmlJavaTypeAdapter(SachnummerEquivalenceClassAdapter.class)
    private SachnummerEquivalenceClass equivalenceClass;

    public Sachnummer() {
    }

    public Sachnummer(final String value, final SachnummerEquivalenceClass equivalenceClass) {
        this();
        this.value = value;
        this.equivalenceClass = equivalenceClass;
    }

    public String getValue() {
        return this.value;
    }

    public SachnummerEquivalenceClass getEquivalenceClass() {
        return this.equivalenceClass;
    }

}
