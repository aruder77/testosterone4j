package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import de.msg.xt.mdt.SachnummerEquivalenceClass;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Sachnummer {
  @XmlAttribute
  private String value;
  
  @XmlAttribute
  private SachnummerEquivalenceClass equivalenceClass;
  
  public Sachnummer(final String value, final SachnummerEquivalenceClass equivalenceClass) {
    this.value = value;
    this.equivalenceClass = equivalenceClass;
    
  }
  
  public String getValue() {
    return this.value;
  }
  
  public SachnummerEquivalenceClass getEquivalenceClass() {
    return this.equivalenceClass;
  }
}
