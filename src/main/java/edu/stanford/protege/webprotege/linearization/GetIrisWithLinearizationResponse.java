package edu.stanford.protege.webprotege.linearization;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.Response;

import java.util.List;

@JsonTypeName(GetIrisWithLinearizationRequest.CHANNEL)
public record GetIrisWithLinearizationResponse(@JsonProperty("iris") List<String> iris) implements Response {

    @JsonCreator
    public static GetIrisWithLinearizationResponse create(@JsonProperty("iris") List<String> iris) {
        return new GetIrisWithLinearizationResponse(iris);
    }
}
