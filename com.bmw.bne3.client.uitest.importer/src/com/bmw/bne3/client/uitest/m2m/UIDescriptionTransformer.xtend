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
import de.msg.xt.mdt.tdsl.tDsl.Import
import java.util.HashMap
import de.msg.xt.mdt.tdsl.tDsl.Operation
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import org.eclipse.xtext.xbase.XbaseFactory
import org.eclipse.emf.ecore.EReference

class UIDescriptionTransformer {
	
	@Inject extension MetaModelExtensions
	@Inject extension NamingExtensions
	
	@Inject
	IScopeProvider scopeProvider
	
	
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
	 * 
	 * 
	 * 
	 * Mapping der Controls:
	 * HexDecBinField		=> Composite + 3 TextControl
	 * StringField			=> TextControl
	 * RadioButtonField	=> RadioButton 				? SWT-DATA-ID?
	 * BrowserField		=> StyledText				?
	 * PushButtonField		=> Button					?
	 * LongField			=> TextControl
	 * EnumCombo		=> ComboBox
	 * DisplayPackageField	=> TextControl
	 * DisplayTypeField		=> TextControl
	 * CheckButtonField	=> CheckBox
 	 * ComboFielld
	 * DoubleField			=> TextControl
	 * ConsoleField		=> TextControl
	 * NavigationFilter		=> Compositer + Text + 2 Button
	 * NavigationTreeViewer	=> Tree
	 * SpacerField		=> Label
	 */
	def transform(UIDescription descr, PackageDeclaration pack, EditorNode editorNode) {
		editorNode.createActivity(pack)				
	}
	
	def dispatch Activity createActivity(EditorNode node, PackageDeclaration pack) { 
		val factory = TDslFactory::eINSTANCE
		
		var Activity activity = null
		if (node?.key != null && node.key.trim.length != 0) {
			val activityName = node.key.convertToId.toFirstUpper
			
			activity = pack.elements.filter(typeof(Activity)).findFirst[it.name?.equals(activityName)]
			if (activity == null) {
				activity = factory.createActivity
				pack.elements += activity
				activity.name = activityName
				activity.parent = findEObjectRef(activity, TDslPackage$Literals::ACTIVITY__PARENT, "com.bmw.bne3.client.uitest.activities.EditorActivity") as Activity
			}
			
			for (page : node.pages) {
				if (page.key != null) {
					val subActivity = page.createActivity(pack)
					if (subActivity != null) {
						val fieldName = page.label.convertToId
						var Field field = activity.fields.findFirst[it.name.equals(fieldName)]
						if (field == null) {
							field = factory.createField
							activity.fields += field
							field.name = page.label.convertToId
						}
						field.uniqueId = page.key
						field.control = findControl(field, "de.msg.xt.mdt.tdsl.swtbot.TabItem")
						insertFieldOperationMappings(field)
						val pushOperations = field.operations.filter [it.name.name == "push"]
						var OperationMapping pushOperation = null
						if (!pushOperations.empty) {
							pushOperation = pushOperations.head
							val condNextAct1 = factory.createConditionalNextActivity
							pushOperation.nextActivities += condNextAct1			
							condNextAct1.next = subActivity							
						
							val operationName = page.key.convertToId + "Page"
							var operation =  activity.operations.findFirst[it.name.equals(operationName)]
							if (operation == null) {
								operation = factory.createActivityOperation
								activity.operations += operation
								operation.name = operationName
						
								val body = TDslFactory.eINSTANCE.createActivityOperationBlock
								operation.body = body
								val statement1 = factory.createStatementLine
								body.expressions += statement1
								val opCall = factory.createOperationCall
								statement1.statement = opCall
								opCall.operation = pushOperation
						
								val condNextAct2 = factory.createConditionalNextActivity
								operation.nextActivities += condNextAct2			
								condNextAct2.next = subActivity
							}
						}
					}
				}
			}
		}
		
		return activity 
	}
	
