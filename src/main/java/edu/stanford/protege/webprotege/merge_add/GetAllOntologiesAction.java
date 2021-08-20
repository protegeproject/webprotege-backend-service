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
public abstract class GetAllOntologiesAction implements ProjectAction<GetAllOntologiesResult> {

    public static final String CHANNEL = "projects.GetAllOntologies";

    @JsonCreator
    public static GetAllOntologiesAction create(@JsonProperty("projectId") ProjectId projectId,
                                                @JsonProperty("documentId") DocumentId documentId) {
        return new AutoValue_GetAllOntologiesAction(projectId, documentId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public abstract DocumentId getDocumentId();
}
