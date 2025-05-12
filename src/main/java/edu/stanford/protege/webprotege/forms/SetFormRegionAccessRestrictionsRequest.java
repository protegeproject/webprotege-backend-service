package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;

import java.util.List;

import static edu.stanford.protege.webprotege.forms.SetFormRegionAccessRestrictionsRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record SetFormRegionAccessRestrictionsRequest(@JsonProperty("projectId") ProjectId projectId,
                                                     @JsonProperty("accessRestrictions") List<FormRegionAccessRestriction> accessRestrictions) implements ProjectRequest<SetFormRegionAccessRestrictionsResponse> {

    public static final String CHANNEL = "webprotege.forms.SetFormRegionAccessRestrictions";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
