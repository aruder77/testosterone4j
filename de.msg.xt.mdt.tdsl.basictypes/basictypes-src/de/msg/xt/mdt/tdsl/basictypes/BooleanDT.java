package de.msg.xt.mdt.tdsl.basictypes;

import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BooleanDT implements DataType<Boolean,BooleanDTEquivalenceClass> {
  @XmlAttribute
  private Boolean _value;
  
  @XmlAttribute
  private BooleanDTEquivalenceClass _equivalenceClass;
  
  public BooleanDT() {
  }
  
  public BooleanDT(final Boolean value, final BooleanDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public Boolean getValue() {
    return this._value;
  }
  
  public void setValue(final Boolean value) {
    this._value = value;
  }
  
  public BooleanDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final BooleanDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<BooleanDTEquivalenceClass> getEquivalenceClassEnum() {
    return de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.class;
  }
}
