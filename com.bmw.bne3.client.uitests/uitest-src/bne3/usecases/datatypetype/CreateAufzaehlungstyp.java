package bne3.usecases.datatypetype;

import bne3.activities.EditorActivity;
import bne3.activities.EthernetNavigator;
import bne3.activities.MainWindow;
import bne3.activities.datatypetype.AufzaehlungstypenCodingTypeSearch;
import bne3.activities.datatypetype.AufzaehlungstypenEditor;
import bne3.activities.datatypetype.AufzaehlungstypenEditorAllgemeinPage;
import bne3.activities.datatypetype.AufzaehlungstypenEditorSpeziellPage;
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
public class CreateAufzaehlungstyp extends BaseUseCase implements Runnable {
  @XmlElement
  private ShortName codingShortName;
  
  public CreateAufzaehlungstyp() {
  }
  
  public CreateAufzaehlungstyp(final Generator generator) {
    this();
    this.generator = generator;
    this.codingShortName = this.getOrGenerateValue(bne3.datatypes.ShortName.class, "bne3.usecases.datatypetype.CreateAufzaehlungstyp.codingShortName");
  }
  
  public CreateAufzaehlungstyp(final ShortName codingShortName) {
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
    activity = ((EthernetNavigator)activity).addAufzaehlungstyp();
    stack.push(activity);
    activity = ((AufzaehlungstypenEditor)activity).allgemeinPage();
    LongName str1c9172cc = getOrGenerateValue(LongName.class, "str1c9172cc");
    ((AufzaehlungstypenEditorAllgemeinPage)activity).longname_setText(str1c9172cc);
    final LongName longName = str1c9172cc;
    ShortName str33307d37 = getOrGenerateValue(ShortName.class, "str33307d37");
    ((AufzaehlungstypenEditorAllgemeinPage)activity).shortname_setText(str33307d37);
    final ShortName shortName = str33307d37;
    StringDT str56ee8b66 = new StringDT("ABC", null);
    ((AufzaehlungstypenEditorAllgemeinPage)activity).packageSelectionCombo_setText(str56ee8b66);
    StringDT contextMenuEntry44a8b37a = new StringDT("Element hinzuf\u00FCgen", null);
    ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry44a8b37a);
    IntegerDT row54d6b86b = new IntegerDT(0, null);
    StringDT columnName54d6b86b = new StringDT("Text", null);
    StringDT textValue54d6b86b = getOrGenerateValue(StringDT.class, "textValue54d6b86b");
    ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row54d6b86b, columnName54d6b86b, textValue54d6b86b);
    final StringDT text1 = textValue54d6b86b;
    IntegerDT row59405bde = new IntegerDT(0, null);
    StringDT columnName59405bde = new StringDT("Sprache", null);
    LanguageDT textValue59405bde = getOrGenerateValue(LanguageDT.class, "textValue59405bde");
    ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row59405bde, columnName59405bde, textValue59405bde);
    final LanguageDT sprache = textValue59405bde;
    StringDT contextMenuEntry31109bf6 = new StringDT("Element hinzuf\u00FCgen", null);
    ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry31109bf6);
    IntegerDT row6b780b2d = new IntegerDT(1, null);
    StringDT columnName6b780b2d = new StringDT("Text", null);
    StringDT textValue6b780b2d = new StringDT("Second", null);
    ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row6b780b2d, columnName6b780b2d, textValue6b780b2d);
    IntegerDT row3fb5a050 = new IntegerDT(1, null);
    StringDT columnName3fb5a050 = new StringDT("Sprache", null);
    LanguageDT textValue3fb5a050 = getOrGenerateValue(LanguageDT.class, "textValue3fb5a050");
    ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row3fb5a050, columnName3fb5a050, textValue3fb5a050);
    IntegerDT row3c82fa = new IntegerDT(1, null);
    StringDT contextMenuEntry3c82fa = new StringDT("Element l\u00F6schen", null);
    ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenuOnRow(row3c82fa, contextMenuEntry3c82fa);
    stack.push(activity);
    activity = ((AufzaehlungstypenEditorAllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((AufzaehlungstypenEditor)activity).speziellPage();
    try {
      Thread.sleep(100);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
    stack.push(activity);
    activity = ((AufzaehlungstypenEditorSpeziellPage)activity).codingSelectionSearchButton_push();
    StringDT str22bfd8fa = new StringDT(new Function0<String>() {
      public String apply() {
        String _value = CreateAufzaehlungstyp.this.codingShortName.getValue();
        return _value;
      }
    }.apply(), null);
    ((AufzaehlungstypenCodingTypeSearch)activity).searchText_setText(str22bfd8fa);
    ((AufzaehlungstypenCodingTypeSearch)activity).searchButton_push();
    IntegerDT row56be3f61 = new IntegerDT(0, null);
    ((AufzaehlungstypenCodingTypeSearch)activity).elements_selectRow(row56be3f61);
    stack.push(activity);
    activity = ((AufzaehlungstypenCodingTypeSearch)activity).okButton_push();
    stack.push(activity);
    activity = ((AufzaehlungstypenEditor)activity).speziellPage();
    StringDT contextMenuEntry43a554f6 = new StringDT("Element hinzuf\u00FCgen", null);
    ((AufzaehlungstypenEditorSpeziellPage)activity).enumElements_invokeContextMenu(contextMenuEntry43a554f6);
    IntegerDT row46101113 = new IntegerDT(0, null);
    StringDT columnName46101113 = new StringDT("Wert", null);
    StringDT textValue46101113 = new StringDT("5", null);
    ((AufzaehlungstypenEditorSpeziellPage)activity).enumElements_setTextByName(row46101113, columnName46101113, textValue46101113);
    IntegerDT row6c19c589 = new IntegerDT(0, null);
    StringDT columnName6c19c589 = new StringDT("Synonym", null);
    StringDT textValue6c19c589 = new StringDT("Synonym 1", null);
    ((AufzaehlungstypenEditorSpeziellPage)activity).enumElements_setTextByName(row6c19c589, columnName6c19c589, textValue6c19c589);
    stack.push(activity);
    activity = ((AufzaehlungstypenEditorSpeziellPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((EditorActivity)activity).saveAndClose();
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    StringDT bne3_activities_EthernetNavigator_openAufzaehlungstyp_title = new StringDT(new Function0<String>() {
      public String apply() {
        String _value = shortName.getValue();
        return _value;
      }
    }.apply(), null);
    stack.push(activity);
    activity = ((EthernetNavigator)activity).openAufzaehlungstyp(bne3_activities_EthernetNavigator_openAufzaehlungstyp_title);
    stack.push(activity);
    activity = ((AufzaehlungstypenEditor)activity).allgemeinPage();
    final LongName actualLongName = ((AufzaehlungstypenEditorAllgemeinPage)activity).longname_getText();
    final ShortName actualShortName = ((AufzaehlungstypenEditorAllgemeinPage)activity).shortname_getText();
    final StringDT actualPackage = ((AufzaehlungstypenEditorAllgemeinPage)activity).packageSelectionCombo_getText();
    final StringDT actualtext = ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Text", null));
    final StringDT actualSprache = ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Sprache", null));
    final IntegerDT actualAnzEntries = ((AufzaehlungstypenEditorAllgemeinPage)activity).textuelleBeschreibung_getRowCount();
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
      String _value_4 = actualPackage.getValue();
      Assert.assertEquals("ABC", _value_4);
    }
    if (this.generator == null) {
      String _value_5 = text1.getValue();
      String _value_6 = actualtext.getValue();
      Assert.assertEquals(_value_5, _value_6);
    }
    if (this.generator == null) {
      String _value_7 = sprache.getValue();
      String _value_8 = actualSprache.getValue();
      Assert.assertEquals(_value_7, _value_8);
    }
    if (this.generator == null) {
      Integer _value_9 = actualAnzEntries.getValue();
      Assert.assertEquals(1, (_value_9).intValue());
    }
    stack.push(activity);
    activity = ((AufzaehlungstypenEditorAllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((AufzaehlungstypenEditor)activity).speziellPage();
    final StringDT actualCoding = ((AufzaehlungstypenEditorSpeziellPage)activity).codingSelectionText_getText();
    final StringDT actualWert = ((AufzaehlungstypenEditorSpeziellPage)activity).enumElements_getTextByName(new IntegerDT(0, null), new StringDT("Wert", null));
    final StringDT actualBinary = ((AufzaehlungstypenEditorSpeziellPage)activity).enumElements_getTextByName(new IntegerDT(0, null), new StringDT("Wert (Bin\u00E4r)", null));
    final StringDT actualHex = ((AufzaehlungstypenEditorSpeziellPage)activity).enumElements_getTextByName(new IntegerDT(0, null), new StringDT("Wert (Hexadezimal)", null));
    final StringDT actualSynonym = ((AufzaehlungstypenEditorSpeziellPage)activity).enumElements_getTextByName(new IntegerDT(0, null), new StringDT("Synonym", null));
    if (this.generator == null) {
      String _value_10 = actualCoding.getValue();
      String _value_11 = this.codingShortName.getValue();
      String _plus = (_value_11 + " \\([0-9]+\\)");
      boolean _matches = _value_10.matches(_plus);
      Assert.assertTrue(_matches);
    }
    if (this.generator == null) {
      String _value_12 = actualWert.getValue();
      Assert.assertEquals("5", _value_12);
    }
    if (this.generator == null) {
      String _value_13 = actualBinary.getValue();
      Assert.assertEquals("101", _value_13);
    }
    if (this.generator == null) {
      String _value_14 = actualHex.getValue();
      Assert.assertEquals("5", _value_14);
    }
    if (this.generator == null) {
      String _value_15 = actualSynonym.getValue();
      Assert.assertEquals("Synonym 1", _value_15);
    }
  }
}
