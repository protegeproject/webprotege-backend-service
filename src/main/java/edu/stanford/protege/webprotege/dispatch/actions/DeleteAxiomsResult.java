package edu.stanford.protege.webprotege.dispatch.actions;

import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;

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
    public ProjectId projectId() {
        return projectId;
    }

    public int getDeletedAxiomsCount() {
        return deletedAxiomsCount;
    }
}
