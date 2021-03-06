package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(EntityNameControlDescriptor.TYPE)
@AutoValue

public abstract class EntityNameControlDescriptor implements FormControlDescriptor {

    protected static final String TYPE = "ENTITY_NAME";

    @JsonCreator
    @Nonnull
    public static EntityNameControlDescriptor get(@Nullable @JsonProperty("placeholder") LanguageMap languageMap,
                                                  @Nullable @JsonProperty("matchCriteria") CompositeRootCriteria criteria) {
        return new AutoValue_EntityNameControlDescriptor(languageMap == null ? LanguageMap.empty() : languageMap,
                                                         criteria);
    }

    @Nonnull
    public static EntityNameControlDescriptor getDefault() {
        return new AutoValue_EntityNameControlDescriptor(LanguageMap.empty(),
                                                         getDefaultEntityMatchCriteria());
    }

    public static CompositeRootCriteria getDefaultEntityMatchCriteria() {
        return CompositeRootCriteria.get(
                ImmutableList.of(EntityTypeIsOneOfCriteria.get(
                        ImmutableSet.of(EntityType.CLASS)
                )),
                MultiMatchType.ALL
        );
    }

    public static String getFieldTypeId() {
        return TYPE;
    }

    @Nonnull
    @Override
    @JsonIgnore
    public String getAssociatedType() {
        return TYPE;
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    public abstract LanguageMap getPlaceholder();

    @JsonIgnore
    @Nullable
    protected abstract CompositeRootCriteria getMatchCriteriaInternal();

    @Nonnull
    public Optional<CompositeRootCriteria> getMatchCriteria() {
        return Optional.ofNullable(getMatchCriteriaInternal());
    }
}
