package bne3.datatypes;

import bne3.datatypes.IntervallgrenzeDTEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IntervallgrenzeDT implements DataType<String,IntervallgrenzeDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private IntervallgrenzeDTEquivalenceClass _equivalenceClass;
  
  public IntervallgrenzeDT() {
  }
  
  public IntervallgrenzeDT(final String value, final IntervallgrenzeDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public IntervallgrenzeDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final IntervallgrenzeDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<IntervallgrenzeDTEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.IntervallgrenzeDTEquivalenceClass.class;
  }
}
