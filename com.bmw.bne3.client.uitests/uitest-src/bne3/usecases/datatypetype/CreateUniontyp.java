package bne3.usecases.datatypetype;

import bne3.activities.EditorActivity;
import bne3.activities.EthernetNavigator;
import bne3.activities.MainWindow;
import bne3.activities.datatypetype.UniontypEditor;
import bne3.activities.datatypetype.UniontypEditorAllgemeinPage;
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
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@XmlRootElement
public class CreateUniontyp extends BaseUseCase implements Runnable {
  @XmlElement
  private ShortName codingShortName;
  
  public CreateUniontyp() {
  }
  
  public CreateUniontyp(final Generator generator) {
    this();
    this.generator = generator;
    this.codingShortName = this.getOrGenerateValue(bne3.datatypes.ShortName.class, "bne3.usecases.datatypetype.CreateUniontyp.codingShortName");
  }
  
  public CreateUniontyp(final ShortName codingShortName) {
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
    activity = ((EthernetNavigator)activity).addUniontyp();
    stack.push(activity);
    activity = ((UniontypEditor)activity).allgemeinPage();
    LongName str46cf280f = getOrGenerateValue(LongName.class, "str46cf280f");
    ((UniontypEditorAllgemeinPage)activity).longname_setText(str46cf280f);
    final LongName longName = str46cf280f;
    ShortName str576d9eb7 = getOrGenerateValue(ShortName.class, "str576d9eb7");
    ((UniontypEditorAllgemeinPage)activity).shortname_setText(str576d9eb7);
    final ShortName shortName = str576d9eb7;
    StringDT str6dba9b8d = new StringDT("ABC", null);
    ((UniontypEditorAllgemeinPage)activity).packageSelectionCombo_setText(str6dba9b8d);
    StringDT contextMenuEntry5eece3eb = new StringDT("Element hinzuf\u00FCgen", null);
    ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry5eece3eb);
    IntegerDT row3c3d03d4 = new IntegerDT(0, null);
    StringDT columnName3c3d03d4 = new StringDT("Text", null);
    StringDT textValue3c3d03d4 = getOrGenerateValue(StringDT.class, "textValue3c3d03d4");
    ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row3c3d03d4, columnName3c3d03d4, textValue3c3d03d4);
    final StringDT text1 = textValue3c3d03d4;
    IntegerDT row7ad35d21 = new IntegerDT(0, null);
    StringDT columnName7ad35d21 = new StringDT("Sprache", null);
    LanguageDT textValue7ad35d21 = getOrGenerateValue(LanguageDT.class, "textValue7ad35d21");
    ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row7ad35d21, columnName7ad35d21, textValue7ad35d21);
    final LanguageDT sprache = textValue7ad35d21;
    StringDT contextMenuEntry432ccbd2 = new StringDT("Element hinzuf\u00FCgen", null);
    ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry432ccbd2);
    IntegerDT row6d309889 = new IntegerDT(1, null);
    StringDT columnName6d309889 = new StringDT("Text", null);
    StringDT textValue6d309889 = new StringDT("Second", null);
    ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row6d309889, columnName6d309889, textValue6d309889);
    IntegerDT row4e4130e1 = new IntegerDT(1, null);
    StringDT columnName4e4130e1 = new StringDT("Sprache", null);
    LanguageDT textValue4e4130e1 = getOrGenerateValue(LanguageDT.class, "textValue4e4130e1");
    ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row4e4130e1, columnName4e4130e1, textValue4e4130e1);
    IntegerDT row78f63d48 = new IntegerDT(1, null);
    StringDT contextMenuEntry78f63d48 = new StringDT("Element l\u00F6schen", null);
    ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenuOnRow(row78f63d48, contextMenuEntry78f63d48);
    stack.push(activity);
    activity = ((UniontypEditorAllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((EditorActivity)activity).saveAndClose();
    stack.push(activity);
    activity = ((MainWindow)activity).findEthernetNavigator();
    StringDT bne3_activities_EthernetNavigator_openUniontyp_title = new StringDT(new Function0<String>() {
      public String apply() {
        String _value = shortName.getValue();
        return _value;
      }
    }.apply(), null);
    stack.push(activity);
    activity = ((EthernetNavigator)activity).openUniontyp(bne3_activities_EthernetNavigator_openUniontyp_title);
    stack.push(activity);
    activity = ((UniontypEditor)activity).allgemeinPage();
    final LongName actualLongName = ((UniontypEditorAllgemeinPage)activity).longname_getText();
    final ShortName actualShortName = ((UniontypEditorAllgemeinPage)activity).shortname_getText();
    final StringDT actualPackage = ((UniontypEditorAllgemeinPage)activity).packageSelectionCombo_getText();
    final StringDT actualtext = ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Text", null));
    final StringDT actualSprache = ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_getTextByName(new IntegerDT(0, null), new StringDT("Sprache", null));
    final IntegerDT actualAnzEntries = ((UniontypEditorAllgemeinPage)activity).textuelleBeschreibung_getRowCount();
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
    activity = ((UniontypEditorAllgemeinPage)activity).returnToEditor();
    stack.push(activity);
    activity = ((UniontypEditor)activity).speziellPage();
  }
}
