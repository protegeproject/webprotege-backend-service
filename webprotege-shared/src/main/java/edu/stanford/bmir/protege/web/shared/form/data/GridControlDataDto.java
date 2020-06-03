package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlOrderBy;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridControlDataDto implements FormControlDataDto {

    @Nonnull
    public static GridControlDataDto get(@Nonnull GridControlDescriptor descriptor,
                                         @Nonnull Page<GridRowDataDto> rows,
                                         @Nonnull ImmutableList<GridControlOrderBy> ordering) {
        return new AutoValue_GridControlDataDto(descriptor, rows, ordering);
    }

    @Nonnull
    public abstract GridControlDescriptor getDescriptor();

    @Nonnull
    public abstract Page<GridRowDataDto> getRows();

    @Nonnull
    public abstract ImmutableList<GridControlOrderBy> getOrdering();

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public GridControlData toFormControlData() {
        return GridControlData.get(getDescriptor(),
                                   getRows().transform(GridRowDataDto::toGridRowData),
                                   getOrdering());
    }
}
