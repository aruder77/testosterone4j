package de.msg.xt.mdt.base;

public interface DataType<DT, EC> {

    DT getValue();

    void setValue(DT value);

    EC getEquivalenceClass();

    void setEquivalenceClass(EC equivalenceClass);

    Class<EC> getEquivalenceClassEnum();
}
