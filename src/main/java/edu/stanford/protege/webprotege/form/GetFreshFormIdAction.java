package edu.stanford.protege.webprotege.form;


import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
public class GetFreshFormIdAction implements ProjectAction<GetFreshFormIdResult> {

    private ProjectId projectId;

    public GetFreshFormIdAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }


    private GetFreshFormIdAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
