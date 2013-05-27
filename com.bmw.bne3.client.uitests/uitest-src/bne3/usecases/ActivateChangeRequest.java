package bne3.usecases;

import bne3.activities.EthernetNavigator;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.BaseUseCase;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import java.util.Stack;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ActivateChangeRequest extends BaseUseCase implements Runnable {
  @XmlElement
  private StringDT name;
  
  public ActivateChangeRequest() {
  }
  
  public ActivateChangeRequest(final Generator generator) {
    this();
    this.generator = generator;
    this.name = this.getOrGenerateValue(de.msg.xt.mdt.tdsl.basictypes.StringDT.class, "bne3.usecases.ActivateChangeRequest.name");
  }
  
  public ActivateChangeRequest(final StringDT name) {
    this();
    this.name = name;
  }
  
  public void setName(final StringDT name) {
    this.name = name;
  }
  
  @Override
  public void run() {
    execute(EthernetNavigator.find());
  }
  
  public void execute(final EthernetNavigator initialActivity) {
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = initialActivity;
    StringDT bne3_activities_EthernetNavigator_activateChangeRequest_title = this.name;
    ((EthernetNavigator)activity).activateChangeRequest(bne3_activities_EthernetNavigator_activateChangeRequest_title);
  }
}
