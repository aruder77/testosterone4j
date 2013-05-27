package bne3.activities;

import bne3.activities.MainWindow;
import bne3.activities.MainWindowAdapter;
import bne3.activities.ViewActivityAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class ViewActivity extends AbstractActivity {
  private final static String ID = "bne3.activities.ViewActivity";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private ViewActivityAdapter contextAdapter;
  
  public static ViewActivity find() {
    return new ViewActivity(activityLocator.find(ID, ViewActivityAdapter.class));
  }
  
  public ViewActivity() {
    super();
  }
  
  public ViewActivity(final ViewActivityAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
  
  public MainWindow close() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "close", null);
    Object o = contextAdapter.close();
    MainWindow nextActivity = null;
    if (o instanceof MainWindow) {
      nextActivity = (MainWindow)o;
    } else {
      MainWindowAdapter adapter = injector.getInstance(MainWindowAdapter.class);
      adapter.setContext(o);
      nextActivity = new MainWindow(adapter);
    }
    return nextActivity;
  }
  
  public MainWindow returnToMain() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "returnToMain", null);
    Object o = contextAdapter.returnToMain();
    MainWindow nextActivity = null;
    if (o instanceof MainWindow) {
      nextActivity = (MainWindow)o;
    } else {
      MainWindowAdapter adapter = injector.getInstance(MainWindowAdapter.class);
      adapter.setContext(o);
      nextActivity = new MainWindow(adapter);
    }
    return nextActivity;
  }
}
