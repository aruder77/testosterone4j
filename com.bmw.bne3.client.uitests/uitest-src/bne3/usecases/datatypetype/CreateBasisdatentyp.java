package bne3.usecases.datatypetype;

import bne3.activities.EditorActivity;
import bne3.activities.EthernetNavigator;
import bne3.activities.MainWindow;
import bne3.activities.datatypetype.BasisDatentypEditor;
import bne3.activities.datatypetype.BasisDatentypSpeziellPage;
import bne3.activities.datatypetype.BasisdatentypEditorAllgemeinPage;
import bne3.activities.datatypetype.CodingTypeSearch;
import bne3.datatypes.LanguageDT;
import bne3.datatypes.LongName;
import bne3.datatypes.ShortName;
import de.msg.xt.mdt.base.AbstractActivity;
import de.msg.xt.mdt.base.BaseUseCase;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.tdsl.basictypes.IntegerDT;
import de.msg.xt.mdt.tdsl.basictypes.StringDT;
import java.util.Stack;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import junit.framework.Assert;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@XmlRootElement
public class CreateBasisdatentyp extends BaseUseCase implements Runnable {
  @XmlElement
  private ShortName codingShortName;
  
  public CreateBasisdatentyp() {
  }
  
  public CreateBasisdatentyp(final Generator generator) {
    this();
    this.generator = generator;
    this.codingShortName = this.getOrGenerateValue(bne3.datatypes.ShortName.class, "bne3.usecases.datatypetype.CreateBasisdatentyp.codingShortName");
  }
  
  public CreateBasisdatentyp(final ShortName codingShortName) {
    this();
    this.codingShortName = codingShortName;
  }
  
  public void setCodingShortName(final ShortName codingShortName) {
    this.codingShortName = codingShortName;
  }
  
  @Override
  public void run() {
    execute(MainWindow.find());
  }
  
