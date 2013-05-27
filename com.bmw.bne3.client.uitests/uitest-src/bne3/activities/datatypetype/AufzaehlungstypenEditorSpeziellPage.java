package bne3.activities.datatypetype;

import bne3.activities.datatypetype.AufzaehlungstypenCodingTypeSearch;
import bne3.activities.datatypetype.AufzaehlungstypenEditor;
import bne3.activities.datatypetype.AufzaehlungstypenEditorAdapter;
import bne3.activities.datatypetype.AufzaehlungstypenEditorSpeziellPageAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.basictypes.BooleanDT;
import de.msg.xt.mdt.tdsl.basictypes.IntegerDT;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import de.msg.xt.mdt.tdsl.swtbot.Button;
import de.msg.xt.mdt.tdsl.swtbot.TableControl;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;

public class AufzaehlungstypenEditorSpeziellPage extends AbstractActivity {
  private final static String ID = "Speziell";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private AufzaehlungstypenEditorSpeziellPageAdapter contextAdapter;
  
  public static AufzaehlungstypenEditorSpeziellPage find() {
    return new AufzaehlungstypenEditorSpeziellPage(activityLocator.find(ID, AufzaehlungstypenEditorSpeziellPageAdapter.class));
  }
  
  public AufzaehlungstypenEditorSpeziellPage() {
    super();
  }
  
  public AufzaehlungstypenEditorSpeziellPage(final AufzaehlungstypenEditorSpeziellPageAdapter contextAdapter) {
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
  
  public TableControl getEnumElementsTableControl() {
    return this.contextAdapter.getTableControl("bne3.table.EnumElementType");
  }
  
  public AufzaehlungstypenEditorSpeziellPage codingSelectionText_setText(final StringDT str) {
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
  
  public AufzaehlungstypenCodingTypeSearch codingSelectionSearchButton_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionSearchButton", Button.class.getName(), "push", null);
    getCodingSelectionSearchButtonButton().push();
    return AufzaehlungstypenCodingTypeSearch.find();
  }
  
  public BooleanDT codingSelectionSearchButton_isEnabled() {
    Boolean value = getCodingSelectionSearchButtonButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionSearchButton", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public AufzaehlungstypenEditorSpeziellPage codingSelectionNavigation_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionNavigation", Button.class.getName(), "push", null);
    getCodingSelectionNavigationButton().push();
    return this;
  }
  
  public BooleanDT codingSelectionNavigation_isEnabled() {
    Boolean value = getCodingSelectionNavigationButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "codingSelectionNavigation", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public AufzaehlungstypenEditorSpeziellPage enumElements_invokeContextMenu(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "invokeContextMenu", null, contextMenuEntry.getValue().toString());
    getEnumElementsTableControl().invokeContextMenu(contextMenuEntry.getValue());
    return this;
  }
  
  public AufzaehlungstypenEditorSpeziellPage enumElements_invokeContextMenuOnRow(final IntegerDT row, final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "invokeContextMenuOnRow", null, row.getValue().toString(), contextMenuEntry.getValue().toString());
    getEnumElementsTableControl().invokeContextMenuOnRow(row.getValue(), contextMenuEntry.getValue());
    return this;
  }
  
  public AufzaehlungstypenEditorSpeziellPage enumElements_isContextMenuEnabled(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "isContextMenuEnabled", null, contextMenuEntry.getValue().toString());
    getEnumElementsTableControl().isContextMenuEnabled(contextMenuEntry.getValue());
    return this;
  }
  
  public StringDT enumElements_getText(final IntegerDT row, final IntegerDT column) {
    String value = getEnumElementsTableControl().getText(row.getValue(), column.getValue()); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "getText", value != null ? value.toString() : "null" , row.getValue().toString(), column.getValue().toString());
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public StringDT enumElements_getTextByName(final IntegerDT row, final StringDT columnName) {
    String value = getEnumElementsTableControl().getTextByName(row.getValue(), columnName.getValue()); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "getTextByName", value != null ? value.toString() : "null" , row.getValue().toString(), columnName.getValue().toString());
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public AufzaehlungstypenEditorSpeziellPage enumElements_setText(final IntegerDT row, final IntegerDT column, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "setText", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getEnumElementsTableControl().setText(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public AufzaehlungstypenEditorSpeziellPage enumElements_setTextByName(final IntegerDT row, final StringDT columnName, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "setTextByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getEnumElementsTableControl().setTextByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public AufzaehlungstypenEditorSpeziellPage enumElements_selectValue(final IntegerDT row, final IntegerDT column, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "selectValue", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getEnumElementsTableControl().selectValue(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public AufzaehlungstypenEditorSpeziellPage enumElements_selectValueByName(final IntegerDT row, final StringDT columnName, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "selectValueByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getEnumElementsTableControl().selectValueByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public IntegerDT enumElements_getRowCount() {
    Integer value = getEnumElementsTableControl().getRowCount(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "getRowCount", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.IntegerDT(value, de.msg.xt.mdt.tdsl.basictypes.IntegerDTEquivalenceClass.getByValue(value));
  }
  
  public AufzaehlungstypenEditorSpeziellPage enumElements_checkRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "checkRow", null, row.getValue().toString());
    getEnumElementsTableControl().checkRow(row.getValue());
    return this;
  }
  
  public AufzaehlungstypenEditorSpeziellPage enumElements_selectRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "enumElements", TableControl.class.getName(), "selectRow", null, row.getValue().toString());
    getEnumElementsTableControl().selectRow(row.getValue());
    return this;
  }
  
  public AufzaehlungstypenEditor returnToEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "returnToEditor", null);
    Object o = contextAdapter.returnToEditor();
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
}
