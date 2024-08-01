package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@AutoValue
public abstract class CompositePrimitiveFormControlDataMatchCriteria implements PrimitiveFormControlDataMatchCriteria {

    @JsonCreator
    @Nonnull
    public static CompositePrimitiveFormControlDataMatchCriteria get(@JsonProperty(PropertyNames.CRITERIA) @Nonnull ImmutableList<? extends PrimitiveFormControlDataMatchCriteria> criteria,
                                                                     @JsonProperty(PropertyNames.MATCH_TYPE) @Nonnull MultiMatchType matchType) {
        return new AutoValue_CompositePrimitiveFormControlDataMatchCriteria(matchType, ImmutableList.copyOf(criteria));
    }

    @JsonProperty(PropertyNames.MATCH_TYPE)
    @Nonnull
    public abstract MultiMatchType getMultiMatchType();

    @JsonProperty(PropertyNames.CRITERIA)
    @Nonnull
    public abstract ImmutableList<PrimitiveFormControlDataMatchCriteria> getCriteria();

    @Override
    public <R> R accept(@Nonnull PrimitiveFormControlDataMatchCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
