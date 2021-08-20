package edu.stanford.protege.webprotege.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

@AutoValue

@JsonTypeName("GetAllOntologies")
public abstract class GetUploadedAndProjectOntologyIdsAction implements ProjectAction<GetUploadedAndProjectOntologyIdsResult> {

    public static final String CHANNEL = "ontologies.GetAllOntologies";

    /**
     * Gets a {@link GetUploadedAndProjectOntologyIdsAction} action that retrieves the ontology Ids of an uploaded set of
     * ontologies together with the project ontology Ids
     * @param projectId The project Ids
     * @param documentId The id of the uploaded source document
     */
    @JsonCreator
    public static GetUploadedAndProjectOntologyIdsAction create(@JsonProperty("projectId") ProjectId projectId,
                                                                @JsonProperty("documentId") DocumentId documentId) {
        return new AutoValue_GetUploadedAndProjectOntologyIdsAction(projectId, documentId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public abstract DocumentId getDocumentId();
}
