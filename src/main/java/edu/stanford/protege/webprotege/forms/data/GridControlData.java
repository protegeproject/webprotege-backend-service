package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrdering;
import edu.stanford.protege.webprotege.forms.field.GridControlDescriptor;
import edu.stanford.protege.webprotege.common.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue

@JsonTypeName("GridControlData")
public abstract class GridControlData implements ComplexFormControlValue {

    @Nonnull
    public static GridControlData get(@Nonnull GridControlDescriptor descriptor,
                                      @Nonnull Page<GridRowData> rows,
                                      @Nonnull ImmutableSet<FormRegionOrdering> ordering) {
        return new AutoValue_GridControlData(descriptor, rows, ordering);
    }

    @Override
    public <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormControlDataVisitor visitor) {
        visitor.visit(this);
    }

    @Nonnull
    public abstract GridControlDescriptor getDescriptor();

    @Nonnull
    public abstract Page<GridRowData> getRows();

    @Nonnull
    public abstract ImmutableSet<FormRegionOrdering> getOrdering();
}
