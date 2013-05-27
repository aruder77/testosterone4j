package bne3.usecases;

import bne3.activities.AllgemeinPage;
import bne3.activities.CodingEditor;
import bne3.activities.CodingEditorKodierungPage;
import bne3.activities.EditorActivity;
import bne3.activities.EthernetNavigator;
import bne3.activities.MainWindow;
import bne3.activities.ViewActivity;
import bne3.datatypes.BasisdatentypDT;
import bne3.datatypes.ChangeRequestTitle;
import bne3.datatypes.DoubleStringDT;
import bne3.datatypes.EncodingDT;
import bne3.datatypes.IntegerStringDT;
import bne3.datatypes.IntervallgrenzeDT;
import bne3.datatypes.KategorieDT;
import bne3.datatypes.LanguageDT;
import bne3.datatypes.LongName;
import bne3.datatypes.ShortName;
import bne3.datatypes.TerminationDT;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.BaseUseCase;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.tdsl.basictypes.BooleanDT;
import de.msg.xt.mdt.tdsl.basictypes.IntegerDT;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import java.util.Stack;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import junit.framework.Assert;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@XmlRootElement
public class CreateCoding extends BaseUseCase implements Runnable {
  @XmlElement
  private ShortName shortName;
  
  public CreateCoding() {
  }
  
  public CreateCoding(final Generator generator) {
    this();
    this.generator = generator;
    this.shortName = this.getOrGenerateValue(bne3.datatypes.ShortName.class, "bne3.usecases.CreateCoding.shortName");
  }
  
  public CreateCoding(final ShortName shortName) {
    this();
    this.shortName = shortName;
  }
  
  public void setShortName(final ShortName shortName) {
    this.shortName = shortName;
  }
  
  @Override
  public void run() {
    execute(MainWindow.find());
  }
  
