package bne3.activities;

import bne3.activities.CodingEditor;
import bne3.activities.CodingEditorAdapter;
import bne3.activities.EthernetNavigatorAdapter;
import bne3.activities.MainWindow;
import bne3.activities.ViewActivity;
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
import bne3.activities.paket.PaketEditor;
import bne3.activities.paket.PaketEditorAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.basictypes.BooleanDT;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import de.msg.xt.mdt.tdsl.swtbot.Button;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;
import de.msg.xt.mdt.tdsl.swtbot.TreeControl;
import java.util.Stack;

public class EthernetNavigator extends ViewActivity {
  private final static String ID = "bne3.activities.EthernetNavigator";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private EthernetNavigatorAdapter contextAdapter;
  
  public static EthernetNavigator find() {
    return new EthernetNavigator(activityLocator.find(ID, EthernetNavigatorAdapter.class));
  }
  
  public EthernetNavigator() {
    super();
  }
  
  public EthernetNavigator(final EthernetNavigatorAdapter contextAdapter) {
    super(contextAdapter);
    this.contextAdapter = contextAdapter;
  }
  
  public TextControl getFilterTextTextControl() {
    return this.contextAdapter.getTextControl("filterText");
  }
  
  public Button getSearchButtonButton() {
    return this.contextAdapter.getButton("searchButton");
  }
  
  public Button getClearButtonButton() {
    return this.contextAdapter.getButton("clearButton");
  }
  
  public TreeControl getTreeTreeControl() {
    return this.contextAdapter.getTreeControl("ethernet.navigator.tree");
  }
  
  public EthernetNavigator filterText_setText(final StringDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "filterText", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getFilterTextTextControl().setText(str.getValue());
    return this;
  }
  
  public StringDT filterText_getText() {
    String value = getFilterTextTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "filterText", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT filterText_isEnabled() {
    Boolean value = getFilterTextTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "filterText", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public EthernetNavigator searchButton_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "searchButton", Button.class.getName(), "push", null);
    getSearchButtonButton().push();
    return this;
  }
  
