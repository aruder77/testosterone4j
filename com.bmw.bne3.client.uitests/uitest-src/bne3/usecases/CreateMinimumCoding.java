package bne3.usecases;

import bne3.activities.AllgemeinPage;
import bne3.activities.CodingEditor;
import bne3.activities.EditorActivity;
import bne3.activities.EthernetNavigator;
import bne3.activities.MainWindow;
import bne3.activities.ViewActivity;
import bne3.datatypes.ChangeRequestTitle;
import bne3.datatypes.ShortName;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.BaseUseCase;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import java.util.Stack;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreateMinimumCoding extends BaseUseCase implements Runnable {
  @XmlElement
  private ShortName shortName;
  
  public CreateMinimumCoding() {
  }
  
  public CreateMinimumCoding(final Generator generator) {
    this();
    this.generator = generator;
    this.shortName = this.getOrGenerateValue(bne3.datatypes.ShortName.class, "bne3.usecases.CreateMinimumCoding.shortName");
  }
  
  public CreateMinimumCoding(final ShortName shortName) {
    this();
    this.shortName = shortName;
  }
  
  public void setShortName(final ShortName shortName) {
    this.shortName = shortName;
  }
  
  @Override
  public void run() {
    execute(MainWindow.find());
  }
  
  public void execute(final MainWindow initialActivity) {
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = initialActivity;
    long _currentTimeMillis = System.currentTimeMillis();
    final String changeRequestName = ("ChangeRequest" + Long.valueOf(_currentTimeMillis));
    getOrGenerateSubUseCase(bne3.usecases.CreateChangeRequest.class, "CreateChangeRequest").setName(new ChangeRequestTitle(changeRequestName, null));
    getOrGenerateSubUseCase(bne3.usecases.CreateChangeRequest.class, "CreateChangeRequest").execute((MainWindow)activity);
    getOrGenerateSubUseCase(bne3.usecases.OpenEthernetNavigator.class, "OpenEthernetNavigator").execute((MainWindow)activity);
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    StringDT bne3_activities_EthernetNavigator_activateChangeRequest_title = new StringDT(changeRequestName, null);
    ((EthernetNavigator)activity).activateChangeRequest(bne3_activities_EthernetNavigator_activateChangeRequest_title);
    StringDT nodePath736832d1 = new StringDT("Ethernet/Datentyp-Kodierungen", null);
    StringDT contextMenuEntry736832d1 = new StringDT("Datentyp-Kodierung hinzuf\u00FCgen", null);
    ((EthernetNavigator)activity).tree_invokeContextMenu(nodePath736832d1, contextMenuEntry736832d1);
    stack.push(activity);
    activity = ((ViewActivity)activity).returnToMain();
    stack.push(activity);
    activity = ((MainWindow)activity).findNewCodingEditor();
    stack.push(activity);
    activity = ((CodingEditor)activity).allgemeinPage();
    ShortName str78af98ef = this.shortName;
    ((AllgemeinPage)activity).shortname_setText(str78af98ef);
    stack.push(activity);
    activity = ((AllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((EditorActivity)activity).saveAndClose();
  }
}
