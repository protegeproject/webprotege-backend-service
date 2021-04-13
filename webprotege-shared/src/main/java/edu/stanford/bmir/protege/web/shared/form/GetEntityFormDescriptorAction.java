package edu.stanford.bmir.protege.web.shared.form;

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
 * 2020-01-16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetEntityFormDescriptor")
public abstract class GetEntityFormDescriptorAction implements ProjectAction<GetEntityFormDescriptorResult> {

    @JsonCreator
    public static GetEntityFormDescriptorAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("formId") FormId formId) {
        return new AutoValue_GetEntityFormDescriptorAction(projectId, formId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();
}
