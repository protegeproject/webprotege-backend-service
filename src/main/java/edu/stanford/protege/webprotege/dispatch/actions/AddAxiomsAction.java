package edu.stanford.protege.webprotege.dispatch.actions;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ContentChangeRequest;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 *
 * This is a webprotege side action.  It won't work on the client.
 */
public record AddAxiomsAction(@Nonnull ChangeRequestId changeRequestId,
                             @Nonnull ProjectId projectId,
                             @Nonnull AxiomsSource axiomsSource,
                             @Nonnull String commitMessage) implements ProjectAction<AddAxiomsResult>, ContentChangeRequest {

    public static final String CHANNEL = "webprotege.axiomsSource.AddAxioms";

    public AddAxiomsAction(@Nonnull ChangeRequestId changeRequestId,
                           @Nonnull ProjectId projectId,
                           @Nonnull AxiomsSource axiomsSource,
                           @Nonnull String commitMessage) {
        this.changeRequestId = checkNotNull(changeRequestId);
        this.projectId = checkNotNull(projectId);
        this.axiomsSource = checkNotNull(axiomsSource);
        this.commitMessage = checkNotNull(commitMessage);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
