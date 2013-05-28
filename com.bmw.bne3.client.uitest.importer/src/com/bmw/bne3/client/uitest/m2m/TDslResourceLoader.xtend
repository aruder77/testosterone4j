package com.bmw.bne3.client.uitest.m2m

import java.util.ArrayList
import java.util.List
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl

class TDslResourceLoader {
	
	def List<Resource> findTdslResource() {
		val root = ResourcesPlugin::workspace.root
		val resources = new ArrayList<Resource>()
		root.addFilesInResource(resources)
		resources
	}
	
	def dispatch void addFilesInResource(IFile file, List<Resource> resourceList) {
		if (file.name.endsWith(".tdsl")) {
			val resourceSet = new ResourceSetImpl
			val uri = URI::createURI(file.locationURI.toString)
			val resource = resourceSet.getResource(uri, true)
			resourceList.add(resource)
		}
	}

	def dispatch void addFilesInResource(IContainer container, List<Resource> resourceList) {
		for(member : container.members()) {
			member.addFilesInResource(resourceList)
		}
	}

	def dispatch void addFilesInResource(IResource resource, List<Resource> resourceList) {
		// do nothing
	}

}