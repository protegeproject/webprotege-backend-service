package edu.stanford.protege.webprotege.postcoordination;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

import java.util.List;

@JsonTypeName(GetPostcoordinationAxisToGenericScaleRequest.CHANNEL)
public record GetPostcoordinationAxisToGenericScaleResponse(
        @JsonProperty("postcoordinationAxisToGenericScales") List<PostcoordinationAxisToGenericScale> postcoordinationAxisToGenericScales
) implements Response {

    public static GetPostcoordinationAxisToGenericScaleResponse create(List<PostcoordinationAxisToGenericScale> postcoordinationAxisToGenericScales){
        return new GetPostcoordinationAxisToGenericScaleResponse(postcoordinationAxisToGenericScales);
    }
}
