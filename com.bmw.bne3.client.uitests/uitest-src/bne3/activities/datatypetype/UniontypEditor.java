package bne3.activities.datatypetype;

import bne3.activities.EditorActivity;
import bne3.activities.datatypetype.UniontypEditorAdapter;
import bne3.activities.datatypetype.UniontypEditorAllgemeinPage;
import bne3.activities.datatypetype.UniontypEditorAllgemeinPageAdapter;
import bne3.activities.datatypetype.UniontypEditorSpeziellPage;
import bne3.activities.datatypetype.UniontypEditorSpeziellPageAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class UniontypEditor extends EditorActivity {
  private final static String ID = "StructureDatatypeTypePresentation";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private UniontypEditorAdapter contextAdapter;
  
  public static UniontypEditor find() {
    return new UniontypEditor(activityLocator.find(ID, UniontypEditorAdapter.class));
  }
  
  public UniontypEditor() {
    super();
  }
  
  public UniontypEditor(final UniontypEditorAdapter contextAdapter) {
    super(contextAdapter);
    this.contextAdapter = contextAdapter;
  }
  
  public UniontypEditorAllgemeinPage allgemeinPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "allgemeinPage", null);
    Object o = contextAdapter.allgemeinPage();
    UniontypEditorAllgemeinPage nextActivity = null;
    if (o instanceof UniontypEditorAllgemeinPage) {
      nextActivity = (UniontypEditorAllgemeinPage)o;
    } else {
      UniontypEditorAllgemeinPageAdapter adapter = injector.getInstance(UniontypEditorAllgemeinPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new UniontypEditorAllgemeinPage(adapter);
    }
    return nextActivity;
  }
  
  public UniontypEditorSpeziellPage speziellPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "speziellPage", null);
    Object o = contextAdapter.speziellPage();
    UniontypEditorSpeziellPage nextActivity = null;
    if (o instanceof UniontypEditorSpeziellPage) {
      nextActivity = (UniontypEditorSpeziellPage)o;
    } else {
      UniontypEditorSpeziellPageAdapter adapter = injector.getInstance(UniontypEditorSpeziellPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new UniontypEditorSpeziellPage(adapter);
    }
    return nextActivity;
  }
}
