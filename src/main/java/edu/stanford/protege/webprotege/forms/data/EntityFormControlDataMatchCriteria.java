package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@AutoValue
public abstract class EntityFormControlDataMatchCriteria implements PrimitiveFormControlDataMatchCriteria {

    @JsonCreator
    public static EntityFormControlDataMatchCriteria get(@JsonProperty(PropertyNames.CRITERIA) @Nonnull EntityMatchCriteria entityMatchCriteria) {
        return new AutoValue_EntityFormControlDataMatchCriteria(entityMatchCriteria);
    }

    @JsonProperty(PropertyNames.CRITERIA)
    public abstract EntityMatchCriteria getEntityMatchCriteria();

    @Override
    public <R> R accept(@Nonnull PrimitiveFormControlDataMatchCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}


