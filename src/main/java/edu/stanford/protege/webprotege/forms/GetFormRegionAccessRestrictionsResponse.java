package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Response;

import java.util.List;

@JsonTypeName(GetFormRegionAccessRestrictionsRequest.CHANNEL)
public record GetFormRegionAccessRestrictionsResponse(@JsonProperty("projectId")ProjectId projectId,
                                                      @JsonProperty("accessRestrictions") List<FormRegionAccessRestriction> accessRestrictions) implements Response {

}
