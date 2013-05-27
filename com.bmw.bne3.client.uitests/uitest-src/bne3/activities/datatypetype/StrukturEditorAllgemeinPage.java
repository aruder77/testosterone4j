package bne3.activities.datatypetype;

import bne3.activities.datatypetype.StrukturEditor;
import bne3.activities.datatypetype.StrukturEditorAdapter;
import bne3.activities.datatypetype.StrukturEditorAllgemeinPageAdapter;
import bne3.datatypes.FormalAnnotationDT;
import bne3.datatypes.LanguageDT;
import bne3.datatypes.LongName;
import bne3.datatypes.ShortName;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.basictypes.BooleanDT;
import de.msg.xt.mdt.tdsl.basictypes.IntegerDT;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import de.msg.xt.mdt.tdsl.swtbot.Button;
import de.msg.xt.mdt.tdsl.swtbot.ComboBox;
import de.msg.xt.mdt.tdsl.swtbot.TableControl;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;

public class StrukturEditorAllgemeinPage extends AbstractActivity {
  private final static String ID = "bne3.activities.datatypetype.StrukturEditorAllgemeinPage";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private StrukturEditorAllgemeinPageAdapter contextAdapter;
  
  public static StrukturEditorAllgemeinPage find() {
    return new StrukturEditorAllgemeinPage(activityLocator.find(ID, StrukturEditorAllgemeinPageAdapter.class));
  }
  
  public StrukturEditorAllgemeinPage() {
    super();
  }
  
