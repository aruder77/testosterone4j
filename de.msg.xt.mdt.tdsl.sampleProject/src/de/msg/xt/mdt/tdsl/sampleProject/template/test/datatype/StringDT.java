package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringDT {
    @XmlAttribute
    private final String value;

    @XmlAttribute
    private final StringDTEquivalenceClass equivalenceClass;

    public StringDT(final String value, final StringDTEquivalenceClass equivalenceClass) {
        this.value = value;
        this.equivalenceClass = equivalenceClass;

    }

    public String getValue() {
        return this.value;
    }

    public StringDTEquivalenceClass getEquivalenceClass() {
        return this.equivalenceClass;
    }
}
