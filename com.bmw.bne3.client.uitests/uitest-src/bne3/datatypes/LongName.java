package bne3.datatypes;

import bne3.datatypes.LongNameEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LongName implements DataType<String,LongNameEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private LongNameEquivalenceClass _equivalenceClass;
  
  public LongName() {
  }
  
  public LongName(final String value, final LongNameEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public LongNameEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final LongNameEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<LongNameEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.LongNameEquivalenceClass.class;
  }
}
