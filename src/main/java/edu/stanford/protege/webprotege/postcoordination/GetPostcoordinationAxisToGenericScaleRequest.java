package edu.stanford.protege.webprotege.postcoordination;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Request;

@JsonTypeName(GetPostcoordinationAxisToGenericScaleRequest.CHANNEL)
public record GetPostcoordinationAxisToGenericScaleRequest() implements Request<GetPostcoordinationAxisToGenericScaleResponse> {
    public final static String CHANNEL = "webprotege.postcoordination.GetPostcoordinationAxisToGenericScale";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
