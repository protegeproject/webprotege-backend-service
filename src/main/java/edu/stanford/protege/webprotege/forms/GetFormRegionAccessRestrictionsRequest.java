package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Request;

@JsonTypeName(GetFormRegionAccessRestrictionsRequest.CHANNEL)
public record GetFormRegionAccessRestrictionsRequest(@JsonProperty("projectId") ProjectId projectId) implements Request<GetFormRegionAccessRestrictionsResponse> {

    public static final String CHANNEL = "webprotege.forms.GetFormRegionAccessRestrictions";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
