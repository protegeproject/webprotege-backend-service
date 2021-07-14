package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
@AutoValue

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
