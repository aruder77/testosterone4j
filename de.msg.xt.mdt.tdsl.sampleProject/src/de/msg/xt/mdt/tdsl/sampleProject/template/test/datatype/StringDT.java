package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import de.msg.xt.mdt.base.DataType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class StringDT implements DataType<String, StringDTEquivalenceClass> {
    @XmlAttribute
    private String value;

    @XmlAttribute
    private StringDTEquivalenceClass equivalenceClass;

    public StringDT() {
    }

    public StringDT(final String value, final StringDTEquivalenceClass equivalenceClass) {
        this();
        this.value = value;
        this.equivalenceClass = equivalenceClass;

    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public StringDTEquivalenceClass getEquivalenceClass() {
        return this.equivalenceClass;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setEquivalenceClass(StringDTEquivalenceClass equivalenceClass) {
        this.equivalenceClass = equivalenceClass;
    }

    @Override
    public Class<StringDTEquivalenceClass> getEquivalenceClassEnum() {
        return StringDTEquivalenceClass.class;
    }
}
