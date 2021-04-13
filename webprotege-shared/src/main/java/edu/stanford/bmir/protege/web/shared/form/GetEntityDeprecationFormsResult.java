package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetEntityDeprecationForms")
public abstract class GetEntityDeprecationFormsResult implements Result {

    private static final String REPLACEMENT_ENTITY_CRITERIA = "replacementEntityCriteria";

    private static final String FORM_DESCRIPTORS = "formDescriptors";

    private static final String REFERENCES_COUNT = "referencesCount";

    @JsonCreator
    @Nonnull
    public static GetEntityDeprecationFormsResult create(@JsonProperty(FORM_DESCRIPTORS) @Nonnull ImmutableList<FormDescriptorDto> formDtos,
                                                      @JsonProperty(REFERENCES_COUNT) long referencesCount,
                                                      @JsonProperty(REPLACEMENT_ENTITY_CRITERIA) @Nullable CompositeRootCriteria replacementEntityCriteria) {
        return new AutoValue_GetEntityDeprecationFormsResult(formDtos, referencesCount, replacementEntityCriteria);
    }

    @Nonnull
    public abstract ImmutableList<FormDescriptorDto> getFormDescriptors();

    public abstract long getReferencesCount();

    @JsonIgnore
    @Nonnull
    public Optional<CompositeRootCriteria> getReplacedByFilterCriteria() {
        return Optional.ofNullable(getReplacedByFilterCriteriaInternal());
    }

    @JsonProperty(REPLACEMENT_ENTITY_CRITERIA)
    @Nullable
    public abstract CompositeRootCriteria getReplacedByFilterCriteriaInternal();
}
