package de.msg.xt.mdt.tdsl.swtbot.control;

import java.util.StringTokenizer;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.TreeControl;

public class SWTBotTreeControl implements TreeControl {

	protected transient SWTBotTree swtBotTree;

	public static SWTBotTreeControl findControl(final ActivityContext context, final String id) {
		return new SWTBotTreeControl(context.getBot().tree());
	}

	public SWTBotTreeControl(final SWTBotTree tree) {
		swtBotTree = tree;
	}

	@Override
	public void selectNode(final String nodePath) {
		getItem(nodePath).select();
	}

	private SWTBotTreeItem getItem(final String nodePath) {
		final StringTokenizer st = new StringTokenizer(nodePath, "/");
		SWTBotTreeItem item = swtBotTree.expandNode(st.nextToken(), true);
		while (st.hasMoreTokens()) {
			final String nodeText = st.nextToken();
			item = item.getNode(nodeText);
		}
		return item;
	}

	@Override
	public void doubleClickNode(final String nodePath) {
		getItem(nodePath).doubleClick();
	}

	@Override
	public Boolean isEnabled() {
		return swtBotTree.isEnabled();
	}

	@Override
	public void invokeContextMenu(final String nodePath, final String contextMenuEntry) {
		getItem(nodePath).contextMenu(contextMenuEntry).click();
	}

	@Override
	public Boolean isContextMenuEnabled(final String nodePath, final String contextMenuEntry) {
		return getItem(nodePath).contextMenu(contextMenuEntry).isEnabled();
	}

	@Override
	public Boolean hasChildNode(final String nodePath, final String nodePattern, final Boolean recursive) {
		SWTBotTreeItem[] items = null;
		if (nodePath.isEmpty() || nodePath.equals("/")) {
			items = swtBotTree.getAllItems();
		} else {
			items = getItem(nodePath).getItems();
		}
		for (final SWTBotTreeItem item : items) {
			if (item.getText().matches(nodePattern)) {
				return true;
			}
			if (recursive) {
				if (hasChildNode(nodePath + "/" + item.getText(), nodePattern, true)) {
					return true;
				}
			}
		}
		return false;
	}
}