/* _____________________________________________________________________________
 *
 * Project: BNE
 * File:    SWTBotTableControl.java
 * Version: $Revision$
 * _____________________________________________________________________________
 *
 * Created by:        qxf0198
 * Creation date:     22.03.2013
 * Modified by:       $Author$
 * Modification date: $Date$
 * Description:       See class comment
 * _____________________________________________________________________________
 *
 * Copyright: (C) BMW 2013, all rights reserved
 * _____________________________________________________________________________
 */
package de.msg.xt.mdt.tdsl.swtbot.control;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;

import de.msg.xt.mdt.tdsl.swtbot.TableControl;

/**
 * TODO Replace with class description.
 * 
 * @version $Revision$
 * @author qxf0198
 * @since 22.03.2013
 */
public class SWTBotTableControl implements TableControl {

	protected transient SWTBotTable table;

	@Override
	public void invokeContextMenu(final String contextMenuEntry) {
		table.contextMenu(contextMenuEntry).click();
	}

	@Override
	public void isContextMenuEnabled(final String contextMenuEntry) {
		table.contextMenu(contextMenuEntry).isEnabled();
	}

	@Override
	public String getText(final Integer row, final Integer column) {
		return table.cell(row, column);
	}

	@Override
	public String getTextByName(final Integer row, final String columnName) {
		return table.cell(row, columnName);
	}

	@Override
	public void setText(final Integer row, final Integer column, final String textValue) {
		table.click(row, column);
		final SWTBot bot = new SWTBot();
		final Text text = bot.widget(widgetOfType(Text.class), table.widget);
		final SWTBotText t = new SWTBotText(text);
		t.setText(textValue);
		t.pressShortcut(KeyStroke.getInstance(SWT.CR));
	}

	@Override
	public void setTextByName(final Integer row, final String columnName, final String textValue) {
		setText(row, table.indexOfColumn(columnName), textValue);
	}

	@Override
	public Integer getRowCount() {
		return table.rowCount();
	}

}
