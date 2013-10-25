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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.ui.resource.XtextResourceSetProvider;
import org.eclipse.xtext.util.CancelIndicator;

import com.bmw.bne3.client.uitest.m2m.UIDescriptionTransformer;
import com.bmw.smartfaces.model.EditorNode;
import com.bmw.smartfaces.model.UIDescription;
import com.google.inject.Inject;

import de.msg.xt.mdt.tdsl.tDsl.Import;
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration;
import de.msg.xt.mdt.tdsl.tDsl.TDslFactory;
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage;
import de.msg.xt.mdt.tdsl.tDsl.TestModel;
import de.msg.xt.mdt.tdsl.tDsl.Toolkit;

public class ActivityImportAction implements IObjectActionDelegate {

	private Shell shell;

	private IWorkbenchPart part;

	@Inject
	UIDescriptionTransformer transformer;

	@Inject
	XtextResourceSetProvider provider;

	@Inject
	IScopeProvider scopeProvider;

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

		final DirectoryDialog dlg = new DirectoryDialog(shell);
		dlg.setText("Select output directory");
		dlg.setMessage("Please select the output directory.");
		final String fn = dlg.open();

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
			EditorNode editorNode = (EditorNode) o;
			String fileName = fn + "/"
					+ transformer.convertToId(editorNode.getLabel()) + ".tdsl";

			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			final IPath location = Path.fromOSString(fileName);
			final IFile ifile = workspace.getRoot()
					.getFileForLocation(location);

			Resource resource = loadOrCreateResource(file);
			TestModel model = loadOrCreateTestModel(resource);

			String namespace = ifile.getName().substring(0,
					ifile.getName().length() - 4);
			String packageName = BASE_PACKAGE + "." + namespace.toLowerCase();
			PackageDeclaration pack = loadOrCreateElement(model,
					TDslPackage.Literals.TEST_MODEL__PACKAGES,
					PackageDeclaration.class, packageName);

			addImport(pack, "de.msg.xt.mdt.tdsl.swtbot.*");
			addImport(pack, "de.msg.xt.mdt.tdsl.basictypes.*");
			addImport(pack, "com.bmw.bne3.client.uitest.datatypes.*");
			addImport(pack, "com.bmw.bne3.client.uitest.activities.*");

			if (fn != null) {
				writeToFile(ifile, model);
			}

			IScope scope = scopeProvider.getScope(pack,
					TDslPackage.Literals.PACKAGE_DECLARATION__SUT_REF);
			Toolkit toolkit = (Toolkit) scope
					.getSingleElement(
							transformer
									.fqnForName("com.bmw.bne3.client.uitest.bnetoolkit.Stdtoolkit"))
					.getEObjectOrProxy();
			pack.setSutRef(toolkit);

			transformToTestModel(description, pack, (EditorNode) o);

			writeToFile(ifile, model);
		}

		MessageDialog.openInformation(shell, "Importer",
				"Activities imported successfully.");
	}

	private void addImport(PackageDeclaration pack, String string) {
		for (Import imp : pack.getImports()) {
			if (imp.getImportedNamespace().equals(string)) {
				return;
			}
		}
		Import imp = TDslFactory.eINSTANCE.createImport();
		imp.setImportedNamespace(string);
		pack.getImports().add(imp);
	}

	private TestModel loadOrCreateTestModel(Resource resource) {
		TestModel testModel = null;
		if (resource.getContents().size() > 0)
			testModel = (TestModel) resource.getContents().get(0);
		else {
			testModel = TDslFactory.eINSTANCE.createTestModel();
			resource.getContents().add(testModel);
		}
		return null;
	}

	private EAttribute getNameEAttribute(EReference ref) {
		EAttribute nameAttribute = null;
		EList<EAttribute> attributes = ref.getEReferenceType()
				.getEAllAttributes();
		for (EAttribute attribute : attributes) {
			if (attribute.getName().equalsIgnoreCase("name")) {
				nameAttribute = attribute;
				break;
			}
		}
		return nameAttribute;
	}

	private <T extends EObject> T loadOrCreateElement(EObject parentElement,
			EReference ref, Class<T> elementClass, String name) {
		T elem = null;
		List<EObject> list = (List<EObject>) parentElement.eGet(ref);
		if (list != null) {
			for (EObject o : list) {
				EAttribute nameAttribute = getNameEAttribute(ref);
				if (o.eGet(nameAttribute).equals(name)) {
					elem = (T) o;
					break;
				}
			}
		}
		if (elem == null) {
			elem = (T) TDslFactory.eINSTANCE.create(ref.getEReferenceType());
			list.add(elem);
		}
		return elem;
	}

	private Resource loadOrCreateResource(IFile file) {
		final ResourceSet rs = provider.get(file.getProject());
		Resource resource = null;
		URI url = URI.createPlatformResourceURI(file.getFullPath().toString(),
				true);
		if (file.exists())
			resource = rs.getResource(url, true);
		else
			resource = rs.createResource(url);
		return resource;
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
