package bne3.activities.paket;

import bne3.activities.EditorActivity;
import bne3.activities.paket.PaketEditorAdapter;
import bne3.activities.paket.PaketEditorAllgemeinPage;
import bne3.activities.paket.PaketEditorAllgemeinPageAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class PaketEditor extends EditorActivity {
  private final static String ID = "PackageTypePresentation";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private PaketEditorAdapter contextAdapter;
  
  public static PaketEditor find() {
    return new PaketEditor(activityLocator.find(ID, PaketEditorAdapter.class));
  }
  
  public PaketEditor() {
    super();
  }
  
  public PaketEditor(final PaketEditorAdapter contextAdapter) {
    super(contextAdapter);
    this.contextAdapter = contextAdapter;
  }
  
  public PaketEditorAllgemeinPage allgemeinPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "allgemeinPage", null);
    Object o = contextAdapter.allgemeinPage();
    PaketEditorAllgemeinPage nextActivity = null;
    if (o instanceof PaketEditorAllgemeinPage) {
      nextActivity = (PaketEditorAllgemeinPage)o;
    } else {
      PaketEditorAllgemeinPageAdapter adapter = injector.getInstance(PaketEditorAllgemeinPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new PaketEditorAllgemeinPage(adapter);
    }
    return nextActivity;
  }
}
