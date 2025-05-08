package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.dispatch.Action;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
@AutoValue

@JsonTypeName("GetAvailableProjectsWithPermission")
public abstract class GetAvailableProjectsWithPermissionAction implements Action<GetAvailableProjectsWithPermissionResult>, Request<GetAvailableProjectsWithPermissionResult> {

    public static final String CHANNEL = "webprotege.projects.GetAvailableProjectsWithPermission";

    @JsonCreator
    public static GetAvailableProjectsWithPermissionAction create(@JsonProperty("permission") @Nonnull Capability permission) {
        return new AutoValue_GetAvailableProjectsWithPermissionAction(permission);
    }

    @Nonnull
    public abstract Capability getPermission();

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
