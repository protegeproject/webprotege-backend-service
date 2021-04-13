package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(value = "GetAvailableProjectsWithPermission")
public abstract class GetAvailableProjectsWithPermissionResult implements Result {

    @JsonCreator
    public static GetAvailableProjectsWithPermissionResult create(@JsonProperty("projects") @Nonnull ImmutableList<ProjectDetails> projects) {
        return new AutoValue_GetAvailableProjectsWithPermissionResult(projects);
    }

    @Nonnull
    public abstract ImmutableList<ProjectDetails> getProjects();
}
