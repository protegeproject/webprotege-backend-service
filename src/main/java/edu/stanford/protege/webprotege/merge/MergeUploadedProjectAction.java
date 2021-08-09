package edu.stanford.protege.webprotege.merge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
@AutoValue

@JsonTypeName("MergeUploadedProject")
public abstract class MergeUploadedProjectAction implements ProjectAction<MergeUploadedProjectResult> {

    @JsonCreator
    public static MergeUploadedProjectAction create(@JsonProperty("projectId") ProjectId projectId,
                                                    @JsonProperty("documentId") DocumentId uploadedDocumentId,
                                                    @JsonProperty("commitMessage") String commitMessage) {
        return new AutoValue_MergeUploadedProjectAction(projectId, uploadedDocumentId, commitMessage);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract DocumentId getDocumentId();

    public abstract String getCommitMessage();
}
