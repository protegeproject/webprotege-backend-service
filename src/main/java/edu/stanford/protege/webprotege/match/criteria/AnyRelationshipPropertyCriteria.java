package edu.stanford.protege.webprotege.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
@AutoValue

@JsonTypeName("AnyProperty")
public abstract class AnyRelationshipPropertyCriteria implements RelationshipPropertyCriteria {

    @Nonnull
    @JsonCreator
    public static AnyRelationshipPropertyCriteria get() {
        return new AutoValue_AnyRelationshipPropertyCriteria();
    }

    @Override
    public <R> R accept(@Nonnull RelationshipPropertyCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
