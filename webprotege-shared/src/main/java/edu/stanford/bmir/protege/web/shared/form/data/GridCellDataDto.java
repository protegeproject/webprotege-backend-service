package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridCellDataDto {

    public static GridCellDataDto get(@Nonnull GridColumnId columnId,
                                      @Nullable Page<FormControlDataDto> values) {
        return new AutoValue_GridCellDataDto(columnId, values);
    }

    @Nonnull
    public abstract GridColumnId getColumnId();

    @Nonnull
    public abstract Page<FormControlDataDto> getValues();

    public GridCellData toGridCellData() {
        return GridCellData.get(getColumnId(),
                getValues().transform(FormControlDataDto::toFormControlData));
    }
}
