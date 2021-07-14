package edu.stanford.protege.webprotege.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Jun 2018
 */
@AutoValue

@JsonTypeName("EntityIsNotDeprecated")
public abstract class EntityIsNotDeprecatedCriteria implements EntityMatchCriteria {

    @JsonCreator
    @Nonnull
    public static EntityIsNotDeprecatedCriteria get() {
        return new AutoValue_EntityIsNotDeprecatedCriteria();
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
