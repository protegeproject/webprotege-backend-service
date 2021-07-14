package edu.stanford.protege.webprotege.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 *
 * Criteria that matches any annotation value
 */
@AutoValue

@JsonTypeName("AnyAnnotationValue")
public abstract class AnyAnnotationValueCriteria implements AnnotationValueCriteria {

    @Nonnull
    @JsonCreator
    public static AnyAnnotationValueCriteria get() {
        return new AutoValue_AnyAnnotationValueCriteria();
    }

    /**
     * A convenicen method that returns an instance of {@link AnyAnnotationPropertyCriteria}.
     */
    @Nonnull
    @JsonCreator
    public static AnyAnnotationValueCriteria anyValue() {
        return get();
    }

    @Override
    public <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
