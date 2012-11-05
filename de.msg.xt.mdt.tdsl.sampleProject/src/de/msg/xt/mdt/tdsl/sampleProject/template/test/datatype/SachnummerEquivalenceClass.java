package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import java.util.Set;

import de.msg.xt.mdt.Tags;
import de.msg.xt.mdt.base.EquivalenceClass;

public class SachnummerEquivalenceClass implements EquivalenceClass {
    private final static short EMPTY_ID = -1;

    private final static short snrString_ID = 0;

    public final static SachnummerEquivalenceClass INSTANCE = new SachnummerEquivalenceClass(EMPTY_ID, new Tags[] {});

    public final static SachnummerEquivalenceClass snrString = new SachnummerEquivalenceClass(snrString_ID, new Tags[] {});

    private final static SachnummerEquivalenceClass[] VALUES = new SachnummerEquivalenceClass[] { snrString };

    private Set<EquivalenceClass> allValues;

    private short id;

    private String value;

    private Tags[] tags;

    public SachnummerEquivalenceClass() {
    }

    SachnummerEquivalenceClass(final short id, final Tags[] tags) {
        this.id = id;
        this.tags = tags;

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

    public SachnummerEquivalenceClass getById(final short id) {
        for (EquivalenceClass value : values()) {
            SachnummerEquivalenceClass strValue = (SachnummerEquivalenceClass) value;
            if (strValue.getId() == id) {
                return strValue;
            }
        }
        return null;

    }
}
