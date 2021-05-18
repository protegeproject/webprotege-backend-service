package edu.stanford.bmir.protege.web.server.dispatch.actions;

import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.project.HasProjectId;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Apr 2018
 */
public class DeleteAxiomsResult implements Result, HasProjectId {

    @Nonnull
    private ProjectId projectId;

    private int deletedAxiomsCount;

    public DeleteAxiomsResult(@Nonnull ProjectId projectId,
                              int deletedAxiomsCount) {
        this.projectId = projectId;
        this.deletedAxiomsCount = deletedAxiomsCount;
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public int getDeletedAxiomsCount() {
        return deletedAxiomsCount;
    }
}
