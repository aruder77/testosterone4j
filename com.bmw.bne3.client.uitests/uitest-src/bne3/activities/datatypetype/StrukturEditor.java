package bne3.activities.datatypetype;

import bne3.activities.EditorActivity;
import bne3.activities.datatypetype.StrukturEditorAdapter;
import bne3.activities.datatypetype.StrukturEditorAllgemeinPage;
import bne3.activities.datatypetype.StrukturEditorAllgemeinPageAdapter;
import bne3.activities.datatypetype.StrukturEditorSpeziellPage;
import bne3.activities.datatypetype.StrukturEditorSpeziellPageAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class StrukturEditor extends EditorActivity {
  private final static String ID = "StructureDatatypeTypePresentation";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private StrukturEditorAdapter contextAdapter;
  
  public static StrukturEditor find() {
    return new StrukturEditor(activityLocator.find(ID, StrukturEditorAdapter.class));
  }
  
  public StrukturEditor() {
    super();
  }
  
  public StrukturEditor(final StrukturEditorAdapter contextAdapter) {
    super(contextAdapter);
    this.contextAdapter = contextAdapter;
  }
  
  public StrukturEditorAllgemeinPage allgemeinPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "allgemeinPage", null);
    Object o = contextAdapter.allgemeinPage();
    StrukturEditorAllgemeinPage nextActivity = null;
    if (o instanceof StrukturEditorAllgemeinPage) {
      nextActivity = (StrukturEditorAllgemeinPage)o;
    } else {
      StrukturEditorAllgemeinPageAdapter adapter = injector.getInstance(StrukturEditorAllgemeinPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new StrukturEditorAllgemeinPage(adapter);
    }
    return nextActivity;
  }
  
  public StrukturEditorSpeziellPage speziellPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "speziellPage", null);
    Object o = contextAdapter.speziellPage();
    StrukturEditorSpeziellPage nextActivity = null;
    if (o instanceof StrukturEditorSpeziellPage) {
      nextActivity = (StrukturEditorSpeziellPage)o;
    } else {
      StrukturEditorSpeziellPageAdapter adapter = injector.getInstance(StrukturEditorSpeziellPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new StrukturEditorSpeziellPage(adapter);
    }
    return nextActivity;
  }
}
