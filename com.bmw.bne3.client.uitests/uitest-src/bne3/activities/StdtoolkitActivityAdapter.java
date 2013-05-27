package bne3.activities;

import de.msg.xt.mdt.base.ActivityAdapter;
import de.msg.xt.mdt.tdsl.swtbot.Button;
import de.msg.xt.mdt.tdsl.swtbot.ComboBox;
import de.msg.xt.mdt.tdsl.swtbot.Label;
import de.msg.xt.mdt.tdsl.swtbot.RadioButton;
import de.msg.xt.mdt.tdsl.swtbot.TableControl;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;
import de.msg.xt.mdt.tdsl.swtbot.TreeControl;

public interface StdtoolkitActivityAdapter extends ActivityAdapter {
  public abstract TextControl getTextControl(final String controlName);
  
  public abstract TreeControl getTreeControl(final String controlName);
  
  public abstract Label getLabel(final String controlName);
  
  public abstract Button getButton(final String controlName);
  
  public abstract TableControl getTableControl(final String controlName);
  
  public abstract ComboBox getComboBox(final String controlName);
  
  public abstract RadioButton getRadioButton(final String controlName);
}
