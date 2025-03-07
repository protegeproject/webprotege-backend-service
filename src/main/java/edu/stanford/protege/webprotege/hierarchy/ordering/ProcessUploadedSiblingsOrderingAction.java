package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;


@JsonTypeName(ProcessUploadedSiblingsOrderingAction.CHANNEL)
public record ProcessUploadedSiblingsOrderingAction(@JsonProperty("projectId") ProjectId projectId,
                                                    @JsonProperty("documentId") DocumentId uploadedDocumentId,
                                                    @JsonProperty("overrideExisting") boolean overrideExisting) implements ProjectAction<ProcessUploadedSiblingsOrderingResponse> {

    public final static String CHANNEL = "webprotege.hierarchies.ProcessUploadedSiblingsOrdering";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
