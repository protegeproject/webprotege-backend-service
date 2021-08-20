package edu.stanford.protege.webprotege.dispatch.actions;

import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionNumber;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2018
 *
 * Serverside action only
 */
public class GetRevisionAction implements ProjectAction<GetRevisionResult> {

    public static final String CHANNEL = "revisions.GetRevision";

    private final ProjectId projectId;

    private final RevisionNumber revisionNumber;

    public GetRevisionAction(@Nonnull ProjectId projectId,
                             @Nonnull RevisionNumber revisionNumber) {
        this.projectId = checkNotNull(projectId);
        this.revisionNumber = checkNotNull(revisionNumber);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }
}
