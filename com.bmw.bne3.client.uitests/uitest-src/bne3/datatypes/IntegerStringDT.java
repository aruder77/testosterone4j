package bne3.datatypes;

import bne3.datatypes.IntegerStringDTEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IntegerStringDT implements DataType<String,IntegerStringDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private IntegerStringDTEquivalenceClass _equivalenceClass;
  
  public IntegerStringDT() {
  }
  
  public IntegerStringDT(final String value, final IntegerStringDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public IntegerStringDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final IntegerStringDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<IntegerStringDTEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.IntegerStringDTEquivalenceClass.class;
  }
}
