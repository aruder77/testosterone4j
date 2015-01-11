package org.testosterone4j.tdsl.ui.contentassist;

/*
 * #%L
 * org.testosterone4j.tdsl.ui
 * %%
 * Copyright (C) 2015 Axel Ruder
 * %%
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * #L%
 */

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
