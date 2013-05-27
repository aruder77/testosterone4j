package bne3.activities;

import bne3.activities.MainWindow;
import bne3.activities.MainWindowAdapter;
import bne3.activities.OpenViewDialogAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;

public class OpenViewDialog extends AbstractActivity {
  private final static String ID = "bne3.activities.OpenViewDialog";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private OpenViewDialogAdapter contextAdapter;
  
  public static OpenViewDialog find() {
    return new OpenViewDialog(activityLocator.find(ID, OpenViewDialogAdapter.class));
  }
  
  public OpenViewDialog() {
    super();
  }
  
  public OpenViewDialog(final OpenViewDialogAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
  
  public OpenViewDialog selectView(final StringDT category, final StringDT viewId) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "selectView", null, category.getValue().toString(), viewId.getValue().toString());
    Object o = contextAdapter.selectView(category.getValue(), viewId.getValue());
    OpenViewDialog nextActivity = null;
    if (o instanceof OpenViewDialog) {
      nextActivity = (OpenViewDialog)o;
    } else {
      OpenViewDialogAdapter adapter = injector.getInstance(OpenViewDialogAdapter.class);
      adapter.setContext(o);
      nextActivity = new OpenViewDialog(adapter);
    }
    return nextActivity;
  }
  
  public MainWindow ok() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "ok", null);
    Object o = contextAdapter.ok();
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
  
  public MainWindow cancel() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "cancel", null);
    Object o = contextAdapter.cancel();
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