  public void execute(final MainWindow initialActivity) {
    Stack<AbstractActivity> stack = new Stack<AbstractActivity>();
    AbstractActivity activity = initialActivity;
    getOrGenerateSubUseCase(bne3.usecases.CreateMinimumCoding.class, "CreateMinimumCoding").setShortName(this.codingShortName);
    getOrGenerateSubUseCase(bne3.usecases.CreateMinimumCoding.class, "CreateMinimumCoding").execute((MainWindow)activity);
    getOrGenerateSubUseCase(bne3.usecases.OpenEthernetNavigator.class, "OpenEthernetNavigator").execute((MainWindow)activity);
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    stack.push(activity);
    activity = ((EthernetNavigator)activity).addBasisDatentyp();
    stack.push(activity);
    activity = ((BasisDatentypEditor)activity).allgemeinPage();
    LongName str576948f9 = getOrGenerateValue(LongName.class, "str576948f9");
    ((BasisdatentypEditorAllgemeinPage)activity).longname_setText(str576948f9);
    final LongName longName = str576948f9;
    ShortName str70dbefd5 = getOrGenerateValue(ShortName.class, "str70dbefd5");
    ((BasisdatentypEditorAllgemeinPage)activity).shortname_setText(str70dbefd5);
    final ShortName shortName = str70dbefd5;
    StringDT str4006ba92 = new StringDT("ABC", null);
    ((BasisdatentypEditorAllgemeinPage)activity).packageSelectionCombo_setText(str4006ba92);
    StringDT contextMenuEntry6c711017 = new StringDT("Element hinzuf\u00FCgen", null);
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry6c711017);
    IntegerDT row5f4d553f = new IntegerDT(0, null);
    StringDT columnName5f4d553f = new StringDT("Text", null);
    StringDT textValue5f4d553f = getOrGenerateValue(StringDT.class, "textValue5f4d553f");
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row5f4d553f, columnName5f4d553f, textValue5f4d553f);
    final StringDT text1 = textValue5f4d553f;
    IntegerDT row796de5c3 = new IntegerDT(0, null);
    StringDT columnName796de5c3 = new StringDT("Sprache", null);
    LanguageDT textValue796de5c3 = getOrGenerateValue(LanguageDT.class, "textValue796de5c3");
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row796de5c3, columnName796de5c3, textValue796de5c3);
    final LanguageDT sprache = textValue796de5c3;
    StringDT contextMenuEntry618778e0 = new StringDT("Element hinzuf\u00FCgen", null);
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry618778e0);
    IntegerDT row3cffaa5b = new IntegerDT(1, null);
    StringDT columnName3cffaa5b = new StringDT("Text", null);
    StringDT textValue3cffaa5b = new StringDT("Second", null);
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row3cffaa5b, columnName3cffaa5b, textValue3cffaa5b);
    IntegerDT row217825e9 = new IntegerDT(1, null);
    StringDT columnName217825e9 = new StringDT("Sprache", null);
    LanguageDT textValue217825e9 = getOrGenerateValue(LanguageDT.class, "textValue217825e9");
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row217825e9, columnName217825e9, textValue217825e9);
    IntegerDT row5cf01b29 = new IntegerDT(1, null);
    StringDT contextMenuEntry5cf01b29 = new StringDT("Element l\u00F6schen", null);
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenuOnRow(row5cf01b29, contextMenuEntry5cf01b29);
    stack.push(activity);
    activity = ((BasisdatentypEditorAllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((BasisDatentypEditor)activity).speziellPage();
    try {
      Thread.sleep(100);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
    stack.push(activity);
    activity = ((BasisDatentypSpeziellPage)activity).codingSelectionSearchButton_push();
    StringDT str1cc7e8ae = new StringDT(new Function0<String>() {
      public String apply() {
        String _value = CreateBasisdatentyp.this.codingShortName.getValue();
        return _value;
      }
    }.apply(), null);
    ((CodingTypeSearch)activity).searchText_setText(str1cc7e8ae);
    ((CodingTypeSearch)activity).searchButton_push();
    IntegerDT row4c7d75a5 = new IntegerDT(0, null);
    ((CodingTypeSearch)activity).elements_selectRow(row4c7d75a5);
    stack.push(activity);
    activity = ((CodingTypeSearch)activity).okButton_push();
    stack.push(activity);
    activity = ((EditorActivity)activity).saveAndClose();
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    StringDT bne3_activities_EthernetNavigator_openBasisDatentyp_title = new StringDT(new Function0<String>() {
      public String apply() {
        String _value = shortName.getValue();
        return _value;
      }
    }.apply(), null);
    stack.push(activity);
    activity = ((EthernetNavigator)activity).openBasisDatentyp(bne3_activities_EthernetNavigator_openBasisDatentyp_title);
    stack.push(activity);
    activity = ((BasisDatentypEditor)activity).allgemeinPage();
    final LongName actualLongName = ((BasisdatentypEditorAllgemeinPage)activity).longname_getText();
    final ShortName actualShortName = ((BasisdatentypEditorAllgemeinPage)activity).shortname_getText();
    final StringDT actualPackage = ((BasisdatentypEditorAllgemeinPage)activity).packageSelectionCombo_getText();
    final StringDT actualtext = ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Text", null));
    final StringDT actualSprache = ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Sprache", null));
    final IntegerDT actualAnzEntries = ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_getRowCount();
    if (this.generator == null) {
      String _value = longName.getValue();
      String _value_1 = actualLongName.getValue();
      Assert.assertEquals(_value, _value_1);
    }
    if (this.generator == null) {
      String _value_2 = shortName.getValue();
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
    if (this.generator == null) {
      String _value_9 = actualPackage.getValue();
      Assert.assertEquals("ABC", _value_9);
    }
    stack.push(activity);
    activity = ((BasisdatentypEditorAllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((BasisDatentypEditor)activity).speziellPage();
    final StringDT actualCoding = ((BasisDatentypSpeziellPage)activity).codingSelectionText_getText();
    if (this.generator == null) {
      String _value_10 = actualCoding.getValue();
      String _value_11 = this.codingShortName.getValue();
      String _plus = (_value_11 + " \\([0-9]+\\)");
      boolean _matches = _value_10.matches(_plus);
      Assert.assertTrue(_matches);
    }
  }
}
