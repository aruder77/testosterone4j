package bne3.datatypes;

import bne3.datatypes.ShortNameEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ShortName implements DataType<String,ShortNameEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private ShortNameEquivalenceClass _equivalenceClass;
  
  public ShortName() {
  }
  
  public ShortName(final String value, final ShortNameEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public ShortNameEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final ShortNameEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<ShortNameEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.ShortNameEquivalenceClass.class;
  }
}
