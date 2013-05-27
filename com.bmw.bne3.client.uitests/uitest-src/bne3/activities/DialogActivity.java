package bne3.activities;

import bne3.activities.DialogActivityAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class DialogActivity extends AbstractActivity {
  private final static String ID = "bne3.activities.DialogActivity";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private DialogActivityAdapter contextAdapter;
  
  public static DialogActivity find() {
    return new DialogActivity(activityLocator.find(ID, DialogActivityAdapter.class));
  }
  
  public DialogActivity() {
    super();
  }
  
  public DialogActivity(final DialogActivityAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
}