  public void execute(final MainWindow initialActivity) {
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = initialActivity;
    long _currentTimeMillis = System.currentTimeMillis();
    final String changeRequestName = ("ChangeRequest" + Long.valueOf(_currentTimeMillis));
    getOrGenerateSubUseCase(bne3.usecases.CreateChangeRequest.class, "CreateChangeRequest").setName(new ChangeRequestTitle(changeRequestName, null));
    getOrGenerateSubUseCase(bne3.usecases.CreateChangeRequest.class, "CreateChangeRequest").execute((MainWindow)activity);
    getOrGenerateSubUseCase(bne3.usecases.OpenEthernetNavigator.class, "OpenEthernetNavigator").execute((MainWindow)activity);
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    getOrGenerateSubUseCase(bne3.usecases.ActivateChangeRequest.class, "ActivateChangeRequest").setName(new StringDT(changeRequestName, null));
    getOrGenerateSubUseCase(bne3.usecases.ActivateChangeRequest.class, "ActivateChangeRequest").execute((EthernetNavigator)activity);
    StringDT nodePath6d79aa50 = new StringDT("Ethernet/Datentyp-Kodierungen", null);
    StringDT contextMenuEntry6d79aa50 = new StringDT("Datentyp-Kodierung hinzuf\u00FCgen", null);
    ((EthernetNavigator)activity).tree_invokeContextMenu(nodePath6d79aa50, contextMenuEntry6d79aa50);
    stack.push(activity);
    activity = ((ViewActivity)activity).returnToMain();
    stack.push(activity);
    activity = ((MainWindow)activity).findNewCodingEditor();
    stack.push(activity);
    activity = ((CodingEditor)activity).allgemeinPage();
    LongName str34ed49d8 = getOrGenerateValue(LongName.class, "str34ed49d8");
    ((AllgemeinPage)activity).longname_setText(str34ed49d8);
    final LongName longName = str34ed49d8;
    ShortName str1ed385a2 = this.shortName;
    ((AllgemeinPage)activity).shortname_setText(str1ed385a2);
    StringDT contextMenuEntry36a4f72a = new StringDT("Element hinzuf\u00FCgen", null);
    ((AllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry36a4f72a);
    IntegerDT row10688f5a = new IntegerDT(0, null);
    StringDT columnName10688f5a = new StringDT("Text", null);
    StringDT textValue10688f5a = getOrGenerateValue(StringDT.class, "textValue10688f5a");
    ((AllgemeinPage)activity).textuelleBeschreibung_setTextByName(row10688f5a, columnName10688f5a, textValue10688f5a);
    final StringDT text1 = textValue10688f5a;
    IntegerDT row5742776d = new IntegerDT(0, null);
    StringDT columnName5742776d = new StringDT("Sprache", null);
    LanguageDT textValue5742776d = getOrGenerateValue(LanguageDT.class, "textValue5742776d");
    ((AllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row5742776d, columnName5742776d, textValue5742776d);
    final LanguageDT sprache = textValue5742776d;
    StringDT contextMenuEntry400fb52b = new StringDT("Element hinzuf\u00FCgen", null);
    ((AllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry400fb52b);
    IntegerDT row31da7772 = new IntegerDT(1, null);
    StringDT columnName31da7772 = new StringDT("Text", null);
    StringDT textValue31da7772 = new StringDT("Second", null);
    ((AllgemeinPage)activity).textuelleBeschreibung_setTextByName(row31da7772, columnName31da7772, textValue31da7772);
    IntegerDT row6bc7821a = new IntegerDT(1, null);
    StringDT columnName6bc7821a = new StringDT("Sprache", null);
    LanguageDT textValue6bc7821a = getOrGenerateValue(LanguageDT.class, "textValue6bc7821a");
    ((AllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row6bc7821a, columnName6bc7821a, textValue6bc7821a);
    IntegerDT row14dd3258 = new IntegerDT(1, null);
    StringDT contextMenuEntry14dd3258 = new StringDT("Element l\u00F6schen", null);
    ((AllgemeinPage)activity).textuelleBeschreibung_invokeContextMenuOnRow(row14dd3258, contextMenuEntry14dd3258);
    stack.push(activity);
    activity = ((AllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((CodingEditor)activity).kodierungPage();
    try {
      Thread.sleep(100);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
    BasisdatentypDT str41d0118d = getOrGenerateValue(BasisdatentypDT.class, "str41d0118d");
    ((CodingEditorKodierungPage)activity).physBasisDatenTyp_setText(str41d0118d);
    final BasisdatentypDT physBasisDatenTyp = str41d0118d;
    DoubleStringDT str1b41cdee = getOrGenerateValue(DoubleStringDT.class, "str1b41cdee");
    ((CodingEditorKodierungPage)activity).physMinimum_setText(str1b41cdee);
    final DoubleStringDT physMinimum = str1b41cdee;
    IntervallgrenzeDT str12b34087 = getOrGenerateValue(IntervallgrenzeDT.class, "str12b34087");
    ((CodingEditorKodierungPage)activity).physMinimumIntervallgrenze_setText(str12b34087);
    final IntervallgrenzeDT physMinimumIntervallgrenze = str12b34087;
    DoubleStringDT str61f4c343 = getOrGenerateValue(DoubleStringDT.class, "str61f4c343");
    ((CodingEditorKodierungPage)activity).physAufloesung_setText(str61f4c343);
    final DoubleStringDT physAufloesung = str61f4c343;
    DoubleStringDT str15e320e4 = getOrGenerateValue(DoubleStringDT.class, "str15e320e4");
    ((CodingEditorKodierungPage)activity).physMaximum_setText(str15e320e4);
    final DoubleStringDT physMaximum = str15e320e4;
    IntervallgrenzeDT str6126d994 = getOrGenerateValue(IntervallgrenzeDT.class, "str6126d994");
    ((CodingEditorKodierungPage)activity).physMaximumIntervallgrenze_setText(str6126d994);
    final IntervallgrenzeDT physMaximumIntervallgrenze = str6126d994;
    BasisdatentypDT str4459a8e6 = getOrGenerateValue(BasisdatentypDT.class, "str4459a8e6");
    ((CodingEditorKodierungPage)activity).internalBasisDatenTyp_setText(str4459a8e6);
    final BasisdatentypDT internalBasisDatenTyp = str4459a8e6;
    KategorieDT str5db45cd6 = getOrGenerateValue(KategorieDT.class, "str5db45cd6");
    ((CodingEditorKodierungPage)activity).internalKategorie_setText(str5db45cd6);
    final KategorieDT internalKategorie = str5db45cd6;
    EncodingDT str52d65560 = getOrGenerateValue(EncodingDT.class, "str52d65560");
    ((CodingEditorKodierungPage)activity).internalEncoding_setText(str52d65560);
    final EncodingDT internalEncoding = str52d65560;
    TerminationDT str4b6104ea = getOrGenerateValue(TerminationDT.class, "str4b6104ea");
    ((CodingEditorKodierungPage)activity).internalTermination_setText(str4b6104ea);
    final TerminationDT internalTermination = str4b6104ea;
    ((CodingEditorKodierungPage)activity).fixedBitLengthButton_click();
    IntegerStringDT str2b3c394 = getOrGenerateValue(IntegerStringDT.class, "str2b3c394");
    ((CodingEditorKodierungPage)activity).bitLength_setText(str2b3c394);
    final IntegerStringDT bitLength = str2b3c394;
    stack.push(activity);
    activity = ((CodingEditorKodierungPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((EditorActivity)activity).saveAndClose();
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    StringDT bne3_activities_EthernetNavigator_openCoding_title = new StringDT(new Function0<String>() {
      public String apply() {
        String _value = CreateCoding.this.shortName.getValue();
        return _value;
      }
    }.apply(), null);
    stack.push(activity);
    activity = ((EthernetNavigator)activity).openCoding(bne3_activities_EthernetNavigator_openCoding_title);
    stack.push(activity);
    activity = ((CodingEditor)activity).allgemeinPage();
    final LongName actualLongName = ((AllgemeinPage)activity).longname_getText();
    final ShortName actualShortName = ((AllgemeinPage)activity).shortname_getText();
    final StringDT actualtext = ((AllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Text", null));
    final StringDT actualSprache = ((AllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Sprache", null));
    final IntegerDT actualAnzEntries = ((AllgemeinPage)activity).textuelleBeschreibung_getRowCount();
    if (this.generator == null) {
      String _value = longName.getValue();
      String _value_1 = actualLongName.getValue();
      Assert.assertEquals(_value, _value_1);
    }
    if (this.generator == null) {
      String _value_2 = this.shortName.getValue();
      String _value_3 = actualShortName.getValue();
      Assert.assertEquals(_value_2, _value_3);
    }
    if (this.generator == null) {
      String _value_4 = text1.getValue();
      String _value_5 = actualtext.getValue();
      Assert.assertEquals(_value_4, _value_5);
    }
    if (this.generator == null) {
      String _value_6 = sprache.getValue();
      String _value_7 = actualSprache.getValue();
      Assert.assertEquals(_value_6, _value_7);
    }
    if (this.generator == null) {
      Integer _value_8 = actualAnzEntries.getValue();
      Assert.assertEquals(1, (_value_8).intValue());
    }
    stack.push(activity);
    activity = ((AllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((CodingEditor)activity).kodierungPage();
    try {
      Thread.sleep(100);
    } catch (Exception _e_1) {
      throw Exceptions.sneakyThrow(_e_1);
    }
    final BasisdatentypDT actualPhysBasisDatenTyp = ((CodingEditorKodierungPage)activity).physBasisDatenTyp_getText();
    final DoubleStringDT actualPhysMinimum = ((CodingEditorKodierungPage)activity).physMinimum_getText();
    final DoubleStringDT actualPhysMaximum = ((CodingEditorKodierungPage)activity).physMaximum_getText();
    final DoubleStringDT actualPhysAufloesung = ((CodingEditorKodierungPage)activity).physAufloesung_getText();
    final IntervallgrenzeDT actualPhysMinimumIntervallgrenze = ((CodingEditorKodierungPage)activity).physMinimumIntervallgrenze_getText();
    final IntervallgrenzeDT actualPhysMaximumIntervallgrenze = ((CodingEditorKodierungPage)activity).physMaximumIntervallgrenze_getText();
    final BasisdatentypDT actualInternalBasisDatenTyp = ((CodingEditorKodierungPage)activity).internalBasisDatenTyp_getText();
    final KategorieDT actualInternalKategorie = ((CodingEditorKodierungPage)activity).internalKategorie_getText();
    final EncodingDT actualInternalEncoding = ((CodingEditorKodierungPage)activity).internalEncoding_getText();
    final TerminationDT actualInternalTermination = ((CodingEditorKodierungPage)activity).internalTermination_getText();
    final BooleanDT actualFixedBitlength = ((CodingEditorKodierungPage)activity).fixedBitLengthButton_isSelected();
    final IntegerStringDT actualBitLength = ((CodingEditorKodierungPage)activity).bitLength_getText();
    if (this.generator == null) {
      String _value_9 = physBasisDatenTyp.getValue();
      String _value_10 = actualPhysBasisDatenTyp.getValue();
      Assert.assertEquals(_value_9, _value_10);
    }
    if (this.generator == null) {
      String _value_11 = physMinimum.getValue();
      String _value_12 = actualPhysMinimum.getValue();
      Assert.assertEquals(_value_11, _value_12);
    }
    if (this.generator == null) {
      String _value_13 = physMaximum.getValue();
      String _value_14 = actualPhysMaximum.getValue();
      Assert.assertEquals(_value_13, _value_14);
    }
    if (this.generator == null) {
      String _value_15 = physAufloesung.getValue();
      String _value_16 = actualPhysAufloesung.getValue();
      Assert.assertEquals(_value_15, _value_16);
    }
    if (this.generator == null) {
      String _value_17 = physMinimumIntervallgrenze.getValue();
      String _value_18 = actualPhysMinimumIntervallgrenze.getValue();
      Assert.assertEquals(_value_17, _value_18);
    }
    if (this.generator == null) {
      String _value_19 = physMaximumIntervallgrenze.getValue();
      String _value_20 = actualPhysMaximumIntervallgrenze.getValue();
      Assert.assertEquals(_value_19, _value_20);
    }
    if (this.generator == null) {
      String _value_21 = internalBasisDatenTyp.getValue();
      String _value_22 = actualInternalBasisDatenTyp.getValue();
      Assert.assertEquals(_value_21, _value_22);
    }
    if (this.generator == null) {
      String _value_23 = internalKategorie.getValue();
      String _value_24 = actualInternalKategorie.getValue();
      Assert.assertEquals(_value_23, _value_24);
    }
    if (this.generator == null) {
      String _value_25 = internalEncoding.getValue();
      String _value_26 = actualInternalEncoding.getValue();
      Assert.assertEquals(_value_25, _value_26);
    }
    if (this.generator == null) {
      String _value_27 = internalTermination.getValue();
      String _value_28 = actualInternalTermination.getValue();
      Assert.assertEquals(_value_27, _value_28);
    }
    if (this.generator == null) {
      Boolean _value_29 = actualFixedBitlength.getValue();
      Assert.assertEquals(true, (_value_29).booleanValue());
    }
    if (this.generator == null) {
      String _value_30 = bitLength.getValue();
      String _value_31 = actualBitLength.getValue();
      Assert.assertEquals(_value_30, _value_31);
    }
  }
}
