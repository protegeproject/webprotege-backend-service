package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("UpdateFormDescriptor")
public abstract class UpdateFormDescriptorAction implements ProjectAction<UpdateFormDescriptorResult> {

    @JsonCreator
    public static UpdateFormDescriptorAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("formDescriptor") @Nonnull FormDescriptor descriptor) {
        return new AutoValue_UpdateFormDescriptorAction(projectId, descriptor);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormDescriptor getFormDescriptor();
}
