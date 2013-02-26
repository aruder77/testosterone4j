package de.msg.xt.mdt.base;

import java.io.Serializable;

public interface DataType<DT, EC> extends Serializable {

    DT getValue();

    void setValue(DT value);

    EC getEquivalenceClass();

    void setEquivalenceClass(EC equivalenceClass);

    Class<EC> getEquivalenceClassEnum();
}
