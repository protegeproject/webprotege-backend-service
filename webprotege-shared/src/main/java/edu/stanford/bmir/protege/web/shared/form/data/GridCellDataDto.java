package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.FilterState;
import edu.stanford.bmir.protege.web.shared.form.HasFilterState;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridCellDataDto implements HasFilterState {

    public static GridCellDataDto get(@Nonnull GridColumnId columnId,
                                      @Nullable Page<FormControlDataDto> values,
                                      @Nonnull FilterState filterState) {
        return new AutoValue_GridCellDataDto(columnId, values, filterState);
    }

    @Nonnull
    public abstract GridColumnId getColumnId();

    @Nonnull
    public abstract Page<FormControlDataDto> getValues();

    @Nonnull
    @Override
    public abstract FilterState getFilterState();

    /**
     * Determines whether this cell data is filtered empty
     * @return true if this cel data is filtered empty (all values have been filtered out)
     * otherwise false.
     */
    public boolean isFilteredEmpty() {
        return getFilterState().equals(FilterState.FILTERED)
                && getValues().getPageElements().isEmpty();
    }

    public GridCellData toGridCellData() {
        return GridCellData.get(getColumnId(),
                getValues().transform(FormControlDataDto::toFormControlData));
    }
}
