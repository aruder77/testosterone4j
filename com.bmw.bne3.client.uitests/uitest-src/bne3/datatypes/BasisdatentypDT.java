package bne3.datatypes;

import bne3.datatypes.BasisdatentypDTEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BasisdatentypDT implements DataType<String,BasisdatentypDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private BasisdatentypDTEquivalenceClass _equivalenceClass;
  
  public BasisdatentypDT() {
  }
  
  public BasisdatentypDT(final String value, final BasisdatentypDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public BasisdatentypDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final BasisdatentypDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<BasisdatentypDTEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.BasisdatentypDTEquivalenceClass.class;
  }
}
