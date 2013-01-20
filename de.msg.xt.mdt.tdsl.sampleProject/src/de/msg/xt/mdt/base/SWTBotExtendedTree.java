/* _____________________________________________________________________________
 *
 * Project: ACM
 * File:    SWTBotExtendedTree.java
 * Version: $Revision$
 * _____________________________________________________________________________
 *
 * Created by:        reitznej
 * Creation date:     17.01.2012
 * Modified by:       $Author$
 * Modification date: $Date$
 * Description:       See class comment
 * _____________________________________________________________________________
 *
 * Copyright: (C) DAIMLER 2012, all rights reserved
 * _____________________________________________________________________________
 */
package de.msg.xt.mdt.base;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.SelfDescribing;

/**
 * Extends the functionality provided by the underlying SWTBotTree.
 * 
 * @version $Revision$
 * @author reitznej
 * @since 17.01.2012
 */
public class SWTBotExtendedTree extends SWTBotTree {

    /**
     * Constructor description.
     * 
     * @param tree
     */
    public SWTBotExtendedTree(Tree tree) {
        super(tree);
    }

    /**
     * Constructor description.
     * 
     * @param tree
     * @param description
     */
    public SWTBotExtendedTree(Tree tree, SelfDescribing description) {
        super(tree, description);
    }

    /**
     * Expands whole tree.
     */
    public void expandAll() {
        expandAllToLevel(-1);
    }

    /**
     * Expands all nodes starting at level 0 up to levelLimit.
     * 
     * @param levelLimit
     *            the max level for which nodes are expanded. Choose -1 for unlimited level.
     */
    public void expandAllToLevel(int levelLimit) {
        expandAll(getAllItems(), 0, levelLimit);
    }

    protected void expandAll(SWTBotTreeItem[] items, int curLevel, int levelLimit) {
        if (levelLimit >= 0 && curLevel > levelLimit) {
            return;
        }
        for (SWTBotTreeItem item : items) {
            item.expand();
            expandAll(item.getItems(), curLevel + 1, levelLimit);
        }
    }

    /**
     * Expands all nodes beginning from to top to find the row on which a double click will be executed.
     * 
     * @param row
     *            the row index starting at 0 on which the double click shall be executed.
     */
    public void doubleClick(int row) {
        doubleClick(row, -1);
    }

    /**
     * Expands all nodes beginning from to top to find the row on which a double click will be executed.
     * 
     * @param row
     *            the row index starting at 0 on which the double click shall be executed.
     * @param expandLevel
     *            the max level for which nodes are expanded. Choose -1 for unlimited level.
     */
    public void doubleClick(int row, int expandLevel) {
        doubleClick(getAllItems(), 0, 0, row, expandLevel);
    }

    protected boolean doubleClick(SWTBotTreeItem[] items, int curRowCount, int curLevel, int destRow, int levelLimit) {
        int nextRow = curRowCount;
        if (levelLimit >= 0 && curLevel > levelLimit) {
            return false;
        }
        for (SWTBotTreeItem item : items) {
            if (curRowCount == destRow) {
                item.doubleClick();
                return true;
            }
            if (doubleClick(item.getItems(), ++nextRow, curLevel + 1, destRow, levelLimit)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * execute a double click on the last element in the list. Traverse through the tree, find and expand the nodes with
     * the given names.
     * 
     * @param nodeName
     *            list of the node names that lead to the last element
     */
    public void doubleClick(String... nodeName) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < nodeName.length; i++) {
            list.add(nodeName[i]);
        }

        doubleClick(getAllItems(), 0, list);
    }

    protected boolean doubleClick(SWTBotTreeItem[] items, int curRowCount, List<String> nodeName) {
        int nextRow = curRowCount;
        for (SWTBotTreeItem item : items) {
            //find the node with the given name
            if (!item.getText().trim().equals(nodeName.get(curRowCount).trim())) {
                continue;
            }
            if (curRowCount < nodeName.size() - 1) {
                //expand if not yet expanded
                if (!item.isExpanded()) {
                    item.expand();
                }

                //repeat with next level
                if (doubleClick(item.getItems(), ++nextRow, nodeName)) {
                    return true;
                }
            } else {
                //open last element
                item.doubleClick();
                return true;
            }
        }
        return false;
    }
}
