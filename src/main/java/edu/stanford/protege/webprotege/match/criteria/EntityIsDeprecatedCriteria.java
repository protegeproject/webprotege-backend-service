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

@JsonTypeName("EntityIsDeprecated")
public abstract class EntityIsDeprecatedCriteria implements EntityMatchCriteria {

    @JsonCreator
    @Nonnull
    public static EntityIsDeprecatedCriteria get() {
        return new AutoValue_EntityIsDeprecatedCriteria();
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
