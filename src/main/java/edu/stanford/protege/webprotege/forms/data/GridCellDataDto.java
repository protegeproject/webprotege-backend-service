package edu.stanford.protege.webprotege.forms.data;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.FilterState;
import edu.stanford.protege.webprotege.forms.HasFilterState;
import edu.stanford.protege.webprotege.forms.field.GridColumnId;
import edu.stanford.protege.webprotege.common.Page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue

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
