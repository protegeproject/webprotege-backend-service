package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;


@AutoValue
@JsonTypeName("EntityNameControlDescriptorDto")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class EntityNameControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static EntityNameControlDescriptorDto get(@JsonProperty(PropertyNames.PLACEHOLDER) @Nonnull LanguageMap placeholder,
                                                     @JsonProperty(PropertyNames.CRITERIA) @Nullable CompositeRootCriteria matchCriteria) {
        return new AutoValue_EntityNameControlDescriptorDto(placeholder, matchCriteria);
    }

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public EntityNameControlDescriptor toFormControlDescriptor() {
        return EntityNameControlDescriptor.get(getPlaceholder(), getMatchCriteriaInternal());
    }

    @Nonnull
    @JsonProperty(PropertyNames.PLACEHOLDER)
    public abstract LanguageMap getPlaceholder();

    @JsonProperty(PropertyNames.CRITERIA)
    @Nullable
    protected abstract CompositeRootCriteria getMatchCriteriaInternal();

    @Nonnull
    @JsonIgnore
    public Optional<CompositeRootCriteria> getMatchCriteria() {
        return Optional.ofNullable(getMatchCriteriaInternal());
    }
}
