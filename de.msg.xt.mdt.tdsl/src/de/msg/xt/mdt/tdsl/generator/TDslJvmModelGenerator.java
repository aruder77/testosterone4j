package de.msg.xt.mdt.tdsl.generator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmEnumerationLiteral;
import org.eclipse.xtext.common.types.JvmEnumerationType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.util.Wrapper;
import org.eclipse.xtext.xbase.compiler.JvmModelGenerator;
import org.eclipse.xtext.xbase.compiler.TreeAppendableUtil;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

import com.google.inject.Inject;

/**
 * This class is only needed due to an Xtext bug. When an enum has methods, the
 * Xtext generator does not generate the necessary ';' after the enum literals
 * (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=377002).
 * 
 * @author axel
 */
@SuppressWarnings("restriction")
public class TDslJvmModelGenerator extends JvmModelGenerator {

	@Inject
	private TreeAppendableUtil _treeAppendableUtil;

	@Override
	protected ITreeAppendable _generateBody(final JvmEnumerationType it,
			final ITreeAppendable appendable) {
		ITreeAppendable _xblockexpression = null;
		{
			this.generateJavaDoc(it, appendable);
			this.generateAnnotations(it, appendable, true);
			this.generateModifier(it, appendable);
			appendable.append("enum ");
			ITreeAppendable _traceSignificant = this._treeAppendableUtil
					.traceSignificant(appendable, it);
			String _simpleName = it.getSimpleName();
			_traceSignificant.append(_simpleName);
			appendable.append(" ");
			this.generateExtendsClause(it, appendable);
			appendable.append("{");
			final Wrapper<Boolean> b = Wrapper.<Boolean> wrap(Boolean
					.valueOf(true));
			EList<JvmEnumerationLiteral> _literals = it.getLiterals();
			final Procedure1<JvmEnumerationLiteral> _function = new Procedure1<JvmEnumerationLiteral>() {
				@Override
				public void apply(final JvmEnumerationLiteral it) {
					ITreeAppendable _trace = appendable.trace(it);
					Boolean _get = b.get();
					boolean _generateEnumLiteral = TDslJvmModelGenerator.this
							.generateEnumLiteral(it, _trace,
									(_get).booleanValue());
					b.set(Boolean.valueOf(_generateEnumLiteral));
				}
			};
			IterableExtensions.<JvmEnumerationLiteral> forEach(_literals,
					_function);
			EList<JvmMember> _members = it.getMembers();
			// this is the Xtext-bug fix
			if (!_members.isEmpty()) {
				appendable.append(";");
			}
			// end fix
			final Function1<JvmMember, Boolean> _function_1 = new Function1<JvmMember, Boolean>() {
				@Override
				public Boolean apply(final JvmMember it) {
					boolean _not = (!(it instanceof JvmEnumerationLiteral));
					return Boolean.valueOf(_not);
				}
			};
			Iterable<JvmMember> _filter = IterableExtensions
					.<JvmMember> filter(_members, _function_1);
			final Procedure1<JvmMember> _function_2 = new Procedure1<JvmMember>() {
				@Override
				public void apply(final JvmMember it) {
					ITreeAppendable _trace = appendable.trace(it);
					Boolean _get = b.get();
					boolean _generateMember = TDslJvmModelGenerator.this
							.generateMember(it, _trace, (_get).booleanValue());
					b.set(Boolean.valueOf(_generateMember));
				}
			};
			IterableExtensions.<JvmMember> forEach(_filter, _function_2);
			ITreeAppendable _newLine = appendable.newLine();
			ITreeAppendable _append = _newLine.append("}");
			ITreeAppendable _newLine_1 = _append.newLine();
			_xblockexpression = (_newLine_1);
		}
		return _xblockexpression;
	}
}
