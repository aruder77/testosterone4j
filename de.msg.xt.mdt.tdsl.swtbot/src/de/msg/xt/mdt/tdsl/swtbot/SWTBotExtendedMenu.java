/* _____________________________________________________________________________
 *
 * Project: ACM
 * File:    SWTBotMenuFinder.java
 * Version: $Revision$
 * _____________________________________________________________________________
 *
 * Created by:        reitznej
 * Creation date:     22.11.2011
 * Modified by:       $Author$
 * Modification date: $Date$
 * Description:       See class comment
 * _____________________________________________________________________________
 *
 * Copyright: (C) DAIMLER 2011, all rights reserved
 * _____________________________________________________________________________
 */
package de.msg.xt.mdt.tdsl.swtbot;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.*;

import java.util.List;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.finders.MenuFinder;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

/**
 * Provides a fluent API for menu items which can be found by their test id.
 * 
 * @version $Revision$
 * @author reitznej
 * @since 22.11.2011
 */
public class SWTBotExtendedMenu extends SWTBotMenu {

    /**
     * Contrary to method menu a SWTBotExtendedMenu is instantiated and returned.
     * 
     * @param i18nKey
     * @return the extended wrapper for menu
     */
    public SWTBotExtendedMenu subMenu(final String i18nKey) {
        return new SWTBotExtendedMenu(menu(i18nKey));
    }

    /**
     * @param w
     *            the widget.
     * @param description
     *            the description of the widget, this will be reported by {@link #toString()}
     */
    public SWTBotExtendedMenu(MenuItem w, SelfDescribing description) {
        super(w, description);
    }

    /**
     * Wrapping of an existing SWTBotMenu object.
     * 
     * @param botMenu
     *            the object that will get wrapped.
     */
    public SWTBotExtendedMenu(final SWTBotMenu botMenu) {
        this(botMenu.widget, WidgetMatcherFactory.withId(botMenu.getId()));
    }

    /**
     * Gets the menu matching the given id registered in the widget's data with the swtbot test key.
     * 
     * @param id
     *            the id of the menu item that is to be found
     * @return the first menu that matches the id
     */
    public SWTBotExtendedMenu menuWithId(final String id) {
        final Matcher<? extends Widget> matcher = withId(id);
        MenuItem menuItem = syncExec(new WidgetResult<MenuItem>() {
            @Override
            public MenuItem run() {
                Menu bar = SWTBotExtendedMenu.this.widget.getMenu();
                System.out.println(" bar:  " + bar);
                Matcher<MenuItem> withId = withId(id);
                List<MenuItem> menus = new MenuFinder().findMenus(bar, withId, true);
                if (!menus.isEmpty()) {
                    return menus.get(0);
                }
                return null;
            }
        });
        return new SWTBotExtendedMenu(menuItem, matcher);
    }

    /**
     * 
     * check if the menu item exists in this menu.
     * 
     * @param localizedTitle
     *            the localized title of the menu item
     * @return true if the item with the given name was found, false otherwise
     */
    public boolean checkIfMenuItemExists(final String localizedTitle) {
        return syncExec(new BoolResult() {
            @Override
            public Boolean run() {
                final MenuItem[] menuItems = SWTBotExtendedMenu.this.widget.getMenu().getItems();
                for (int i = 0; i < menuItems.length; i++) {
                    if (menuItems[i].getText().equals(localizedTitle)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
