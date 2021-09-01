package edu.stanford.protege.webprotege.forms;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrdering;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrderingDirection;

import javax.annotation.Nonnull;

@AutoValue
public abstract class FormRegionOrderingIndex {

    public static FormRegionOrderingIndex get(@Nonnull ImmutableSet<FormRegionOrdering> orderings) {
        return new AutoValue_FormRegionOrderingIndex(orderings);
    }

    @Nonnull
    public abstract ImmutableSet<FormRegionOrdering> getOrderings();

    public FormRegionOrderingDirection getOrderingDirection(@Nonnull FormRegionId formRegionId) {
        return getOrderings()
                .stream()
                .filter(ordering -> ordering.getRegionId().equals(formRegionId))
                .findFirst()
                .map(FormRegionOrdering::getDirection)
                .orElse(FormRegionOrderingDirection.ASC);
    }
}
