package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.activity.Tags;

public enum StringDTEquivalenceClass implements EquivalenceClass {

    EMPTY(new Tags[] { Tags.Pflichtfeld, Tags.Test }, ""), SHORT(new Tags[] {}, "shortStr"), LONG(new Tags[] {},
            "longlonglonglonglong long long very long String");

    private String value;

    private Tags[] tags;

    StringDTEquivalenceClass(final Tags[] tags, String value) {
        this.tags = tags;
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public Tags[] getTags() {
        return this.tags;
    }

    public static StringDTEquivalenceClass getByValue(final String value) {
        for (EquivalenceClass eqvalue : values()) {
            StringDTEquivalenceClass strValue = (StringDTEquivalenceClass) eqvalue;
            if (strValue.getValue() != null && strValue.getValue().equals(value)) {
                return strValue;
            }
        }
        return null;

    }
}
