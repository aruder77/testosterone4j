package bne3.datatypes;

import bne3.datatypes.KategorieDTEquivalenceClass;
import de.msg.xt.mdt.base.DataType;
import de.msg.xt.mdt.base.Tag;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class KategorieDT implements DataType<String,KategorieDTEquivalenceClass> {
  @XmlAttribute
  private String _value;
  
  @XmlAttribute
  private KategorieDTEquivalenceClass _equivalenceClass;
  
  public KategorieDT() {
  }
  
  public KategorieDT(final String value, final KategorieDTEquivalenceClass equivalenceClass) {
    this._value = value;
    this._equivalenceClass = equivalenceClass;
  }
  
  public String getValue() {
    return this._value;
  }
  
  public void setValue(final String value) {
    this._value = value;
  }
  
  public KategorieDTEquivalenceClass getEquivalenceClass() {
    return this._equivalenceClass;
  }
  
  public void setEquivalenceClass(final KategorieDTEquivalenceClass equivalenceClass) {
    this._equivalenceClass = equivalenceClass;
  }
  
  public Tag[] getTags() {
    return getEquivalenceClass().getTags();
  }
  
  public Class<KategorieDTEquivalenceClass> getEquivalenceClassEnum() {
    return bne3.datatypes.KategorieDTEquivalenceClass.class;
  }
}
