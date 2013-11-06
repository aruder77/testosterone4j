package com.bmw.bne3.client.uitests.validation

import com.bmw.smartfaces.model.EditorNode
import com.bmw.smartfaces.model.FieldNode
import com.bmw.smartfaces.model.PageNode
import com.bmw.smartfaces.model.UIDescription
import de.msg.xt.mdt.tdsl.jvmmodel.MetaModelExtensions
import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import javax.inject.Inject
import org.eclipse.emf.common.notify.Notification
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.util.EContentAdapter
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.lib.Functions.Function1
import org.eclipse.xtext.xbase.validation.XbaseValidator
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1

class BneTdslValidator extends XbaseValidator {

	@Inject extension MetaModelExtensions

	UIDescription uiDesc = null

	Map<String, Map<String, List<String>>> editorPages = new HashMap<String, Map<String, List<String>>>

	override protected getEPackages() {
		val result = new ArrayList<EPackage>();
		result.add(TDslPackage.eINSTANCE);
		return result
	}

	@Check
	def checkActivityKeyExists(Activity activity) {
		UIDescription
		val actId = if(activity?.uniqueId == null) activity.name else activity.uniqueId
		if (!editorPages.containsKey(actId)) {
			val containingAct = activity.containingActivity
			if (containingAct != null) {
				val containingActId = if(containingAct?.uniqueId == null) containingAct.name else containingAct.uniqueId
				if (editorPages.get(containingActId)?.get(actId) == null) {
					error("No view/editor/page exists with this key: " + actId + " in editor " + containingActId, TDslPackage.Literals.ACTIVITY__NAME)
				} 				
			} else {
				var found = false
				for (pageFields: editorPages.values) {
					if(pageFields.containsKey(actId)) {
						found = true
					}
				}	
				if (!found)
					error("No view/editor/page exists with this key: " + actId, TDslPackage.Literals.ACTIVITY__NAME)
				
			}			
		}
	}

	@Check
	def checkFieldKeyExists(Field field) {
		UIDescription
		val fieldId = field.uniqueId
		val activity = field.parentActivity
		val actId = if(activity?.uniqueId == null) activity.name else activity.uniqueId
		
		// check if field is a page-tab
		if (editorPages.get(actId)?.containsKey(fieldId))
			return
		
		// check if field is in page-activity
		val containingActivity = activity.containingActivity
		if (containingActivity != null) {
			val editorId = if(containingActivity.uniqueId == null) containingActivity.name else containingActivity.uniqueId
			if (!editorPages.get(editorId)?.get(actId)?.contains(fieldId)) {
				error("No field exists with this key: " + fieldId + " in page " + actId + " in editor " + editorId, TDslPackage.Literals.FIELD__NAME)
			}
		} else {
			var found = false
			for (pageFields: editorPages.values) {
				if(pageFields.get(actId) != null && pageFields.get(actId).contains(fieldId)) {
					found = true
				}
			}	
			if (!found)
				error("No field exists with this key: " + fieldId + " in any page with id " + actId, TDslPackage.Literals.FIELD__NAME)
		}		
	}

	def UIDescription getUIDescription() {
		if (uiDesc == null) {
			uiDesc = readModel
			updateUiKeys(uiDesc)

			uiDesc.eAdapters.add(new MyContentAdapter([Notification notification|updateUiKeys(uiDesc)]))
		}
		uiDesc
	}

	def void updateUiKeys(UIDescription descr) {
		editorPages.clear

		val editorList = EcoreUtil2::getAllContentsOfType(UIDescription, typeof(EditorNode))
		for (editorNode : editorList) {
			val pages = new HashMap<String, List<String>>
			editorPages.put(editorNode.key, pages)
			val pageList = EcoreUtil2::getAllContentsOfType(editorNode, typeof(PageNode))
			for (page : pageList) {
				val fields = new ArrayList<String>
				pages.put(page.key, fields)
				val fieldList = EcoreUtil2::getAllContentsOfType(page, typeof(FieldNode))
				for (field : fieldList) {
					fields.add(field.keyIdentifier)
				}
			}
		}
	}

	def String keyIdentifier(FieldNode field) {
		var fieldUniqueId = field.key

		if (field.fieldReference?.table != null) {
			fieldUniqueId = field.fieldReference.table.key
		}
		
		fieldUniqueId
	}

	def UIDescription readModel() {
		val ResourceSet rs = new ResourceSetImpl();
		val Resource resource = rs.getResource(
			URI.createURI("platform:/resource/com.bmw.bne3.client/sf_core_ui/sf_ui_core.uidefinition"), true);
		EcoreUtil2.resolveAll(resource, [|false]);
		return resource.getContents().get(0) as UIDescription
	}

}

class MyContentAdapter extends EContentAdapter {

	Procedure1<Notification> notifyFunction

	new(Procedure1<Notification> function) {
		super()
		notifyFunction = function
	}

	override notifyChanged(Notification notification) {
		notifyFunction.apply(notification)
	}

}
