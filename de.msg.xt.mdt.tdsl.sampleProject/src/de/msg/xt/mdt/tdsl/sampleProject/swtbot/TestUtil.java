/* _____________________________________________________________________________
 *
 * Project: ACM
 * File: ACMTestHelper.java
 * Version: $Revision$
 * _____________________________________________________________________________
 *
 * created by: rudera
 * created at: 03.11.2010
 * changed by: $Author$
 * changed at: $Date$
 * Description: see comment on class
 * _____________________________________________________________________________
 *
 * copyright: (C) DAIMLER 2010, all rights reserved
 * _____________________________________________________________________________
 */
package de.msg.xt.mdt.tdsl.sampleProject.swtbot;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

/**
 * This class offers some static utility methods to setup and tear down ACM
 * tests, especially SWT- and SWTBot-UI-Tests.
 * 
 * @version $Revision$
 * @author $Author$
 * @since 25.08.2011
 */
public class TestUtil {

    /**
     * The default perspective that is used for UI-Tests.
     */
    private static final String DEFAULT_PERSPECTIVE = "com.daimler.acm.base.AcmPerspective"; //$NON-NLS-1$

    /**
     * The instance for the workbench bot.
     */
    private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

    /**
     * Logger instance for this class.
     */
    private static Logger logger = Logger.getLogger(TestUtil.class.toString());

    /**
     * Explicit window shell identifier.
     */
    private static final String SAVE_RESOURCE = "Save Resource";

