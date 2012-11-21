package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import java.util.Set;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.activity.Tags;

public class StringDTEquivalenceClass implements EquivalenceClass {
    private final static short EMPTY_ID = -1;

    private final static short emptyString_ID = 0;

    private final static short shortString_ID = 1;

    private final static short longString_ID = 2;

    public final static StringDTEquivalenceClass INSTANCE = new StringDTEquivalenceClass(EMPTY_ID, new Tags[] {}, null);

    public final static StringDTEquivalenceClass emptyString = new StringDTEquivalenceClass(emptyString_ID, new Tags[] {
            Tags.Pflichtfeld, Tags.Test }, "");

    public final static StringDTEquivalenceClass shortString = new StringDTEquivalenceClass(shortString_ID, new Tags[] {},
            "shortStr");

    public final static StringDTEquivalenceClass longString = new StringDTEquivalenceClass(longString_ID, new Tags[] {},
            "longlonglonglonglong long long very long String");

    private final static StringDTEquivalenceClass[] VALUES = new StringDTEquivalenceClass[] { emptyString, shortString,
            longString };

    private Set<EquivalenceClass> allValues;

    private short id;

    private String value;

    private Tags[] tags;

    public StringDTEquivalenceClass() {
    }

    StringDTEquivalenceClass(final short id, final Tags[] tags, String value) {
        this.id = id;
        this.tags = tags;
        this.value = value;
    }

    public short getId() {
        return this.id;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public Tags[] getTags() {
        return this.tags;
    }

    public Set<EquivalenceClass> values() {
        if (this.allValues == null) {
            this.allValues = new java.util.HashSet<EquivalenceClass>();
            java.util.Collections.addAll(this.allValues, VALUES);
        }
        return this.allValues;

    }

    public StringDTEquivalenceClass getById(final short id) {
        for (EquivalenceClass value : values()) {
            StringDTEquivalenceClass strValue = (StringDTEquivalenceClass) value;
            if (strValue.getId() == id) {
                return strValue;
            }
        }
        return null;

    }

    public StringDTEquivalenceClass getByValue(final String value) {
        for (EquivalenceClass eqvalue : values()) {
            StringDTEquivalenceClass strValue = (StringDTEquivalenceClass) eqvalue;
            if (strValue.getValue() != null && strValue.getValue().equals(value)) {
                return strValue;
            }
        }
        return null;

    }

}
