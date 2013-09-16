/*
* generated by Xtext
*/
package de.msg.xt.mdt.tdsl.ui.labeling

import com.google.inject.Inject
import de.msg.xt.mdt.tdsl.jvmmodel.MetaModelExtensions
import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation
import de.msg.xt.mdt.tdsl.tDsl.Control
import de.msg.xt.mdt.tdsl.tDsl.DataType
import de.msg.xt.mdt.tdsl.tDsl.EquivalenceClass
import de.msg.xt.mdt.tdsl.tDsl.Field
import de.msg.xt.mdt.tdsl.tDsl.Operation
import de.msg.xt.mdt.tdsl.tDsl.PackageDeclaration
import de.msg.xt.mdt.tdsl.tDsl.Predicate
import de.msg.xt.mdt.tdsl.tDsl.StatementLine
import de.msg.xt.mdt.tdsl.tDsl.TagsDeclaration
import de.msg.xt.mdt.tdsl.tDsl.Test
import de.msg.xt.mdt.tdsl.tDsl.Toolkit
import de.msg.xt.mdt.tdsl.tDsl.Type
import de.msg.xt.mdt.tdsl.tDsl.UseCase
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
import org.eclipse.xtext.xbase.ui.labeling.XbaseLabelProvider

/**
 * Provides labels for a EObjects.
 * 
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#labelProvider
 */
class TDslLabelProvider extends XbaseLabelProvider {
	
	private static final String THEME = "diamond";
	

	@Inject
	MetaModelExtensions metaModelExtensions;

	@Inject
	new(AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}
	

	def text(StatementLine line) {
		"Line " + metaModelExtensions.getIndexInParentBlock(line);
	}

	def String image(UseCase line) {
		return THEME + "/database/script_16.gif";
	}

	def String image(PackageDeclaration packDecl) {
		return "package_obj.png";
	}

	def String image(Activity activity) {
		return THEME + "/general/window_16.gif";
	}

	def String image(ActivityOperation operation) {
		return THEME + "/general/gear_16.gif";
	}

	def String image(Field field) {
		return THEME + "/database/text_field_16.gif";
	}

	def String image(Test test) {
		return THEME + "/general/check_mark_16.gif";
	}

	def String image(Type type) {
		return THEME + "/general/sphere_16.gif";
	}

	def String image(DataType dataType) {
		return THEME + "/database/database_16.gif";
	}

	def String image(EquivalenceClass equivalenceClass) {
		return THEME + "/general/pyramid_16.gif";
	}

	def String image(Control control) {
		return THEME + "/database/play_16.gif";
	}

	def String image(Operation operation) {
		return THEME + "/general/gear_16.gif";
	}

	def String image(Predicate predicate) {
		return THEME + "/accounting/abacus_16.gif";
	}

	def String image(Toolkit toolkit) {
		return THEME + "/database/objects_16.gif";
	}

	def String image(TagsDeclaration tagDecl) {
		return THEME + "/projectManagement/tag_16.gif";
	}


	// Labels and icons can be computed like this:
	
//	def text(Greeting ele) {
//		'A greeting to ' + ele.name
//	}
//
//	def image(Greeting ele) {
//		'Greeting.gif'
//	}
}
