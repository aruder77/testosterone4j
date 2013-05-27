package bne3.activities.datatypetype;

import bne3.activities.EditorActivity;
import bne3.activities.datatypetype.AufzaehlungstypenEditorAdapter;
import bne3.activities.datatypetype.AufzaehlungstypenEditorAllgemeinPage;
import bne3.activities.datatypetype.AufzaehlungstypenEditorAllgemeinPageAdapter;
import bne3.activities.datatypetype.AufzaehlungstypenEditorSpeziellPage;
import bne3.activities.datatypetype.AufzaehlungstypenEditorSpeziellPageAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class AufzaehlungstypenEditor extends EditorActivity {
  private final static String ID = "EnumDatatypeTypePresentation";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private AufzaehlungstypenEditorAdapter contextAdapter;
  
  public static AufzaehlungstypenEditor find() {
    return new AufzaehlungstypenEditor(activityLocator.find(ID, AufzaehlungstypenEditorAdapter.class));
  }
  
  public AufzaehlungstypenEditor() {
    super();
  }
  
  public AufzaehlungstypenEditor(final AufzaehlungstypenEditorAdapter contextAdapter) {
    super(contextAdapter);
    this.contextAdapter = contextAdapter;
  }
  
  public AufzaehlungstypenEditorAllgemeinPage allgemeinPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "allgemeinPage", null);
    Object o = contextAdapter.allgemeinPage();
    AufzaehlungstypenEditorAllgemeinPage nextActivity = null;
    if (o instanceof AufzaehlungstypenEditorAllgemeinPage) {
      nextActivity = (AufzaehlungstypenEditorAllgemeinPage)o;
    } else {
      AufzaehlungstypenEditorAllgemeinPageAdapter adapter = injector.getInstance(AufzaehlungstypenEditorAllgemeinPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new AufzaehlungstypenEditorAllgemeinPage(adapter);
    }
    return nextActivity;
  }
  
  public AufzaehlungstypenEditorSpeziellPage speziellPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "speziellPage", null);
    Object o = contextAdapter.speziellPage();
    AufzaehlungstypenEditorSpeziellPage nextActivity = null;
    if (o instanceof AufzaehlungstypenEditorSpeziellPage) {
      nextActivity = (AufzaehlungstypenEditorSpeziellPage)o;
    } else {
      AufzaehlungstypenEditorSpeziellPageAdapter adapter = injector.getInstance(AufzaehlungstypenEditorSpeziellPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new AufzaehlungstypenEditorSpeziellPage(adapter);
    }
    return nextActivity;
  }
}
