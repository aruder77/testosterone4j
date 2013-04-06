package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import java.io.Serializable;

import de.msg.xt.mdt.base.EquivalenceClass;
import de.msg.xt.mdt.base.Tag;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.activity.Tags;

public enum StringDTEquivalenceClass implements EquivalenceClass, Serializable {

    EMPTY, SHORT, LONG;

    @Override
    public String getValue() {
        String value = null;
        switch (this) {
        case EMPTY:
            value = "";
            break;
        case SHORT:
            value = "shortStr";
            break;
        case LONG:
            value = "longlonglonglonglong long long very long String";
            break;
        }
        return value;
    }

    @Override
    public Tag[] getTags() {
        Tag[] tags = null;
        switch (this) {
        case EMPTY:
            tags = new Tag[] { Tags.Pflichtfeld, Tags.Test };
            break;
        case SHORT:
            tags = new Tag[] {};
            break;
        case LONG:
            tags = new Tag[] {};
            break;
        }

        return tags;
    }

    public static StringDTEquivalenceClass getByValue(final String value) {
        StringDTEquivalenceClass clazz = null;
        if (value != null) {
            switch (value) {
            case "":
                clazz = EMPTY;
                break;
            case "shortStr":
                clazz = SHORT;
                break;
            case "longlonglonglonglong long long very long String":
                clazz = LONG;
                break;
            }
        }
        return clazz;
    }
}
