package com.bmw.bne3.client.uitest.m2m

import de.msg.xt.mdt.tdsl.tDsl.TestModel
import com.bmw.smartfaces.model.UIDescription
import de.msg.xt.mdt.tdsl.tDsl.TDslFactory
import org.eclipse.xtext.EcoreUtil2
import com.bmw.smartfaces.model.Folder
import com.bmw.smartfaces.model.EditorNode
import de.msg.xt.mdt.tdsl.tDsl.Activity
import com.bmw.smartfaces.model.PageNode
import de.msg.xt.mdt.tdsl.jvmmodel.MetaModelExtensions
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import com.bmw.smartfaces.model.FieldNode
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.Control
import de.msg.xt.mdt.tdsl.jvmmodel.NamingExtensions
import javax.inject.Inject
import org.eclipse.xtext.scoping.IScopeProvider
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.scoping.IGlobalScopeProvider
import de.msg.xt.mdt.tdsl.tDsl.Control

class UIDescriptionTransformer {
	
	@Inject extension MetaModelExtensions
	@Inject extension NamingExtensions
	
	@Inject
	IScopeProvider scopeProvider
	
	
	def basePackage() {
		"com.bmw.bne3"
	}
	
	def packageName(Folder folder) {
		var String packageName = folder?.folderName?.toLowerCase.convertToId
		if (folder?.parentFolder != null) {
			packageName = folder.parentFolder.packageName + "." + packageName
		}
		return packageName
	}
	
	/**
	 * - Editoren werden nach Activities übersetzt
	 * - die Folderhierarchie über den Editoren wird zu Packages transformiert
	 * - der Editor-Typ wird zu BasisActivity übersetzt
	 * - Pages in Editoren werden zu Sub-Activities übersetzt
	 * - alle fieldNodes werden zu Fields
	 * 		* factory übersetzt zu Control
	 * 		* wenn keine factory sondern fieldReference, dann Tabelle
	 * 
	 * Strategie:
	 * - Iterieren über alle Folder die direkt Editoren beinhalten
	 * - dafür erzeugen wir eine PackageDecl
	 * - iterieren über alle Editoren des Folders
	 * - Für jeden Editor:
	 * 		* erzeuge Activity
	 * 		* erzeuge Felder
	 * 		* erzeuge Activities für Pages
	 * 			- für alle direkten und indirekten Felder erzeuge Fields
	 * 			- return ActivityOperation
	 * 		* erzeuge ActivityOperations für Pages
	 */
	def TestModel transform(UIDescription descr, TestModel model) {
		
		val factory = TDslFactory::eINSTANCE 

		val concreteFolders = EcoreUtil2::getAllContentsOfType(descr, typeof(Folder)).filter [
			!it.editors.empty
		]
		
		for (folder : concreteFolders) {
			if (folder.packageName != null) {
				val pack = factory.createPackageDeclaration;
				model.packages.add(pack);
				pack.setName(folder.packageName)
			
				for (editor : folder.editors) {
					editor.createActivity(pack)
				}			
			}
		}
				
		return model
	}
	
	def dispatch Activity createActivity(EditorNode node, PackageDeclaration pack) { 
		val factory = TDslFactory::eINSTANCE
		
		var Activity activity = null
		if (node?.key != null && node.key.trim.length != 0) {
			activity = factory.createActivity
			pack.elements += activity
		
			activity.name = node.key.convertToId
		
			for (page : node.pages) {
				if (page.key != null) {
					val subActivity = page.createActivity(pack)
					
					if (subActivity != null) {
						val operation = factory.createActivityOperation
						activity.operations += operation
						operation.name = page.key.convertToId + "PageActivity"			
						val condNextAct = factory.createConditionalNextActivity
						operation.nextActivities += condNextAct			
						condNextAct.next = subActivity
					}
				}
			}
		}
		
		return activity 
	}
	
	def dispatch Activity createActivity(PageNode page, PackageDeclaration pack) {
		val factory = TDslFactory::eINSTANCE
		var Activity activity
		if (page?.key != null && page.key.trim.length != 0) { 
			activity = factory.createActivity
			pack.elements += activity
			activity.name = page.editor.key.convertToId + "_" + page.key.convertToId
			activity.setUniqueId(page.key)
			
			val fields = EcoreUtil2::getAllContentsOfType(page, typeof(FieldNode))
			
			for (field : fields) {
				createField(field, activity)
			}
			
			val operation = factory.createActivityOperation
			activity.operations += operation
			operation.name = "returnToEditor"
			val condNextAct = factory.createConditionalNextActivity
			operation.nextActivities += condNextAct
			condNextAct.returnToLastActivity = true
		}
		
		return activity
	}
	def Field createField(FieldNode node, Activity activity) { 
		val factory = TDslFactory::eINSTANCE
		var Field field
		
		if (node.key != null && node.key.trim.length != 0) {
			field = factory.createField
			activity.fields += field
			field.name = "f" + node.key.convertToId
			field.uniqueId = node.key
			
			//field.control = determineControl(node, field)
			
			if (field.control == null) {
				createControl(node, field)
			}
		}
		
		return field		
	}
	def createControl(FieldNode node, Field field) { 
		var controlName = "someControl"
		if (node.factory != null)
			controlName = node.factory.classname.convertToId
		else if (node.fieldReference != null)
			controlName = "tableControl"

		var Control control = null
		for (currentControl : field.parentActivity.packageDeclaration.elements.filter(typeof(Control))) {
			if (currentControl.name.equals(controlName)) {
				control = currentControl
			}
		}

		if (control == null) {
			control = TDslFactory::eINSTANCE.createControl
			field.parentActivity.packageDeclaration.elements += control
			control.setName(controlName)
			val op = TDslFactory::eINSTANCE.createOperation
			control.operations += op
			op.name = "isEnabled"
		}
		field.control = control
	}

	
	def Control determineControl(FieldNode node, Field field) { 
		val resource = field.eResource
		val scope = scopeProvider.getScope(field, TDslPackage$Literals::FIELD__CONTROL)
		for (element : scope.allElements) {
			System::out.println("Element in scope: " + element.name.toString)
		}
		if (node.factory != null) {
			return scope.getSingleElement(mapFactoryToControlName(node.factory.classname)) as Control
		} else {
			return scope.getSingleElement(QualifiedName::create("de", "msg", "xt", "mdt", "tdsl", "swtbot", "TableControl")) as Control			
		}
	}
	
	def QualifiedName mapFactoryToControlName(String string) { 
		return QualifiedName::create("de", "msg", "xt", "mdt", "tdsl", "swtbot", "TextControl")
	}
	
	def editor(PageNode node) { 
		(node.eContainer as EditorNode)
	}

	def String convertToId(String id) {
		id.trim.replace(' ', '').replaceAll("ß","ss").replace('-','_').replace('.','_').replace('/','_').replace("ä", "ae").replace("ö", "Oe").replace("ü", "ue").replace("Ä", "Ae").replace("Ö", "Oe").replace("Ü", "Ue").replace('\"','').replace("(","").replace(")", "")
	}
	
}