  public BooleanDT searchButton_isEnabled() {
    Boolean value = getSearchButtonButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "searchButton", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public EthernetNavigator clearButton_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "clearButton", Button.class.getName(), "push", null);
    getClearButtonButton().push();
    return this;
  }
  
  public BooleanDT clearButton_isEnabled() {
    Boolean value = getClearButtonButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "clearButton", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public EthernetNavigator tree_selectNode(final StringDT nodePath) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "tree", TreeControl.class.getName(), "selectNode", null, nodePath.getValue().toString());
    getTreeTreeControl().selectNode(nodePath.getValue());
    return this;
  }
  
  public EthernetNavigator tree_doubleClickNode(final StringDT nodePath) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "tree", TreeControl.class.getName(), "doubleClickNode", null, nodePath.getValue().toString());
    getTreeTreeControl().doubleClickNode(nodePath.getValue());
    return this;
  }
  
  public BooleanDT tree_isEnabled() {
    Boolean value = getTreeTreeControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "tree", TreeControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public EthernetNavigator tree_invokeContextMenu(final StringDT nodePath, final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "tree", TreeControl.class.getName(), "invokeContextMenu", null, nodePath.getValue().toString(), contextMenuEntry.getValue().toString());
    getTreeTreeControl().invokeContextMenu(nodePath.getValue(), contextMenuEntry.getValue());
    return this;
  }
  
  public EthernetNavigator activateChangeRequest(final StringDT title) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "activateChangeRequest", null, title.getValue().toString());
    Object o = contextAdapter.activateChangeRequest(title.getValue());
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
  
  public CodingEditor openCoding(final StringDT title) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openCoding", null, title.getValue().toString());
    Object o = contextAdapter.openCoding(title.getValue());
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
  
  public Aenderungsantrag addChangeRequest() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "addChangeRequest", null);
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = this;
    StringDT nodePath1cb12da0 = new StringDT("\u00C4nderungsmanagement/\u00C4nderungsantrag", null);
    StringDT contextMenuEntry1cb12da0 = new StringDT("\u00C4nderungsantrag hinzuf\u00FCgen", null);
    ((EthernetNavigator)activity).tree_invokeContextMenu(nodePath1cb12da0, contextMenuEntry1cb12da0);
    stack.push(activity);
    activity = ((ViewActivity)activity).returnToMain();
    stack.push(activity);
    activity = ((MainWindow)activity).findNewAenderungsantragEditor();
    return (Aenderungsantrag)activity;
  }
  
  public Aenderungsantrag openChangeRequest(final StringDT title) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openChangeRequest", null, title.getValue().toString());
    Object o = contextAdapter.openChangeRequest(title.getValue());
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
  
  public BasisDatentypEditor addBasisDatentyp() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "addBasisDatentyp", null);
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = this;
    StringDT nodePath7e43c568 = new StringDT("Ethernet/Datentypen/Basisdatentypen", null);
    StringDT contextMenuEntry7e43c568 = new StringDT("Basisdatentyp hinzuf\u00FCgen", null);
    ((EthernetNavigator)activity).tree_invokeContextMenu(nodePath7e43c568, contextMenuEntry7e43c568);
    stack.push(activity);
    activity = ((ViewActivity)activity).returnToMain();
    stack.push(activity);
    activity = ((MainWindow)activity).findNewBasisdatentypEditor();
    return (BasisDatentypEditor)activity;
  }
  
  public BasisDatentypEditor openBasisDatentyp(final StringDT title) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openBasisDatentyp", null, title.getValue().toString());
    Object o = contextAdapter.openBasisDatentyp(title.getValue());
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
  
  public AufzaehlungstypenEditor addAufzaehlungstyp() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "addAufzaehlungstyp", null);
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = this;
    StringDT nodePath235f003e = new StringDT("Ethernet/Datentypen/Aufz\u00E4hlungen", null);
    StringDT contextMenuEntry235f003e = new StringDT("Aufz\u00E4hlung hinzuf\u00FCgen", null);
    ((EthernetNavigator)activity).tree_invokeContextMenu(nodePath235f003e, contextMenuEntry235f003e);
    stack.push(activity);
    activity = ((ViewActivity)activity).returnToMain();
    stack.push(activity);
    activity = ((MainWindow)activity).findNewAufzaehlungstypEditor();
    return (AufzaehlungstypenEditor)activity;
  }
  
  public AufzaehlungstypenEditor openAufzaehlungstyp(final StringDT title) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openAufzaehlungstyp", null, title.getValue().toString());
    Object o = contextAdapter.openAufzaehlungstyp(title.getValue());
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
  
  public StrukturEditor addStrukturtyp() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "addStrukturtyp", null);
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = this;
    StringDT nodePath36c8d2bf = new StringDT("Ethernet/Datentypen/Strukturen", null);
    StringDT contextMenuEntry36c8d2bf = new StringDT("Struktur hinzuf\u00FCgen", null);
    ((EthernetNavigator)activity).tree_invokeContextMenu(nodePath36c8d2bf, contextMenuEntry36c8d2bf);
    stack.push(activity);
    activity = ((ViewActivity)activity).returnToMain();
    stack.push(activity);
    activity = ((MainWindow)activity).findNewStrukturtypEditor();
    return (StrukturEditor)activity;
  }
  
  public StrukturEditor openStrukturtyp(final StringDT title) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openStrukturtyp", null, title.getValue().toString());
    Object o = contextAdapter.openStrukturtyp(title.getValue());
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
  
  public UniontypEditor addUniontyp() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "addUniontyp", null);
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = this;
    StringDT nodePath33f16f93 = new StringDT("Ethernet/Datentypen/Unions", null);
    StringDT contextMenuEntry33f16f93 = new StringDT("Union hinzuf\u00FCgen", null);
    ((EthernetNavigator)activity).tree_invokeContextMenu(nodePath33f16f93, contextMenuEntry33f16f93);
    stack.push(activity);
    activity = ((ViewActivity)activity).returnToMain();
    stack.push(activity);
    activity = ((MainWindow)activity).findNewUniontypEditor();
    return (UniontypEditor)activity;
  }
  
  public UniontypEditor openUniontyp(final StringDT title) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openUniontyp", null, title.getValue().toString());
    Object o = contextAdapter.openUniontyp(title.getValue());
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
  
  public UniontypEditor addPaket() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "addPaket", null);
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = this;
    StringDT nodePath34292e47 = new StringDT("Ethernet/Datentypen/Unions", null);
    StringDT contextMenuEntry34292e47 = new StringDT("Union hinzuf\u00FCgen", null);
    ((EthernetNavigator)activity).tree_invokeContextMenu(nodePath34292e47, contextMenuEntry34292e47);
    stack.push(activity);
    activity = ((ViewActivity)activity).returnToMain();
    stack.push(activity);
    activity = ((MainWindow)activity).findNewUniontypEditor();
    return (UniontypEditor)activity;
  }
  
  public PaketEditor openPaket(final StringDT title) {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "openPaket", null, title.getValue().toString());
    Object o = contextAdapter.openPaket(title.getValue());
    PaketEditor nextActivity = null;
    if (o instanceof PaketEditor) {
      nextActivity = (PaketEditor)o;
    } else {
      PaketEditorAdapter adapter = injector.getInstance(PaketEditorAdapter.class);
      adapter.setContext(o);
      nextActivity = new PaketEditor(adapter);
    }
    return nextActivity;
  }
}
