package bne3.usecases.paket;

import bne3.activities.EditorActivity;
import bne3.activities.EthernetNavigator;
import bne3.activities.MainWindow;
import bne3.activities.datatypetype.BasisDatentypEditor;
import bne3.activities.datatypetype.BasisdatentypEditorAllgemeinPage;
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
public class CreatePaket extends BaseUseCase implements Runnable {
  @XmlElement
  private ShortName codingShortName;
  
  public CreatePaket() {
  }
  
  public CreatePaket(final Generator generator) {
    this();
    this.generator = generator;
    this.codingShortName = this.getOrGenerateValue(bne3.datatypes.ShortName.class, "bne3.usecases.paket.CreatePaket.codingShortName");
  }
  
  public CreatePaket(final ShortName codingShortName) {
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
    LongName strcd58010 = getOrGenerateValue(LongName.class, "strcd58010");
    ((BasisdatentypEditorAllgemeinPage)activity).longname_setText(strcd58010);
    final LongName longName = strcd58010;
    ShortName str6cdce049 = getOrGenerateValue(ShortName.class, "str6cdce049");
    ((BasisdatentypEditorAllgemeinPage)activity).shortname_setText(str6cdce049);
    final ShortName shortName = str6cdce049;
    StringDT contextMenuEntry3876b3d9 = new StringDT("Element hinzuf\u00FCgen", null);
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry3876b3d9);
    IntegerDT row403a5450 = new IntegerDT(0, null);
    StringDT columnName403a5450 = new StringDT("Text", null);
    StringDT textValue403a5450 = getOrGenerateValue(StringDT.class, "textValue403a5450");
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row403a5450, columnName403a5450, textValue403a5450);
    final StringDT text1 = textValue403a5450;
    IntegerDT row184ff809 = new IntegerDT(0, null);
    StringDT columnName184ff809 = new StringDT("Sprache", null);
    LanguageDT textValue184ff809 = getOrGenerateValue(LanguageDT.class, "textValue184ff809");
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row184ff809, columnName184ff809, textValue184ff809);
    final LanguageDT sprache = textValue184ff809;
    StringDT contextMenuEntry12867ccc = new StringDT("Element hinzuf\u00FCgen", null);
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenu(contextMenuEntry12867ccc);
    IntegerDT row54a52df3 = new IntegerDT(1, null);
    StringDT columnName54a52df3 = new StringDT("Text", null);
    StringDT textValue54a52df3 = new StringDT("Second", null);
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_setTextByName(row54a52df3, columnName54a52df3, textValue54a52df3);
    IntegerDT row40569319 = new IntegerDT(1, null);
    StringDT columnName40569319 = new StringDT("Sprache", null);
    LanguageDT textValue40569319 = getOrGenerateValue(LanguageDT.class, "textValue40569319");
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_selectValueByName(row40569319, columnName40569319, textValue40569319);
    IntegerDT row399297bc = new IntegerDT(1, null);
    StringDT contextMenuEntry399297bc = new StringDT("Element l\u00F6schen", null);
    ((BasisdatentypEditorAllgemeinPage)activity).textuelleBeschreibung_invokeContextMenuOnRow(row399297bc, contextMenuEntry399297bc);
    stack.push(activity);
    activity = ((BasisdatentypEditorAllgemeinPage)activity).returnToEditor();
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
    stack.push(activity);
    activity = ((BasisdatentypEditorAllgemeinPage)activity).returnToEditor();
  }
}
