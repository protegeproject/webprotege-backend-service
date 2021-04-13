package edu.stanford.bmir.protege.web.shared.merge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ComputeProjectMerge")
public abstract class ComputeProjectMergeAction implements ProjectAction<ComputeProjectMergeResult> {

    
    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract DocumentId getProjectDocumentId();

    @JsonCreator
    public static ComputeProjectMergeAction create(@JsonProperty("projectId") ProjectId projectId,
                                                   @JsonProperty("projectDocumentId") DocumentId projectDocumentId) {
        return new AutoValue_ComputeProjectMergeAction(projectId, projectDocumentId);
    }
}

