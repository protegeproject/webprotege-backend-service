package edu.stanford.protege.webprotege.linearization;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;


@JsonTypeName(GetEntityLinearizationsRequest.CHANNEL)
public record GetEntityLinearizationsRequest(@JsonProperty("entityIRI") String entityIRI,
                                             @JsonProperty("projectId") ProjectId projectId) implements Request<GetEntityLinearizationsResponse> {

    public static final String CHANNEL = "webprotege.linearization.GetEntityLinearizations";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

}
