package bne3.datatypes;

import bne3.datatypes.EncodingDTEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EncodingDT implements DataType<String,EncodingDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private EncodingDTEquivalenceClass _equivalenceClass;
  
  public EncodingDT() {
  }
  
  public EncodingDT(final String value, final EncodingDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public EncodingDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final EncodingDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<EncodingDTEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.EncodingDTEquivalenceClass.class;
  }
}
