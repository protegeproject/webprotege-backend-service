package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
@AutoValue

@JsonTypeName(value = "GetAvailableProjectsWithPermission")
public abstract class GetAvailableProjectsWithPermissionResult implements Result {

    @JsonCreator
    public static GetAvailableProjectsWithPermissionResult create(@JsonProperty("projects") @Nonnull ImmutableList<ProjectDetails> projects) {
        return new AutoValue_GetAvailableProjectsWithPermissionResult(projects);
    }

    @Nonnull
    public abstract ImmutableList<ProjectDetails> getProjects();
}
