package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
@AutoValue
@GwtCompatible(serializable = true)
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
