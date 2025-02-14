package edu.stanford.protege.webprotege.linearization;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

@JsonTypeName(CreateLinearizationFromParentRequest.CHANNEL)
public record CreateLinearizationFromParentResponse() implements Response {
}
