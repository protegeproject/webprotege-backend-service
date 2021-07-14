package edu.stanford.protege.webprotege.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
@AutoValue

@JsonTypeName("LangTagIsEmpty")
public abstract class LangTagIsEmptyCriteria implements LangTagCriteria {

    @JsonCreator
    @Nonnull
    public static LangTagIsEmptyCriteria get() {
        return new AutoValue_LangTagIsEmptyCriteria();
    }

    @Override
    public <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull LiteralCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
