package edu.stanford.protege.webprotege.dispatch.actions;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.revision.RevisionDetails;
import edu.stanford.protege.webprotege.revision.RevisionNumber;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 *
 * This is a webprotege side result.  It won't work on the client.
 */
public class GetRevisionsResult implements Result {

    private final ImmutableList<RevisionDetails> revisions;

    private final RevisionNumber currentRevisionNumber;

    public GetRevisionsResult(@Nonnull ImmutableList<RevisionDetails> revisions,
                              @Nonnull RevisionNumber currentRevisionNumber) {
        this.revisions = revisions;
        this.currentRevisionNumber = currentRevisionNumber;
    }

    public ImmutableList<RevisionDetails> getRevisions() {
        return revisions;
    }

    public RevisionNumber getCurrentRevisionNumber() {
        return currentRevisionNumber;
    }
}
