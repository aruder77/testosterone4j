package com.bmw.bne3.client.uitest.importer.popup.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.util.StringInputStream;

public class ConvertControlAction implements IObjectActionDelegate {

	private Shell shell;

	private IWorkbenchPart part;

	private final String[][] controlMap = new String[][] {
			{ "com_bmw_smartfaces_boxed_controls_StringFieldFactory", "TextControl" },
			{ "com_bmw_smartfaces_boxed_controls_LongFieldFactory", "TextControl" },
			{ "com_bmw_smartfaces_boxed_controls_ComboFieldFactory", "ComboBox" },
			{ "com_bmw_smartfaces_boxed_controls_DoubleFieldFactory", "TextControl" },
			{ "com_bmw_smartfaces_boxed_controls_HexDecBinFieldFactory", "TextControl" }, // TODO
			{ "com_bmw_smartfaces_boxed_controls_SpacerFieldFactory", "Label" },
			{ "com_bmw_smartfaces_boxed_controls_TableControlFactory", "TableControl" },

			{ "com_bmw_smartfaces_boxed_controls_buttons_RadioButtonFieldFactory", "RadioButton" },
			{ "com_bmw_smartfaces_boxed_controls_buttons_PushButtonFieldFactory", "Button" },
			{ "com_bmw_smartfaces_boxed_controls_buttons_CheckButtonFieldFactory", "CheckBox" },

			{ "com_bmw_smartfaces_boxed_controls_browser_BrowserFieldFactory", "StyledText" },

			{ "com_bmw_smartfaces_boxed_controls_console_ConsoleFieldFactory", "TextControl" },

			{ "com_bmw_bne3_client_controls_EnumComboFactory", "TableControl" },
			{ "com_bmw_bne3_client_controls_TypeSelectionComboFactory", "TableControl" },
			{ "com_bmw_bne3_client_controls_NavigationFilterFactory", "TextControl" }, // TODO
			{ "com_bmw_bne3_client_controls_NavigationTreeViewerFactory", "Tree" },

			{ "com_bmw_bne3_client_controls_display_DisplayPackageFieldFactory", "TableControl" },
			{ "com_bmw_bne3_client_controls_display_DisplayTypeFieldFactory", "TableControl" } };

	/**
	 * Constructor for Action1.
	 */
	public ConvertControlAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	@Override
	public void setActivePart(final IAction action, final IWorkbenchPart targetPart) {
		part = targetPart;
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(final IAction action) {
		final IFile file = getSelectedFile();
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents()));
			final StringBuffer sb = new StringBuffer();
			String line = reader.readLine();
			while (line != null) {
				String currentLine = line;
				for (final String[] element : controlMap) {
					currentLine = currentLine.replaceAll(element[0], element[1]);
				}
				sb.append(currentLine + "\n");
				line = reader.readLine();
			}
			reader.close();
			file.setContents(new StringInputStream(sb.toString()), true, true, null);
		} catch (final CoreException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private IFile getSelectedFile() {
		final ISelectionService selectionService = part.getSite().getWorkbenchWindow().getSelectionService();
		final IStructuredSelection selection = (IStructuredSelection) selectionService.getSelection();
		final IFile file = (IFile) selection.getFirstElement();
		return file;
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(final IAction action, final ISelection selection) {
	}

}
