package bne3.usecases;

import bne3.activities.MainWindow;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.BaseUseCase;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import java.util.Stack;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OpenEthernetNavigator extends BaseUseCase implements Runnable {
  public OpenEthernetNavigator() {
  }
  
  public OpenEthernetNavigator(final Generator generator) {
    this();
    this.generator = generator;
  }
  
  @Override
  public void run() {
    execute(MainWindow.find());
  }
  
  public void execute(final MainWindow initialActivity) {
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = initialActivity;
    getOrGenerateSubUseCase(bne3.usecases.OpenView.class, "OpenView").setCategory(new StringDT("Other", null));
    getOrGenerateSubUseCase(bne3.usecases.OpenView.class, "OpenView").setName(new StringDT("EthernetNavigator", null));
    getOrGenerateSubUseCase(bne3.usecases.OpenView.class, "OpenView").execute((MainWindow)activity);
  }
}