    /**
     * This method should be called in the @Before or @BeforeClass method of
     * each SWT-Bot test case. It initializes some SWT-Bot things.
     */
    public static void initializeUiTest() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US"; //$NON-NLS-1$
    }

    /**
     * Resets the workbench to a defined state. Usually executed in an
     * 
     * @Before-method of a UI-Test. It closes the Welcome-screen and all
     *                editors, switches to the {@link #DEFAULT_PERSPECTIVE} and
     *                resets it.
     * 
     */
    public static void resetWorkbench() {
        // Zugriffe auf das UI / SWT-Komponenten muss im
        // UI-Thread erfolgen
        UIThreadRunnable.syncExec(new VoidResult() {
            @Override
            public void run() {
                resetWorkbenchInternal();
            }
        });
        // close views
        try {
            bot.viewById("com.siemens.ct.mp3m.ui.views.logical.LogicalView").close();
            bot.viewById("com.siemens.ct.mp3m.ui.views.physical.FileSystemView").close();
            bot.viewById("com.siemens.ct.mp3m.ui.views.physical.VirtualFileSystemView").close();
            bot.viewById("com.siemens.ct.mp3m.ui.views.logical.VirtualTableView").close();
        } catch (WidgetNotFoundException e) {
            // do nothing, no problem...
            logger.log(Level.INFO, "Welcome View", e);
        }
    }

    /**
     * Resets the active perspective.
     * 
     */
    public static void resetPerspective() {
        // MainMenu.window().menu(Messages.ACMTestHelper_Menu_Reset_Perspective).click();
        // bot.shell(Messages.ACMTestHelper_Title_Reset_Perspective).activate();
        // bot.button(Messages.ACMTestHelper_Button_OK).click();
    }

    /**
     * Opens the view with the given id.
     * 
     * @param viewId
     *            the id of the view (Eclipse-ID as in plugin-extension).
     * @return an {@link SWTBotView} object representing the opened view
     */
    public static SWTBotView openView(final String viewId) {
        UIThreadRunnable.syncExec(new VoidResult() {
            @Override
            public void run() {
                try {
                    IWorkbench workbench = PlatformUI.getWorkbench();
                    workbench.getActiveWorkbenchWindow().getActivePage().showView(viewId);
                } catch (WorkbenchException e) {
                    throw new WidgetNotFoundException(e.getMessage(), e);
                }
            }
        });

        return findView(viewId);
    }

    /**
     * Verifies that the view with the given id is open.
     * 
     * @param viewId
     *            the id of the view (Eclipse-ID as in plugin-extension).
     * @return true if the view is open, false otherwise
     */
    public static boolean isViewOpen(String viewId) {
        boolean isOpen = true;
        try {
            findView(viewId);
        } catch (WidgetNotFoundException ex) {
            isOpen = false;
        }
        return isOpen;
    }

    /**
     * Verifies that the view with the given id is open and active.
     * 
     * @param viewId
     *            the id of the view (Eclipse-ID as in plugin-extension).
     * @return true if the view is open and active, false otherwise
     */
    public static boolean isViewActive(String viewId) {
        boolean isOpen = true;
        try {
            findActiveView(viewId);
        } catch (WidgetNotFoundException ex) {
            isOpen = false;
        }
        return isOpen;
    }

    /**
     * Finds the view with the given id (it must be open already) and returns an
     * {@link SWTBotView} object representing the view.
     * 
     * @param viewId
     *            the id of the view (Eclipse-ID as in plugin-extension).
     * @return an {@link SWTBotView} object representing the view
     */
    public static SWTBotView findView(String viewId) {
        SWTBotView view = bot.viewById(viewId);
        view.show();
        return view;
    }

    /**
     * Finds the active view with the given id (it must be open already) and
     * returns an {@link SWTBotView} object representing the view.
     * 
     * @param viewId
     *            the id of the view (Eclipse-ID as in plugin-extension).
     * @return an {@link SWTBotView} object representing the view
     */
    public static SWTBotView findActiveView(String viewId) {
        SWTBotView view = bot.viewById(viewId);
        if (!view.isActive()) {
            throw new WidgetNotFoundException("Active view is not the requested editor: " + viewId);
        }
        return view;
    }

    /**
     * Verifies that the editor with the given id is open.
     * 
     * @param editorId
     *            the id of the editor (Eclipse-ID as in plugin-extension).
     * @return true if the editor is open, false otherwise
     */
    public static boolean isEditorOpen(String editorId) {
        boolean isOpen = true;
        try {
            findEditor(editorId);
        } catch (WidgetNotFoundException ex) {
            isOpen = false;
        }
        return isOpen;
    }

    /**
     * Verifies that the editor with the given id is open and active.
     * 
     * @param editorId
     *            the id of the editor (Eclipse-ID as in plugin-extension).
     * @return true if the editor is open and active, false otherwise
     */
    public static boolean isEditorActive(String editorId) {
        boolean isOpen = true;
        try {
            findActiveEditor(editorId);
        } catch (WidgetNotFoundException ex) {
            isOpen = false;
        }
        return isOpen;
    }

    /**
     * Finds the editor with the given id (it must be open already) and returns
     * an {@link SWTBotEditor} object representing the editor.
     * 
     * @param editorId
     *            the id of the editor (Eclipse-ID as in plugin-extension).
     * @return an {@link SWTBotEditor} object representing the editor
     */
    public static SWTBotEditor findEditor(String editorId) {
        SWTBotEditor editor = bot.editorById(editorId);
        editor.show();
        return editor;
    }

    /**
     * Finds the active editor with the given id (it must be open already) and
     * returns an {@link SWTBotEditor} object representing the editor.
     * 
     * @param editorId
     *            the id of the editor (Eclipse-ID as in plugin-extension).
     * @return an {@link SWTBotEditor} object representing the active editor
     */
    public static SWTBotEditor findActiveEditor(String editorId) {
        SWTBotEditor editor = bot.activeEditor();
        if (!editor.getReference().getId().equals(editorId)) {
            throw new WidgetNotFoundException("Active editor is not the requested editor: " + editorId);
        }
        return editor;
    }

    /**
     * Verifies that the popup with the given id is open and active.
     * 
     * @param popupId
     *            the id of the popup.
     * @return true if the popup is open and active, false otherwise
     */
    public static boolean isPopupActive(String popupId) {
        SWTBotShell shell = null;
        try {
            shell = bot.shellWithId(popupId);
        } catch (WidgetNotFoundException ex) {
            logger.log(Level.INFO, "widget not found for " + popupId, ex);
        }
        return shell != null && shell.isActive();
    }

    /**
     * Finds the active popup with the given id (it must be open already) and
     * returns an {@link SWTBotShell} object representing the popup.
     * 
     * @param popupId
     *            the id of the popup.
     * @return an {@link SWTBotShell} object representing the popup
     */
    public static SWTBotShell findActivePopup(String popupId) {
        SWTBotShell shell = bot.shellWithId(popupId);
        if (!shell.isActive()) {
            Assert.fail("Active shell is not the requested popup: " + popupId);
        }
        return shell;
    }

    /**
     * Closes the view with the given id.
     * 
     * @param viewId
     *            the id of the view (Eclipse-ID as in plugin-extension).
     */
    public static void closeView(String viewId) {
        bot.viewById(viewId).close();
    }

    /**
     * Calls the refresh-infrastructure action.
     */
    public static void refresh() {
        // MainMenu.file().subMenu(TestSupport.PluginProperties.Refresh).click();
    }

    /**
     * Opens the given perspective.
     * 
     * @param perspective
     *            the perspective to switch to. Please note that this is the
     *            perspective label as shown to the user, not the
     *            perspective-id.
     */
    public static void openPerspective(String perspective) {
        // MainMenu.window().menu(Messages.ACMTestHelper_Menu_Open_Perspective).menu(Messages.ACMTestHelper_Menu_Other).click();
        // bot.shell(Messages.ACMTestHelper_Title_Open_Perspective).activate();
        // bot.table().select(perspective);
        // bot.button(Messages.ACMTestHelper_Button_OK).click();
    }

    /**
     * Ggf. offene Fenster schließen, alle Editoren schliessen, aktuelle
     * Perspektive zuruecksetzen, Standard-Perspektive aktivieren, diese auch
     * zurücksetzen
     */
    private static void resetWorkbenchInternal() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = workbenchWindow.getActivePage();
        Shell activeShell = Display.getCurrent().getActiveShell();
        if (activeShell != null && activeShell != workbenchWindow.getShell()) {
            activeShell.close();
        }
        page.closeAllEditors(false);
        page.resetPerspective();
    }

    /**
     * Sets up an SWT-test. It creates a new shell and returns a
     * {@link Composite} to use for your widgets. This method should only be
     * called ONCE during the test case and it is recommended to call it in your
     * 
     * @BeforeClass-method. The {@link Composite} that is returned must be given
     *                      to the {@link #tearDownSWT(Composite)} -method to
     *                      clean up.
     * @return a {@link Composite} which you can use for your tests.
     */
    public static Composite setUpSWT() {
        Shell shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout());
        shell.open();
        return shell;
    }

    /**
     * Releases the resources of an SWT-Test. Should be called after the
     * execution of your tests, preferrably in your
     * 
     * @AfterClass-method. Please note that the given {@link Composite} c must
     *                     be the exact same {@link Composite} which was
     *                     returned earlier by the {@link #setUpSWT()}-method.
     * 
     * @param c
     *            the {@link Composite} that was retured during test
     *            inititlization by the {@link #setUpSWT()}-method.
     */
    public static void tearDownSWT(Composite c) {
        if (!(c instanceof Shell)) {
            throw new IllegalArgumentException("Parameter must be the composite returned by setUpSWT()!"); //$NON-NLS-1$
        }
        final Shell shell = (Shell) c;
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                shell.dispose();
            }
        });
    }

    /**
     * Confirms the warning dialog using 'OK'.
     */
    public static void confirmOkWarningDialog() {
        SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        // wbBot.shell(com.daimler.acm.base.Messages.title_ACMWarning).bot().button(com.daimler.acm.base.Messages.button_ok).click();
    }

    /**
     * Confirms the message dialog using 'YES'.
     */
    public static void confirmYesMessageDialog() {
        // SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        // wbBot.shell(com.daimler.acm.base.Messages.title_ACMConfirm).bot().button(com.daimler.acm.base.Messages.button_yes)
        // .click();
    }

    /**
     * Confirms the message dialog using 'OK'.
     */
    public static void confirmOkMessageDialog() {
        // SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        // wbBot.shell(com.daimler.acm.base.Messages.title_ACMConfirm).bot().button(com.daimler.acm.base.Messages.button_ok).click();
    }

    /**
     * Confirms the message dialog using 'No'.
     */
    public static void confirmNoMessageDialog() {
        // SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        // wbBot.shell(com.daimler.acm.base.Messages.title_ACMConfirm).bot().button(com.daimler.acm.base.Messages.button_no).click();
    }

    /**
     * Confirms the message dialog popping up when you close a dirty editor.
     */
    public static void confirmToCloseDialog() {
        SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        wbBot.shell(SAVE_RESOURCE).bot().button(0).click();
    }

    /**
     * Confirms the error dialog using 'OK'.
     */
    public static void confirmOkErrorDialog() {
        // SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        // wbBot.shell(com.daimler.acm.base.Messages.title_ACMError).bot().button(0).click();
    }

    /**
     * check if the warning dialog is active.
     * 
     * @return true if the dialog is active
     */
    public static boolean isWarningDialogActive() {
        // SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        // SWTBotShell shell =
        // wbBot.shell(com.daimler.acm.base.Messages.title_ACMWarning);
        // return shell != null && shell.isActive();
        return true;
    }

    /**
     * check if the message dialog is active.
     * 
     * @return true if the dialog is active
     */
    public static boolean isErrorDialogActive() {
        // SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        // SWTBotShell shell =
        // wbBot.shell(com.daimler.acm.base.Messages.title_ACMError);
        // return shell != null && shell.isActive();
        return true;
    }

    /**
     * check if the message dialog is active.
     * 
     * @return true if the dialog is active
     */
    public static boolean isMessageDialogActive() {
        // SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        // return
        // wbBot.shell(com.daimler.acm.base.Messages.title_ACMConfirm).isActive();
        return true;
    }

    /**
     * Confirms the message dialog popping up when you close a dirty editor.
     */
    public static void confirmNoSaveToCloseDialog() {
        SWTWorkbenchBot wbBot = new SWTWorkbenchBot();
        wbBot.shell(SAVE_RESOURCE).bot().button(1).click();
    }

    /**
     * Logs test data.
     * 
     * @param testDataSet
     *            the set of test data to be logged.
     */
    public static void logParameters(List<Object[]> testDataSet) {
        for (int j = 0; j < testDataSet.size(); j++) {
            Map<String, Object> parameters = (Map<String, Object>) testDataSet.get(j)[0];
            logParameters(parameters);
        }
    }

    /**
     * Logs test data.
     * 
     * @param parameters
     *            the test data to be logged.
     */
    public static void logParameters(Map<String, Object> parameters) {
        logParameters(parameters, -1);
    }

    /**
     * Logs test data.
     * 
     * @param parameters
     *            the test data to be logged.
     * @param index
     *            start index.
     */
    public static void logParameters(Map<String, Object> parameters, int index) {
        String logStr = getTestDataString(parameters, index);
        logger.log(Level.INFO, logStr);
        System.out.println(logStr);
    }

    /**
     * Logs test data.
     * 
     * @param parameters
     *            the test data to be logged.
     * @param index
     *            start index.
     * @return the test data as string.
     */
    public static String getTestDataString(Map<String, Object> parameters, int index) {
        StringBuffer sb = new StringBuffer();
        sb.append("Test Data [");
        if (index >= 0) {
            sb.append(index).append("]: [");
        }

        int i = 0;
        for (Entry<String, Object> entry : parameters.entrySet()) {
            sb.append(entry.toString());
            if (i < parameters.entrySet().size() - 1) {
                sb.append(", ");
            }
            i++;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Appends current system time to the input string.
     * 
     * @param s
     *            the input string.
     * @return a unique string prefixed by s.
     */
    public static String makeStringUnique(final String s) {
        return s + "-" + System.currentTimeMillis();
    }

    /**
     * Speaking method for easier scripting.
     * 
     * @return current date in format like 24.12.13
     */
    public static String currentDate_DDMMYY() {
        return currentDate(DateFormat.SHORT);
    }

    /**
     * Current date as formatted string representation.
     * 
     * @param dateStyle
     *            one of DateFormat.SHORT, MEDIUM, LONG, FULL
     * @return the formatted date string.
     */
    public static String currentDate(int dateStyle) {
        DateFormat df = DateFormat.getDateInstance(dateStyle);
        return df.format(new Date());
    }

    /**
     * Checks if the control contained in swtbotWrapper is active in the ACM
     * sense.
     * 
     * @param swtbotWrapper
     *            the SWTBot representation for the control.
     * @return true when the control is active.
     */
    public static boolean isActive(final AbstractSWTBot<? extends Widget> swtbotWrapper) {
        return UIThreadRunnable.syncExec(new Result<Boolean>() {
            @Override
            public Boolean run() {
                // if (swtbotWrapper.widget instanceof Control) {
                // return ((Control) swtbotWrapper.widget).isEnabled();
                // }
                return swtbotWrapper.isActive();
            }
        });
    }

    /**
     * Checks if the control contained in swtbotWrapper is inactive in the ACM
     * sense.
     * 
     * @param swtbotWrapper
     *            the SWTBot representation for the control.
     * @return true when the control is inactive.
     */
    public static boolean isInActive(final AbstractSWTBot<? extends Widget> swtbotWrapper) {
        return UIThreadRunnable.syncExec(new Result<Boolean>() {
            @Override
            public Boolean run() {
                // if (swtbotWrapper.widget instanceof Control) {
                // return ((Control) swtbotWrapper.widget).isEnabled();
                // }
                return !swtbotWrapper.isActive();
            }
        });
        // if (swtbotWrapper.widget instanceof Control) {
        // return AcmDialogTools.isInActive((Control) swtbotWrapper.widget);
        // }
        // return !swtbotWrapper.isEnabled();
    }
}
