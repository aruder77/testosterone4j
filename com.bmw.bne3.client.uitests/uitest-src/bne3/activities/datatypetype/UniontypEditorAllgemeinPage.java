package bne3.activities.datatypetype;

import bne3.activities.datatypetype.UniontypEditor;
import bne3.activities.datatypetype.UniontypEditorAdapter;
import bne3.activities.datatypetype.UniontypEditorAllgemeinPageAdapter;
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

public class UniontypEditorAllgemeinPage extends AbstractActivity {
  private final static String ID = "bne3.activities.datatypetype.UniontypEditorAllgemeinPage";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private UniontypEditorAllgemeinPageAdapter contextAdapter;
  
  public static UniontypEditorAllgemeinPage find() {
    return new UniontypEditorAllgemeinPage(activityLocator.find(ID, UniontypEditorAllgemeinPageAdapter.class));
  }
  
  public UniontypEditorAllgemeinPage() {
    super();
  }
  
  public UniontypEditorAllgemeinPage(final UniontypEditorAllgemeinPageAdapter contextAdapter) {
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
  
  public UniontypEditorAllgemeinPage longname_setText(final LongName str) {
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
  
  public UniontypEditorAllgemeinPage shortname_setText(final ShortName str) {
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
  
  public UniontypEditorAllgemeinPage packageSelectionCombo_setText(final StringDT str) {
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
  
  public UniontypEditorAllgemeinPage packageSelectionNavigation_push() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "packageSelectionNavigation", Button.class.getName(), "push", null);
    getPackageSelectionNavigationButton().push();
    return this;
  }
  
  public BooleanDT packageSelectionNavigation_isEnabled() {
    Boolean value = getPackageSelectionNavigationButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "packageSelectionNavigation", Button.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public UniontypEditorAllgemeinPage textuelleBeschreibung_invokeContextMenu(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "invokeContextMenu", null, contextMenuEntry.getValue().toString());
    getTextuelleBeschreibungTableControl().invokeContextMenu(contextMenuEntry.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage textuelleBeschreibung_invokeContextMenuOnRow(final IntegerDT row, final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "invokeContextMenuOnRow", null, row.getValue().toString(), contextMenuEntry.getValue().toString());
    getTextuelleBeschreibungTableControl().invokeContextMenuOnRow(row.getValue(), contextMenuEntry.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage textuelleBeschreibung_isContextMenuEnabled(final StringDT contextMenuEntry) {
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
  
  public UniontypEditorAllgemeinPage textuelleBeschreibung_setText(final IntegerDT row, final IntegerDT column, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "setText", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getTextuelleBeschreibungTableControl().setText(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage textuelleBeschreibung_setTextByName(final IntegerDT row, final StringDT columnName, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "setTextByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getTextuelleBeschreibungTableControl().setTextByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage textuelleBeschreibung_selectValue(final IntegerDT row, final IntegerDT column, final LanguageDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "selectValue", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getTextuelleBeschreibungTableControl().selectValue(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage textuelleBeschreibung_selectValueByName(final IntegerDT row, final StringDT columnName, final LanguageDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "selectValueByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getTextuelleBeschreibungTableControl().selectValueByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public IntegerDT textuelleBeschreibung_getRowCount() {
    Integer value = getTextuelleBeschreibungTableControl().getRowCount(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "getRowCount", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.IntegerDT(value, de.msg.xt.mdt.tdsl.basictypes.IntegerDTEquivalenceClass.getByValue(value));
  }
  
  public UniontypEditorAllgemeinPage textuelleBeschreibung_checkRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "checkRow", null, row.getValue().toString());
    getTextuelleBeschreibungTableControl().checkRow(row.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage textuelleBeschreibung_selectRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "textuelleBeschreibung", TableControl.class.getName(), "selectRow", null, row.getValue().toString());
    getTextuelleBeschreibungTableControl().selectRow(row.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage formaleAnnotationen_invokeContextMenu(final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "invokeContextMenu", null, contextMenuEntry.getValue().toString());
    getFormaleAnnotationenTableControl().invokeContextMenu(contextMenuEntry.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage formaleAnnotationen_invokeContextMenuOnRow(final IntegerDT row, final StringDT contextMenuEntry) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "invokeContextMenuOnRow", null, row.getValue().toString(), contextMenuEntry.getValue().toString());
    getFormaleAnnotationenTableControl().invokeContextMenuOnRow(row.getValue(), contextMenuEntry.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage formaleAnnotationen_isContextMenuEnabled(final StringDT contextMenuEntry) {
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
  
  public UniontypEditorAllgemeinPage formaleAnnotationen_setText(final IntegerDT row, final IntegerDT column, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "setText", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getFormaleAnnotationenTableControl().setText(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage formaleAnnotationen_setTextByName(final IntegerDT row, final StringDT columnName, final StringDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "setTextByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getFormaleAnnotationenTableControl().setTextByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage formaleAnnotationen_selectValue(final IntegerDT row, final IntegerDT column, final FormalAnnotationDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "selectValue", null, row.getValue().toString(), column.getValue().toString(), textValue.getValue().toString());
    getFormaleAnnotationenTableControl().selectValue(row.getValue(), column.getValue(), textValue.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage formaleAnnotationen_selectValueByName(final IntegerDT row, final StringDT columnName, final FormalAnnotationDT textValue) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "selectValueByName", null, row.getValue().toString(), columnName.getValue().toString(), textValue.getValue().toString());
    getFormaleAnnotationenTableControl().selectValueByName(row.getValue(), columnName.getValue(), textValue.getValue());
    return this;
  }
  
  public IntegerDT formaleAnnotationen_getRowCount() {
    Integer value = getFormaleAnnotationenTableControl().getRowCount(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "getRowCount", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.IntegerDT(value, de.msg.xt.mdt.tdsl.basictypes.IntegerDTEquivalenceClass.getByValue(value));
  }
  
  public UniontypEditorAllgemeinPage formaleAnnotationen_checkRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "checkRow", null, row.getValue().toString());
    getFormaleAnnotationenTableControl().checkRow(row.getValue());
    return this;
  }
  
  public UniontypEditorAllgemeinPage formaleAnnotationen_selectRow(final IntegerDT row) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "formaleAnnotationen", TableControl.class.getName(), "selectRow", null, row.getValue().toString());
    getFormaleAnnotationenTableControl().selectRow(row.getValue());
    return this;
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
