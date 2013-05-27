package de.msg.xt.mdt.tdsl.basictypes;

import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import de.msg.xt.mdt.tdsl.basictypes.IntegerDTEquivalenceClass;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IntegerDT implements DataType<Integer,IntegerDTEquivalenceClass> {
  @XmlAttribute
  private Integer _value;
  
  @XmlAttribute
  private IntegerDTEquivalenceClass _equivalenceClass;
  
  public IntegerDT() {
  }
  
  public IntegerDT(final Integer value, final IntegerDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public Integer getValue() {
    return this._value;
  }
  
  public void setValue(final Integer value) {
    this._value = value;
  }
  
  public IntegerDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final IntegerDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<IntegerDTEquivalenceClass> getEquivalenceClassEnum() {
    return de.msg.xt.mdt.tdsl.basictypes.IntegerDTEquivalenceClass.class;
  }
}
