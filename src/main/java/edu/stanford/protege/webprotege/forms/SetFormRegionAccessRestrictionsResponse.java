package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Response;

@JsonTypeName(SetFormRegionAccessRestrictionsRequest.CHANNEL)
public record SetFormRegionAccessRestrictionsResponse(@JsonProperty("projectId") ProjectId projectId) implements Response {

}