  public StrukturEditorAllgemeinPage(final StrukturEditorAllgemeinPageAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
  
  public TextControl getLongnameTextControl() {
    return this.contextAdapter.getTextControl("longname");
  }
  
  public TextControl getShortnameTextControl() {
    return this.contextAdapter.getTextControl("shortname");
  }
  
  public ComboBox getPackageSelectionComboComboBox() {
    return this.contextAdapter.getComboBox("typeSelectionCombo.combo");
  }
  
  public Button getPackageSelectionNavigationButton() {
    return this.contextAdapter.getButton("typeSelectionCombo.jumpButton");
  }
  
  public TableControl getTextuelleBeschreibungTableControl() {
    return this.contextAdapter.getTableControl("bne3.table.Descriptions");
  }
  
  public TableControl getFormaleAnnotationenTableControl() {
    return this.contextAdapter.getTableControl("components.table.FormalAnnotations");
  }
  
  public StrukturEditorAllgemeinPage longname_setText(final LongName str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "longname", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getLongnameTextControl().setText(str.getValue());
    return this;
  }
  
  public LongName longname_getText() {
    String value = getLongnameTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "longname", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.LongName(value, bne3.datatypes.LongNameEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT longname_isEnabled() {
    Boolean value = getLongnameTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "longname", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public StrukturEditorAllgemeinPage shortname_setText(final ShortName str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "shortname", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getShortnameTextControl().setText(str.getValue());
    return this;
  }
  
  public ShortName shortname_getText() {
    String value = getShortnameTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "shortname", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.ShortName(value, bne3.datatypes.ShortNameEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT shortname_isEnabled() {
    Boolean value = getShortnameTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "shortname", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public StrukturEditorAllgemeinPage packageSelectionCombo_setText(final StringDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "packageSelectionCombo", ComboBox.class.getName(), "setText", null, str.getValue().toString());
    getPackageSelectionComboComboBox().setText(str.getValue());
    return this;
  }
  
  public StringDT packageSelectionCombo_getText() {
    String value = getPackageSelectionComboComboBox().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "packageSelectionCombo", ComboBox.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT packageSelectionCombo_isEnabled() {
    Boolean value = getPackageSelectionComboComboBox().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "packageSelectionCombo", ComboBox.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public StrukturEditorAllgemeinPage packageSelectionNavigation_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "packageSelectionNavigation", Button.class.getName(), "push", null);
    getPackageSelectionNavigationButton().push();
    return this;
  }
  
  public BooleanDT packageSelectionNavigation_isEnabled() {
    Boolean value = getPackageSelectionNavigationButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "packageSelectionNavigation", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public StrukturEditorAllgemeinPage textuelleBeschreibung_invokeContextMenu(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "invokeContextMenu", null, contextMenuEntry.getValue().toString());
    getTextuelleBeschreibungTableControl().invokeContextMenu(contextMenuEntry.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage textuelleBeschreibung_invokeContextMenuOnRow(final IntegerDT row, final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "invokeContextMenuOnRow", null, row.getValue().toString(), contextMenuEntry.getValue().toString());
    getTextuelleBeschreibungTableControl().invokeContextMenuOnRow(row.getValue(), contextMenuEntry.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage textuelleBeschreibung_isContextMenuEnabled(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "isContextMenuEnabled", null, contextMenuEntry.getValue().toString());
    getTextuelleBeschreibungTableControl().isContextMenuEnabled(contextMenuEntry.getValue());
    return this;
  }
  
  public StringDT textuelleBeschreibung_getText(final IntegerDT row, final IntegerDT column) {
    String value = getTextuelleBeschreibungTableControl().getText(row.getValue(), column.getValue()); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "getText", value != null ? value.toString() : "null" , row.getValue().toString(), column.getValue().toString());
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public StringDT textuelleBeschreibung_getTextByName(final IntegerDT row, final StringDT columnName) {
    String value = getTextuelleBeschreibungTableControl().getTextByName(row.getValue(), columnName.getValue()); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "getTextByName", value != null ? value.toString() : "null" , row.getValue().toString(), columnName.getValue().toString());
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public StrukturEditorAllgemeinPage textuelleBeschreibung_setText(final IntegerDT row, final IntegerDT column, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "setText", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getTextuelleBeschreibungTableControl().setText(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage textuelleBeschreibung_setTextByName(final IntegerDT row, final StringDT columnName, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "setTextByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getTextuelleBeschreibungTableControl().setTextByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage textuelleBeschreibung_selectValue(final IntegerDT row, final IntegerDT column, final LanguageDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "selectValue", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getTextuelleBeschreibungTableControl().selectValue(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage textuelleBeschreibung_selectValueByName(final IntegerDT row, final StringDT columnName, final LanguageDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "selectValueByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getTextuelleBeschreibungTableControl().selectValueByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public IntegerDT textuelleBeschreibung_getRowCount() {
    Integer value = getTextuelleBeschreibungTableControl().getRowCount(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "getRowCount", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.IntegerDT(value, de.msg.xt.mdt.tdsl.basictypes.IntegerDTEquivalenceClass.getByValue(value));
  }
  
  public StrukturEditorAllgemeinPage textuelleBeschreibung_checkRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "checkRow", null, row.getValue().toString());
    getTextuelleBeschreibungTableControl().checkRow(row.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage textuelleBeschreibung_selectRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "selectRow", null, row.getValue().toString());
    getTextuelleBeschreibungTableControl().selectRow(row.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage formaleAnnotationen_invokeContextMenu(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "invokeContextMenu", null, contextMenuEntry.getValue().toString());
    getFormaleAnnotationenTableControl().invokeContextMenu(contextMenuEntry.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage formaleAnnotationen_invokeContextMenuOnRow(final IntegerDT row, final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "invokeContextMenuOnRow", null, row.getValue().toString(), contextMenuEntry.getValue().toString());
    getFormaleAnnotationenTableControl().invokeContextMenuOnRow(row.getValue(), contextMenuEntry.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage formaleAnnotationen_isContextMenuEnabled(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "isContextMenuEnabled", null, contextMenuEntry.getValue().toString());
    getFormaleAnnotationenTableControl().isContextMenuEnabled(contextMenuEntry.getValue());
    return this;
  }
  
  public StringDT formaleAnnotationen_getText(final IntegerDT row, final IntegerDT column) {
    String value = getFormaleAnnotationenTableControl().getText(row.getValue(), column.getValue()); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "getText", value != null ? value.toString() : "null" , row.getValue().toString(), column.getValue().toString());
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public StringDT formaleAnnotationen_getTextByName(final IntegerDT row, final StringDT columnName) {
    String value = getFormaleAnnotationenTableControl().getTextByName(row.getValue(), columnName.getValue()); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "getTextByName", value != null ? value.toString() : "null" , row.getValue().toString(), columnName.getValue().toString());
    return new de.msg.xt.mdt.tdsl.basictypes.StringDT(value, de.msg.xt.mdt.tdsl.basictypes.StringDTEquivalenceClass.getByValue(value));
  }
  
  public StrukturEditorAllgemeinPage formaleAnnotationen_setText(final IntegerDT row, final IntegerDT column, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "setText", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getFormaleAnnotationenTableControl().setText(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage formaleAnnotationen_setTextByName(final IntegerDT row, final StringDT columnName, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "setTextByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getFormaleAnnotationenTableControl().setTextByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage formaleAnnotationen_selectValue(final IntegerDT row, final IntegerDT column, final FormalAnnotationDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "selectValue", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getFormaleAnnotationenTableControl().selectValue(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage formaleAnnotationen_selectValueByName(final IntegerDT row, final StringDT columnName, final FormalAnnotationDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "selectValueByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getFormaleAnnotationenTableControl().selectValueByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public IntegerDT formaleAnnotationen_getRowCount() {
    Integer value = getFormaleAnnotationenTableControl().getRowCount(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "getRowCount", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.IntegerDT(value, de.msg.xt.mdt.tdsl.basictypes.IntegerDTEquivalenceClass.getByValue(value));
  }
  
  public StrukturEditorAllgemeinPage formaleAnnotationen_checkRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "checkRow", null, row.getValue().toString());
    getFormaleAnnotationenTableControl().checkRow(row.getValue());
    return this;
  }
  
  public StrukturEditorAllgemeinPage formaleAnnotationen_selectRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "selectRow", null, row.getValue().toString());
    getFormaleAnnotationenTableControl().selectRow(row.getValue());
    return this;
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
