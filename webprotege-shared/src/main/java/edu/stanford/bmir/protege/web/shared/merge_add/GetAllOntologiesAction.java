package edu.stanford.bmir.protege.web.shared.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@AutoValue

@JsonTypeName("GetAllOntologies")
public abstract class GetAllOntologiesAction implements ProjectAction<GetAllOntologiesResult> {

    @JsonCreator
    public static GetAllOntologiesAction create(@JsonProperty("projectId") ProjectId projectId,
                                                @JsonProperty("documentId") DocumentId documentId) {
        return new AutoValue_GetAllOntologiesAction(projectId, documentId);
    }

    public abstract DocumentId getDocumentId();
}
