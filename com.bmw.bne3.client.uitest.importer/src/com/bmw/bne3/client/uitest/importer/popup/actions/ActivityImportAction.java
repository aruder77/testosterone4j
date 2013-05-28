package com.bmw.bne3.client.uitest.importer.popup.actions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.resource.SaveOptions;

import com.bmw.bne3.client.uitest.m2m.UIDescriptionTransformer;
import com.bmw.smartfaces.model.UIDescription;
import com.google.inject.Inject;

import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration;
import de.msg.xt.mdt.tdsl.tDsl.TDslFactory;
import de.msg.xt.mdt.tdsl.tDsl.TestModel;

public class ActivityImportAction implements IObjectActionDelegate {

	private Shell shell;

	private IWorkbenchPart part;

	@Inject
	UIDescriptionTransformer transformer;

	/**
	 * Constructor for Action1.
	 */
	public ActivityImportAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.part = targetPart;
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		IFile file = getSelectedFile();
		UIDescription description = readModel(file);

		TestModel model = TDslFactory.eINSTANCE.createTestModel();
		PackageDeclaration pack = TDslFactory.eINSTANCE
				.createPackageDeclaration();
		pack.setName("fakepackage");
		model.getPackages().add(pack);

		FileDialog dlg = new FileDialog(shell, SWT.SAVE);
		dlg.setFilterNames(new String[] { "TDsl Files" });
		dlg.setFilterExtensions(new String[] { "*.tdsl" });
		String fn = dlg.open();
		if (fn != null)
			writeToFile(fn, model);

		transformToTestModel(description, model);

		writeToFile(fn, model);

		MessageDialog.openInformation(shell, "Importer",
				"Activities imported successfully.");
	}

	private TestModel transformToTestModel(UIDescription description,
			TestModel model) {
		return transformer.transform(description, model);
	}

	private void writeToFile(String fileName, TestModel model) {
		ResourceSet rs = new ResourceSetImpl();
		// File f = new File(fileName);
		// Resource resource = null;
		// if (f.exists()) {
		// resource = rs.getResource(URI.createURI(fileName), true);
		// } else {
		// resource = rs.createResource(URI.createURI(fileName));
		// }
		Resource resource = rs.createResource(URI.createURI(fileName));

		if (!resource.getContents().contains(model)) {
			resource.getContents().add(model);
		}
		try {
			resource.save(new FileOutputStream(fileName), SaveOptions
					.newBuilder().format().getOptions().toOptionsMap());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private IFile getSelectedFile() {
		ISelectionService selectionService = this.part.getSite()
				.getWorkbenchWindow().getSelectionService();
		IStructuredSelection selection = (IStructuredSelection) selectionService
				.getSelection();
		IFile file = (IFile) selection.getFirstElement();
		return file;
	}

	private UIDescription readModel(IFile file) {
		ResourceSet rs = new ResourceSetImpl();
		Resource resource = rs.getResource(
				URI.createURI(file.getLocationURI().toString()), true);
		return (UIDescription) resource.getContents().get(0);
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
