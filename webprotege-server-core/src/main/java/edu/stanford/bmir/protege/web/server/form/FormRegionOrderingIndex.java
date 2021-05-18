package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.server.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.server.form.field.FormRegionOrderingDirection;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

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
