/**
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.testosterone4j.tdsl.tests.ui.contentassist;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.common.types.access.jdt.IJavaProjectProvider;
import org.eclipse.xtext.common.types.access.jdt.JdtTypeProviderFactory;
import org.eclipse.xtext.junit4.ui.ContentAssistProcessorTestBuilder;
import org.eclipse.xtext.junit4.util.ResourceLoadHelper;
import org.eclipse.xtext.resource.FileExtensionProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.testosterone4j.tdsl.tests.util.PluginProjectUtil;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

@SuppressWarnings("all")
public class MyAbstractContentAssistTest implements ResourceLoadHelper, IJavaProjectProvider {
	@Inject
	private Provider<XtextResourceSet> resourceSetProvider;

	@Inject
	private FileExtensionProvider fileExtensionProvider;

	@Inject
	private Injector injector;

	private static IJavaProject javaProject;

	@BeforeClass
	public static void setUp() {
		try {
			final List<String> srcFolders = new ArrayList<String>();
			srcFolders.add("src");
			final List<IProject> referencedProjects = Collections.EMPTY_LIST;
			final Set<String> requiredBundles = new HashSet<String>();
			requiredBundles.add("org.junit;bundle-version=\"4.11.0\"");
			requiredBundles.add("org.testosterone4j.base");
			requiredBundles.add("org.eclipse.xtext.xbase.lib");
			// requiredBundles.add("org.eclipse.xtext");
			// requiredBundles.add("org.eclipse.xtext.xbase");
			// requiredBundles.add("org.eclipse.xtext.generator");
			// requiredBundles.add("org.eclipse.emf.codegen.ecore");
			// requiredBundles.add("org.eclipse.emf.mwe.utils");
			// requiredBundles.add("org.eclipse.emf.mwe2.launch");
			// requiredBundles.add("org.eclipse.xtext.util");
			// requiredBundles.add("org.eclipse.emf.ecore");
			// requiredBundles.add("org.eclipse.emf.common");
			// requiredBundles.add("org.antlr.runtime");
			// requiredBundles.add("org.eclipse.xtext.common.types");
			// requiredBundles.add("org.apache.commons.logging");
			// requiredBundles.add("org.eclipse.xtend.lib");
			// requiredBundles.add("com.google.guava");
			// requiredBundles.add("org.slf4j.api");
			// requiredBundles.add("org.eclipse.xtext.xbase.junit");
			final List<String> exportedPackages = Collections.EMPTY_LIST;
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			for (final IWorkbenchWindow w : PlatformUI.getWorkbench().getWorkbenchWindows()) {
				final IWorkbenchWindow win = w;
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						System.out.println("Window: " + win.getShell().getText());
					}
				});
			}
			window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
			final Shell shell = window.getShell();
			final IProject project = PluginProjectUtil.createPluginProject("contentAssistTest", srcFolders, referencedProjects,
					requiredBundles, exportedPackages, new NullProgressMonitor(), shell);
			MyAbstractContentAssistTest.javaProject = JavaCore.create(project);
		} catch (final Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	@AfterClass
	public static void tearDown() {
		try {
			final IProject _project = MyAbstractContentAssistTest.javaProject.getProject();
			final NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
			_project.delete(true, _nullProgressMonitor);
		} catch (final Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<String> expect(final String[]... arrays) {
		final ArrayList<String> expectation = CollectionLiterals.<String> newArrayList();
		for (final String[] array : arrays) {
			CollectionExtensions.<String> addAll(expectation, array);
		}
		return expectation;
	}

	protected ContentAssistProcessorTestBuilder newBuilder() throws Exception {
		final ContentAssistProcessorTestBuilder _contentAssistProcessorTestBuilder = new ContentAssistProcessorTestBuilder(
				injector, this);
		return _contentAssistProcessorTestBuilder;
	}

	@Override
	public XtextResource getResourceFor(final InputStream stream) {
		try {
			XtextResource _xblockexpression = null;
			{
				final XtextResourceSet set = resourceSetProvider.get();
				initializeTypeProvider(set);
				final String _primaryFileExtension = fileExtensionProvider.getPrimaryFileExtension();
				final String _plus = ("Test." + _primaryFileExtension);
				final URI _createURI = URI.createURI(_plus);
				final Resource result = set.createResource(_createURI);
				result.load(stream, null);
				_xblockexpression = (((XtextResource) result));
			}
			return _xblockexpression;
		} catch (final Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	protected void initializeTypeProvider(final XtextResourceSet set) {
		final JdtTypeProviderFactory _jdtTypeProviderFactory = new JdtTypeProviderFactory(this);
		final JdtTypeProviderFactory typeProviderFactory = _jdtTypeProviderFactory;
		typeProviderFactory.findOrCreateTypeProvider(set);
		final IJavaProject _javaProject = getJavaProject(set);
		set.setClasspathURIContext(_javaProject);
	}

	@Override
	public IJavaProject getJavaProject(final ResourceSet resourceSet) {
		return MyAbstractContentAssistTest.javaProject;
	}
}
