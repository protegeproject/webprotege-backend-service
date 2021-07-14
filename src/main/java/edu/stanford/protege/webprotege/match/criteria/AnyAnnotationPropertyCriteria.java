package edu.stanford.protege.webprotege.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 *
 * Criteria for matching any annotation property
 */
@AutoValue

@JsonTypeName("AnyAnnotationProperty")
public abstract class AnyAnnotationPropertyCriteria implements AnnotationPropertyCriteria {

    @Nonnull
    @JsonCreator
    public static AnyAnnotationPropertyCriteria get() {
        return new AutoValue_AnyAnnotationPropertyCriteria();
    }

    @Nonnull
    public static AnyAnnotationPropertyCriteria anyAnnotationProperty() {
        return get();
    }

    @Override
    public <R> R accept(@Nonnull AnnotationPropertyCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
