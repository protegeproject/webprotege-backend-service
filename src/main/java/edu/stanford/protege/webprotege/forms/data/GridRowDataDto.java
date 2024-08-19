package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

@AutoValue
public abstract class GridRowDataDto {

    @JsonCreator
    @Nonnull
    public static GridRowDataDto get(@JsonProperty(PropertyNames.SUBJECT) @Nullable FormSubjectDto subject,
                                     @JsonProperty(PropertyNames.CELLS) @Nonnull ImmutableList<GridCellDataDto> cellData) {
        return new AutoValue_GridRowDataDto(subject, cellData);
    }

    @Nonnull
    public static GridRowDataDto get(@Nonnull ImmutableList<GridCellDataDto> cellData) {
        return new AutoValue_GridRowDataDto(null, cellData);
    }


    @JsonProperty(PropertyNames.SUBJECT)
    @Nullable
    protected abstract FormSubjectDto getSubjectInternal();

    @JsonIgnore
    @Nonnull
    public Optional<FormSubjectDto> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @JsonProperty(PropertyNames.CELLS)
    @Nonnull
    public abstract ImmutableList<GridCellDataDto> getCells();

    /**
     * Determines whether this row contains cells that have been filtered empty
     *
     * @return true if this row contains cells that have been filtered empty, otherwise false.
     */
    public boolean containsFilteredEmptyCells() {
        return getCells().stream().anyMatch(GridCellDataDto::isFilteredEmpty);
    }

    public GridRowData toGridRowData() {
        return GridRowData.get(getSubject().map(FormSubjectDto::toFormSubject).orElse(null),
                               getCells().stream().map(GridCellDataDto::toGridCellData).collect(toImmutableList()));
    }
}
