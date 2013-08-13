package de.msg.xt.mdt.tdsl.ui.contentassist;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentProposalPriorities;

public class TDslProposalPriorities extends ContentProposalPriorities {

	@Override
	protected void adjustPriority(ICompletionProposal proposal, String prefix,
			int priority) {
		if (proposal != null && proposal.getDisplayString() != null
				&& !proposal.getDisplayString().contains(".")) {
			super.adjustPriority(proposal, prefix, priority
					* proposalWithPrefixMultiplier);
		}
		super.adjustPriority(proposal, prefix, priority);
	}
}
