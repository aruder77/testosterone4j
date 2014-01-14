/**
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.testosterone4j.tdsl.tests.ui.contentassist;

import static org.eclipse.xtext.junit4.ui.util.IResourcesSetupUtil.monitor;

import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.xtext.common.types.access.jdt.IJavaProjectProvider;
import org.eclipse.xtext.common.types.access.jdt.JdtTypeProviderFactory;
import org.eclipse.xtext.junit4.ui.ContentAssistProcessorTestBuilder;
import org.eclipse.xtext.junit4.ui.util.JavaProjectSetupUtil;
import org.eclipse.xtext.junit4.util.ResourceLoadHelper;
import org.eclipse.xtext.resource.FileExtensionProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

@SuppressWarnings("all")
public class MyAbstractContentAssistTest implements ResourceLoadHelper,
		IJavaProjectProvider {
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
			IProject project = JavaProjectSetupUtil
					.createSimpleProject("contentAssistTest");
			JavaCore.initializeAfterLoad(monitor());
			IJavaProject javaProject = JavaProjectSetupUtil
					.makeJavaProject(project);

			JavaProjectSetupUtil
					.addToClasspath(
							javaProject,
							JavaCore.newContainerEntry(new Path(
									"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.6")));
			JavaProjectSetupUtil.addToClasspath(javaProject, JavaCore
					.newContainerEntry(new Path(
							"org.eclipse.jdt.junit.JUNIT_CONTAINER/4")));

			MyAbstractContentAssistTest.javaProject = javaProject;

		} catch (Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	@AfterClass
	public static void tearDown() {
		try {
			IProject _project = MyAbstractContentAssistTest.javaProject
					.getProject();
			NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
			_project.delete(true, _nullProgressMonitor);
		} catch (Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<String> expect(final String[]... arrays) {
		final ArrayList<String> expectation = CollectionLiterals
				.<String> newArrayList();
		for (final String[] array : arrays) {
			CollectionExtensions.<String> addAll(expectation, array);
		}
		return expectation;
	}

	protected ContentAssistProcessorTestBuilder newBuilder() throws Exception {
		ContentAssistProcessorTestBuilder _contentAssistProcessorTestBuilder = new ContentAssistProcessorTestBuilder(
				this.injector, this);
		return _contentAssistProcessorTestBuilder;
	}

	@Override
	public XtextResource getResourceFor(final InputStream stream) {
		try {
			XtextResource _xblockexpression = null;
			{
				final XtextResourceSet set = this.resourceSetProvider.get();
				this.initializeTypeProvider(set);
				String _primaryFileExtension = this.fileExtensionProvider
						.getPrimaryFileExtension();
				String _plus = ("Test." + _primaryFileExtension);
				URI _createURI = URI.createURI(_plus);
				final Resource result = set.createResource(_createURI);
				result.load(stream, null);
				_xblockexpression = (((XtextResource) result));
			}
			return _xblockexpression;
		} catch (Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	protected void initializeTypeProvider(final XtextResourceSet set) {
		JdtTypeProviderFactory _jdtTypeProviderFactory = new JdtTypeProviderFactory(
				this);
		final JdtTypeProviderFactory typeProviderFactory = _jdtTypeProviderFactory;
		typeProviderFactory.findOrCreateTypeProvider(set);
		IJavaProject _javaProject = this.getJavaProject(set);
		set.setClasspathURIContext(_javaProject);
	}

	@Override
	public IJavaProject getJavaProject(final ResourceSet resourceSet) {
		return MyAbstractContentAssistTest.javaProject;
	}
}
