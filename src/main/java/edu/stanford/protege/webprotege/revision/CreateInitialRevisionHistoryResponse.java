package edu.stanford.protege.webprotege.revision;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.Response;

import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@JsonTypeName(CreateInitialRevisionHistoryRequest.CHANNEL)
public record CreateInitialRevisionHistoryResponse(@JsonProperty("documentLocation") BlobLocation revisionHistoryLocation) implements Response {

    public CreateInitialRevisionHistoryResponse(@JsonProperty("documentLocation") BlobLocation revisionHistoryLocation) {
        this.revisionHistoryLocation = Objects.requireNonNull(revisionHistoryLocation);
    }
}
