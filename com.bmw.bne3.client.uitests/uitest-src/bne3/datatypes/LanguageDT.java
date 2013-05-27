package bne3.datatypes;

import bne3.datatypes.LanguageDTEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LanguageDT implements DataType<String,LanguageDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private LanguageDTEquivalenceClass _equivalenceClass;
  
  public LanguageDT() {
  }
  
  public LanguageDT(final String value, final LanguageDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public LanguageDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final LanguageDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<LanguageDTEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.LanguageDTEquivalenceClass.class;
  }
}
