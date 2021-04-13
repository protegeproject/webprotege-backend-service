package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetProjectFormDescriptors")
public abstract class GetProjectFormDescriptorsResult implements Result, HasProjectId {

    @JsonCreator
    @Nonnull
    public static GetProjectFormDescriptorsResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                         @JsonProperty("formDescriptors") @Nonnull ImmutableList<FormDescriptor> formDescriptors,
                                                         @JsonProperty("formSelectors") @Nonnull ImmutableList<EntityFormSelector> selectionCriteria) {
        return new AutoValue_GetProjectFormDescriptorsResult(projectId, formDescriptors, selectionCriteria);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ImmutableList<FormDescriptor> getFormDescriptors();

    public abstract ImmutableList<EntityFormSelector> getFormSelectors();
}
