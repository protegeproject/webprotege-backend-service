package edu.stanford.protege.webprotege.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Jun 2018
 */
@AutoValue

@JsonTypeName("DateIsBefore")
public abstract class DateIsBeforeCriteria implements DateCriteria {

    @Nonnull
    @JsonCreator
    public static DateIsBeforeCriteria get(@JsonProperty(YEAR) int year,
                                           @JsonProperty(MONTH) int month,
                                           @JsonProperty(DAY) int day) {
        DateCriteria.checkArgs(year, month, day);
        return new AutoValue_DateIsBeforeCriteria(year,
                                                  month,
                                                  day);
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
