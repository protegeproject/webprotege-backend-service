package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.forms.FilterState;
import edu.stanford.protege.webprotege.forms.HasFilterState;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrdering;
import edu.stanford.protege.webprotege.forms.field.GridControlDescriptor;
import edu.stanford.protege.webprotege.common.Page;

import javax.annotation.Nonnull;

@AutoValue

@JsonTypeName("GridControlDataDto")
public abstract class GridControlDataDto implements FormControlDataDto, HasFilterState {

    @JsonCreator
    @Nonnull
    public static GridControlDataDto get(@JsonProperty("descriptor") @Nonnull GridControlDescriptor descriptor,
                                         @JsonProperty("rows") @Nonnull Page<GridRowDataDto> rows,
                                         @JsonProperty("ordering") @Nonnull ImmutableSet<FormRegionOrdering> ordering,
                                         @JsonProperty("depth") int depth,
                                         @JsonProperty("filterState") @Nonnull FilterState filterState) {
        return new AutoValue_GridControlDataDto(depth, descriptor, rows, ordering, filterState);
    }

    @Nonnull
    public abstract GridControlDescriptor getDescriptor();

    @Nonnull
    public abstract Page<GridRowDataDto> getRows();

    @Nonnull
    public abstract ImmutableSet<FormRegionOrdering> getOrdering();

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

    @Nonnull
    @Override
    public abstract FilterState getFilterState();

    /**
     * Determines whether this grid is empty because it has been filtered empty.
     * @return true if this grid is empty and this is due to filtering
     */
    public boolean isFilteredEmpty() {
        return getFilterState().equals(FilterState.FILTERED) &&
                getRows().getTotalElements() == 0;
    }
}
