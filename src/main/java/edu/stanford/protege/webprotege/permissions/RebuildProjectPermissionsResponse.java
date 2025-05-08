package edu.stanford.protege.webprotege.permissions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

@JsonTypeName(RebuildProjectPermissionsRequest.CHANNEL)
public record RebuildProjectPermissionsResponse() implements Response {

}
