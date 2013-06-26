package com.bmw.bne3.client.uitest.importer.popup.actions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.ui.resource.XtextResourceSetProvider;
import org.eclipse.xtext.util.CancelIndicator;

import com.bmw.bne3.client.uitest.m2m.UIDescriptionTransformer;
import com.bmw.smartfaces.model.EditorNode;
import com.bmw.smartfaces.model.UIDescription;
import com.google.inject.Inject;

import de.msg.xt.mdt.tdsl.tDsl.Import;
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration;
import de.msg.xt.mdt.tdsl.tDsl.TDslFactory;
import de.msg.xt.mdt.tdsl.tDsl.TestModel;

public class ActivityImportAction implements IObjectActionDelegate {

	private Shell shell;

	private IWorkbenchPart part;

	@Inject
	UIDescriptionTransformer transformer;

	@Inject
	XtextResourceSetProvider provider;

	private static String BASE_PACKAGE = "com.bmw.bne3.client.uitest.activities";

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
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
		part = targetPart;
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(final IAction action) {
		final IFile file = getSelectedFile();
		final UIDescription description = readModel(file);

		final FileDialog dlg = new FileDialog(shell, SWT.SAVE);
		dlg.setFilterNames(new String[] { "TDsl Files" });
		dlg.setFilterExtensions(new String[] { "*.tdsl" });
		final String fn = dlg.open();

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IPath location = Path.fromOSString(fn);
		final IFile ifile = workspace.getRoot().getFileForLocation(location);

		final TestModel model = TDslFactory.eINSTANCE.createTestModel();
		final PackageDeclaration pack = TDslFactory.eINSTANCE
				.createPackageDeclaration();
		pack.setName("fakepackage");
		model.getPackages().add(pack);
		final Import imp0 = TDslFactory.eINSTANCE.createImport();
		imp0.setImportedNamespace("de.msg.xt.mdt.tdsl.swtbot.*");
		pack.getImports().add(imp0);

		String namespace = ifile.getName().substring(0,
				ifile.getName().length() - 4);
		pack.setName(BASE_PACKAGE + "." + namespace.toLowerCase());
		Import imp = TDslFactory.eINSTANCE.createImport();
		imp.setImportedNamespace("de.msg.xt.mdt.tdsl.swtbot.*");
		pack.getImports().add(imp);
		Import imp2 = TDslFactory.eINSTANCE.createImport();
		imp2.setImportedNamespace("de.msg.xt.mdt.tdsl.basictypes.*");
		pack.getImports().add(imp2);
		Import imp3 = TDslFactory.eINSTANCE.createImport();
		imp3.setImportedNamespace("com.bmw.bne3.client.uitest.datatypes.*");
		pack.getImports().add(imp3);
		Import imp4 = TDslFactory.eINSTANCE.createImport();
		imp4.setImportedNamespace("com.bmw.bne3.client.uitest.activities.*");
		pack.getImports().add(imp4);

		if (fn != null) {
			writeToFile(ifile, model);
		}

		List<EditorNode> editorList = EcoreUtil2.getAllContentsOfType(
				description, EditorNode.class);
		ILabelProvider labelProvider = new LabelProvider() {
			@Override
			public String getText(Object element) {
				EditorNode node = (EditorNode) element;
				return node.getLabel();
			}
		};
		ListSelectionDialog editorSelectionDialog = new ListSelectionDialog(
				shell, editorList, ArrayContentProvider.getInstance(),
				labelProvider, "Please select editors to import");
		editorSelectionDialog.setBlockOnOpen(true);
		int result = editorSelectionDialog.open();

		Object[] resultList = editorSelectionDialog.getResult();

		for (Object o : resultList) {
			transformToTestModel(description, pack, (EditorNode) o);
		}

		writeToFile(ifile, model);

		MessageDialog.openInformation(shell, "Importer",
				"Activities imported successfully.");
	}

	private void transformToTestModel(final UIDescription description,
			final PackageDeclaration pack, EditorNode editorNode) {
		transformer.transform(description, pack, editorNode);
	}

	private void writeToFile(final IFile file, final TestModel model) {
		final ResourceSet rs = provider.get(file.getProject());
		// final Resource resource =
		// rs.createResource(URI.createURI(file.getLocation().toString()));
		final Resource resource = rs
				.createResource(URI.createPlatformResourceURI(file
						.getFullPath().toString(), true));

		if (!resource.getContents().contains(model)) {
			resource.getContents().add(model);
		}
		try {
			resource.save(new FileOutputStream(file.getLocation().toString()),
					SaveOptions.newBuilder().format().getOptions()
							.toOptionsMap());
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		/*
		 * URI uri = d.eResource().getURI(); uri = uri.trimFragment(); uri =
		 * uri.trimFileExtension(); uri = uri.appendFileExtension("model");
		 * ResourceSet rSet = d.eResource().getResourceSet(); final
		 * IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace()
		 * .getRoot(); IResource file =
		 * workspaceRoot.findMember(uri.toPlatformString(true)); if (file ==
		 * null || !file.exists()) { Resource createResource =
		 * rSet.createResource(uri);
		 * createResource.save(Collections.emptyMap());
		 * createResource.setTrackingModification(true); } final Resource
		 * resource = rSet.getResource(uri, true);
		 * 
		 * if (obj instanceof DiagramDialog) { final CommandStack commandStack =
		 * editingDomain.getCommandStack(); commandStack.execute(new
		 * RecordingCommand(editingDomain) {
		 * 
		 * @Override protected void doExecute() { //Save DiagramDialog at proper
		 * position ((DocumentRoot)
		 * resource.getContents().get(0)).getProcedure()
		 * .get(0).add((DiagramDialog ) obj); } }); }
		 */
	}

	private IFile getSelectedFile() {
		final ISelectionService selectionService = part.getSite()
				.getWorkbenchWindow().getSelectionService();
		final IStructuredSelection selection = (IStructuredSelection) selectionService
				.getSelection();
		final IFile file = (IFile) selection.getFirstElement();
		return file;
	}

	private UIDescription readModel(final IFile file) {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource resource = rs.getResource(
				URI.createURI(file.getLocationURI().toString()), true);
		EcoreUtil2.resolveAll(resource, new CancelIndicator() {

			@Override
			public boolean isCanceled() {
				return false;
			}
		});
		return (UIDescription) resource.getContents().get(0);
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
	}

}
