package bne3.datatypes;

import bne3.datatypes.DoubleStringDTEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DoubleStringDT implements DataType<String,DoubleStringDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private DoubleStringDTEquivalenceClass _equivalenceClass;
  
  public DoubleStringDT() {
  }
  
  public DoubleStringDT(final String value, final DoubleStringDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public DoubleStringDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final DoubleStringDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<DoubleStringDTEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.DoubleStringDTEquivalenceClass.class;
  }
}
