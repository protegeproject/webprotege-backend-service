package edu.stanford.protege.webprotege.linearization;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;

import java.util.List;

@JsonTypeName(GetIrisWithLinearizationRequest.CHANNEL)
public record GetIrisWithLinearizationRequest(
        @JsonProperty("iris") List<String> iris,
        @JsonProperty("projectId") ProjectId projectId
) implements Request<GetIrisWithLinearizationResponse> {

    public final static String CHANNEL = "webprotege.linearization.GetIrisWithLinearization";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public static GetIrisWithLinearizationRequest create(List<String> iris, ProjectId projectId) {
        return new GetIrisWithLinearizationRequest(iris, projectId);
    }

}
