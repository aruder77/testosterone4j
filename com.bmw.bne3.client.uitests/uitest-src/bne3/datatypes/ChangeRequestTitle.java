package bne3.datatypes;

import bne3.datatypes.ChangeRequestTitleEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChangeRequestTitle implements DataType<String,ChangeRequestTitleEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private ChangeRequestTitleEquivalenceClass _equivalenceClass;
  
  public ChangeRequestTitle() {
  }
  
  public ChangeRequestTitle(final String value, final ChangeRequestTitleEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public ChangeRequestTitleEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final ChangeRequestTitleEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<ChangeRequestTitleEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.ChangeRequestTitleEquivalenceClass.class;
  }
}
