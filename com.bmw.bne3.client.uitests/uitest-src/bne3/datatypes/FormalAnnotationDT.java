package bne3.datatypes;

import bne3.datatypes.FormalAnnotationDTEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FormalAnnotationDT implements DataType<String,FormalAnnotationDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private FormalAnnotationDTEquivalenceClass _equivalenceClass;
  
  public FormalAnnotationDT() {
  }
  
  public FormalAnnotationDT(final String value, final FormalAnnotationDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public FormalAnnotationDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final FormalAnnotationDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<FormalAnnotationDTEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.FormalAnnotationDTEquivalenceClass.class;
  }
}
