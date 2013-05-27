package bne3.usecases;

import bne3.activities.EditorActivity;
import bne3.activities.EthernetNavigator;
import bne3.activities.MainWindow;
import bne3.activities.changerequest.Aenderungsantrag;
import bne3.datatypes.ChangeRequestTitle;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.BaseUseCase;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import java.util.Stack;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import junit.framework.Assert;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@XmlRootElement
public class CreateChangeRequest extends BaseUseCase implements Runnable {
  @XmlElement
  private ChangeRequestTitle name;
  
  public CreateChangeRequest() {
  }
  
  public CreateChangeRequest(final Generator generator) {
    this();
    this.generator = generator;
    this.name = this.getOrGenerateValue(bne3.datatypes.ChangeRequestTitle.class, "bne3.usecases.CreateChangeRequest.name");
  }
  
  public CreateChangeRequest(final ChangeRequestTitle name) {
    this();
    this.name = name;
  }
  
  public void setName(final ChangeRequestTitle name) {
    this.name = name;
  }
  
  @Override
  public void run() {
    execute(MainWindow.find());
  }
  
  public void execute(final MainWindow initialActivity) {
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = initialActivity;
    getOrGenerateSubUseCase(bne3.usecases.OpenEthernetNavigator.class, "OpenEthernetNavigator").execute((MainWindow)activity);
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    stack.push(activity);
    activity = ((EthernetNavigator)activity).addChangeRequest();
    ChangeRequestTitle str52008628 = this.name;
    ((Aenderungsantrag)activity).titel_setText(str52008628);
    stack.push(activity);
    activity = ((EditorActivity)activity).saveAndClose();
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    StringDT bne3_activities_EthernetNavigator_openChangeRequest_title = new StringDT(new Function0<String>() {
      public String apply() {
        String _value = CreateChangeRequest.this.name.getValue();
        return _value;
      }
    }.apply(), null);
    stack.push(activity);
    activity = ((EthernetNavigator)activity).openChangeRequest(bne3_activities_EthernetNavigator_openChangeRequest_title);
    final ChangeRequestTitle actualTitle = ((Aenderungsantrag)activity).titel_getText();
    if (this.generator == null) {
      String _value = this.name.getValue();
      String _value_1 = actualTitle.getValue();
      Assert.assertEquals(_value, _value_1);
    }
  }
}
