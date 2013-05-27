package bne3.activities;

import bne3.activities.CodingEditor;
import bne3.activities.CodingEditorAdapter;
import bne3.activities.EthernetNavigator;
import bne3.activities.EthernetNavigatorAdapter;
import bne3.activities.MainWindowAdapter;
import bne3.activities.OpenViewDialog;
import bne3.activities.OpenViewDialogAdapter;
import bne3.activities.changerequest.Aenderungsantrag;
import bne3.activities.changerequest.AenderungsantragAdapter;
import bne3.activities.datatypetype.AufzaehlungstypenEditor;
import bne3.activities.datatypetype.AufzaehlungstypenEditorAdapter;
import bne3.activities.datatypetype.BasisDatentypEditor;
import bne3.activities.datatypetype.BasisDatentypEditorAdapter;
import bne3.activities.datatypetype.StrukturEditor;
import bne3.activities.datatypetype.StrukturEditorAdapter;
import bne3.activities.datatypetype.UniontypEditor;
import bne3.activities.datatypetype.UniontypEditorAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;

public class MainWindow extends AbstractActivity {
  private final static String ID = "bne3.activities.MainWindow";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private MainWindowAdapter contextAdapter;
  
  public static MainWindow find() {
    return new MainWindow(activityLocator.find(ID, MainWindowAdapter.class));
  }
  
  public MainWindow() {
    super();
  }
  
  public MainWindow(final MainWindowAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
  
  public OpenViewDialog openView() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openView", null);
    Object o = contextAdapter.openView();
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
  
  public MainWindow openBne3DevPerspective() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openBne3DevPerspective", null);
    Object o = contextAdapter.openBne3DevPerspective();
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
  
  public MainWindow openBne3Perspective() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openBne3Perspective", null);
    Object o = contextAdapter.openBne3Perspective();
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
  
  public MainWindow resetPerspective() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "resetPerspective", null);
    Object o = contextAdapter.resetPerspective();
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
  
  public MainWindow openPerspective(final StringDT perspectiveId) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openPerspective", null, perspectiveId.getValue().toString());
    Object o = contextAdapter.openPerspective(perspectiveId.getValue());
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
  
  public MainWindow openPerspectiveByName(final StringDT perspectiveName) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openPerspectiveByName", null, perspectiveName.getValue().toString());
    Object o = contextAdapter.openPerspectiveByName(perspectiveName.getValue());
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
  
  public EthernetNavigator findEthernetNavigator() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "findEthernetNavigator", null);
    Object o = contextAdapter.findEthernetNavigator();
    EthernetNavigator nextActivity = null;
    if (o instanceof EthernetNavigator) {
      nextActivity = (EthernetNavigator)o;
    } else {
      EthernetNavigatorAdapter adapter = injector.getInstance(EthernetNavigatorAdapter.class);
      adapter.setContext(o);
      nextActivity = new EthernetNavigator(adapter);
    }
    return nextActivity;
  }
  
  public CodingEditor findNewCodingEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "findNewCodingEditor", null);
    Object o = contextAdapter.findNewCodingEditor();
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
  
  public Aenderungsantrag findNewAenderungsantragEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "findNewAenderungsantragEditor", null);
    Object o = contextAdapter.findNewAenderungsantragEditor();
    Aenderungsantrag nextActivity = null;
    if (o instanceof Aenderungsantrag) {
      nextActivity = (Aenderungsantrag)o;
    } else {
      AenderungsantragAdapter adapter = injector.getInstance(AenderungsantragAdapter.class);
      adapter.setContext(o);
      nextActivity = new Aenderungsantrag(adapter);
    }
    return nextActivity;
  }
  
  public BasisDatentypEditor findNewBasisdatentypEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "findNewBasisdatentypEditor", null);
    Object o = contextAdapter.findNewBasisdatentypEditor();
    BasisDatentypEditor nextActivity = null;
    if (o instanceof BasisDatentypEditor) {
      nextActivity = (BasisDatentypEditor)o;
    } else {
      BasisDatentypEditorAdapter adapter = injector.getInstance(BasisDatentypEditorAdapter.class);
      adapter.setContext(o);
      nextActivity = new BasisDatentypEditor(adapter);
    }
    return nextActivity;
  }
  
  public AufzaehlungstypenEditor findNewAufzaehlungstypEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "findNewAufzaehlungstypEditor", null);
    Object o = contextAdapter.findNewAufzaehlungstypEditor();
    AufzaehlungstypenEditor nextActivity = null;
    if (o instanceof AufzaehlungstypenEditor) {
      nextActivity = (AufzaehlungstypenEditor)o;
    } else {
      AufzaehlungstypenEditorAdapter adapter = injector.getInstance(AufzaehlungstypenEditorAdapter.class);
      adapter.setContext(o);
      nextActivity = new AufzaehlungstypenEditor(adapter);
    }
    return nextActivity;
  }
  
  public StrukturEditor findNewStrukturtypEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "findNewStrukturtypEditor", null);
    Object o = contextAdapter.findNewStrukturtypEditor();
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
  
  public UniontypEditor findNewUniontypEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "findNewUniontypEditor", null);
    Object o = contextAdapter.findNewUniontypEditor();
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
