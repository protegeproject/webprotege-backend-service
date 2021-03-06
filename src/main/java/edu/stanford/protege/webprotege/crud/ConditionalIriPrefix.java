package edu.stanford.protege.webprotege.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.criteria.CompositeHierarchyPositionCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
@AutoValue

public abstract class ConditionalIriPrefix {

    public static final String IRI_PREFIX = "iriPrefix";

    public static final String CRITERIA = "criteria";

    public static ConditionalIriPrefix get() {
        return get(EntityCrudKitPrefixSettings.DEFAULT_IRI_PREFIX, CompositeHierarchyPositionCriteria.get());
    }

    @Nonnull
    public static ConditionalIriPrefix get(@Nonnull @JsonProperty(IRI_PREFIX) String iriPrefix,
                                           @Nonnull @JsonProperty(CRITERIA) CompositeHierarchyPositionCriteria criteria) {
        return new AutoValue_ConditionalIriPrefix(iriPrefix, criteria);
    }

    @Nonnull
    @JsonCreator
    protected static ConditionalIriPrefix create(@Nonnull @JsonProperty(IRI_PREFIX) String iriPrefix,
                                                 @Nullable @JsonProperty(CRITERIA) CompositeHierarchyPositionCriteria criteria) {
        if(criteria == null) {
            return new AutoValue_ConditionalIriPrefix(iriPrefix, CompositeHierarchyPositionCriteria.get());
        }
        else {
            return new AutoValue_ConditionalIriPrefix(iriPrefix, criteria);
        }
    }

    @Nonnull
    public abstract String getIriPrefix();

    @JsonProperty(CRITERIA)
    @Nonnull
    public abstract CompositeHierarchyPositionCriteria getCriteria();

}
