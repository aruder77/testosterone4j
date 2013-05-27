package bne3.activities.datatypetype;

import bne3.activities.DialogActivity;
import bne3.activities.datatypetype.BasisDatentypEditor;
import bne3.activities.datatypetype.BasisDatentypSpeziellPage;
import bne3.activities.datatypetype.CodingTypeSearchAdapter;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.basictypes.BooleanDT;
import de.msg.xt.mdt.tdsl.basictypes.IntegerDT;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import de.msg.xt.mdt.tdsl.swtbot.Button;
import de.msg.xt.mdt.tdsl.swtbot.TableControl;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;

public class CodingTypeSearch extends DialogActivity {
  private final static String ID = "bne3.activities.datatypetype.CodingTypeSearch";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private CodingTypeSearchAdapter contextAdapter;
  
  public static CodingTypeSearch find() {
    return new CodingTypeSearch(activityLocator.find(ID, CodingTypeSearchAdapter.class));
  }
  
  public CodingTypeSearch() {
    super();
  }
  
  public CodingTypeSearch(final CodingTypeSearchAdapter contextAdapter) {
    super(contextAdapter);
    this.contextAdapter = contextAdapter;
  }
  
  public TextControl getSearchTextTextControl() {
    return this.contextAdapter.getTextControl("searchText");
  }
  
  public Button getSearchButtonButton() {
    return this.contextAdapter.getButton("searchButton");
  }
  
  public TableControl getElementsTableControl() {
    return this.contextAdapter.getTableControl("elementsTable");
  }
  
  public Button getCancelButtonButton() {
    return this.contextAdapter.getButton("cancel");
  }
  
  public Button getOkButtonButton() {
    return this.contextAdapter.getButton("ok");
  }
  
  public CodingTypeSearch searchText_setText(final StringDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "searchText", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getSearchTextTextControl().setText(str.getValue());
    return this;
  }
  
  public StringDT searchText_getText() {
    String value = getSearchTextTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "searchText", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT searchText_isEnabled() {
    Boolean value = getSearchTextTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "searchText", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingTypeSearch searchButton_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "searchButton", Button.class.getName(), "push", null);
    getSearchButtonButton().push();
    return this;
  }
  
  public BooleanDT searchButton_isEnabled() {
    Boolean value = getSearchButtonButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "searchButton", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingTypeSearch elements_invokeContextMenu(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "invokeContextMenu", null, contextMenuEntry.getValue().toString());
    getElementsTableControl().invokeContextMenu(contextMenuEntry.getValue());
    return this;
  }
  
  public CodingTypeSearch elements_invokeContextMenuOnRow(final IntegerDT row, final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "invokeContextMenuOnRow", null, row.getValue().toString(), contextMenuEntry.getValue().toString());
    getElementsTableControl().invokeContextMenuOnRow(row.getValue(), contextMenuEntry.getValue());
    return this;
  }
  
  public CodingTypeSearch elements_isContextMenuEnabled(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "isContextMenuEnabled", null, contextMenuEntry.getValue().toString());
    getElementsTableControl().isContextMenuEnabled(contextMenuEntry.getValue());
    return this;
  }
  
  public StringDT elements_getText(final IntegerDT row, final IntegerDT column) {
    String value = getElementsTableControl().getText(row.getValue(), column.getValue()); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "getText", value != null ? value.toString() : "null" , row.getValue().toString(), column.getValue().toString());
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public StringDT elements_getTextByName(final IntegerDT row, final StringDT columnName) {
    String value = getElementsTableControl().getTextByName(row.getValue(), columnName.getValue()); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "getTextByName", value != null ? value.toString() : "null" , row.getValue().toString(), columnName.getValue().toString());
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public CodingTypeSearch elements_setText(final IntegerDT row, final IntegerDT column, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "setText", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getElementsTableControl().setText(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public CodingTypeSearch elements_setTextByName(final IntegerDT row, final StringDT columnName, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "setTextByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getElementsTableControl().setTextByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public CodingTypeSearch elements_selectValue(final IntegerDT row, final IntegerDT column, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "selectValue", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getElementsTableControl().selectValue(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public CodingTypeSearch elements_selectValueByName(final IntegerDT row, final StringDT columnName, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "selectValueByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getElementsTableControl().selectValueByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public IntegerDT elements_getRowCount() {
    Integer value = getElementsTableControl().getRowCount(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "getRowCount", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.IntegerDT(value, de.msg.xt.mdt.tdsl.basictypes.IntegerDTEquivalenceClass.getByValue(value));
  }
  
  public CodingTypeSearch elements_checkRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "checkRow", null, row.getValue().toString());
    getElementsTableControl().checkRow(row.getValue());
    return this;
  }
  
  public CodingTypeSearch elements_selectRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "elements", TableControl.class.getName(), "selectRow", null, row.getValue().toString());
    getElementsTableControl().selectRow(row.getValue());
    return this;
  }
  
  public BasisDatentypSpeziellPage cancelButton_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "cancelButton", Button.class.getName(), "push", null);
    getCancelButtonButton().push();
    return BasisDatentypSpeziellPage.find();
  }
  
  public BooleanDT cancelButton_isEnabled() {
    Boolean value = getCancelButtonButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "cancelButton", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public BasisDatentypEditor okButton_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "okButton", Button.class.getName(), "push", null);
    getOkButtonButton().push();
    return BasisDatentypEditor.find();
  }
  
  public BooleanDT okButton_isEnabled() {
    Boolean value = getOkButtonButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "okButton", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
}
