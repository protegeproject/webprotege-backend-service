package edu.stanford.protege.webprotege.crud.icatx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;


@JsonTypeName(GetUniqueIdRequest.CHANNEL)
public record GetUniqueIdResponse(@JsonProperty("uniqueId") String uniqueId) implements Response {
}
