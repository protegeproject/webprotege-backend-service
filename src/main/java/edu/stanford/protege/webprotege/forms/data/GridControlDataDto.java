package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrdering;
import edu.stanford.protege.webprotege.forms.field.GridControlDescriptor;

import javax.annotation.Nonnull;

@AutoValue

@JsonTypeName("GridControlDataDto")
public abstract class GridControlDataDto implements FormControlDataDto, HasFilterState {

    @JsonCreator
    @Nonnull
    public static GridControlDataDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull GridControlDescriptor descriptor,
                                         @JsonProperty(PropertyNames.ROWS) @Nonnull Page<GridRowDataDto> rows,
                                         @JsonProperty(PropertyNames.ORDERING) @Nonnull ImmutableSet<FormRegionOrdering> ordering,
                                         @JsonProperty(PropertyNames.DEPTH) int depth,
                                         @JsonProperty(PropertyNames.FILTER_STATE) @Nonnull FilterState filterState) {
        return new AutoValue_GridControlDataDto(depth, descriptor, rows, ordering, filterState);
    }

    @JsonProperty(PropertyNames.CONTROL)
    @Nonnull
    public abstract GridControlDescriptor getDescriptor();

    @JsonProperty(PropertyNames.ROWS)
    @Nonnull
    public abstract Page<GridRowDataDto> getRows();

    @JsonProperty(PropertyNames.ORDERING)
    @Nonnull
    public abstract ImmutableSet<FormRegionOrdering> getOrdering();

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public GridControlData toFormControlData() {
        return GridControlData.get(getDescriptor(), getRows().transform(GridRowDataDto::toGridRowData), getOrdering());
    }

    @JsonProperty(PropertyNames.FILTER_STATE)
    @Nonnull
    @Override
    public abstract FilterState getFilterState();

    /**
     * Determines whether this grid is empty because it has been filtered empty.
     *
     * @return true if this grid is empty and this is due to filtering
     */
    @JsonIgnore
    public boolean isFilteredEmpty() {
        return getFilterState().equals(FilterState.FILTERED) && getRows().getTotalElements() == 0;
    }
}
