package bne3.usecases.datatypetype;

import bne3.activities.EditorActivity;
import bne3.activities.EthernetNavigator;
import bne3.activities.MainWindow;
import bne3.activities.datatypetype.StrukturEditor;
import bne3.activities.datatypetype.StrukturEditorAllgemeinPage;
import bne3.activities.datatypetype.StrukturEditorSpeziellPage;
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
public class CreateStrukturtyp extends BaseUseCase implements Runnable {
  @XmlElement
  private ShortName codingShortName;
  
  public CreateStrukturtyp() {
  }
  
  public CreateStrukturtyp(final Generator generator) {
    this();
    this.generator = generator;
    this.codingShortName = this.getOrGenerateValue(bne3.datatypes.ShortName.class, "bne3.usecases.datatypetype.CreateStrukturtyp.codingShortName");
  }
  
  public CreateStrukturtyp(final ShortName codingShortName) {
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
    activity = ((EthernetNavigator)activity).addStrukturtyp();
    stack.push(activity);
    activity = ((StrukturEditor)activity).allgemeinPage();
    LongName str73f94ea9 = getOrGenerateValue(LongName.class, "str73f94ea9");
    ((StrukturEditorAllgemeinPage)activity).longname_setText(str73f94ea9);
    final LongName longName = str73f94ea9;
    ShortName str4bf73e50 = getOrGenerateValue(ShortName.class, "str4bf73e50");
    ((StrukturEditorAllgemeinPage)activity).shortname_setText(str4bf73e50);
    final ShortName shortName = str4bf73e50;
    StringDT str26e1af49 = new StringDT("ABC", null);
    ((StrukturEditorAllgemeinPage)activity).packageSelectionCombo_setText(str26e1af49);
    StringDT contextMenuEntry7fb0703a = new StringDT("Element hinzuf\u00FCgen", null);
    ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry7fb0703a);
    IntegerDT row6fc7afff = new IntegerDT(0, null);
    StringDT columnName6fc7afff = new StringDT("Text", null);
    StringDT textValue6fc7afff = getOrGenerateValue(StringDT.class, "textValue6fc7afff");
    ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row6fc7afff, columnName6fc7afff, textValue6fc7afff);
    final StringDT text1 = textValue6fc7afff;
    IntegerDT row6f8e695a = new IntegerDT(0, null);
    StringDT columnName6f8e695a = new StringDT("Sprache", null);
    LanguageDT textValue6f8e695a = getOrGenerateValue(LanguageDT.class, "textValue6f8e695a");
    ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row6f8e695a, columnName6f8e695a, textValue6f8e695a);
    final LanguageDT sprache = textValue6f8e695a;
    StringDT contextMenuEntry5fd2b8e3 = new StringDT("Element hinzuf\u00FCgen", null);
    ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry5fd2b8e3);
    IntegerDT row6baefe19 = new IntegerDT(1, null);
    StringDT columnName6baefe19 = new StringDT("Text", null);
    StringDT textValue6baefe19 = new StringDT("Second", null);
    ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row6baefe19, columnName6baefe19, textValue6baefe19);
    IntegerDT row7a064001 = new IntegerDT(1, null);
    StringDT columnName7a064001 = new StringDT("Sprache", null);
    LanguageDT textValue7a064001 = getOrGenerateValue(LanguageDT.class, "textValue7a064001");
    ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row7a064001, columnName7a064001, textValue7a064001);
    IntegerDT row5e20c1a4 = new IntegerDT(1, null);
    StringDT contextMenuEntry5e20c1a4 = new StringDT("Element l\u00F6schen", null);
    ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenuOnRow(row5e20c1a4, contextMenuEntry5e20c1a4);
    stack.push(activity);
    activity = ((StrukturEditorAllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((StrukturEditor)activity).speziellPage();
    try {
      Thread.sleep(100);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
    stack.push(activity);
    activity = ((StrukturEditorSpeziellPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((EditorActivity)activity).saveAndClose();
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    StringDT bne3_activities_EthernetNavigator_openStrukturtyp_title = new StringDT(new Function0<String>() {
      public String apply() {
        String _value = shortName.getValue();
        return _value;
      }
    }.apply(), null);
    stack.push(activity);
    activity = ((EthernetNavigator)activity).openStrukturtyp(bne3_activities_EthernetNavigator_openStrukturtyp_title);
    stack.push(activity);
    activity = ((StrukturEditor)activity).allgemeinPage();
    final LongName actualLongName = ((StrukturEditorAllgemeinPage)activity).longname_getText();
    final ShortName actualShortName = ((StrukturEditorAllgemeinPage)activity).shortname_getText();
    final StringDT actualPackage = ((StrukturEditorAllgemeinPage)activity).packageSelectionCombo_getText();
    final StringDT actualtext = ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Text", null));
    final StringDT actualSprache = ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Sprache", null));
    final IntegerDT actualAnzEntries = ((StrukturEditorAllgemeinPage)activity).textuelleBeschreibung_getRowCount();
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
  }
}
