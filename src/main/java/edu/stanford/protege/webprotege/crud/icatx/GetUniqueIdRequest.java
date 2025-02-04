package edu.stanford.protege.webprotege.crud.icatx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Request;

@JsonTypeName(GetUniqueIdRequest.CHANNEL)
public record GetUniqueIdRequest(
        @JsonProperty("prefix") String prefix
) implements Request<GetUniqueIdResponse> {

    public static final String CHANNEL = "icatx.identity.GetUniqueId";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
