package bne3.activities;

import bne3.activities.AllgemeinPage;
import bne3.activities.AllgemeinPageAdapter;
import bne3.activities.CodingEditorAdapter;
import bne3.activities.CodingEditorKodierungPage;
import bne3.activities.CodingEditorKodierungPageAdapter;
import bne3.activities.EditorActivity;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;

public class CodingEditor extends EditorActivity {
  private final static String ID = "CodingTypePresentation";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private CodingEditorAdapter contextAdapter;
  
  public static CodingEditor find() {
    return new CodingEditor(activityLocator.find(ID, CodingEditorAdapter.class));
  }
  
  public CodingEditor() {
    super();
  }
  
  public CodingEditor(final CodingEditorAdapter contextAdapter) {
    super(contextAdapter);
    this.contextAdapter = contextAdapter;
  }
  
  public AllgemeinPage allgemeinPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "allgemeinPage", null);
    Object o = contextAdapter.allgemeinPage();
    AllgemeinPage nextActivity = null;
    if (o instanceof AllgemeinPage) {
      nextActivity = (AllgemeinPage)o;
    } else {
      AllgemeinPageAdapter adapter = injector.getInstance(AllgemeinPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new AllgemeinPage(adapter);
    }
    return nextActivity;
  }
  
  public CodingEditor verwendungPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "verwendungPage", null);
    Object o = contextAdapter.verwendungPage();
    CodingEditor nextActivity = null;
    if (o instanceof CodingEditor) {
      nextActivity = (CodingEditor)o;
    } else {
      CodingEditorAdapter adapter = injector.getInstance(CodingEditorAdapter.class);
      adapter.setContext(o);
      nextActivity = new CodingEditor(adapter);
    }
    return nextActivity;
  }
  
  public CodingEditorKodierungPage kodierungPage() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "kodierungPage", null);
    Object o = contextAdapter.kodierungPage();
    CodingEditorKodierungPage nextActivity = null;
    if (o instanceof CodingEditorKodierungPage) {
      nextActivity = (CodingEditorKodierungPage)o;
    } else {
      CodingEditorKodierungPageAdapter adapter = injector.getInstance(CodingEditorKodierungPageAdapter.class);
      adapter.setContext(o);
      nextActivity = new CodingEditorKodierungPage(adapter);
    }
    return nextActivity;
  }
}
