package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue

public abstract class GridCellDataDto implements HasFilterState {

    @JsonCreator
    public static GridCellDataDto get(@JsonProperty(PropertyNames.COLUMN_ID) @Nonnull FormRegionId columnId,
                                      @JsonProperty(PropertyNames.VALUES) @Nullable Page<FormControlDataDto> values,
                                      @JsonProperty(PropertyNames.FILTER_STATE) @Nonnull FilterState filterState) {
        return new AutoValue_GridCellDataDto(columnId, values, filterState);
    }

    @JsonProperty(PropertyNames.COLUMN_ID)
    @Nonnull
    public abstract FormRegionId getColumnId();

    @JsonProperty(PropertyNames.VALUES)
    @Nonnull
    public abstract Page<FormControlDataDto> getValues();

    @JsonProperty(PropertyNames.FILTER_STATE)
    @Nonnull
    @Override
    public abstract FilterState getFilterState();

    /**
     * Determines whether this cell data is filtered empty
     *
     * @return true if this cel data is filtered empty (all values have been filtered out)
     * otherwise false.
     */
    @JsonIgnore
    public boolean isFilteredEmpty() {
        return getFilterState().equals(FilterState.FILTERED) && getValues().getPageElements().isEmpty();
    }

    public GridCellData toGridCellData() {
        return GridCellData.get(getColumnId(), getValues().transform(FormControlDataDto::toFormControlData));
    }
}