	def EObject findEObjectRef(EObject context, EReference reference, String string) { 
		val scope = scopeProvider.getScope(context, reference)
		return scope.getSingleElement(string.fqnForName)?.EObjectOrProxy
	}

	
	def dispatch Activity createActivity(PageNode page, PackageDeclaration pack) {
		val factory = TDslFactory::eINSTANCE
		var Activity activity
		if (page?.key != null && page.key.trim.length != 0) {
			val activityName = page.editor.key.convertToId.toFirstUpper + "_" + page.key.convertToId
			activity = pack.elements.filter(typeof(Activity)).findFirst[it.name.equals(activityName)]
			if (activity == null) { 
				activity = factory.createActivity
				pack.elements += activity
				activity.name = activityName 
				activity.parent = findEObjectRef(activity, TDslPackage$Literals::ACTIVITY__PARENT, "com.bmw.bne3.client.uitest.activities.PageActivity") as Activity
			}
			activity.setUniqueId(page.key)
			
			val fields = EcoreUtil2::getAllContentsOfType(page, typeof(FieldNode))
			
			for (field : fields) {
				createField(field, activity)
			}
			
			if(activity.operations.filter[it.name.equals("returnToEditor")].empty) {
				val operation = factory.createActivityOperation
				activity.operations += operation
				operation.name = "returnToEditor"
				operation.body = TDslFactory.eINSTANCE.createActivityOperationBlock
				val condNextAct = factory.createConditionalNextActivity
				operation.nextActivities += condNextAct
				condNextAct.returnToLastActivity = true
			}
		}
		
		return activity
	}
	
