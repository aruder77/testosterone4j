package bne3.activities.datatypetype;

import bne3.activities.datatypetype.StrukturEditor;
import bne3.activities.datatypetype.StrukturEditorAdapter;
import bne3.activities.datatypetype.StrukturEditorSpeziellPageAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class StrukturEditorSpeziellPage extends AbstractActivity {
  private final static String ID = "Speziell";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private StrukturEditorSpeziellPageAdapter contextAdapter;
  
  public static StrukturEditorSpeziellPage find() {
    return new StrukturEditorSpeziellPage(activityLocator.find(ID, StrukturEditorSpeziellPageAdapter.class));
  }
  
  public StrukturEditorSpeziellPage() {
    super();
  }
  
  public StrukturEditorSpeziellPage(final StrukturEditorSpeziellPageAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
  
  public StrukturEditor returnToEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "returnToEditor", null);
    Object o = contextAdapter.returnToEditor();
    StrukturEditor nextActivity = null;
    if (o instanceof StrukturEditor) {
      nextActivity = (StrukturEditor)o;
    } else {
      StrukturEditorAdapter adapter = injector.getInstance(StrukturEditorAdapter.class);
      adapter.setContext(o);
      nextActivity = new StrukturEditor(adapter);
    }
    return nextActivity;
  }
}
