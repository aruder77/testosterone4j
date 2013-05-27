package bne3.activities.datatypetype;

import bne3.activities.EditorActivity;
import bne3.activities.datatypetype.BasisDatentypEditorAdapter;
import bne3.activities.datatypetype.BasisDatentypSpeziellPage;
import bne3.activities.datatypetype.BasisDatentypSpeziellPageAdapter;
import bne3.activities.datatypetype.BasisdatentypEditorAllgemeinPage;
import bne3.activities.datatypetype.BasisdatentypEditorAllgemeinPageAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class BasisDatentypEditor extends EditorActivity {
  private final static String ID = "CommonDatatypeTypePresentation";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private BasisDatentypEditorAdapter contextAdapter;
  
  public static BasisDatentypEditor find() {
    return new BasisDatentypEditor(activityLocator.find(ID, BasisDatentypEditorAdapter.class));
  }
  
  public BasisDatentypEditor() {
    super();
  }
  
  public BasisDatentypEditor(final BasisDatentypEditorAdapter contextAdapter) {
    super(contextAdapter);
    this.contextAdapter = contextAdapter;
  }
  
  public BasisdatentypEditorAllgemeinPage allgemeinPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "allgemeinPage", null);
    Object o = contextAdapter.allgemeinPage();
    BasisdatentypEditorAllgemeinPage nextActivity = null;
    if (o instanceof BasisdatentypEditorAllgemeinPage) {
      nextActivity = (BasisdatentypEditorAllgemeinPage)o;
    } else {
      BasisdatentypEditorAllgemeinPageAdapter adapter = injector.getInstance(BasisdatentypEditorAllgemeinPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new BasisdatentypEditorAllgemeinPage(adapter);
    }
    return nextActivity;
  }
  
  public BasisDatentypSpeziellPage speziellPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "speziellPage", null);
    Object o = contextAdapter.speziellPage();
    BasisDatentypSpeziellPage nextActivity = null;
    if (o instanceof BasisDatentypSpeziellPage) {
      nextActivity = (BasisDatentypSpeziellPage)o;
    } else {
      BasisDatentypSpeziellPageAdapter adapter = injector.getInstance(BasisDatentypSpeziellPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new BasisDatentypSpeziellPage(adapter);
    }
    return nextActivity;
  }
}
