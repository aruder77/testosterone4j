package bne3.activities.datatypetype;

import bne3.activities.datatypetype.UniontypEditor;
import bne3.activities.datatypetype.UniontypEditorAdapter;
import bne3.activities.datatypetype.UniontypEditorSpeziellPageAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class UniontypEditorSpeziellPage extends AbstractActivity {
  private final static String ID = "Speziell";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private UniontypEditorSpeziellPageAdapter contextAdapter;
  
  public static UniontypEditorSpeziellPage find() {
    return new UniontypEditorSpeziellPage(activityLocator.find(ID, UniontypEditorSpeziellPageAdapter.class));
  }
  
  public UniontypEditorSpeziellPage() {
    super();
  }
  
  public UniontypEditorSpeziellPage(final UniontypEditorSpeziellPageAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
  
  public UniontypEditor returnToEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "returnToEditor", null);
    Object o = contextAdapter.returnToEditor();
    UniontypEditor nextActivity = null;
    if (o instanceof UniontypEditor) {
      nextActivity = (UniontypEditor)o;
    } else {
      UniontypEditorAdapter adapter = injector.getInstance(UniontypEditorAdapter.class);
      adapter.setContext(o);
      nextActivity = new UniontypEditor(adapter);
    }
    return nextActivity;
  }
}
