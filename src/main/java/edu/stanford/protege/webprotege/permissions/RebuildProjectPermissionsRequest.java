package edu.stanford.protege.webprotege.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Request;

@JsonTypeName(RebuildProjectPermissionsRequest.CHANNEL)
public record RebuildProjectPermissionsRequest(@JsonProperty("projectId") ProjectId projectId) implements Request<RebuildProjectPermissionsResponse> {

    public static final String CHANNEL = "webprotege.permissions.RebuildProjectPermissions";

    @JsonCreator
    public static RebuildProjectPermissionsRequest get(@JsonProperty("projectId") ProjectId projectId) {
        return new RebuildProjectPermissionsRequest(projectId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
