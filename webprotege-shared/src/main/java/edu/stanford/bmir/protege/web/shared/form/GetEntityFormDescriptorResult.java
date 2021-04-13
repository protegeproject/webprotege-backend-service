package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetEntityFormDescriptor")
public abstract class GetEntityFormDescriptorResult implements Result {

    public static final String SELECTOR_CRITERIA = "selectorCriteria";

    @JsonCreator
    @Nonnull
    public static GetEntityFormDescriptorResult get(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("formId") @Nonnull FormId formId,
                                                    @JsonProperty("formDescriptor") @Nullable FormDescriptor formDescriptor,
                                                    @JsonProperty("purpose") @Nonnull FormPurpose purpose,
                                                    @JsonProperty(SELECTOR_CRITERIA) @Nullable CompositeRootCriteria formSelector) {
        return new AutoValue_GetEntityFormDescriptorResult(projectId,
                                                           formId,
                                                           formDescriptor,
                                                           purpose,
                                                           formSelector);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();

    @Nullable
    protected abstract FormDescriptor getFormDescriptorInternal();

    @Nonnull
    public Optional<FormDescriptor> getFormDescriptor() {
        return Optional.ofNullable(getFormDescriptorInternal());
    }

    @Nonnull
    public abstract FormPurpose getPurpose();

    @JsonProperty(SELECTOR_CRITERIA)
    @Nullable
    protected abstract CompositeRootCriteria getFormSelectorCriteriaInternal();

    @JsonIgnore
    @Nonnull
    public Optional<CompositeRootCriteria> getFormSelectorCriteria() {
        return Optional.ofNullable(getFormSelectorCriteriaInternal());
    }

}
