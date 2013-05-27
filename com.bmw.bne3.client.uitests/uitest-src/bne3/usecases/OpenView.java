package bne3.usecases;

import bne3.activities.MainWindow;
import bne3.activities.OpenViewDialog;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.BaseUseCase;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import java.util.Stack;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OpenView extends BaseUseCase implements Runnable {
  @XmlElement
  private StringDT category;
  
  @XmlElement
  private StringDT name;
  
  public OpenView() {
  }
  
  public OpenView(final Generator generator) {
    this();
    this.generator = generator;
    this.category = this.getOrGenerateValue(de.msg.xt.mdt.tdsl.basictypes.StringDT.class, "bne3.usecases.OpenView.category");
    this.name = this.getOrGenerateValue(de.msg.xt.mdt.tdsl.basictypes.StringDT.class, "bne3.usecases.OpenView.name");
  }
  
  public OpenView(final StringDT category, final StringDT name) {
    this();
    this.category = category;
    this.name = name;
  }
  
  public void setCategory(final StringDT category) {
    this.category = category;
  }
  
  public void setName(final StringDT name) {
    this.name = name;
  }
  
  @Override
  public void run() {
    execute(MainWindow.find());
  }
  
  public void execute(final MainWindow initialActivity) {
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = initialActivity;
    stack.push(activity);
    activity = ((MainWindow)activity).openView();
    StringDT bne3_activities_OpenViewDialog_selectView_category = this.category;
    StringDT bne3_activities_OpenViewDialog_selectView_viewId = this.name;
    ((OpenViewDialog)activity).selectView(bne3_activities_OpenViewDialog_selectView_category, bne3_activities_OpenViewDialog_selectView_viewId);
    stack.push(activity);
    activity = ((OpenViewDialog)activity).ok();
  }
}
