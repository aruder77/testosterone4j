package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import de.msg.xt.mdt.base.DataType;

@XmlRootElement
public class StringDT implements DataType<String, StringDTEquivalenceClass>, Serializable {
    @XmlAttribute
    private String _value;

    @XmlAttribute
    private StringDTEquivalenceClass _equivalenceClass;

    public StringDT() {
    }

    public StringDT(final String value, final StringDTEquivalenceClass equivalenceClass) {
        this();
        this._value = value;
        this._equivalenceClass = equivalenceClass;

    }

    @Override
    public String getValue() {
        return this._value;
    }

    @Override
    public StringDTEquivalenceClass getEquivalenceClass() {
        return this._equivalenceClass;
    }

    @Override
    public void setValue(String value) {
        this._value = value;
    }

    @Override
    public void setEquivalenceClass(StringDTEquivalenceClass equivalenceClass) {
        this._equivalenceClass = equivalenceClass;
    }

    @Override
    public Class<StringDTEquivalenceClass> getEquivalenceClassEnum() {
        return StringDTEquivalenceClass.class;
    }
}
