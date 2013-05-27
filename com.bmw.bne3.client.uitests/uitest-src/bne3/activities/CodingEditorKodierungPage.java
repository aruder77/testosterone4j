package bne3.activities;

import bne3.activities.CodingEditor;
import bne3.activities.CodingEditorAdapter;
import bne3.activities.CodingEditorKodierungPageAdapter;
import bne3.datatypes.BasisdatentypDT;
import bne3.datatypes.DoubleStringDT;
import bne3.datatypes.EncodingDT;
import bne3.datatypes.IntegerStringDT;
import bne3.datatypes.IntervallgrenzeDT;
import bne3.datatypes.KategorieDT;
import bne3.datatypes.TerminationDT;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.basictypes.BooleanDT;
import de.msg.xt.mdt.tdsl.swtbot.ComboBox;
import de.msg.xt.mdt.tdsl.swtbot.RadioButton;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;

public class CodingEditorKodierungPage extends AbstractActivity {
  private final static String ID = "bne3.activities.CodingEditorKodierungPage";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private CodingEditorKodierungPageAdapter contextAdapter;
  
  public static CodingEditorKodierungPage find() {
    return new CodingEditorKodierungPage(activityLocator.find(ID, CodingEditorKodierungPageAdapter.class));
  }
  
  public CodingEditorKodierungPage() {
    super();
  }
  
  public CodingEditorKodierungPage(final CodingEditorKodierungPageAdapter contextAdapter) {
    this();
    this.contextAdapter = contextAdapter;
  }
  
  public ComboBox getPhysBasisDatenTypComboBox() {
    return this.contextAdapter.getComboBox("coding.physicaltype.basedatatype");
  }
  
  public TextControl getPhysMinimumTextControl() {
    return this.contextAdapter.getTextControl("coding.physicaltype.minimum");
  }
  
  public TextControl getPhysAufloesungTextControl() {
    return this.contextAdapter.getTextControl("coding.physicaltype.precision");
  }
  
  public TextControl getPhysMaximumTextControl() {
    return this.contextAdapter.getTextControl("coding.physicaltype.maximum");
  }
  
  public ComboBox getPhysMinimumIntervallgrenzeComboBox() {
    return this.contextAdapter.getComboBox("coding.physicaltype.intervaltype");
  }
  
  public ComboBox getPhysMaximumIntervallgrenzeComboBox() {
    return this.contextAdapter.getComboBox("coding.physicaltype.maximum.intervaltype");
  }
  
  public ComboBox getInternalBasisDatenTypComboBox() {
    return this.contextAdapter.getComboBox("coding.codedtype.basedatatype");
  }
  
  public ComboBox getInternalKategorieComboBox() {
    return this.contextAdapter.getComboBox("coding.codedtype.category");
  }
  
  public ComboBox getInternalEncodingComboBox() {
    return this.contextAdapter.getComboBox("coding.codedtype.encoding");
  }
  
  public ComboBox getInternalTerminationComboBox() {
    return this.contextAdapter.getComboBox("coding.codedtype.termination");
  }
  
  public RadioButton getFixedBitLengthButtonRadioButton() {
    return this.contextAdapter.getRadioButton("data.codedTypeFixedBitLength");
  }
  
  public TextControl getBitLengthTextControl() {
    return this.contextAdapter.getTextControl("data.codedTypeBitLength");
  }
  
  public RadioButton getVariableBitLengthButtonRadioButton() {
    return this.contextAdapter.getRadioButton("data.codedTypeVariableBitLength");
  }
  
  public CodingEditorKodierungPage physBasisDatenTyp_setText(final BasisdatentypDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physBasisDatenTyp", ComboBox.class.getName(), "setText", null, str.getValue().toString());
    getPhysBasisDatenTypComboBox().setText(str.getValue());
    return this;
  }
  
  public BasisdatentypDT physBasisDatenTyp_getText() {
    String value = getPhysBasisDatenTypComboBox().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physBasisDatenTyp", ComboBox.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.BasisdatentypDT(value, bne3.datatypes.BasisdatentypDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT physBasisDatenTyp_isEnabled() {
    Boolean value = getPhysBasisDatenTypComboBox().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physBasisDatenTyp", ComboBox.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage physMinimum_setText(final DoubleStringDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMinimum", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getPhysMinimumTextControl().setText(str.getValue());
    return this;
  }
  
  public DoubleStringDT physMinimum_getText() {
    String value = getPhysMinimumTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMinimum", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.DoubleStringDT(value, bne3.datatypes.DoubleStringDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT physMinimum_isEnabled() {
    Boolean value = getPhysMinimumTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMinimum", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage physAufloesung_setText(final DoubleStringDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physAufloesung", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getPhysAufloesungTextControl().setText(str.getValue());
    return this;
  }
  
  public DoubleStringDT physAufloesung_getText() {
    String value = getPhysAufloesungTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physAufloesung", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.DoubleStringDT(value, bne3.datatypes.DoubleStringDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT physAufloesung_isEnabled() {
    Boolean value = getPhysAufloesungTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physAufloesung", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage physMaximum_setText(final DoubleStringDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMaximum", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getPhysMaximumTextControl().setText(str.getValue());
    return this;
  }
  
