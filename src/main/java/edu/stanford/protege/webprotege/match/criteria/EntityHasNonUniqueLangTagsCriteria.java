package edu.stanford.protege.webprotege.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Jun 2018
 */
@AutoValue

@JsonTypeName("EntityHasNonUniqueLangTagsCriteria")
public abstract class EntityHasNonUniqueLangTagsCriteria implements EntityMatchCriteria {

    @JsonProperty("property")
    @Nonnull
    public abstract AnnotationPropertyCriteria getPropertyCriteria();

    @JsonCreator
    @Nonnull
    public static EntityHasNonUniqueLangTagsCriteria get(@Nonnull @JsonProperty("property") AnnotationPropertyCriteria property) {
        return new AutoValue_EntityHasNonUniqueLangTagsCriteria(property);
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
