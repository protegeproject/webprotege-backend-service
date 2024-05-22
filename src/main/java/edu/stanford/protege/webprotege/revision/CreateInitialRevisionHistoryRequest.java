package edu.stanford.protege.webprotege.revision;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.Request;

import java.util.List;
import java.util.Objects;

import static edu.stanford.protege.webprotege.revision.CreateInitialRevisionHistoryRequest.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@JsonTypeName(CHANNEL)
public record CreateInitialRevisionHistoryRequest(@JsonProperty("documentLocations") List<BlobLocation> documentLocations) implements Request<CreateInitialRevisionHistoryResponse> {

    public static final String CHANNEL = "webprotege.revisions.CreateInitialRevisionHistory";

    public CreateInitialRevisionHistoryRequest(@JsonProperty("documentLocations") List<BlobLocation> documentLocations) {
        this.documentLocations = Objects.requireNonNull(documentLocations);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
