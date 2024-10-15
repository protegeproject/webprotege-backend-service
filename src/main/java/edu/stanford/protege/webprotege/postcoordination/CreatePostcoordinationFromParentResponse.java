package edu.stanford.protege.webprotege.postcoordination;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

@JsonTypeName(CreatePostcoordinationFromParentRequest.CHANNEL)
public record CreatePostcoordinationFromParentResponse() implements Response {
}
