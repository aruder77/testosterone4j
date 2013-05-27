package bne3.datatypes;

import bne3.datatypes.TerminationDTEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TerminationDT implements DataType<String,TerminationDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private TerminationDTEquivalenceClass _equivalenceClass;
  
  public TerminationDT() {
  }
  
  public TerminationDT(final String value, final TerminationDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public TerminationDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final TerminationDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<TerminationDTEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.TerminationDTEquivalenceClass.class;
  }
}
