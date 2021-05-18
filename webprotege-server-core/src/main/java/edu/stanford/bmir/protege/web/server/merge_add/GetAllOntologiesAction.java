package edu.stanford.bmir.protege.web.server.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.csv.DocumentId;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

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
