package de.msg.xt.mdt.tdsl.basictypes;

import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringDT implements DataType<String,StringDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private StringDTEquivalenceClass _equivalenceClass;
  
  public StringDT() {
  }
  
  public StringDT(final String value, final StringDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public StringDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final StringDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<StringDTEquivalenceClass> getEquivalenceClassEnum() {
    return de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.class;
  }
}
