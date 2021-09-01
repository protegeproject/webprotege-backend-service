package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.criteria.LiteralCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@AutoValue

public abstract class LiteralFormControlDataMatchCriteria implements PrimitiveFormControlDataMatchCriteria {

    public static final String LITERAL_MATCHES = "literalMatches";

    @Nonnull
    public static LiteralFormControlDataMatchCriteria get(@Nonnull LiteralCriteria literalCriteria) {
        return new AutoValue_LiteralFormControlDataMatchCriteria(literalCriteria);
    }

    @JsonProperty(LITERAL_MATCHES)
    @Nonnull
    public abstract LiteralCriteria getLexicalValueCriteria();

    @Override
    public <R> R accept(@Nonnull PrimitiveFormControlDataMatchCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
