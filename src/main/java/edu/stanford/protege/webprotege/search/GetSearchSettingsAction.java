package edu.stanford.protege.webprotege.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
@AutoValue

@JsonTypeName("GetSearchSettings")
public abstract class GetSearchSettingsAction implements ProjectAction<GetSearchSettingsResult> {

    @JsonCreator
    public static GetSearchSettingsAction create(@JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetSearchSettingsAction(projectId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();
}
