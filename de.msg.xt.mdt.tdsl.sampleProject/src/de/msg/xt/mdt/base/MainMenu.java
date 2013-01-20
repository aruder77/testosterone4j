/* _____________________________________________________________________________
 *
 * Project: ACM
 * File:    MenuTestlet.java
 * Version: $Revision$
 * _____________________________________________________________________________
 *
 * Created by:        reitznej
 * Creation date:     25.11.2011
 * Modified by:       $Author$
 * Modification date: $Date$
 * Description:       See class comment
 * _____________________________________________________________________________
 *
 * Copyright: (C) DAIMLER 2011, all rights reserved
 * _____________________________________________________________________________
 */
package de.msg.xt.mdt.base;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * Test helper class for accessing main menus.
 * 
 * @version $Revision$
 * @author reitznej
 * @since 25.11.2011
 */
public class MainMenu {

    /**
     * The instance for the workbench bot.
     */
    private static SWTWorkbenchBot workbenchBot = new SWTWorkbenchBot();

    private static SWTBotExtendedMenu botMenu(final String i18nKey) {
        return new SWTBotExtendedMenu(workbenchBot.menu(i18nKey));
    }

    /**
     * Access to main menu entry.
     * 
     * @return null if widget cannot be found
     */
    public static SWTBotExtendedMenu file() {
        return botMenu("File");
    }

    /**
     * Access to main menu entry.
     * 
     * @return null if widget cannot be found
     */
    public static SWTBotExtendedMenu edit() {
        return botMenu("Edit");
    }

    /**
     * Access to main menu entry.
     * 
     * @return null if widget cannot be found
     */
    public static SWTBotExtendedMenu window() {
        return botMenu("Window");
    }

    /**
     * Access to main menu entry.
     * 
     * @return null if widget cannot be found
     */
    public static SWTBotExtendedMenu help() {
        return botMenu("Help");
    }
}
