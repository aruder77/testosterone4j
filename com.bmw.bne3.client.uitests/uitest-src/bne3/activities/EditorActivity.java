package bne3.activities;

import bne3.activities.EditorActivityAdapter;
import bne3.activities.MainWindow;
import bne3.activities.MainWindowAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class EditorActivity extends AbstractActivity {
  private final static String ID = "bne3.activities.EditorActivity";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private EditorActivityAdapter contextAdapter;
  
  public static EditorActivity find() {
    return new EditorActivity(activityLocator.find(ID, EditorActivityAdapter.class));
  }
  
  public EditorActivity() {
    super();
  }
  
  public EditorActivity(final EditorActivityAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
  
  public MainWindow saveAndClose() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "saveAndClose", null);
    Object o = contextAdapter.saveAndClose();
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
  
  public EditorActivity isDirty() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "isDirty", null);
    Object o = contextAdapter.isDirty();
    EditorActivity nextActivity = null;
    if (o instanceof EditorActivity) {
      nextActivity = (EditorActivity)o;
    } else {
      EditorActivityAdapter adapter = injector.getInstance(EditorActivityAdapter.class);
      adapter.setContext(o);
      nextActivity = new EditorActivity(adapter);
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