  public DoubleStringDT physMaximum_getText() {
    String value = getPhysMaximumTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMaximum", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.DoubleStringDT(value, bne3.datatypes.DoubleStringDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT physMaximum_isEnabled() {
    Boolean value = getPhysMaximumTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMaximum", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage physMinimumIntervallgrenze_setText(final IntervallgrenzeDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMinimumIntervallgrenze", ComboBox.class.getName(), "setText", null, str.getValue().toString());
    getPhysMinimumIntervallgrenzeComboBox().setText(str.getValue());
    return this;
  }
  
  public IntervallgrenzeDT physMinimumIntervallgrenze_getText() {
    String value = getPhysMinimumIntervallgrenzeComboBox().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMinimumIntervallgrenze", ComboBox.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.IntervallgrenzeDT(value, bne3.datatypes.IntervallgrenzeDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT physMinimumIntervallgrenze_isEnabled() {
    Boolean value = getPhysMinimumIntervallgrenzeComboBox().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMinimumIntervallgrenze", ComboBox.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage physMaximumIntervallgrenze_setText(final IntervallgrenzeDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMaximumIntervallgrenze", ComboBox.class.getName(), "setText", null, str.getValue().toString());
    getPhysMaximumIntervallgrenzeComboBox().setText(str.getValue());
    return this;
  }
  
  public IntervallgrenzeDT physMaximumIntervallgrenze_getText() {
    String value = getPhysMaximumIntervallgrenzeComboBox().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMaximumIntervallgrenze", ComboBox.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.IntervallgrenzeDT(value, bne3.datatypes.IntervallgrenzeDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT physMaximumIntervallgrenze_isEnabled() {
    Boolean value = getPhysMaximumIntervallgrenzeComboBox().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "physMaximumIntervallgrenze", ComboBox.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage internalBasisDatenTyp_setText(final BasisdatentypDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalBasisDatenTyp", ComboBox.class.getName(), "setText", null, str.getValue().toString());
    getInternalBasisDatenTypComboBox().setText(str.getValue());
    return this;
  }
  
  public BasisdatentypDT internalBasisDatenTyp_getText() {
    String value = getInternalBasisDatenTypComboBox().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalBasisDatenTyp", ComboBox.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.BasisdatentypDT(value, bne3.datatypes.BasisdatentypDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT internalBasisDatenTyp_isEnabled() {
    Boolean value = getInternalBasisDatenTypComboBox().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalBasisDatenTyp", ComboBox.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage internalKategorie_setText(final KategorieDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalKategorie", ComboBox.class.getName(), "setText", null, str.getValue().toString());
    getInternalKategorieComboBox().setText(str.getValue());
    return this;
  }
  
  public KategorieDT internalKategorie_getText() {
    String value = getInternalKategorieComboBox().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalKategorie", ComboBox.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.KategorieDT(value, bne3.datatypes.KategorieDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT internalKategorie_isEnabled() {
    Boolean value = getInternalKategorieComboBox().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalKategorie", ComboBox.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage internalEncoding_setText(final EncodingDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalEncoding", ComboBox.class.getName(), "setText", null, str.getValue().toString());
    getInternalEncodingComboBox().setText(str.getValue());
    return this;
  }
  
  public EncodingDT internalEncoding_getText() {
    String value = getInternalEncodingComboBox().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalEncoding", ComboBox.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.EncodingDT(value, bne3.datatypes.EncodingDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT internalEncoding_isEnabled() {
    Boolean value = getInternalEncodingComboBox().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalEncoding", ComboBox.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage internalTermination_setText(final TerminationDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalTermination", ComboBox.class.getName(), "setText", null, str.getValue().toString());
    getInternalTerminationComboBox().setText(str.getValue());
    return this;
  }
  
  public TerminationDT internalTermination_getText() {
    String value = getInternalTerminationComboBox().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalTermination", ComboBox.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.TerminationDT(value, bne3.datatypes.TerminationDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT internalTermination_isEnabled() {
    Boolean value = getInternalTerminationComboBox().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "internalTermination", ComboBox.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage fixedBitLengthButton_click() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "fixedBitLengthButton", RadioButton.class.getName(), "click", null);
    getFixedBitLengthButtonRadioButton().click();
    return this;
  }
  
  public BooleanDT fixedBitLengthButton_isSelected() {
    Boolean value = getFixedBitLengthButtonRadioButton().isSelected(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "fixedBitLengthButton", RadioButton.class.getName(), "isSelected", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT fixedBitLengthButton_isEnabled() {
    Boolean value = getFixedBitLengthButtonRadioButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "fixedBitLengthButton", RadioButton.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage bitLength_setText(final IntegerStringDT str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "bitLength", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getBitLengthTextControl().setText(str.getValue());
    return this;
  }
  
  public IntegerStringDT bitLength_getText() {
    String value = getBitLengthTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "bitLength", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.IntegerStringDT(value, bne3.datatypes.IntegerStringDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT bitLength_isEnabled() {
    Boolean value = getBitLengthTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "bitLength", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditorKodierungPage variableBitLengthButton_click() {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "variableBitLengthButton", RadioButton.class.getName(), "click", null);
    getVariableBitLengthButtonRadioButton().click();
    return this;
  }
  
  public BooleanDT variableBitLengthButton_isSelected() {
    Boolean value = getVariableBitLengthButtonRadioButton().isSelected(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "variableBitLengthButton", RadioButton.class.getName(), "isSelected", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT variableBitLengthButton_isEnabled() {
    Boolean value = getVariableBitLengthButtonRadioButton().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "variableBitLengthButton", RadioButton.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
  
  public CodingEditor returnToEditor() {
    this.protocol.appendActivityOperationCall(this.getClass().getName(), "returnToEditor", null);
    Object o = contextAdapter.returnToEditor();
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
}
