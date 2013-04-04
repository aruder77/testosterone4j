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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.TableControl;
import de.msg.xt.mdt.tdsl.swtbot.util.ContextMenuHelper;

/**
 * TODO Replace with class description.
 * 
 * @version $Revision$
 * @author qxf0198
 * @since 22.03.2013
 */
public class SWTBotTableControl implements TableControl {

	protected transient SWTBotTable table;

	public static SWTBotTableControl findControl(final ActivityContext context, final String id) {
		return new SWTBotTableControl(context.getBot().tableWithId(id));
	}

	public SWTBotTableControl(final SWTBotTable table) {
		this.table = table;
	}

	@Override
	public void invokeContextMenu(final String contextMenuEntry) {
		// table.contextMenu(contextMenuEntry).click();

		ContextMenuHelper.clickContextMenu(table, contextMenuEntry);
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
		table.doubleClick(row, column);
		final SWTBot bot = new SWTBot();
		final Text text = bot.widget(widgetOfType(Text.class), table.widget);
		final SWTBotText t = new SWTBotText(text);
		t.setText(textValue);
		t.pressShortcut(SWT.None, SWT.CR);
	}

	@Override
	public void setTextByName(final Integer row, final String columnName, final String textValue) {
		setText(row, table.indexOfColumn(columnName), textValue);
	}

	@Override
	public void selectValue(final Integer row, final Integer column, final String textValue) {
		table.doubleClick(row, column);
		final SWTBot bot = new SWTBot();
		final CCombo text = bot.widget(widgetOfType(CCombo.class), table.widget);
		final SWTBotCCombo t = new SWTBotCCombo(text);
		t.setSelection(textValue);
		t.pressShortcut(SWT.None, SWT.CR);
	}

	@Override
	public void selectValueByName(final Integer row, final String columnName, final String textValue) {
		selectValue(row, table.indexOfColumn(columnName), textValue);
	}

	@Override
	public Integer getRowCount() {
		return table.rowCount();
	}

	@Override
	public void invokeContextMenuOnRow(final Integer row, final String contextMenuEntry) {
		final SWTBotTableItem item = table.getTableItem(row);
		item.select();
		ContextMenuHelper.clickContextMenu(table, contextMenuEntry);
	}
}