	def Field findExistingField(Activity activity, FieldNode node) {
		var field = activity.fields.findFirst[it.uniqueId != null && it.uniqueId.equals(node?.key)]
		if (field == null)
			activity.fields.findFirst[it.name != null && it.name.equals(node?.label?.convertToId)]
		field
	}
	
	
	def Field createField(FieldNode node, Activity activity) { 
		val factory = TDslFactory::eINSTANCE
		var Field field
		
		if (node.key != null && node.key.trim.length != 0) {
			field = activity.findExistingField(node)
			if (field == null) {
				field = factory.createField
				activity.fields += field
			}
			
			field.control = determineControl(node, field)

			var fieldName = node.label?.convertToId
			if (fieldName == null || fieldName.trim.equals(""))
				fieldName = node.key.convertToId
			if (field.name == null || field.name.empty) 
				field.name = fieldName		
			field.uniqueId = node.key
			
			if (node.fieldReference?.table != null) {
				field.uniqueId = node.fieldReference.table.key
				if (field.name == null || field.name.trim.equals("")) {
					if (node.fieldReference.table.name != null) 
						field.name = node.fieldReference.table.name?.convertToId
					else if (node.fieldReference.table.key != null)
						field.name = node.fieldReference.table.key?.convertToId
				}
			}
			
			val fieldNameVal = fieldName
			var isNotUnique = !activity.fields.filter[it.name.equals(fieldNameVal)].empty
			var index = 2
			while (isNotUnique) {
				val currentFieldName = fieldName + index
				isNotUnique = !activity.fields.filter[it.name.equals(currentFieldName)].empty
				index = index + 1
			}
			
			if (field.control == null) {
				createControl(node, field)
			}
			
			field.insertFieldOperationMappings
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
	
	def Control findControl(Field field, String name) {
		val scope = scopeProvider.getScope(field, TDslPackage$Literals::FIELD__CONTROL)
		scope.getSingleElement(name.fqnForName)?.EObjectOrProxy as Control
	}

	def QualifiedName fqnForName(String name) {
		val segments = name.split("\\.")
		return QualifiedName::create(segments)		
	}
	
	def Control determineControl(FieldNode node, Field field) { 
		val scope = scopeProvider.getScope(field, TDslPackage$Literals::FIELD__CONTROL)
		if (node.factory != null) {
			val controlName = mapFactoryToControlName(node.factory.classname)
			if (controlName == null)
				return null
			return (scope.getSingleElement(controlName)?.EObjectOrProxy as Control)
		} 
	}
	
	
	def QualifiedName mapFactoryToControlName(String string) {
		
		val controlMap = newHashMap(
			"com.bmw.smartfaces.boxed.controls.StringFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TextControl",
			"com.bmw.smartfaces.boxed.controls.LongFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TextControl",
			"com.bmw.smartfaces.boxed.controls.ComboFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.ComboBox",
			"com.bmw.smartfaces.boxed.controls.DoubleFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TextControl",
			"com.bmw.smartfaces.boxed.controls.HexDecBinFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.HexDecBinControl",
			"com.bmw.smartfaces.boxed.controls.SpacerFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.Label",
			"com.bmw.smartfaces.boxed.controls.TableControlFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TableControl",
			"com.bmw.smartfaces.boxed.controls.MessageFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.Label",

			"com.bmw.smartfaces.boxed.controls.buttons.RadioButtonFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.RadioButton",
			"com.bmw.smartfaces.boxed.controls.buttons.PushButtonFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.Button",
			"com.bmw.smartfaces.boxed.controls.buttons.CheckButtonFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.CheckBox",

			"com.bmw.smartfaces.boxed.controls.browser.BrowserFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.StyledText",

			"com.bmw.smartfaces.boxed.controls.console.ConsoleFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TextControl",

			"com.bmw.bne3.client.controls.EnumComboFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TableControl",
			"com.bmw.bne3.client.controls.TypeSelectionComboFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TypeSelectionCombo",
			"com.bmw.bne3.client.controls.TypeSelectionFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TypeSelectionField",
			"com.bmw.bne3.client.controls.NavigationFilterFactory" -> "de.msg.xt.mdt.tdsl.swtbot.NavigationFilter", // TODO
			"com.bmw.bne3.client.controls.NavigationTreeViewerFactory" -> "de.msg.xt.mdt.tdsl.swtbot.Tree",

			"com.bmw.bne3.client.controls.display.DisplayPackageFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TableControl",
			"com.bmw.bne3.client.controls.display.DisplayTypeFieldFactory" -> "de.msg.xt.mdt.tdsl.swtbot.TableControl"
		)
		if (controlMap.get(string) == null)
			return null
		val controlName = controlMap.get(string)
		return controlName.fqnForName
	}
	
	def editor(PageNode node) { 
		(node.eContainer as EditorNode)
	}

	def String convertToId(String id) {
		var temp = id.trim.replace(' ', '').replaceAll("ß","ss").replace('-','_').replace('.','_').replace('/','_').replace("ä", "ae").replace("ö", "Oe").replace("ü", "ue").replace("Ä", "Ae").replace("Ö", "Oe").replace("Ü", "Ue").replace('\"','').replace("(","").replace(")", "").replace("!", "").replace(",", "")
		if (temp.length > 0 && !Character::isLetter(temp.charAt(0)))
			temp = "f" + temp
		return temp?.toFirstLower
	}
	
	
	def insertFieldOperationMappings(Field field) {
		val controlOperations = field.getControl().getOperations();
		val opMappings = new HashMap<Operation, OperationMapping>();
		for (opMapping : field.getOperations()) {
			opMappings.put(opMapping.getName(), opMapping);
		}
		for (op : controlOperations) {
			if (!opMappings.containsKey(op)) {
				insertMappingForOperation(field, op);
			}
		}
	}

	def insertMappingForOperation(Field field, Operation op) {
		val factory = TDslFactory::eINSTANCE;
		val mapping = factory.createOperationMapping();
		mapping.setName(op);
		mapping.setDataType(field.defaultDataType(op.getReturnType()));
		val params = op.getParams();
		for (param : params) {
			val dtMapping = factory.createDataTypeMapping();
			dtMapping.setName(param);
			dtMapping.setDatatype(field.defaultDataType(param.getType()));
			mapping.getDataTypeMappings().add(dtMapping);
		}
		field.getOperations().add(mapping);
	}
	
}
