package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@AutoValue

@JsonTypeName("HeadNodeMatches")
public abstract class HeadNodeMatchesCriteria implements NodeMatchesCriteria {

    @JsonCreator
    public static HeadNodeMatchesCriteria get(@Nonnull @JsonProperty("nodeCriteria") EntityMatchCriteria matchCriteria) {
        return new AutoValue_HeadNodeMatchesCriteria(matchCriteria);
    }

    @Override
    public <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public EdgeCriteria simplify() {
        return this;
    }
}
