package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.criteria.RootCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
@AutoValue
public abstract class EntityFormDescriptor {

    @JsonCreator
    public static EntityFormDescriptor valueOf(@JsonProperty(PropertyNames.PROJECT_ID) @Nonnull ProjectId projectId,
                                               @JsonProperty(PropertyNames.FORM_ID) @Nonnull FormId formId,
                                               @JsonProperty(PropertyNames.FORM) @Nonnull FormDescriptor newDescriptor,
                                               @JsonProperty(PropertyNames.PURPOSE) @Nonnull FormPurpose purpose,
                                               @JsonProperty(PropertyNames.CRITERIA) @Nonnull RootCriteria newSelectorCriteria) {
        return new AutoValue_EntityFormDescriptor(projectId, formId, newDescriptor, purpose, newSelectorCriteria);
    }

    @Nonnull
    @JsonProperty(PropertyNames.PROJECT_ID)
    public abstract ProjectId getProjectId();

    @Nonnull
    @JsonProperty(PropertyNames.FORM_ID)
    public abstract FormId getFormId();

    @Nonnull
    @JsonProperty(PropertyNames.FORM)
    public abstract FormDescriptor getDescriptor();

    @Nonnull
    @JsonProperty(PropertyNames.PURPOSE)
    public abstract FormPurpose getPurpose();

    @Nonnull
    @JsonProperty(PropertyNames.CRITERIA)
    public abstract RootCriteria getSelectorCriteria();
}
