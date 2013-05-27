package bne3.activities.datatypetype;

import bne3.activities.datatypetype.BasisDatentypEditor;
import bne3.activities.datatypetype.BasisDatentypEditorAdapter;
import bne3.activities.datatypetype.BasisDatentypSpeziellPageAdapter;
import bne3.activities.datatypetype.CodingTypeSearch;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.basictypes.BooleanDT;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import de.msg.xt.mdt.tdsl.swtbot.Button;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;

public class BasisDatentypSpeziellPage extends AbstractActivity {
  private final static String ID = "Speziell";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private BasisDatentypSpeziellPageAdapter contextAdapter;
  
  public static BasisDatentypSpeziellPage find() {
    return new BasisDatentypSpeziellPage(activityLocator.find(ID, BasisDatentypSpeziellPageAdapter.class));
  }
  
  public BasisDatentypSpeziellPage() {
    super();
  }
  
  public BasisDatentypSpeziellPage(final BasisDatentypSpeziellPageAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
  
  public TextControl getCodingSelectionTextTextControl() {
    return this.contextAdapter.getTextControl("typeSelectionField.text");
  }
  
  public Button getCodingSelectionSearchButtonButton() {
    return this.contextAdapter.getButton("typeSelectionField.searchButton");
  }
  
  public Button getCodingSelectionNavigationButton() {
    return this.contextAdapter.getButton("typeSelectionField.jumpButton");
  }
  
  public BasisDatentypSpeziellPage codingSelectionText_setText(final StringDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionText", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getCodingSelectionTextTextControl().setText(str.getValue());
    return this;
  }
  
  public StringDT codingSelectionText_getText() {
    String value = getCodingSelectionTextTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionText", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT codingSelectionText_isEnabled() {
    Boolean value = getCodingSelectionTextTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionText", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingTypeSearch codingSelectionSearchButton_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionSearchButton", Button.class.getName(), "push", null);
    getCodingSelectionSearchButtonButton().push();
    return CodingTypeSearch.find();
  }
  
  public BooleanDT codingSelectionSearchButton_isEnabled() {
    Boolean value = getCodingSelectionSearchButtonButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionSearchButton", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public BasisDatentypSpeziellPage codingSelectionNavigation_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionNavigation", Button.class.getName(), "push", null);
    getCodingSelectionNavigationButton().push();
    return this;
  }
  
  public BooleanDT codingSelectionNavigation_isEnabled() {
    Boolean value = getCodingSelectionNavigationButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionNavigation", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public BasisDatentypEditor returnToEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "returnToEditor", null);
    Object o = contextAdapter.returnToEditor();
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
}
