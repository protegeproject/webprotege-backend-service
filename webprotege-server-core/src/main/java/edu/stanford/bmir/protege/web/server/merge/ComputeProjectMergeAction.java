package edu.stanford.bmir.protege.web.server.merge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.csv.DocumentId;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
@AutoValue

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

