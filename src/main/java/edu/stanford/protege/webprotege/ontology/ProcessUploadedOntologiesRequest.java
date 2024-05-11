package edu.stanford.protege.webprotege.ontology;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.csv.DocumentId;

import static edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesRequest.CHANNEL;
import static java.util.Objects.requireNonNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-16
 */
@JsonTypeName(CHANNEL)
public record ProcessUploadedOntologiesRequest(@JsonProperty("fileSubmissionId") DocumentId documentId) implements Request<ProcessUploadedOntologiesResponse> {

    public static final String CHANNEL = "webprotege.ontology-processing.ProcessUploadedOntologies";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public ProcessUploadedOntologiesRequest(@JsonProperty("fileSubmissionId") DocumentId documentId) {
        this.documentId = requireNonNull(documentId);
    }
}
