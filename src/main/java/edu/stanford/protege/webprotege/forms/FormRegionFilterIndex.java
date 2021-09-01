package edu.stanford.protege.webprotege.forms;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.forms.data.FormRegionFilter;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-19
 */
@AutoValue
public abstract class FormRegionFilterIndex {

    @Nonnull
    public static FormRegionFilterIndex get(@Nonnull ImmutableSet<FormRegionFilter> filters) {
        return new AutoValue_FormRegionFilterIndex(filters);
    }

    @Nonnull
    public abstract ImmutableSet<FormRegionFilter> getFilters();

    public boolean hasFilter(FormRegionId formRegionId) {
        return getFilters().stream().map(FormRegionFilter::getFormRegionId).anyMatch(id -> id.equals(formRegionId));
    }
}
